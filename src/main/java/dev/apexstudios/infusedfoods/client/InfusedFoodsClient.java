package dev.apexstudios.infusedfoods.client;

import dev.apexstudios.infusedfoods.common.InfusedFoods;
import dev.apexstudios.infusedfoods.common.cauldron.PotionCauldronBlockEntity;
import dev.apexstudios.infusedfoods.common.util.InfusionEntries;
import java.util.List;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jspecify.annotations.Nullable;

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

        modBus.addListener(RegisterClientExtensionsEvent.class, event -> event.registerFluidType(new IClientFluidTypeExtensions() {
            @Override
            public int getTintColor(FluidStack stack) {
                return stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).getColorOr(PotionContents.BASE_POTION_COLOR);
            }

            @Override
            public Identifier getFlowingTexture() {
                return IClientFluidTypeExtensions.of(NeoForgeMod.WATER_TYPE.value()).getFlowingTexture();
            }

            @Override
            public Identifier getStillTexture() {
                return IClientFluidTypeExtensions.of(NeoForgeMod.WATER_TYPE.value()).getStillTexture();
            }

            @Nullable
            @Override
            public Identifier getOverlayTexture() {
                return IClientFluidTypeExtensions.of(NeoForgeMod.WATER_TYPE.value()).getOverlayTexture();
            }
        }, InfusionEntries.POTION_FLUID_TYPE.value()));
    }
}
