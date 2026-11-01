package dev.apexstudios.infusedfoods.api;

import dev.apexstudios.registree.api.Registree;
import dev.apexstudios.registree.api.holders.DeferredBlockEntityType;
import dev.apexstudios.registree.api.holders.DeferredFluidPair;
import dev.apexstudios.registree.api.holders.DeferredRecipeSerializer;
import dev.apexstudios.registree.api.util.BlockEntityRegistrations;
import dev.apexstudios.registree.api.util.BlockRegistrations;
import dev.apexstudios.registree.api.util.CreativeModeTabRegistrations;
import dev.apexstudios.registree.api.util.FluidRegistrations;
import dev.apexstudios.registree.api.util.FluidTypeRegistrations;
import dev.apexstudios.registree.api.util.ItemRegistrations;
import dev.apexstudios.registree.api.util.RecipeSerializerRegistrations;
import java.util.function.UnaryOperator;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PointedDripstoneBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;

public interface InfusedFoods {
    String ID = "infusedfoods";
    Registree REGISTREE = Registree.create(ID);

    TagKey<Item> CLEANSING_AGENTS = REGISTREE.tag(Registries.ITEM, "cleansing_agents");
    TagKey<Potion> INFUSION_BLACKLIST = REGISTREE.tag(Registries.POTION, "infusion_blacklist");
    TagKey<Fluid> POTION_FLUID_TAG = REGISTREE.tag(Registries.FLUID, "potion");

    DeferredRecipeSerializer<CleansingRecipe> CLEANSING_RECIPE = RecipeSerializerRegistrations.register(REGISTREE, "cleansing", CleansingRecipe.MAP_CODEC, CleansingRecipe.STREAM_CODEC);

    DeferredBlock<PotionCauldronBlock> POTION_CAULDRON_BLOCK = BlockRegistrations.register(
            REGISTREE,
            "potion_cauldron",
            PotionCauldronBlock::new,
            () -> BlockBehaviour.Properties.ofLegacyCopy(Blocks.CAULDRON),
            UnaryOperator.identity()
    );

    DeferredHolder<FluidType, PotionFluidType> POTION_FLUID_TYPE = FluidTypeRegistrations.register(
            REGISTREE,
            "potion",
            PotionFluidType::new,
            properties -> properties
                    .fallDistanceModifier(0F)
                    .canExtinguish(true)
                    .canConvertToSource(false)
                    .supportsBoating(true)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
                    .sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH)
                    .canHydrate(true)
                    .isWaterLike(true)
                    .addDripstoneDripping(
                            PointedDripstoneBlock.WATER_TRANSFER_PROBABILITY_PER_RANDOM_TICK,
                            ParticleTypes.DRIPPING_DRIPSTONE_WATER,
                            InfusedFoods.POTION_CAULDRON_BLOCK.value(),
                            SoundEvents.POINTED_DRIPSTONE_DRIP_WATER_INTO_CAULDRON
                    )
    );

    DeferredFluidPair<PotionFluid> POTION_FLUID = FluidRegistrations.registerFlowing(
            REGISTREE,
            "potion",
            PotionFluid.Source::new,
            PotionFluid.Flowing::new,
            POTION_FLUID_TYPE,
            properties -> properties
                    .block(InfusedFoods.POTION_FLUID_BLOCK)
                    .bucket(InfusedFoods.POTION_BUCKET)
                    .explosionResistance(100F)
                    .slopeFindDistance(4)
                    .tickRate(5)
    );

    DeferredBlock<PotionLiquidBlock> POTION_FLUID_BLOCK = BlockRegistrations.register(
            REGISTREE,
            "potion",
            PotionLiquidBlock::new,
            properties -> properties
                    .mapColor(MapColor.WATER)
                    .replaceable()
                    .noCollision()
                    .strength(100F)
                    .pushReaction(PushReaction.POPPED)
                    .noLootTable()
                    .liquid()
                    .sound(SoundType.EMPTY)
    );

    DeferredBlockEntityType<PotionBlockEntity> POTION_BLOCK_ENTITY = BlockEntityRegistrations.register(
            REGISTREE,
            "potion",
            PotionBlockEntity::new,
            POTION_CAULDRON_BLOCK,
            POTION_FLUID_BLOCK
    );

    DeferredItem<BucketItem> POTION_BUCKET = ItemRegistrations.register(
            REGISTREE,
            "potion_bucket",
            PotionBucketItem::new,
            properties -> properties
                    .craftRemainder(Items.BUCKET)
                    .stacksTo(1)
                    .component(DataComponents.POTION_CONTENTS, PotionContents.EMPTY)
    );

    ResourceKey<CreativeModeTab> POTION_BUCKET_TAB = CreativeModeTabRegistrations.register(
            REGISTREE,
            "potion_buckets",
            () -> POTION_BUCKET.value().getDefaultInstance(),
            (parameters, output) -> parameters.holders()
                    .lookupOrThrow(Registries.POTION)
                    .filterFeatures(parameters.enabledFeatures())
                    .listElements()
                    .map(potion -> PotionContents.createItemStack(POTION_BUCKET.value(), potion))
                    .forEach(stack -> output.accept(stack))
    );
}
