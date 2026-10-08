package dev.apexstudios.infusedfoods.data;

import dev.apexstudios.apexcore.api.util.ApexUtil;
import dev.apexstudios.infusedfoods.common.InfusedFoods;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.data.event.GatherDataRegistryEntriesEvent;

@Mod(value = InfusedFoods.ID, dist = Dist.CLIENT)
public final class InfusedFoodsDataEntryPoint {
    public InfusedFoodsDataEntryPoint(IEventBus modBus) {
        modBus.addListener(GatherDataEvent.Client.class, event -> {
            event.createProvider(IFPotionTagsProvider::new);
            event.createBlockAndItemTags(IFBlockTagsProvider::new, IFItemTagsProvider::new);
            event.createProvider(IFFluidTagsProvider::new);
            event.createProvider(IFLanguageProvider::new);
            event.createProvider(IFModelsProvider::new);
            event.createProvider(output -> ApexUtil.createMetadataProvider(output, Component.literal("InfusedFoods resources"), PackType.SERVER_DATA));
        });

        modBus.addListener(GatherDataRegistryEntriesEvent.class, event -> event.recipe(IFRecipesProvider::new));
    }
}
