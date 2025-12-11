package dev.apexstudios.infusedfoods;

import dev.apexstudios.infusedfoods.cauldron.PotionCauldronInteractions;
import dev.apexstudios.infusedfoods.util.InfusionEntries;
import dev.apexstudios.registree.api.Registree;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.RegisterCauldronFluidContentEvent;

@Mod(InfusedFoods.ID)
public final class InfusedFoods {
    public static final String ID = "infusedfoods";
    public static final Registree REGISTREE = Registree.create(ID);

    public InfusedFoods(IEventBus modBus) {
        InfusionEntries.register();

        REGISTREE.registerEvents(modBus);

        modBus.addListener(FMLCommonSetupEvent.class, event -> event.enqueueWork(PotionCauldronInteractions::registerInteractions));
        modBus.addListener(RegisterCauldronFluidContentEvent.class, event -> event.register(InfusionEntries.CAULDRON_BLOCK.value(), InfusionEntries.POTION_FLUID.value(), FluidType.BUCKET_VOLUME, LayeredCauldronBlock.LEVEL));
    }

    public static Identifier identifier(String identifier) {
        return REGISTREE.registryName(identifier);
    }

    public static String id(String identifier) {
        return ID + Identifier.NAMESPACE_SEPARATOR + identifier;
    }
}
