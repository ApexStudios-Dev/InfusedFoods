package dev.apexstudios.infusedfoods.common;

import dev.apexstudios.infusedfoods.common.cauldron.PotionCauldronInteractions;
import dev.apexstudios.infusedfoods.common.util.InfusionEntries;
import dev.apexstudios.registree.Registree;
import dev.apexstudios.registree.registrar.BlockRegistrar;
import dev.apexstudios.registree.registrar.DataComponentTypeRegistrar;
import dev.apexstudios.registree.registrar.FluidRegistrar;
import dev.apexstudios.registree.registrar.FluidTypeRegistrar;
import dev.apexstudios.registree.registrar.ItemRegistrar;
import dev.apexstudios.registree.registrar.RecipeSerializerRegistrar;
import dev.apexstudios.registree.registrar.Registrar;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.alchemy.Potion;
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
    public static final BlockRegistrar BLOCKS = REGISTREE.blocks();
    public static final ItemRegistrar ITEMS = REGISTREE.items();
    public static final DataComponentTypeRegistrar DATA_COMPONENT_TYPES = REGISTREE.dataComponentTypes();
    public static final RecipeSerializerRegistrar RECIPE_SERIALIZERS = REGISTREE.recipeSerializers();
    public static final Registrar<Potion> POTIONS = REGISTREE.registrarOrCreate(Registries.POTION);
    public static final FluidTypeRegistrar FLUID_TYPES = REGISTREE.fluidTypes();
    public static final FluidRegistrar FLUIDS = REGISTREE.fluids();

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
