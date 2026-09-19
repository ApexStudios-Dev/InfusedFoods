package dev.apexstudios.infusedfoods.data;

import dev.apexstudios.infusedfoods.common.InfusedFoods;
import dev.apexstudios.infusedfoods.common.util.InfusionEntries;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.neoforged.neoforge.common.Tags;

final class IFFluidTagsProvider extends FluidTagsProvider {
    public IFFluidTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, InfusedFoods.ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        tag(Tags.Fluids.POTION).add(InfusionEntries.POTION_FLUID.getKey());
    }
}
