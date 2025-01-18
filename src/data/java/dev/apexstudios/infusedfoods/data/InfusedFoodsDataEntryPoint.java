package dev.apexstudios.infusedfoods.data;

import dev.apexstudios.apexcore.lib.component.block.types.LayeredCauldronBlockComponent;
import dev.apexstudios.apexcore.lib.data.ProviderTypes;
import dev.apexstudios.apexcore.lib.data.ResourceGenerator;
import dev.apexstudios.infusedfoods.InfusedFoods;
import dev.apexstudios.infusedfoods.cauldron.PotionCauldronSetup;
import dev.apexstudios.infusedfoods.fluid.PotionFluidSetup;
import dev.apexstudios.infusedfoods.recipe.CleansingRecipe;
import dev.apexstudios.infusedfoods.recipe.HideEffectsRecipe;
import dev.apexstudios.infusedfoods.recipe.RecipeSetup;
import java.util.Optional;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.blockstates.Variant;
import net.minecraft.client.data.models.blockstates.VariantProperties;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.model.item.DynamicFluidContainerModel;
import net.neoforged.neoforge.common.Tags;

@Mod(value = InfusedFoods.ID, dist = Dist.CLIENT)
public final class InfusedFoodsDataEntryPoint {
    public InfusedFoodsDataEntryPoint(IEventBus modBus) {
        ResourceGenerator.of(modBus, generator -> {
            generator.pack()
                    .providing(ProviderTypes.POTION_TAGS, (context, provider) -> provider
                            .tag(InfusedFoods.POTION_BLACKLIST).withElement(Potions.MUNDANE)
                    )
                    .providing(ProviderTypes.ITEM_TAGS, (context, provider) -> {
                        var bucketsPotion = ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "buckets/potion"));
                        var potionsBucket = ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "potions/bucket"));
                        provider.tag(bucketsPotion).withElement(PotionFluidSetup.BUCKET);
                        provider.tag(potionsBucket).withElement(PotionFluidSetup.BUCKET);
                        provider.tag(Tags.Items.BUCKETS).withTag(bucketsPotion);
                        provider.tag(Tags.Items.POTIONS).withTag(potionsBucket);

                        provider.tag(RecipeSetup.CLEANSING_AGENT).withTag(Tags.Items.BUCKETS_MILK);
                        provider.tag(RecipeSetup.EFFECTS_HIDER).withElement(Items.SPIDER_EYE);
                    })
                    .providing(ProviderTypes.BLOCK_TAGS, (context, provider) -> {
                        provider.tag(BlockTags.CAULDRONS).withElement(PotionCauldronSetup.BLOCK);
                    })
                    .providing(ProviderTypes.FLUID_TAGS, (context, provider) -> {
                        provider.tag(Tags.Fluids.POTION).withElement(PotionFluidSetup.FLUID);
                    })
                    .providing(ProviderTypes.LANGUAGE, (context, provider) -> provider
                            .addCreativeModeTab(InfusedFoods.INFUSED_FOODS, "Infused Foods")
                            .addCreativeModeTab(PotionFluidSetup.POTION_BUCKETS, "Potion Buckets")
                            .add(InfusedFoods.ITEM_BLACKLIST, "Infusion Blacklist (Items)")
                            .add(InfusedFoods.POTION_BLACKLIST, "Infusion Blacklist (Potions)")
                            .addItem(PotionFluidSetup.BUCKET, "Potion Bucket")
                            .addBlock(PotionCauldronSetup.BLOCK, "Potion Cauldron")
                    )
                    .providing(ProviderTypes.MODELS, (context, provider) -> {
                        provider.knownBlocks(PotionCauldronSetup.BLOCK).knownItems(PotionFluidSetup.BUCKET);
                        createPotionBucket(provider.itemModels());
                        createPotionCauldron(provider.blockModels());
                    })
                    .providing(ProviderTypes.RECIPES, (context, provider) -> {
                        SpecialRecipeBuilder.special(CleansingRecipe::new).save(provider.output(), InfusedFoods.id("cleansing"));
                        SpecialRecipeBuilder.special(HideEffectsRecipe::new).save(provider.output(), InfusedFoods.id("hide_effects"));
                    });
        });
    }

    private void createPotionBucket(ItemModelGenerators itemModels) {
        itemModels.itemModelOutput.accept(PotionFluidSetup.BUCKET.value(), new DynamicFluidContainerModel.Unbaked(
                new DynamicFluidContainerModel.Textures(
                        Optional.empty(),
                        Optional.of(ModelLocationUtils.getModelLocation(Items.BUCKET)),
                        Optional.of(ModelLocationUtils.decorateItemModelLocation("neoforge:mask/bucket_fluid_drip")),
                        Optional.of(ModelLocationUtils.decorateItemModelLocation("neoforge:mask/bucket_fluid_cover_drip"))
                ),
                PotionFluidSetup.FLUID.value(),
                false,
                true,
                true
        ));
    }

    private void createPotionCauldron(BlockModelGenerators blockModels) {
        var block = PotionCauldronSetup.BLOCK.value();
        var waterStill = TextureMapping.getBlockTexture(Blocks.WATER, "_still");

        blockModels.registerSimpleFlatItemModel(block);

        blockModels.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block)
                .with(PropertyDispatch.property(LayeredCauldronBlockComponent.LEVEL)
                        .select(1, Variant.variant().with(VariantProperties.MODEL, createCauldronVariantModel(block, ModelTemplates.CAULDRON_LEVEL1, "_level1", waterStill, blockModels)))
                        .select(2, Variant.variant().with(VariantProperties.MODEL, createCauldronVariantModel(block, ModelTemplates.CAULDRON_LEVEL2, "_level2", waterStill, blockModels)))
                        .select(3, Variant.variant().with(VariantProperties.MODEL, createCauldronVariantModel(block, ModelTemplates.CAULDRON_FULL, "_full", waterStill, blockModels)))
                )
        );
    }

    private ResourceLocation createCauldronVariantModel(Block block, ModelTemplate template, String suffix, ResourceLocation fluidTexture, BlockModelGenerators blockModels) {
        return template.createWithSuffix(
                block,
                suffix,
                TextureMapping.cauldron(fluidTexture),
                blockModels.modelOutput
        );
    }
}
