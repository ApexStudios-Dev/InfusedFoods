package dev.apexstudios.infusedfoods.cauldron;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;

public final class PotionCauldronBlock extends LayeredCauldronBlock {
    PotionCauldronBlock(Properties properties) {
        super(Biome.Precipitation.RAIN, PotionCauldronSetup.INTERACTIONS, properties);
    }

    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos pos, BlockState oldBlockState, boolean movedByPiston) {
        if(!blockState.is(oldBlockState.getBlock()))
            CauldronPotionHandler.set(level, pos, PotionContents.EMPTY);

        super.onPlace(blockState, level, pos, oldBlockState, movedByPiston);
    }

    @Override
    public void affectNeighborsAfterRemoval(BlockState blockState, ServerLevel level, BlockPos pos, boolean movedByPiston) {
        CauldronPotionHandler.set(level, pos, PotionContents.EMPTY);
        super.affectNeighborsAfterRemoval(blockState, level, pos, movedByPiston);
    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos pos, RandomSource random) {
        var potionContent = CauldronPotionHandler.get(level, pos);
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
}
