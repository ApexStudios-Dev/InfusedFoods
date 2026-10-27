package dev.apexstudios.infusedfoods.data;

import dev.apexstudios.infusedfoods.api.InfusedFoods;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;

final class ModelProvider extends dev.apexstudios.apexcore.api.data.ModelProvider {
    ModelProvider(PackOutput output) {
        super(output, InfusedFoods.REGISTREE);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        var textures = TextureMapping.cauldron(TextureMapping.getBlockTexture(Blocks.WATER, "_still"));

        blockModels.blockStateOutput.accept(MultiVariantGenerator
                .dispatch(InfusedFoods.INFUSION_CAULDRON.value())
                .with(PropertyDispatch.initial(LayeredCauldronBlock.LEVEL)
                        .generate(level -> {
                            ModelTemplate template;
                            var suffix = "_level" + level;

                            if(level == 1) {
                                template = ModelTemplates.CAULDRON_LEVEL1;
                            } else if(level == 2) {
                                template = ModelTemplates.CAULDRON_LEVEL2;
                            } else {
                                template = ModelTemplates.CAULDRON_FULL;
                                suffix = "_full";
                            }

                            return BlockModelGenerators.plainVariant(template.createWithSuffix(
                                    InfusedFoods.INFUSION_CAULDRON.value(),
                                    suffix,
                                    textures,
                                    blockModels.modelOutput
                            ));
                        })
                )
        );
    }
}
