package dev.apexstudios.infusedfoods.cauldron;

import dev.apexstudios.apexcore.lib.component.block.types.LayeredCauldronBlockComponent;
import dev.apexstudios.apexcore.lib.registree.holder.DeferredAttachment;
import dev.apexstudios.apexcore.lib.registree.holder.DeferredBlock;
import dev.apexstudios.infusedfoods.InfusedFoods;
import dev.apexstudios.infusedfoods.fluid.PotionFluidSetup;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.RegisterCauldronFluidContentEvent;
import net.neoforged.neoforge.network.PacketDistributor;

public interface PotionCauldronSetup {
    CauldronInteraction.InteractionMap INTERACTIONS = CauldronInteraction.newInteractionMap(InfusedFoods.id("potion_cauldron"));

    DeferredAttachment<CauldronPotionHandler> ATTACHMENT = InfusedFoods.REGISTREE.registerAttachment(
            "cauldron_potions",
            builder -> builder
                    .serialize(new CauldronPotionHandler.Serializer())
                    .copyHandler((attachment, holder, provider) -> attachment.copy()),
            CauldronPotionHandler::new
    );

    DeferredBlock<PotionCauldronBlock> BLOCK = InfusedFoods.REGISTREE.registerBlock("potion_cauldron", PotionCauldronBlock::new, BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE)
            .requiresCorrectToolForDrops()
            .strength(2F)
            .noOcclusion()
    );

    static void register(IEventBus modBus) {
        modBus.addListener(FMLCommonSetupEvent.class, event -> event.enqueueWork(PotionCauldronSetup::registerInteractions));
        modBus.addListener(RegisterCauldronFluidContentEvent.class, event -> event.register(BLOCK.value(), PotionFluidSetup.FLUID.value(), FluidType.BUCKET_VOLUME, LayeredCauldronBlock.LEVEL));

        NeoForge.EVENT_BUS.addListener(PlayerEvent.PlayerLoggedInEvent.class, event -> {
            if(event.getEntity() instanceof ServerPlayer player)
                PacketDistributor.sendToPlayer(player, new ClientboundSyncPotionHandler(CauldronPotionHandler.getInstance(player.level())));
        });

        NeoForge.EVENT_BUS.addListener(PlayerEvent.PlayerChangedDimensionEvent.class, event -> {
            if(event.getEntity() instanceof ServerPlayer player)
                PacketDistributor.sendToPlayer(player, new ClientboundSyncPotionHandler(CauldronPotionHandler.getInstance(player.level())));
        });
    }

    private static void registerInteractions() {
        // Empty Cauldron
        var map = CauldronInteraction.EMPTY.map();

        map.put(Items.POTION, any(InteractionResult.TRY_WITH_EMPTY_HAND,
                // vanilla (Water Bottle -> Water Cauldron)
                PotionCauldronSetup::waterBottle2WaterCauldron,
                // ours (Potion -> Potion Cauldron)
                PotionCauldronSetup::potion2PotionCauldron
        ));

        map.put(PotionFluidSetup.BUCKET.value(), PotionCauldronSetup::bucket2PotionCauldron);

        // Potion Cauldron
        map = INTERACTIONS.map();
        map.put(Items.POTION, PotionCauldronSetup::potion2PotionCauldron);
        map.put(Items.GLASS_BOTTLE, PotionCauldronSetup::potionCauldron2Potion);
        map.put(Items.BUCKET, PotionCauldronSetup::potionCauldron2Bucket);
    }

    static InteractionResult waterBottle2WaterCauldron(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack stack) {
        var contents = stack.get(DataComponents.POTION_CONTENTS);

        if(contents != null && contents.is(Potions.WATER)) {
            if(!level.isClientSide) {
                var item = stack.getItem();
                player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
                player.awardStat(Stats.USE_CAULDRON);
                player.awardStat(Stats.ITEM_USED.get(item));
                level.setBlockAndUpdate(pos, Blocks.WATER_CAULDRON.defaultBlockState());
                level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1F, 1F);
                level.gameEvent(null, GameEvent.FLUID_PLACE, pos);
            }
        }

        return InteractionResult.PASS;
    }

    static InteractionResult potion2PotionCauldron(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack stack) {
        var current = CauldronPotionHandler.get(level, pos);
        var contents = stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
        var hasPotion = contents != PotionContents.EMPTY && contents.hasEffects();
        var potion = contents.potion().orElse(Potions.WATER);
        var isValidPotion = current == PotionContents.EMPTY || current.is(potion) || InfusedFoods.isValidPotion(potion);
        var fluidLevel = blockState.getValueOrElse(LayeredCauldronBlockComponent.LEVEL, 0);

        if(hasPotion && fluidLevel < LayeredCauldronBlockComponent.MAX_FILL_LEVEL && isValidPotion) {
            if(!level.isClientSide) {
                player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
                player.awardStat(Stats.USE_CAULDRON);
                player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
                level.setBlockAndUpdate(pos, blockState.hasProperty(LayeredCauldronBlockComponent.LEVEL) ? blockState.setValue(LayeredCauldronBlockComponent.LEVEL, fluidLevel + 1) : BLOCK.value().defaultBlockState());
                level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1F, 1F);
                level.gameEvent(null, GameEvent.FLUID_PLACE, pos);

                CauldronPotionHandler.set(level, pos, contents);
            }
        }

        return InteractionResult.PASS;
    }

    static InteractionResult bucket2PotionCauldron(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack stack) {
        var contents = stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
        var potion = contents.potion().orElse(Potions.WATER);

        if(contents.hasEffects() && InfusedFoods.isValidPotion(potion) && !blockState.hasProperty(LayeredCauldronBlockComponent.LEVEL)) {
            if(!level.isClientSide) {
                var item = stack.getItem();
                player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.BUCKET)));
                player.awardStat(Stats.USE_CAULDRON);
                player.awardStat(Stats.ITEM_USED.get(item));
                level.setBlockAndUpdate(pos, BLOCK.value().defaultBlockState().setValue(LayeredCauldronBlockComponent.LEVEL, LayeredCauldronBlockComponent.MAX_FILL_LEVEL));
                level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1F, 1F);
                level.gameEvent(null, GameEvent.FLUID_PLACE, pos);

                CauldronPotionHandler.set(level, pos, contents);
            }
        }

        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }

    static InteractionResult potionCauldron2Potion(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack stack) {
        if(!level.isClientSide) {
            var contents = CauldronPotionHandler.get(level, pos);
            var filled = new ItemStack(Items.POTION);
            filled.set(DataComponents.POTION_CONTENTS, contents);

            player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, filled));
            player.awardStat(Stats.USE_CAULDRON);
            player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
            LayeredCauldronBlock.lowerFillLevel(blockState, level, pos);
            level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1F, 1F);
            level.gameEvent(null, GameEvent.FLUID_PICKUP, pos);

            if(blockState.getValue(LayeredCauldronBlockComponent.LEVEL) <= LayeredCauldronBlockComponent.MIN_FILL_LEVEL)
                CauldronPotionHandler.set(level, pos, PotionContents.EMPTY);
        }

        return InteractionResult.SUCCESS;
    }

    static InteractionResult potionCauldron2Bucket(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack stack) {
        var fluidLevel = blockState.getValueOrElse(LayeredCauldronBlockComponent.LEVEL, 0);

        if(fluidLevel >= LayeredCauldronBlockComponent.MAX_FILL_LEVEL) {
            if(!level.isClientSide) {
                var contents = CauldronPotionHandler.get(level, pos);
                var filled = PotionFluidSetup.BUCKET.toStack();
                filled.set(DataComponents.POTION_CONTENTS, contents);

                player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, filled));
                player.awardStat(Stats.USE_CAULDRON);
                player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
                level.setBlockAndUpdate(pos, Blocks.CAULDRON.defaultBlockState());
                level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1F, 1F);
                level.gameEvent(null, GameEvent.FLUID_PICKUP, pos);

                CauldronPotionHandler.set(level, pos, PotionContents.EMPTY);
            }

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }

    static InteractionResult potionCauldron2Food(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack stack) {
        if(InfusedFoods.isValidFood(stack) && !stack.has(DataComponents.POTION_CONTENTS)) {
            if(!level.isClientSide) {
                var contents = CauldronPotionHandler.get(level, pos);
                var filled = stack.copyWithCount(1);
                filled.set(DataComponents.POTION_CONTENTS, contents);

                stack.consume(1, player);
                player.addItem(filled);

                player.awardStat(Stats.USE_CAULDRON);
                player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
                LayeredCauldronBlock.lowerFillLevel(blockState, level, pos);
                // TODO: dip sounds?
                // level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1F, 1F);
                // level.gameEvent(null, GameEvent.FLUID_PICKUP, pos);

                if(blockState.getValue(LayeredCauldronBlockComponent.LEVEL) <= LayeredCauldronBlockComponent.MIN_FILL_LEVEL)
                    CauldronPotionHandler.set(level, pos, PotionContents.EMPTY);
            }

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }

    static CauldronInteraction any(InteractionResult defaultResult, CauldronInteraction... interactions) {
        return (blockState, level, pos, player, hand, stack) -> {
            for(var interaction : interactions) {
                var result = interaction.interact(blockState, level, pos, player, hand, stack);

                if(result.consumesAction())
                    return result;
            }

            return defaultResult;
        };
    }
}
