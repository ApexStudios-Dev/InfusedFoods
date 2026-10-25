package dev.apexstudios.infusedfoods.data;

import dev.apexstudios.infusedfoods.api.InfusedFoods;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.Tags;

final class ItemTagProvider extends TagsProvider<Item> {
    ItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, Registries.ITEM, lookupProvider, InfusedFoods.ID);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void addTags(HolderLookup.Provider registries) {
        tag(InfusedFoods.CLEANSING_AGENTS).addTags(Tags.Items.BUCKETS_MILK, Tags.Items.DRINKS_MILK);
    }
}
