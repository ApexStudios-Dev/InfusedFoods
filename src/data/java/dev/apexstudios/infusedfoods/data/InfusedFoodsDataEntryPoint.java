package dev.apexstudios.infusedfoods.data;

import dev.apexstudios.apexcore.lib.component.block.types.LayeredCauldronBlockComponent;
import dev.apexstudios.apexcore.lib.data.ProviderTypes;
import dev.apexstudios.apexcore.lib.data.ResourceGenerator;
import dev.apexstudios.infusedfoods.InfusedFoods;
import dev.apexstudios.infusedfoods.cauldron.PotionCauldronSetup;
import dev.apexstudios.infusedfoods.recipe.CleansingRecipe;
import dev.apexstudios.infusedfoods.recipe.HideEffectsRecipe;
import dev.apexstudios.infusedfoods.recipe.RecipeSetup;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
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
                        provider.tag(RecipeSetup.CLEANSING_AGENT)
                                .withTag(Tags.Items.BUCKETS_MILK)
                                .withTag(Tags.Items.DRINKS_MILK);

                        provider.tag(RecipeSetup.EFFECTS_HIDER).withElement(Items.FERMENTED_SPIDER_EYE);
                    })
                    .providing(ProviderTypes.BLOCK_TAGS, (context, provider) -> {
                        provider.tag(BlockTags.CAULDRONS).withElement(PotionCauldronSetup.BLOCK);
                    })
                    .providing(ProviderTypes.FLUID_TAGS, (context, provider) -> {
                        provider.tag(Tags.Fluids.POTION).withElement(InfusedFoods.POTION_FLUID);
                    })
                    .providing(ProviderTypes.LANGUAGE, (context, provider) -> provider
                            .addCreativeModeTab(InfusedFoods.INFUSED_FOODS, "Infused Foods")
                            .add(InfusedFoods.ITEM_BLACKLIST, "Infusion Blacklist (Items)")
                            .add(InfusedFoods.POTION_BLACKLIST, "Infusion Blacklist (Potions)")
                            .addBlock(PotionCauldronSetup.BLOCK, "Potion Cauldron")
                            .add(InfusedFoods.POTION_FLUID_TYPE.getKey(), "fluid_type", "Potion")
                            .add(RecipeSetup.CLEANSING_AGENT, "Cleansing Agents")
                            .add(RecipeSetup.EFFECTS_HIDER, "Potion Effect Hiders")
                    )
                    .providing(ProviderTypes.MODELS, (context, provider) -> {
                        createPotionCauldron(provider.blockModels());
                    })
                    .providing(ProviderTypes.RECIPES, (context, provider) -> {
                        SpecialRecipeBuilder.special(CleansingRecipe::new).save(provider.output(), InfusedFoods.id("cleansing"));
                        SpecialRecipeBuilder.special(HideEffectsRecipe::new).save(provider.output(), InfusedFoods.id("hide_effects"));
                    });
        });
    }

    private void createPotionCauldron(BlockModelGenerators blockModels) {
        var block = PotionCauldronSetup.BLOCK.value();
        var waterStill = TextureMapping.getBlockTexture(Blocks.WATER, "_still");

        blockModels.registerSimpleFlatItemModel(block);

        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
                .with(PropertyDispatch.initial(LayeredCauldronBlockComponent.LEVEL)
                        .select(1, BlockModelGenerators.plainVariant(createCauldronVariantModel(block, ModelTemplates.CAULDRON_LEVEL1, "_level1", waterStill, blockModels)))
                        .select(2, BlockModelGenerators.plainVariant(createCauldronVariantModel(block, ModelTemplates.CAULDRON_LEVEL2, "_level2", waterStill, blockModels)))
                        .select(3, BlockModelGenerators.plainVariant(createCauldronVariantModel(block, ModelTemplates.CAULDRON_FULL, "_full", waterStill, blockModels)))
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
