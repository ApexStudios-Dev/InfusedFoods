package dev.apexstudios.infusedfoods.common.util;

import dev.apexstudios.apexcore.api.ItemOnlyFluid;
import dev.apexstudios.infusedfoods.common.CleansingRecipe;
import dev.apexstudios.infusedfoods.common.InfusedFoods;
import dev.apexstudios.infusedfoods.common.cauldron.PotionCauldronBlock;
import dev.apexstudios.infusedfoods.common.cauldron.PotionCauldronBlockEntity;
import dev.apexstudios.registree.holder.DeferredBlockEntityType;
import dev.apexstudios.registree.holder.DeferredDataComponentType;
import dev.apexstudios.registree.holder.DeferredFluid;
import dev.apexstudios.registree.holder.DeferredFluidType;
import dev.apexstudios.registree.holder.DeferredRecipeSerializer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Unit;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.jspecify.annotations.Nullable;

public interface InfusionEntries {
    DeferredRecipeSerializer<CleansingRecipe> CLEANSING_RECIPE = InfusedFoods.RECIPE_SERIALIZERS.register("cleansing", CleansingRecipe.CODEC, CleansingRecipe.STREAM_CODEC);
    DeferredDataComponentType<Unit> HIDE_EFFECTS_COMPONENT = InfusedFoods.DATA_COMPONENT_TYPES.registerUnit("hide_effects");

    DeferredFluidType<FluidType> POTION_FLUID_TYPE = InfusedFoods.FLUID_TYPES.builder("potion")
            .clientExtensions(() -> () -> new IClientFluidTypeExtensions() {
                private IClientFluidTypeExtensions delegate() {
                    return IClientFluidTypeExtensions.of(Fluids.WATER);
                }

                @Override
                public int getTintColor(FluidStack stack) {
                    return stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).getColorOr(PotionContents.BASE_POTION_COLOR);
                }

                @Override
                public Identifier getFlowingTexture() {
                    return delegate().getFlowingTexture();
                }

                @Override
                public Identifier getStillTexture() {
                    return delegate().getStillTexture();
                }

                @Nullable
                @Override
                public Identifier getOverlayTexture() {
                    return delegate().getOverlayTexture();
                }
            })
            .register();

    DeferredFluid<Fluid> POTION_FLUID = InfusedFoods.FLUIDS.register("potion", ItemOnlyFluid.simpleFactory(POTION_FLUID_TYPE, Items.POTION));

    DeferredBlock<PotionCauldronBlock> CAULDRON_BLOCK = InfusedFoods.BLOCKS
            .builder("potion_cauldron", PotionCauldronBlock::new)
            .properties(properties -> properties
                    .mapColor(MapColor.STONE)
                    .requiresCorrectToolForDrops()
                    .strength(2F)
                    .noOcclusion()
            )
            .colorHandler(() -> () -> (blockState, level, pos, tintIndex) -> {
                if(level == null || pos == null || !(level.getBlockEntity(pos) instanceof PotionCauldronBlockEntity blockEntity))
                    return PotionContents.BASE_POTION_COLOR;

                return blockEntity.getPotionContents().getColor();
            })
            .blockEntityType(PotionCauldronBlockEntity::new)
            .register();

    DeferredBlockEntityType<PotionCauldronBlockEntity> CAULDRON_BLOCK_ENTITY = DeferredBlockEntityType.createBlockEntityType(CAULDRON_BLOCK.getId());

    static void register() { }
}
