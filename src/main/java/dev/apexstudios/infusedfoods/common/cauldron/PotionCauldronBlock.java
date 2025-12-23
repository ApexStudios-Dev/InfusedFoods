package dev.apexstudios.infusedfoods.common.cauldron;

import dev.apexstudios.infusedfoods.common.util.InfusionEntries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public final class PotionCauldronBlock extends LayeredCauldronBlock implements EntityBlock {
    public PotionCauldronBlock(Properties properties) {
        super(Biome.Precipitation.RAIN, PotionCauldronInteractions.INTERACTIONS, properties);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        var interactionResult = PotionCauldronInteractions.potionCauldron2Food(blockState, level, pos, player, hand, stack);

        if(interactionResult.consumesAction())
            return interactionResult;

        return super.useItemOn(stack, blockState, level, pos, player, hand, result);
    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos pos, RandomSource random) {
        var blockEntity = InfusionEntries.CAULDRON_BLOCK_ENTITY.get(level, pos);

        if(blockEntity == null)
            return;

        var potionContent = blockEntity.getPotionContents();
        var fluidLevel = blockState.getValue(LEVEL);
        var particle = ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, potionContent.getColor());

        var x = pos.getX() + .5D;
        var y = pos.getY() + .5D + (.25D * fluidLevel);
        var z = pos.getZ() + .5D;

        for(var i = 0; i < 2; i ++) {
            var rand = random.nextInt(0, 4);

            if(rand == 0)
                level.addParticle(particle, x + .25D, y, z, 0D, 0D, 0D);
            else if(rand == 1)
                level.addParticle(particle, x - .25D, y, z, 0D, 0D, 0D);
            else if(rand == 2)
                level.addParticle(particle, x, y, z + .25D, 0D, 0D, 0D);
            else
                level.addParticle(particle, x, y, z - .25D, 0D, 0D, 0D);
        }
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState blockState) {
        return new PotionCauldronBlockEntity(pos, blockState);
    }
}
