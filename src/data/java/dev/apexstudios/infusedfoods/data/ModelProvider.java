package dev.apexstudios.infusedfoods.data;

import dev.apexstudios.infusedfoods.api.InfusedFoods;
import java.util.Optional;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.neoforged.neoforge.client.model.item.DynamicFluidContainerModel;
import net.neoforged.neoforge.common.NeoForgeMod;

final class ModelProvider extends dev.apexstudios.apexcore.api.data.ModelProvider {
    ModelProvider(PackOutput output) {
        super(output, InfusedFoods.REGISTREE);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        var textures = TextureMapping.cauldron(TextureMapping.getBlockTexture(Blocks.WATER, "_still"));

        blockModels.blockStateOutput.accept(MultiVariantGenerator
                .dispatch(InfusedFoods.POTION_CAULDRON_BLOCK.value())
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
                                    InfusedFoods.POTION_CAULDRON_BLOCK.value(),
                                    suffix,
                                    textures,
                                    blockModels.modelOutput
                            ));
                        })
                )
        );

        blockModels.createNonTemplateModelBlock(InfusedFoods.POTION_FLUID_BLOCK.value());
        //itemModels.generateFlatItem(InfusedFoods.POTION_BUCKET.value(), ModelTemplates.FLAT_ITEM);

        itemModels.itemModelOutput.accept(
                InfusedFoods.POTION_BUCKET.value(),
                new DynamicFluidContainerModel.Unbaked(
                        new DynamicFluidContainerModel.Textures(
                                Optional.empty(),
                                Optional.of(new Material(ModelLocationUtils.getModelLocation(Items.BUCKET))),
                                Optional.of(new Material(Identifier.fromNamespaceAndPath(NeoForgeMod.MOD_ID, "item/mask/bucket_fluid_drip"))),
                                Optional.empty()
                        ),
                        InfusedFoods.POTION_FLUID.value(),
                        false,
                        false,
                        false,
                        false
                )
        );
    }
}
