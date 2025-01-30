package dev.apexstudios.infusedfoods;

import dev.apexstudios.infusedfoods.cauldron.PotionCauldronClientSetup;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(value = InfusedFoods.ID, dist = Dist.CLIENT)
public final class InfusedFoodsClient {
    public InfusedFoodsClient(IEventBus modBus) {
        PotionCauldronClientSetup.register(modBus);
    }
}
