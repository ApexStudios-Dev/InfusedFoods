package dev.apexstudios.infusedfoods.fluid;

import dev.apexstudios.infusedfoods.InfusedFoods;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.capability.wrappers.FluidBucketWrapper;

public final class PotionBucketHandler extends FluidBucketWrapper {
    public PotionBucketHandler(ItemStack container) {
        super(container);
    }

    @Override
    public FluidStack getFluid() {
        if (!container.has(DataComponents.POTION_CONTENTS))
            return FluidStack.EMPTY;

        var stack = new FluidStack(PotionFluidSetup.FLUID, FluidType.BUCKET_VOLUME);
        stack.copyFrom(container, DataComponents.POTION_CONTENTS);
        return stack;
    }

    @Override
    public boolean canFillFluidType(FluidStack fluid) {
        var contents = fluid.get(DataComponents.POTION_CONTENTS);
        return contents != null && InfusedFoods.isValidPotion(contents);
    }
}
