package dev.apexstudios.infusedfoods.data;

import dev.apexstudios.infusedfoods.api.InfusedFoods;
import java.util.stream.Stream;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;

final class ModelProvider extends net.minecraft.client.data.models.ModelProvider {
    ModelProvider(PackOutput output) {
        super(output, InfusedFoods.ID);
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

    @Override
    protected Stream<? extends Holder<Block>> getKnownBlocks() {
        return InfusedFoods.REGISTREE.registrar(Registries.BLOCK).streamHolders();
    }

    @Override
    protected Stream<? extends Holder<Item>> getKnownItems() {
        return Stream.empty();
    }
}
