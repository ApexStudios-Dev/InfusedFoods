package dev.apexstudios.infusedfoods.data;

import dev.apexstudios.apexcore.api.data.ItemTagsProvider;
import dev.apexstudios.infusedfoods.common.InfusedFoods;
import dev.apexstudios.infusedfoods.common.util.InfusionTags;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;

final class IFItemTagsProvider extends ItemTagsProvider {
    IFItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags) {
        super(output, lookupProvider, blockTags, InfusedFoods.ID);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void addTags(HolderLookup.Provider registries) {
        tag(InfusionTags.CLEANSING_AGENT).addTags(Tags.Items.BUCKETS_MILK, Tags.Items.DRINKS_MILK);
    }
}
