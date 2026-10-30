package dev.apexstudios.infusedfoods.client;

import dev.apexstudios.infusedfoods.api.InfusedFoods;
import dev.apexstudios.infusedfoods.api.PotionBlockEntity;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterFluidModelsEvent;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.fluid.FluidTintSource;
import net.neoforged.neoforge.fluids.FluidStack;

@Mod(value = InfusedFoods.ID, dist = Dist.CLIENT)
public final class InfusedFoodsClientEntryPoint {
    public InfusedFoodsClientEntryPoint(IEventBus modBus) {
        modBus.addListener(RegisterColorHandlersEvent.BlockTintSources.class, event -> {
            event.register(
                    List.of(blockTint()),
                    InfusedFoods.POTION_CAULDRON_BLOCK.value()
            );

            event.register(
                    List.of(blockTint()),
                    InfusedFoods.POTION_FLUID_BLOCK.value()
            );
        });

        modBus.addListener(RegisterFluidModelsEvent.class, event -> event.register(
                new FluidModel.Unbaked(
                        new Material(Identifier.withDefaultNamespace("block/water_still")),
                        new Material(Identifier.withDefaultNamespace("block/water_flow")),
                        new Material(Identifier.withDefaultNamespace("block/water_overlay")),
                        fluidTint()
                ),
                InfusedFoods.POTION_FLUID,
                InfusedFoods.FLOWING_POTION_FLUID
        ));

        modBus.addListener(RegisterClientExtensionsEvent.class, event -> event.registerFluidType(new IClientFluidTypeExtensions() {
            private final Identifier UNDERWATER_LOCATION = Identifier.withDefaultNamespace("textures/misc/underwater.png");

            @Override
            public Identifier getRenderOverlayTexture(Minecraft mc) {
                return UNDERWATER_LOCATION;
            }
        }, InfusedFoods.POTION_FLUID_TYPE));
    }

    private static BlockTintSource blockTint() {
        return new BlockTintSource() {
            @Override
            public int color(BlockState blockState) {
                return PotionContents.BASE_POTION_COLOR;
            }

            @Override
            public int colorInWorld(BlockState blockState, BlockAndTintGetter level, BlockPos pos) {
                var potionContents = level.getModelData(pos).get(PotionBlockEntity.MODEL_PROPERTY);
                return potionContents == null ? color(blockState) : potionContents.getColor();
            }
        };
    }

    private static FluidTintSource fluidTint() {
        return new FluidTintSource() {
            @Override
            public int color(FluidState fluidState) {
                return PotionContents.BASE_POTION_COLOR;
            }

            @Override
            public int colorInWorld(FluidState fluidState, BlockState blockState, BlockAndTintGetter level, BlockPos pos) {
                var potionContents = level.getModelData(pos).get(PotionBlockEntity.MODEL_PROPERTY);
                return potionContents == null ? color(blockState) : potionContents.getColor();
            }

            @Override
            public int colorAsStack(FluidStack stack) {
                return stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).getColor();
            }
        };
    }
}
