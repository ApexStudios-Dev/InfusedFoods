package dev.apexstudios.infusedfoods.data;

import dev.apexstudios.infusedfoods.common.InfusedFoods;
import dev.apexstudios.infusedfoods.common.util.InfusionEntries;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;

final class IFModelsProvider extends ModelProvider {
    IFModelsProvider(PackOutput output) {
        super(output, InfusedFoods.ID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        createPotionCauldron(blockModels);
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
