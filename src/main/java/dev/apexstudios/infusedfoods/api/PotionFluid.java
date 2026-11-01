package dev.apexstudios.infusedfoods.api;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

public sealed abstract class PotionFluid extends BaseFlowingFluid {
    public static final Identifier SOURCE_TEXTURE = InfusedFoods.POTION_FLUID.getId(false).withPrefix("block/");
    public static final Identifier FLOWING_TEXTURE = InfusedFoods.POTION_FLUID.getId(true).withPrefix("block/");
    public static final Identifier OVERLAY_TEXTURE = SOURCE_TEXTURE.withSuffix("_overlay");
    public static final Identifier UNDERWATER_TEXTURE = InfusedFoods.POTION_FLUID.getId(false).withPath(name -> "textures/misc/" + name + "_underwater.png");

    protected PotionFluid(Properties properties) {
        super(properties);
    }

    @Override
    protected void entityInside(Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier) {
        if (level.getBlockEntity(pos) instanceof PotionBlockEntity blockEntity) {
            blockEntity.applyTo(entity);
        }
    }

    @Override
    protected void spreadTo(LevelAccessor level, BlockPos pos, BlockState blockState, Direction direction, FluidState fluidState) {
        super.spreadTo(level, pos, blockState, direction, fluidState);

        if(level.getBlockEntity(pos) instanceof PotionBlockEntity blockEntity) {
            var sourcePos = pos.relative(direction.getOpposite());

            if(level.getBlockEntity(sourcePos) instanceof PotionBlockEntity sourceBlockEntity) {
                blockEntity.setPotionContents(sourceBlockEntity.getPotionContents());
            }
        }
    }

    public static final class Flowing extends PotionFluid {
        Flowing(Properties properties) {
            super(properties);

            registerDefaultState(defaultFluidState().setValue(LEVEL, 7));
        }

        @Override
        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
        }

        @Override
        public int getAmount(FluidState fluidState) {
            return fluidState.getValue(LEVEL);
        }

        @Override
        public boolean isSource(FluidState fluidState) {
            return false;
        }
    }

    public static final class Source extends PotionFluid {
        Source(Properties properties) {
            super(properties);
        }

        @Override
        public int getAmount(FluidState fluidState) {
            return 8;
        }

        @Override
        public boolean isSource(FluidState fluidState) {
            return true;
        }
    }
}
