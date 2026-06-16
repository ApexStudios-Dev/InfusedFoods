package dev.apexstudios.infusedfoods.common.util;

import dev.apexstudios.apexcore.api.ItemOnlyFluid;
import dev.apexstudios.infusedfoods.common.CleansingRecipe;
import dev.apexstudios.infusedfoods.common.InfusedFoods;
import dev.apexstudios.infusedfoods.common.cauldron.PotionCauldronBlock;
import dev.apexstudios.infusedfoods.common.cauldron.PotionCauldronBlockEntity;
import dev.apexstudios.registree.holder.DeferredBlockEntity;
import dev.apexstudios.registree.holder.DeferredDataComponent;
import dev.apexstudios.registree.holder.DeferredFluid;
import dev.apexstudios.registree.holder.DeferredFluidType;
import dev.apexstudios.registree.holder.DeferredRecipeSerializer;
import dev.apexstudios.registree.holder.Holders;
import net.minecraft.util.Unit;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredBlock;

public interface InfusionEntries {
    DeferredRecipeSerializer<CleansingRecipe> CLEANSING_RECIPE = InfusedFoods.REGISTREE.recipeSerializer("cleansing", CleansingRecipe.CODEC, CleansingRecipe.STREAM_CODEC);

    DeferredDataComponent<Unit> HIDE_EFFECTS_COMPONENT = InfusedFoods.REGISTREE.dataComponent("hide_effects", properties -> properties
            .persistent(Unit.CODEC)
            .networkSynchronized(Unit.STREAM_CODEC)
    );

    DeferredFluidType<FluidType> POTION_FLUID_TYPE = InfusedFoods.REGISTREE.fluidType("potion").register();

    DeferredFluid<Fluid> POTION_FLUID = InfusedFoods.REGISTREE.fluid("potion", ItemOnlyFluid.simpleFactory(POTION_FLUID_TYPE, Items.POTION))
            .model(() -> () -> InfusionClientEntries.FLUID_MODEL)
            .register();

    DeferredBlock<PotionCauldronBlock> CAULDRON_BLOCK = InfusedFoods.REGISTREE.block("potion_cauldron", PotionCauldronBlock::new)
            .properties(properties -> properties
                    .mapColor(MapColor.STONE)
                    .requiresCorrectToolForDrops()
                    .strength(2F)
                    .noOcclusion()
            )
            .tintSources(() -> () -> InfusionClientEntries.BLOCK_TINT_SOURCES)
            .blockEntity(PotionCauldronBlockEntity::new)
            .register();

    DeferredBlockEntity<PotionCauldronBlockEntity> CAULDRON_BLOCK_ENTITY = Holders.createBlockEntity(CAULDRON_BLOCK);

    static void register() { }
}
