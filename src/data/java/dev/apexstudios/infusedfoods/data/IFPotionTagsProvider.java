package dev.apexstudios.infusedfoods.data;

import dev.apexstudios.infusedfoods.common.InfusedFoods;
import dev.apexstudios.infusedfoods.common.util.InfusionTags;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.PotionTagsProvider;
import net.minecraft.world.item.alchemy.PotionIds;

final class IFPotionTagsProvider extends PotionTagsProvider {
    IFPotionTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, InfusedFoods.ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        tag(InfusionTags.POTION_BLACKLIST).add(PotionIds.MUNDANE);
    }
}
