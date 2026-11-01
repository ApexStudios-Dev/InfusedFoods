package dev.apexstudios.infusedfoods.data;

import dev.apexstudios.infusedfoods.api.InfusedFoods;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionIds;

final class PotionTagProvider extends TagsProvider<Potion> {
    PotionTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, Registries.POTION, lookupProvider, InfusedFoods.ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        tag(InfusedFoods.INFUSION_BLACKLIST).add(PotionIds.MUNDANE);
    }
}
