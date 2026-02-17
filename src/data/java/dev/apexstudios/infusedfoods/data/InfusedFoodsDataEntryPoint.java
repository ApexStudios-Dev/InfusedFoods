package dev.apexstudios.infusedfoods.data;

import dev.apexstudios.apexcore.api.data.ProviderTypes;
import dev.apexstudios.apexcore.api.data.ResourceGenerator;
import dev.apexstudios.infusedfoods.common.CleansingRecipe;
import dev.apexstudios.infusedfoods.common.InfusedFoods;
import dev.apexstudios.infusedfoods.common.util.InfusionEntries;
import dev.apexstudios.infusedfoods.common.util.InfusionTags;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.renderer.block.model.Material;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.Tags;

@Mod(value = InfusedFoods.ID, dist = Dist.CLIENT)
public final class InfusedFoodsDataEntryPoint {
    public InfusedFoodsDataEntryPoint(IEventBus modBus) {
        ResourceGenerator.of(modBus, generator -> generator.pack()
                .providing(ProviderTypes.POTION_TAGS, (context, provider) -> provider
                        .tag(InfusionTags.POTION_BLACKLIST).withElement(Potions.MUNDANE)
                )
                .providing(ProviderTypes.ITEM_TAGS, (context, provider) -> provider
                        .tag(InfusionTags.CLEANSING_AGENT)
                        .withTag(Tags.Items.BUCKETS_MILK)
                        .withTag(Tags.Items.DRINKS_MILK))
                .providing(ProviderTypes.BLOCK_TAGS, (context, provider) -> provider
                        .tag(BlockTags.CAULDRONS)
                        .withElement(InfusionEntries.CAULDRON_BLOCK)
                )
                .providing(ProviderTypes.FLUID_TAGS, (context, provider) -> provider
                        .tag(Tags.Fluids.POTION)
                        .withElement(InfusionEntries.POTION_FLUID)
                )
                .providing(ProviderTypes.LANGUAGE, (context, provider) -> provider
                        .add(InfusionTags.ITEM_BLACKLIST, "Infusion Blacklist (Items)")
                        .add(InfusionTags.POTION_BLACKLIST, "Infusion Blacklist (Potions)")
                        .addBlock(InfusionEntries.CAULDRON_BLOCK, "Potion Cauldron")
                        .add(InfusionEntries.POTION_FLUID_TYPE.getKey(), "fluid_type", "Potion")
                        .add(InfusionTags.CLEANSING_AGENT, "Cleansing Agents")
                )
                .providing(ProviderTypes.MODELS, (context, provider) -> createPotionCauldron(provider.blockModels()))
                .providing(ProviderTypes.RECIPES, (context, provider) -> SpecialRecipeBuilder
                        .special(() -> CleansingRecipe.INSTANCE)
                        .save(provider.output(), InfusedFoods.id("cleansing"))
                ));
    }

    private void createPotionCauldron(BlockModelGenerators blockModels) {
        var block = InfusionEntries.CAULDRON_BLOCK.value();
        var waterStill = TextureMapping.getBlockTexture(Blocks.WATER, "_still");

        blockModels.registerSimpleFlatItemModel(block);

        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
                .with(PropertyDispatch.initial(LayeredCauldronBlock.LEVEL)
                        .select(1, BlockModelGenerators.plainVariant(createCauldronVariantModel(block, ModelTemplates.CAULDRON_LEVEL1, "_level1", waterStill, blockModels)))
                        .select(2, BlockModelGenerators.plainVariant(createCauldronVariantModel(block, ModelTemplates.CAULDRON_LEVEL2, "_level2", waterStill, blockModels)))
                        .select(3, BlockModelGenerators.plainVariant(createCauldronVariantModel(block, ModelTemplates.CAULDRON_FULL, "_full", waterStill, blockModels)))
                )
        );
    }

    private Identifier createCauldronVariantModel(Block block, ModelTemplate template, String suffix, Material fluidTexture, BlockModelGenerators blockModels) {
        return template.createWithSuffix(
                block,
                suffix,
                TextureMapping.cauldron(fluidTexture),
                blockModels.modelOutput
        );
    }
}
