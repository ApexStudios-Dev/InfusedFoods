package dev.apexstudios.infusedfoods.cauldron;

import dev.apexstudios.apexcore.lib.component.block.types.LayeredCauldronBlockComponent;
import dev.apexstudios.infusedfoods.InfusedFoods;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public final class PotionCauldronPotionFluidHandler implements IFluidHandler {
    private final Level level;
    private final BlockPos pos;

    public PotionCauldronPotionFluidHandler(Level level, BlockPos pos) {
        this.level = level;
        this.pos = pos;
    }

    private FluidStack getFluid() {
        var content = CauldronPotionHandler.get(level, pos);

        if(content == PotionContents.EMPTY)
            return FluidStack.EMPTY;

        var fluidLevel = level.getBlockState(pos).getValue(LayeredCauldronBlockComponent.LEVEL);
        var amount = FluidType.BUCKET_VOLUME * fluidLevel / LayeredCauldronBlockComponent.MAX_FILL_LEVEL;

        var fluid = InfusedFoods.POTION_FLUID.toStack(amount);
        fluid.set(DataComponents.POTION_CONTENTS, content);
        return fluid;
    }

    @Override
    public int getTanks() {
        return 1;
    }

    @Override
    public FluidStack getFluidInTank(int tank) {
        return getFluid();
    }

    @Override
    public int getTankCapacity(int tank) {
        return 0;
    }

    @Override
    public boolean isFluidValid(int tank, FluidStack stack) {
        return false;
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        return 0;
    }

    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        return null;
    }

    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        return null;
    }
}
