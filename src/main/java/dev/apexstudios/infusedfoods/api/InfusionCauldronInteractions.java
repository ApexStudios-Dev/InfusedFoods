package dev.apexstudios.infusedfoods.api;

import dev.apexstudios.infusedfoods.core.InfusionCauldronBlockEntity;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Prediction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.gameevent.GameEvent;

public interface InfusionCauldronInteractions {
    CauldronInteraction.Dispatcher DISPATCHER = new CauldronInteraction.Dispatcher();
    Identifier IDENTIFIER = InfusedFoods.REGISTREE.identifier("infusion");

    CauldronInteraction INFUSE = (blockState, level, pos, player, hand, stack) -> {
        if(!(level.getBlockEntity(pos) instanceof InfusionCauldronBlockEntity blockEntity)) {
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

        var split = stack.consumeAndReturn(1, player);
        split.set(DataComponents.POTION_CONTENTS, blockEntity.getPotionContents());
        player.getInventory().placeItemBackInInventory(split, Prediction.PREDICTED);

        LayeredCauldronBlock.lowerFillLevel(blockState, level, pos);
        return InteractionResult.SUCCESS;
    };

    CauldronInteraction FILL_CAULDRON = (blockState, level, pos, player, hand, stack) -> {
        if(!(level.getBlockEntity(pos) instanceof InfusionCauldronBlockEntity blockEntity)) {
            return InteractionResult.TRY_WITH_EMPTY_HAND;
        }

        if(blockState.getValue(LayeredCauldronBlock.LEVEL) == LayeredCauldronBlock.MAX_FILL_LEVEL) {
            return InteractionResult.TRY_WITH_EMPTY_HAND;
        }

        var potion = stack.get(DataComponents.POTION_CONTENTS);

        if(potion == null || potion.is(InfusedFoods.INFUSION_BLACKLIST) || blockEntity.getPotionContents() != potion) {
            return InteractionResult.TRY_WITH_EMPTY_HAND;
        }

        if(!level.isClientSide()) {
            player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
            player.awardStat(Stats.USE_CAULDRON);
            player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
            level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1F, 1F);
            level.gameEvent(null, GameEvent.FLUID_PLACE, pos);
        }

        level.setBlockAndUpdate(pos, blockState.cycle(LayeredCauldronBlock.LEVEL));
        return InteractionResult.SUCCESS;
    };

    CauldronInteraction EMPTY_CAULDRON = (blockState, level, pos, player, hand, stack) -> {
        if (!(level.getBlockEntity(pos) instanceof InfusionCauldronBlockEntity blockEntity)) {
            return InteractionResult.TRY_WITH_EMPTY_HAND;
        }

        if(!level.isClientSide()) {
            var potion = new ItemStack(Items.POTION);
            potion.set(DataComponents.POTION_CONTENTS, blockEntity.getPotionContents());

            player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, potion));
            player.awardStat(Stats.USE_CAULDRON);
            player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
            level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1F, 1F);
            level.gameEvent(null, GameEvent.FLUID_PICKUP, pos);
        }

        LayeredCauldronBlock.lowerFillLevel(blockState, level, pos);
        return InteractionResult.SUCCESS;
    };

    CauldronInteraction EMPTY_TO_WATER_OR_INFUSION = (blockState, level, pos, player, hand, stack) -> {
        var potion = stack.get(DataComponents.POTION_CONTENTS);

        if(potion == null) {
            return InteractionResult.TRY_WITH_EMPTY_HAND;
        }

        var block = Blocks.WATER_CAULDRON;

        if(!potion.is(Potions.WATER)) {
            block = InfusedFoods.INFUSION_CAULDRON.value();
        }

        if(!level.isClientSide()) {
            player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
            player.awardStat(Stats.USE_CAULDRON);
            player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
            level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1F, 1F);
            level.gameEvent(null, GameEvent.FLUID_PLACE, pos);
        }

        level.setBlockAndUpdate(pos, block.defaultBlockState());

        if(level.getBlockEntity(pos) instanceof InfusionCauldronBlockEntity blockEntity) {
            blockEntity.setPotionContents(potion);
        }

        return InteractionResult.SUCCESS;
    };
}
