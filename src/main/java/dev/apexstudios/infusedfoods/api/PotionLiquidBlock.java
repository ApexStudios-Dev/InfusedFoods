package dev.apexstudios.infusedfoods.api;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public final class PotionLiquidBlock extends LiquidBlock implements EntityBlock {
    PotionLiquidBlock(Properties properties) {
        super(InfusedFoods.POTION_FLUID.value(), properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState blockState) {
        return new PotionBlockEntity(pos, blockState);
    }

    @Override
    public ItemStack pickupBlock(@Nullable LivingEntity user, LevelAccessor level, BlockPos pos, BlockState blockState) {
        if(blockState.getValue(LEVEL) != 0) {
            return ItemStack.EMPTY;
        }

        if(!(level.getBlockEntity(pos) instanceof PotionBlockEntity blockEntity)) {
            return ItemStack.EMPTY;
        }

        var stack = new ItemStack(InfusedFoods.POTION_BUCKET.value());
        stack.set(DataComponents.POTION_CONTENTS, blockEntity.getPotionContents());
        level.setBlock(pos, Blocks.AIR.defaultBlockState(), PotionLiquidBlock.UPDATE_ALL_IMMEDIATE);
        return stack;
    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos pos, RandomSource random) {
        if(level.getBlockEntity(pos) instanceof PotionBlockEntity blockEntity) {
            blockEntity.addParticles(level, random, blockState.getValue(LEVEL));
        }
    }
}
