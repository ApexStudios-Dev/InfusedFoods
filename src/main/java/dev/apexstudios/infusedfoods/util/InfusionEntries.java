package dev.apexstudios.infusedfoods.util;

import com.mojang.serialization.Codec;
import dev.apexstudios.apexcore.lib.fluid.ItemOnlyFluid;
import dev.apexstudios.infusedfoods.InfusedFoods;
import dev.apexstudios.infusedfoods.cauldron.PotionCauldronBlock;
import dev.apexstudios.infusedfoods.cauldron.PotionCauldronBlockEntity;
import dev.apexstudios.infusedfoods.recipe.CleansingRecipe;
import dev.apexstudios.registree.api.holder.DeferredBlock;
import dev.apexstudios.registree.api.holder.DeferredBlockEntity;
import dev.apexstudios.registree.api.holder.DeferredDataComponent;
import dev.apexstudios.registree.api.holder.DeferredFluid;
import dev.apexstudios.registree.api.holder.DeferredFluidType;
import dev.apexstudios.registree.api.holder.DeferredRecipeSerializer;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Unit;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.fluids.FluidType;

public interface InfusionEntries {
    DeferredRecipeSerializer<CleansingRecipe> CLEANSING_RECIPE = InfusedFoods.REGISTREE.registerRecipeSerializer("cleansing", CleansingRecipe.CODEC, CleansingRecipe.STREAM_CODEC);
    DeferredDataComponent<Unit> HIDE_EFFECTS_COMPONENT = InfusedFoods.REGISTREE.registerDataComponent("hide_effects", Codec.unit(Unit.INSTANCE), StreamCodec.unit(Unit.INSTANCE));

    DeferredFluidType<FluidType> POTION_FLUID_TYPE = InfusedFoods.REGISTREE.registerSimpleFluidType("potion");
    DeferredFluid<Fluid> POTION_FLUID = InfusedFoods.REGISTREE.registerFluid("potion", ItemOnlyFluid.simpleFactory(POTION_FLUID_TYPE, Items.POTION));

    DeferredBlock<PotionCauldronBlock> CAULDRON_BLOCK = InfusedFoods.REGISTREE.registerBlock("potion_cauldron", PotionCauldronBlock::new, BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE)
            .requiresCorrectToolForDrops()
            .strength(2F)
            .noOcclusion()
    );

    DeferredBlockEntity<PotionCauldronBlockEntity> CAULDRON_BLOCK_ENTITY = InfusedFoods.REGISTREE.registerBlockEntity("potion_cauldron", PotionCauldronBlockEntity::new, CAULDRON_BLOCK);

    static void register() { }
}
