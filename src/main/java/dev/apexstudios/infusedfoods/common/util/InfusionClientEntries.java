package dev.apexstudios.infusedfoods.common.util;

import dev.apexstudios.infusedfoods.common.cauldron.PotionCauldronBlockEntity;
import java.util.List;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.renderer.block.FluidStateModelSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.client.fluid.FluidTintSource;
import net.neoforged.neoforge.fluids.FluidStack;

public interface InfusionClientEntries {
    FluidModel.Unbaked FLUID_MODEL = new FluidModel.Unbaked(
            FluidStateModelSet.WATER_MODEL.stillMaterial(),
            FluidStateModelSet.WATER_MODEL.flowingMaterial(),
            FluidStateModelSet.WATER_MODEL.overlayMaterial(),
            new FluidTintSource() {
                @Override
                public int color(FluidState state) {
                    return PotionContents.BASE_POTION_COLOR;
                }

                @Override
                public int colorAsStack(FluidStack stack) {
                    return stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).getColorOr(PotionContents.BASE_POTION_COLOR);
                }
            }
    );

    List<BlockTintSource> BLOCK_TINT_SOURCES = List.of(new BlockTintSource() {
        @Override
        public int color(BlockState blockState) {
            return PotionContents.BASE_POTION_COLOR;
        }

        @Override
        public int colorInWorld(BlockState blockState, BlockAndTintGetter level, BlockPos pos) {
            if (level.getBlockEntity(pos) instanceof PotionCauldronBlockEntity blockEntity) {
                return blockEntity.getPotionContents().getColor();
            }

            return BlockTintSource.super.colorInWorld(blockState, level, pos);
        }
    });
}
