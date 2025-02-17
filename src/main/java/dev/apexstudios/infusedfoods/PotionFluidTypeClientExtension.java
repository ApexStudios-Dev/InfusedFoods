package dev.apexstudios.infusedfoods;

import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.alchemy.PotionContents;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

final class PotionFluidTypeClientExtension implements IClientFluidTypeExtensions {
    @Override
    public int getTintColor(FluidStack stack) {
        return stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).getColorOr(PotionContents.BASE_POTION_COLOR);
    }

    @Override
    public ResourceLocation getFlowingTexture() {
        return IClientFluidTypeExtensions.of(NeoForgeMod.WATER_TYPE.value()).getFlowingTexture();
    }

    @Override
    public ResourceLocation getStillTexture() {
        return IClientFluidTypeExtensions.of(NeoForgeMod.WATER_TYPE.value()).getStillTexture();
    }

    @Override
    public @Nullable ResourceLocation getOverlayTexture() {
        return IClientFluidTypeExtensions.of(NeoForgeMod.WATER_TYPE.value()).getOverlayTexture();
    }
}
