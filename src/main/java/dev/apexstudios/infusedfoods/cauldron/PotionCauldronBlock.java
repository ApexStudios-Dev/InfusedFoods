package dev.apexstudios.infusedfoods.cauldron;

import dev.apexstudios.apexcore.lib.component.ComponentRegistrar;
import dev.apexstudios.apexcore.lib.component.block.BaseBlockComponentHolder;
import dev.apexstudios.apexcore.lib.component.block.BlockComponent;
import dev.apexstudios.apexcore.lib.component.block.BlockComponentTypes;
import dev.apexstudios.apexcore.lib.component.block.types.LayeredCauldronBlockComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class PotionCauldronBlock extends BaseBlockComponentHolder {
    public static final VoxelShape INSIDE = box(2D, 4D, 2D, 14D, 16D, 14D);

    public static final VoxelShape SHAPE = Shapes.join(
            Shapes.block(),
            Shapes.or(
                    box(0D, 0D, 4D, 16D, 3D, 12D),
                    box(4D, 0D, 0D, 12D, 3D, 16D),
                    box(2D, 0D, 2D, 14D, 3D, 14D),
                    INSIDE
            ),
            BooleanOp.ONLY_FIRST
    );

    PotionCauldronBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void registerComponents(ComponentRegistrar<BlockComponent> registrar) {
        registrar.register(BlockComponentTypes.LAYERED_CAULDRON, builder -> builder.interactions(PotionCauldronSetup.INTERACTIONS));
    }

    @Override
    public InteractionResult useItemOn(ItemStack stack, BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        var interactionResult = PotionCauldronSetup.potionCauldron2Food(blockState, level, pos, player, hand, stack);

        if(interactionResult.consumesAction())
            return interactionResult;

        return super.useItemOn(stack, blockState, level, pos, player, hand, result);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getInteractionShape(BlockState blockState, BlockGetter level, BlockPos pos) {
        return INSIDE;
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
        var fluidLevel = blockState.getValue(LayeredCauldronBlockComponent.LEVEL);
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
