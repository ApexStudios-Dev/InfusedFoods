package dev.apexstudios.infusedfoods.cauldron;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.alchemy.PotionContents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

public interface PotionCauldronClientSetup {
    static void register(IEventBus modBus) {
        modBus.addListener(RegisterColorHandlersEvent.Block.class, event -> event.register((blockState, level, pos, tintIndex) -> {
            var clientLevel = Minecraft.getInstance().level;
            return clientLevel == null || pos == null ? PotionContents.BASE_POTION_COLOR : CauldronPotionHandler.get(clientLevel, pos).getColor();
        }, PotionCauldronSetup.BLOCK.value()));
    }
}
