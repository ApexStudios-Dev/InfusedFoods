package dev.apexstudios.infusedfoods.api;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;

public final class PotionFluidType extends FluidType {
    PotionFluidType(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack getBucket(FluidStack stack) {
        var bucket = super.getBucket(stack);
        bucket.copyFrom(DataComponents.POTION_CONTENTS, stack);
        return bucket;
    }

    @Override
    public Component getDescription(FluidStack stack) {
        var potion = stack.get(DataComponents.POTION_CONTENTS);

        if(potion == null) {
            return super.getDescription(stack);
        }

        return potion.getName(".effect.");
    }
}
