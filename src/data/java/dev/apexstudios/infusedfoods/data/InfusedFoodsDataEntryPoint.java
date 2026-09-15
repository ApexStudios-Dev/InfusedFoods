package dev.apexstudios.infusedfoods.data;

import dev.apexstudios.infusedfoods.common.InfusedFoods;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.metadata.PackMetadataGenerator;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@Mod(value = InfusedFoods.ID, dist = Dist.CLIENT)
public final class InfusedFoodsDataEntryPoint {
    public InfusedFoodsDataEntryPoint(IEventBus modBus) {
        modBus.addListener(GatherDataEvent.Client.class, event -> {
            event.createReloadableRegistryObjects(new RegistrySetBuilder()
                    .add(RecipeProvider.asBootstrap(IFRecipesProvider::new))
            );

            event.createProvider(IFPotionTagsProvider::new);
            event.createProvider(IFItemTagsProvider::new);
            event.createProvider(IFBlockTagsProvider::new);
            event.createProvider(IFFluidTagsProvider::new);
            event.createProvider(IFLanguageProvider::new);
            event.createProvider(IFModelsProvider::new);
            event.createProvider(output -> PackMetadataGenerator.forFeaturePack(output, Component.literal("InfusedFoods resources")));
        });
    }
}
