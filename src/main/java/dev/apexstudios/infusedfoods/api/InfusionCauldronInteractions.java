package dev.apexstudios.infusedfoods.api;

import com.google.common.base.Predicates;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Prediction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public interface InfusionCauldronInteractions {
    CauldronInteraction.Dispatcher DISPATCHER = new CauldronInteraction.Dispatcher();
    Identifier IDENTIFIER = InfusedFoods.REGISTREE.identifier("infusion");

    CauldronInteraction INFUSE = (blockState, level, pos, player, hand, stack) -> {
        if(!(level.getBlockEntity(pos) instanceof PotionBlockEntity blockEntity)) {
            return InteractionResult.TRY_WITH_EMPTY_HAND;
        }

        if(stack.has(DataComponents.POTION_CONTENTS)) {
            return InteractionResult.TRY_WITH_EMPTY_HAND;
        }

        if(!level.isClientSide()) {
            player.awardStat(Stats.USE_CAULDRON);
            player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
            level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1F, 1F);
            level.gameEvent(null, GameEvent.FLUID_PICKUP, pos);
        }

        var infused = stack.consumeAndReturn(1, player);
        infused.set(DataComponents.POTION_CONTENTS, blockEntity.getPotionContents());
        player.getInventory().placeItemBackInInventory(infused, Prediction.PREDICTED);

        LayeredCauldronBlock.lowerFillLevel(blockState, level, pos);
        return InteractionResult.SUCCESS;
    };

    CauldronInteraction FILL_CAULDRON_FROM_BOTTLE = fillCauldron(
            () -> Items.GLASS_BOTTLE,
            blockState -> blockState.getValue(LayeredCauldronBlock.LEVEL) < LayeredCauldronBlock.MAX_FILL_LEVEL,
            (level, pos, blockState) -> level.setBlockAndUpdate(pos, InfusedFoods.POTION_CAULDRON_BLOCK.value().defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, blockState.getValueOrElse(LayeredCauldronBlock.LEVEL, 0) + 1)),
            SoundEvents.BOTTLE_EMPTY
    );

    CauldronInteraction EMPTY_CAULDRON_TO_BOTTLE = emptyCauldron(
            () -> Items.POTION,
            Predicates.alwaysTrue(),
            (level, pos, blockState) -> LayeredCauldronBlock.lowerFillLevel(blockState, level, pos),
            SoundEvents.BOTTLE_FILL
    );

    CauldronInteraction FILL_CAULDRON_FROM_BUCKET = fillCauldron(
            () -> Items.BUCKET,
            blockState -> blockState.getValue(LayeredCauldronBlock.LEVEL) == LayeredCauldronBlock.MAX_FILL_LEVEL,
            (level, pos, blockState) -> level.setBlockAndUpdate(pos, InfusedFoods.POTION_CAULDRON_BLOCK.value().defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, LayeredCauldronBlock.MAX_FILL_LEVEL)),
            SoundEvents.BUCKET_EMPTY
    );

    CauldronInteraction EMPTY_CAULDRON_TO_BUCKET = emptyCauldron(
            InfusedFoods.POTION_BUCKET,
            blockState -> blockState.getValue(LayeredCauldronBlock.LEVEL) == LayeredCauldronBlock.MAX_FILL_LEVEL,
            (level, pos, blockState) -> level.setBlockAndUpdate(pos, Blocks.CAULDRON.defaultBlockState()),
            SoundEvents.BUCKET_FILL
    );

    private static CauldronInteraction fillCauldron(
            Supplier<? extends Item> containerItem,
            Predicate<BlockState> canFill,
            BlockSetter setBlock,
            SoundEvent emptySound
    ) {
        return (blockState, level, pos, player, hand, stack) -> {
            if(blockState.is(InfusedFoods.POTION_CAULDRON_BLOCK) && !canFill.test(blockState)) {
                return InteractionResult.TRY_WITH_EMPTY_HAND;
            }

            if(level.getBlockEntity(pos) instanceof PotionBlockEntity blockEntity && !blockEntity.isSamePotion(stack)) {
                return InteractionResult.TRY_WITH_EMPTY_HAND;
            }

            if (!level.isClientSide()) {
                player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(containerItem.get())));
                player.awardStat(Stats.FILL_CAULDRON);
                player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
                level.playSound(null, pos, emptySound, SoundSource.BLOCKS, 1F, 1F);
                level.gameEvent(null, GameEvent.FLUID_PLACE, pos);
            }

            setBlock.accept(level, pos, blockState);

            if(level.getBlockEntity(pos) instanceof PotionBlockEntity blockEntity) {
                blockEntity.setPotionContents(stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY));
            }

            return InteractionResult.SUCCESS;
        };
    }

    private static CauldronInteraction emptyCauldron(
            Supplier<? extends Item> containerItem,
            Predicate<BlockState> canEmpty,
            BlockSetter setBlock,
            SoundEvent pickupSound
    ) {
        return (blockState, level, pos, player, hand, stack) -> {
            if (!canEmpty.test(blockState)) {
                return InteractionResult.TRY_WITH_EMPTY_HAND;
            }

            if(!(level.getBlockEntity(pos) instanceof PotionBlockEntity blockEntity)) {
                return InteractionResult.TRY_WITH_EMPTY_HAND;
            }

            if (!level.isClientSide()) {
                var container = new ItemStack(containerItem.get());
                container.set(DataComponents.POTION_CONTENTS, blockEntity.getPotionContents());

                player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, container));
                player.awardStat(Stats.USE_CAULDRON);
                player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
                level.playSound(null, pos, pickupSound, SoundSource.BLOCKS, 1F, 1F);
                level.gameEvent(null, GameEvent.FLUID_PICKUP, pos);
            }

            setBlock.accept(level, pos, blockState);
            return InteractionResult.SUCCESS;
        };
    }

    @FunctionalInterface
    interface BlockSetter {
        void accept(Level level, BlockPos pos, BlockState blockState);
    }
}
