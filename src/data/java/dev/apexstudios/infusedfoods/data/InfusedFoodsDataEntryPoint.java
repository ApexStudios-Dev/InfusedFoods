package dev.apexstudios.infusedfoods.data;

import dev.apexstudios.apexcore.api.data.DataGenUtil;
import dev.apexstudios.infusedfoods.api.InfusedFoods;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.data.event.GatherDataRegistryEntriesEvent;

@Mod(value = InfusedFoods.ID, dist = Dist.CLIENT)
public final class InfusedFoodsDataEntryPoint {
    static final Component PACK_DESC = DataGenUtil.packDescription(InfusedFoods.ID);

    public InfusedFoodsDataEntryPoint(IEventBus modBus) {
        modBus.addListener(GatherDataEvent.Client.class, event -> {
            event.createProvider(output -> DataGenUtil.metadata(output, PACK_DESC, PackType.SERVER_DATA));
            event.createProvider(LangProvider::new);
            event.createProvider(BlockTagProvider::new);
            event.createProvider(ItemTagProvider::new);
            event.createProvider(PotionTagProvider::new);
            event.createProvider(ModelProvider::new);
        });

        modBus.addListener(GatherDataRegistryEntriesEvent.class, event -> event
                .lootTable(new LootTableProvider.SubProviderEntry(BlockLootProvider::new, LootContextParamSets.BLOCK))
                .recipe(RecipeProvider::new)
        );
    }
}
