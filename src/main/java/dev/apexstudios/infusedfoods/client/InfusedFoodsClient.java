package dev.apexstudios.infusedfoods.client;

import dev.apexstudios.infusedfoods.common.InfusedFoods;
import dev.apexstudios.infusedfoods.common.cauldron.PotionCauldronBlockEntity;
import dev.apexstudios.infusedfoods.common.util.InfusionEntries;
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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterFluidModelsEvent;
import net.neoforged.neoforge.client.fluid.FluidTintSource;
import net.neoforged.neoforge.fluids.FluidStack;

@Mod(value = InfusedFoods.ID, dist = Dist.CLIENT)
public final class InfusedFoodsClient {
    public InfusedFoodsClient(IEventBus modBus) {
        modBus.addListener(RegisterColorHandlersEvent.BlockTintSources.class, event -> event.register(List.of(new BlockTintSource() {
            @Override
            public int color(BlockState blockState) {
                return PotionContents.BASE_POTION_COLOR;
            }

            @Override
            public int colorInWorld(BlockState blockState, BlockAndTintGetter level, BlockPos pos) {
                return level.getBlockEntity(pos) instanceof PotionCauldronBlockEntity blockEntity ? blockEntity.getPotionContents().getColor() : color(blockState);
            }
        }), InfusionEntries.CAULDRON_BLOCK.value()));

        modBus.addListener(RegisterFluidModelsEvent.class, event -> event.register(new FluidModel.Unbaked(
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
                }),
                InfusionEntries.POTION_FLUID
        ));
    }
}
