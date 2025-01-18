package dev.apexstudios.infusedfoods.recipe;

import com.mojang.serialization.MapCodec;
import dev.apexstudios.apexcore.lib.registree.type.SimpleRecipeSerializer;
import dev.apexstudios.infusedfoods.InfusedFoods;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.ApiStatus;

public final class CleansingRecipe extends CustomRecipe {
    public static final MapCodec<CleansingRecipe> CODEC = SimpleRecipeSerializer.codec(CleansingRecipe::new, CustomRecipe::category);
    public static final StreamCodec<RegistryFriendlyByteBuf, CleansingRecipe> STREAM_CODEC = SimpleRecipeSerializer.steamCodec(CleansingRecipe::new, CustomRecipe::category);

    @ApiStatus.Internal
    public CleansingRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        var cleansingAgent = ItemStack.EMPTY;
        var food = ItemStack.EMPTY;

        for(var stack : input.items()) {
            if(stack.is(RecipeSetup.CLEANSING_AGENT)) {
                if(!cleansingAgent.isEmpty())
                    return false;

                cleansingAgent = stack;
            } else if(InfusedFoods.isValidFood(stack) && stack.has(DataComponents.POTION_CONTENTS)) {
                if(!food.isEmpty())
                    return false;

                food = stack;
            }
        }

        return !cleansingAgent.isEmpty() && !food.isEmpty();
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        var cleansingAgent = ItemStack.EMPTY;
        var food = ItemStack.EMPTY;

        for(var stack : input.items()) {
            if(stack.is(RecipeSetup.CLEANSING_AGENT)) {
                if(!cleansingAgent.isEmpty())
                    return ItemStack.EMPTY;

                cleansingAgent = stack;
            } else if(InfusedFoods.isValidFood(stack) && stack.has(DataComponents.POTION_CONTENTS)) {
                if(!food.isEmpty())
                    return ItemStack.EMPTY;

                food = stack;
            }
        }

        var result = food.copyWithCount(1);
        result.remove(DataComponents.POTION_CONTENTS);
        result.remove(RecipeSetup.HIDE_EFFECTS_COMPONENT);
        return result;
    }

    @Override
    public RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return RecipeSetup.CLEANSING.value();
    }
}
