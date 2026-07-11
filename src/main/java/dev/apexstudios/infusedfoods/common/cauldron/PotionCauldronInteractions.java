package dev.apexstudios.infusedfoods.common.cauldron;

import dev.apexstudios.apexcore.api.block.BlockHelper;
import dev.apexstudios.infusedfoods.common.InfusedFoods;
import dev.apexstudios.infusedfoods.common.util.InfusionEntries;
import dev.apexstudios.infusedfoods.common.util.InfusionUtil;
import dev.apexstudios.infusedfoods.mixin.CauldronInterationDispatcherAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.cauldron.CauldronInteractions;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public interface PotionCauldronInteractions {
    CauldronInteraction.Dispatcher INTERACTIONS = CauldronInteractions.newDispatcher(InfusedFoods.id("potion_cauldron"));

    static void registerInteractions() {
        // Empty Cauldron
        var vanillaWaterBottleInteraction = ((CauldronInterationDispatcherAccessor) CauldronInteractions.EMPTY).InfusedFoods$getItems().getOrDefault(Items.POTION, CauldronInteraction.DEFAULT);
        CauldronInteractions.EMPTY.put(Items.POTION, any(InteractionResult.TRY_WITH_EMPTY_HAND,
                // vanilla (Water Bottle -> Water Cauldron)
                vanillaWaterBottleInteraction,
                // ours (Potion -> Potion Cauldron)
                PotionCauldronInteractions::cauldron2PotionCauldron
        ));

        // Potion Cauldron
        INTERACTIONS.put(Items.POTION, PotionCauldronInteractions::fromPotion);
        INTERACTIONS.put(Items.GLASS_BOTTLE, PotionCauldronInteractions::toPotion);
    }

    private static InteractionResult cauldron2PotionCauldron(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack stack) {
        var contents = stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);

        if(contents != PotionContents.EMPTY && contents.hasEffects()) {
            player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
            player.awardStat(Stats.USE_CAULDRON);
            player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
            level.setBlockAndUpdate(pos, InfusionEntries.CAULDRON_BLOCK.value().defaultBlockState());
            ((PotionCauldronBlockEntity) level.getBlockEntity(pos)).setPotionContents(contents);
            level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1F, 1F);
            level.gameEvent(null, GameEvent.FLUID_PLACE, pos);

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    private static InteractionResult fromPotion(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack stack) {
        var contents = stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
        var fluidLevel = blockState.getValue(LayeredCauldronBlock.LEVEL);

        if(fluidLevel < LayeredCauldronBlock.MAX_FILL_LEVEL && setPotionContents(level, pos, contents, true, true, hand, player, stack))
            return InteractionResult.SUCCESS;

        return InteractionResult.PASS;
    }

    private static InteractionResult toPotion(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack stack) {
        var contents = BlockHelper.getBlockEntityOrThrow(level, pos, InfusionEntries.CAULDRON_BLOCK_ENTITY).getPotionContents();
        var fluidLevel = blockState.getValue(LayeredCauldronBlock.LEVEL);

        if(fluidLevel >= LayeredCauldronBlock.MIN_FILL_LEVEL && setPotionContents(level, pos, contents, false, true, hand, player, stack))
            return InteractionResult.SUCCESS;

        return InteractionResult.PASS;
    }

    static InteractionResult potionCauldron2Food(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack stack) {
        if(InfusionUtil.isValidFood(stack) && !stack.has(DataComponents.POTION_CONTENTS)) {
            var blockEntity = BlockHelper.getBlockEntityOrThrow(level, pos, InfusionEntries.CAULDRON_BLOCK_ENTITY);
            var contents = blockEntity.getPotionContents();
            var filled = stack.copyWithCount(1);
            filled.set(DataComponents.POTION_CONTENTS, contents);

            stack.consume(1, player);
            player.addItem(filled);

            changeFillLevel(level, pos, blockState, false, false, player, stack);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    private static CauldronInteraction any(InteractionResult defaultResult, CauldronInteraction... interactions) {
        return (blockState, level, pos, player, hand, stack) -> {
            for(var interaction : interactions) {
                var result = interaction.interact(blockState, level, pos, player, hand, stack);

                if(result.consumesAction())
                    return result;
            }

            return defaultResult;
        };
    }

    private static boolean setPotionContents(Level level, BlockPos pos, PotionContents newContents, boolean increment, boolean sounds, InteractionHand hand, Player player, ItemStack stack) {
        var blockState = level.getBlockState(pos);
        var blockEntity = BlockHelper.getBlockEntityOrThrow(level, pos, InfusionEntries.CAULDRON_BLOCK_ENTITY);
        var current = blockEntity.getPotionContents();

        var itemHasPotion = newContents == PotionContents.EMPTY || newContents.hasEffects();
        var potion = newContents.potion().orElse(Potions.WATER);
        var isValidPotion = current.is(potion) || InfusionUtil.isValidPotion(potion);

        if(itemHasPotion && isValidPotion) {
            blockEntity.setPotionContents(newContents);

            var newStack = increment ? new ItemStack(Items.GLASS_BOTTLE) : createPotion(newContents);
            player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, newStack));

            changeFillLevel(level, pos, blockState, increment, sounds, player, stack);
            return true;
        }

        return false;
    }

    private static ItemStack createPotion(PotionContents contents) {
        var stack = new ItemStack(Items.POTION);
        stack.set(DataComponents.POTION_CONTENTS, contents);
        return stack;
    }

    private static void changeFillLevel(Level level, BlockPos pos, BlockState blockState, boolean increment, boolean sounds, Player player, ItemStack stack) {
        player.awardStat(Stats.USE_CAULDRON);
        player.awardStat(Stats.ITEM_USED.get(stack.getItem()));

        if (increment) {
            var newBlockState = blockState.setValue(LayeredCauldronBlock.LEVEL, blockState.getValue(LayeredCauldronBlock.LEVEL) + 1);
            level.setBlockAndUpdate(pos, newBlockState);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(newBlockState));

            if(sounds)
                level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1F, 1F);

            level.gameEvent(null, GameEvent.FLUID_PLACE, pos);
        } else {
            LayeredCauldronBlock.lowerFillLevel(blockState, level, pos);

            if(sounds)
                level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1F, 1F);

            level.gameEvent(null, GameEvent.FLUID_PICKUP, pos);
        }
    }
}
