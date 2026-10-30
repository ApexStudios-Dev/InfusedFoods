package dev.apexstudios.infusedfoods.core;

import dev.apexstudios.infusedfoods.api.InfusedFoods;
import dev.apexstudios.infusedfoods.api.InfusionCauldronInteractions;
import dev.apexstudios.infusedfoods.api.PotionBucketResourcHandler;
import net.minecraft.core.cauldron.CauldronInteractions;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.item.DispensibleContainerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.world.poi.ExtendPoiTypesEvent;
import net.neoforged.neoforge.event.RegisterCauldronInteractionEvent;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.RegisterCauldronFluidContentEvent;

@Mod(InfusedFoods.ID)
public final class InfusedFoodsEntryPoint {
    public InfusedFoodsEntryPoint(IEventBus modBus) {
        InfusedFoods.REGISTREE.register(modBus);

        modBus.addListener(RegisterCauldronInteractionEvent.Dispatcher.class, event -> event.register(InfusionCauldronInteractions.IDENTIFIER, InfusionCauldronInteractions.DISPATCHER));

        modBus.addListener(RegisterCauldronInteractionEvent.Interaction.class, event -> {
            var defaultPotion = CauldronInteractions.EMPTY.items.get(Items.POTION);

            CauldronInteractions.EMPTY.put(Items.POTION, (blockState, level, pos, player, hand, stack) -> {
                var result = defaultPotion.interact(blockState, level, pos, player, hand, stack);

                if(!result.consumesAction()) {
                    result = InfusionCauldronInteractions.FILL_CAULDRON_FROM_BOTTLE.interact(blockState, level, pos, player, hand, stack);
                }

                return result;
            });

            CauldronInteractions.addDefaultInteractions(InfusionCauldronInteractions.DISPATCHER);
            InfusionCauldronInteractions.DISPATCHER.put(Tags.Items.FOODS, InfusionCauldronInteractions.INFUSE);
            InfusionCauldronInteractions.DISPATCHER.put(Items.POTION, InfusionCauldronInteractions.FILL_CAULDRON_FROM_BOTTLE);
            InfusionCauldronInteractions.DISPATCHER.put(Items.GLASS_BOTTLE, InfusionCauldronInteractions.EMPTY_CAULDRON_TO_BOTTLE);
            InfusionCauldronInteractions.DISPATCHER.put(Items.BUCKET, InfusionCauldronInteractions.EMPTY_CAULDRON_TO_BUCKET);

            event.registerToAll(InfusedFoods.POTION_BUCKET.value(), InfusionCauldronInteractions.FILL_CAULDRON_FROM_BUCKET);
        });

        modBus.addListener(RegisterCauldronFluidContentEvent.class, event -> event.register(
                InfusedFoods.POTION_CAULDRON_BLOCK.value(),
                InfusedFoods.POTION_FLUID.value(),
                FluidType.BUCKET_VOLUME,
                LayeredCauldronBlock.LEVEL
        ));

        modBus.addListener(ExtendPoiTypesEvent.class, event -> event.addBlockToPoi(PoiTypes.LEATHERWORKER, InfusedFoods.POTION_CAULDRON_BLOCK.value()));

        modBus.addListener(RegisterCapabilitiesEvent.class, event -> event.registerItem(
                Capabilities.Fluid.ITEM,
                (_, access) -> new PotionBucketResourcHandler(access),
                InfusedFoods.POTION_BUCKET
        ));

        modBus.addListener(FMLCommonSetupEvent.class, event -> event.enqueueWork(() -> DispenserBlock.registerBehavior(
                InfusedFoods.POTION_BUCKET,
                new DefaultDispenseItemBehavior() {
                    private final DispenseItemBehavior defaultBehavior = new DefaultDispenseItemBehavior();

                    @Override
                    protected ItemStack execute(BlockSource source, ItemStack dispensed) {
                        var bucket = (DispensibleContainerItem) dispensed.getItem();
                        var target = source.pos().relative(source.state().getValue(DispenserBlock.FACING));
                        var level = source.level();

                        if(bucket.emptyContents(null, level, target, null, dispensed)) {
                            bucket.checkExtraContent(null, level, dispensed, target);
                            return consumeWithRemainder(source, dispensed, new ItemStack(Items.BUCKET));
                        }

                        return defaultBehavior.dispense(source, dispensed);
                    }
                }
        )));
    }
}
