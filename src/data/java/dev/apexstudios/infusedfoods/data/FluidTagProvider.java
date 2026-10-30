package dev.apexstudios.infusedfoods.data;

import dev.apexstudios.infusedfoods.api.InfusedFoods;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.Tags;

final class FluidTagProvider extends TagsProvider<Fluid> {
    FluidTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, Registries.FLUID, lookupProvider, InfusedFoods.ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        tag(InfusedFoods.POTION_FLUID_TAG).add(
                InfusedFoods.POTION_FLUID.key(),
                InfusedFoods.FLOWING_POTION_FLUID.key()
        );

        tag(FluidTags.ENTITY_FLOATABLE).addTag(InfusedFoods.POTION_FLUID_TAG);
        tag(Tags.Fluids.POTION).addTag(InfusedFoods.POTION_FLUID_TAG);
    }
}
