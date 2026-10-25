package dev.apexstudios.infusedfoods.core;

import dev.apexstudios.infusedfoods.api.InfusedFoods;
import dev.apexstudios.infusedfoods.api.InfusionCauldronInteractions;
import net.minecraft.core.cauldron.CauldronInteractions;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.world.poi.ExtendPoiTypesEvent;
import net.neoforged.neoforge.event.RegisterCauldronInteractionEvent;

@Mod(InfusedFoods.ID)
public final class InfusedFoodsEntryPoint {
    public InfusedFoodsEntryPoint(IEventBus modBus) {
        InfusedFoods.REGISTREE.register(modBus);

        modBus.addListener(RegisterCauldronInteractionEvent.Dispatcher.class, event -> event.register(InfusionCauldronInteractions.IDENTIFIER, InfusionCauldronInteractions.DISPATCHER));

        modBus.addListener(RegisterCauldronInteractionEvent.Interaction.class, event -> {
            CauldronInteractions.EMPTY.put(Items.POTION, InfusionCauldronInteractions.EMPTY_TO_WATER_OR_INFUSION);

            CauldronInteractions.addDefaultInteractions(InfusionCauldronInteractions.DISPATCHER);
            InfusionCauldronInteractions.DISPATCHER.put(Tags.Items.FOODS, InfusionCauldronInteractions.INFUSE);
            InfusionCauldronInteractions.DISPATCHER.put(Items.POTION, InfusionCauldronInteractions.FILL_CAULDRON);
            InfusionCauldronInteractions.DISPATCHER.put(Items.GLASS_BOTTLE, InfusionCauldronInteractions.EMPTY_CAULDRON);
        });

        modBus.addListener(ExtendPoiTypesEvent.class, event -> event.addBlockToPoi(PoiTypes.LEATHERWORKER, InfusedFoods.INFUSION_CAULDRON.value()));
    }
}
