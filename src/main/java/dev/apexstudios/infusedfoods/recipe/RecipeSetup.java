package dev.apexstudios.infusedfoods.recipe;

import com.mojang.serialization.Codec;
import dev.apexstudios.apexcore.lib.registree.holder.DeferredDataComponent;
import dev.apexstudios.apexcore.lib.registree.holder.DeferredRecipeSerializer;
import dev.apexstudios.infusedfoods.InfusedFoods;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Unit;
import net.minecraft.world.item.Item;

public interface RecipeSetup {
    DeferredRecipeSerializer<CleansingRecipe> CLEANSING = InfusedFoods.REGISTREE.registerRecipeSerializer("cleansing", CleansingRecipe.CODEC, CleansingRecipe.STREAM_CODEC);
    DeferredRecipeSerializer<HideEffectsRecipe> HIDE_EFFECTS = InfusedFoods.REGISTREE.registerRecipeSerializer("hide_effects", HideEffectsRecipe.CODEC, HideEffectsRecipe.STREAM_CODEC);

    DeferredDataComponent<Unit> HIDE_EFFECTS_COMPONENT = InfusedFoods.REGISTREE.registerDataComponent("hide_effects", Codec.unit(Unit.INSTANCE), StreamCodec.unit(Unit.INSTANCE));

    TagKey<Item> CLEANSING_AGENT = InfusedFoods.REGISTREE.tag(Registries.ITEM, "cleansing_agent");
    TagKey<Item> EFFECTS_HIDER = InfusedFoods.REGISTREE.tag(Registries.ITEM, "effects_hider");

    static void register() {

    }
}
