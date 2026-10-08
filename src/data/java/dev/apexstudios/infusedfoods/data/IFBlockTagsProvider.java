package dev.apexstudios.infusedfoods.data;

import dev.apexstudios.apexcore.api.data.BlockTagsProvider;
import dev.apexstudios.infusedfoods.common.InfusedFoods;
import dev.apexstudios.infusedfoods.common.util.InfusionEntries;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;

final class IFBlockTagsProvider extends BlockTagsProvider {
    IFBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, InfusedFoods.ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        tag(BlockTags.CAULDRONS).add(InfusionEntries.CAULDRON_BLOCK.key());
    }
}
