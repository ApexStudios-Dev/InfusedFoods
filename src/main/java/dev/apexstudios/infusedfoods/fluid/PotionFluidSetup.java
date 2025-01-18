package dev.apexstudios.infusedfoods.fluid;

import dev.apexstudios.apexcore.lib.fluid.ItemOnlyFluid;
import dev.apexstudios.apexcore.lib.registree.holder.DeferredFluid;
import dev.apexstudios.apexcore.lib.registree.holder.DeferredFluidType;
import dev.apexstudios.apexcore.lib.registree.holder.DeferredItem;
import dev.apexstudios.infusedfoods.InfusedFoods;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.fluids.FluidType;

public interface PotionFluidSetup {
    DeferredItem<PotionBucket> BUCKET = InfusedFoods.REGISTREE.registerItem("potion_bucket", PotionBucket::new, properties -> properties
            .stacksTo(1)
            .craftRemainder(Items.BUCKET)
            .component(DataComponents.POTION_CONTENTS, new PotionContents(Potions.MUNDANE))
    );

    DeferredFluidType<FluidType> FLUID_TYPE = InfusedFoods.REGISTREE.registerSimpleFluidType("potion");

    DeferredFluid<Fluid> FLUID = InfusedFoods.REGISTREE.registerFluid("potion", ItemOnlyFluid.simpleFactory(FLUID_TYPE, BUCKET));

    ResourceKey<CreativeModeTab> POTION_BUCKETS = InfusedFoods.REGISTREE.registerCreativeModeTab("potion_buckets", builder -> builder
            .displayItems((parameters, output) -> InfusedFoods.createInfusedItems(BUCKET.toStack(), parameters).forEach(output::accept))
            .icon(BUCKET::toStack)
            .withSearchBar()
    );

    static void register(IEventBus modBus) {
        modBus.addListener(RegisterCapabilitiesEvent.class, event -> event
                .registerItem(Capabilities.FluidHandler.ITEM, (stack, context) -> new PotionBucketHandler(stack), BUCKET)
        );

        modBus.addListener(RegisterClientExtensionsEvent.class, event -> {
            event.registerFluidType(new PotionBucket.ClientExtensions(), FLUID_TYPE);
        });
    }
}
