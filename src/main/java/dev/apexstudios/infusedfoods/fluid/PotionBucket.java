package dev.apexstudios.infusedfoods.fluid;

import java.util.List;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionContents;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.fluids.FluidStack;

public final class PotionBucket extends Item {
    public static final int POTIONS_PER_BUCKET = 4;

    public PotionBucket(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).addPotionTooltip(tooltip::add, 1F * POTIONS_PER_BUCKET, context.tickRate());
    }

    public static final class ClientExtensions implements IClientFluidTypeExtensions {
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
        public ResourceLocation getOverlayTexture() {
            return IClientFluidTypeExtensions.of(NeoForgeMod.WATER_TYPE.value()).getOverlayTexture();
        }
    }
}
