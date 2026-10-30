package dev.apexstudios.infusedfoods.api;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.PointedDripstoneBlock;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;

public final class PotionFluidType extends FluidType {
    PotionFluidType() {
        super(Properties.create()
                .fallDistanceModifier(0F)
                .canExtinguish(true)
                .canConvertToSource(false)
                .supportsBoating(true)
                .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
                .sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH)
                .canHydrate(true)
                .isWaterLike(true)
                .addDripstoneDripping(
                        PointedDripstoneBlock.WATER_TRANSFER_PROBABILITY_PER_RANDOM_TICK,
                        ParticleTypes.DRIPPING_DRIPSTONE_WATER,
                        InfusedFoods.POTION_CAULDRON_BLOCK.value(),
                        SoundEvents.POINTED_DRIPSTONE_DRIP_WATER_INTO_CAULDRON
                )
        );
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
