package dev.apexstudios.infusedfoods.api;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public final class PotionCauldronBlock extends LayeredCauldronBlock implements EntityBlock {
    PotionCauldronBlock(Properties properties) {
        super(Biome.Precipitation.NONE, InfusionCauldronInteractions.DISPATCHER, properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState blockState) {
        return new PotionBlockEntity(pos, blockState);
    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos pos, RandomSource random) {
        if(level.getBlockEntity(pos) instanceof PotionBlockEntity blockEntity) {
            blockEntity.addParticles(level, random, blockState.getValue(LEVEL));
        }
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
        super.entityInside(state, level, pos, entity, effectApplier, isPrecise);

        if(level.getBlockEntity(pos) instanceof PotionBlockEntity blockEntity) {
            blockEntity.applyTo(entity);
        }
    }
}
