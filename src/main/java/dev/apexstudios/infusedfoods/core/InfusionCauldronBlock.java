package dev.apexstudios.infusedfoods.core;

import dev.apexstudios.infusedfoods.api.InfusionCauldronInteractions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public final class InfusionCauldronBlock extends LayeredCauldronBlock implements EntityBlock {
    public InfusionCauldronBlock(Properties properties) {
        super(Biome.Precipitation.NONE, InfusionCauldronInteractions.DISPATCHER, properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState blockState) {
        return new InfusionCauldronBlockEntity(pos, blockState);
    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos pos, RandomSource random) {
        if(!(level.getBlockEntity(pos) instanceof InfusionCauldronBlockEntity blockEntity)) {
            return;
        }

        var potionContent = blockEntity.getPotionContents();
        var fluidLevel = blockState.getValue(LEVEL);
        var particle = ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, potionContent.getColor());

        var x = pos.getX() + .5D;
        var y = pos.getY() + .5D + (.25D * fluidLevel);
        var z = pos.getZ() + .5D;

        for(var i = 0; i < 2; i ++) {
            var rand = random.nextInt(0, 4);

            if(rand == 0) {
                level.addParticle(particle, x + .25D, y, z, 0D, 0D, 0D);
            } else if(rand == 1) {
                level.addParticle(particle, x - .25D, y, z, 0D, 0D, 0D);
            } else if(rand == 2) {
                level.addParticle(particle, x, y, z + .25D, 0D, 0D, 0D);
            } else {
                level.addParticle(particle, x, y, z - .25D, 0D, 0D, 0D);
            }
        }
    }
}
