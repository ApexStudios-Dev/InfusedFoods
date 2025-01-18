package dev.apexstudios.infusedfoods.recipe;

import com.mojang.serialization.MapCodec;
import dev.apexstudios.apexcore.lib.registree.type.SimpleRecipeSerializer;
import dev.apexstudios.infusedfoods.InfusedFoods;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Unit;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.ApiStatus;

public final class HideEffectsRecipe extends CustomRecipe {
    public static final MapCodec<HideEffectsRecipe> CODEC = SimpleRecipeSerializer.codec(HideEffectsRecipe::new, CustomRecipe::category);
    public static final StreamCodec<RegistryFriendlyByteBuf, HideEffectsRecipe> STREAM_CODEC = SimpleRecipeSerializer.steamCodec(HideEffectsRecipe::new, CustomRecipe::category);

    @ApiStatus.Internal
    public HideEffectsRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        var hider = ItemStack.EMPTY;
        var food = ItemStack.EMPTY;

        for(var stack : input.items()) {
            if(stack.is(RecipeSetup.EFFECTS_HIDER)) {
                if(!hider.isEmpty())
                    return false;

                hider = stack;
            } else if(InfusedFoods.isValidFood(stack) && stack.has(DataComponents.POTION_CONTENTS)) {
                if(!food.isEmpty())
                    return false;

                food = stack;
            }
        }

        return !hider.isEmpty() && !food.isEmpty();
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        var hider = ItemStack.EMPTY;
        var food = ItemStack.EMPTY;

        for(var stack : input.items()) {
            if(stack.is(RecipeSetup.EFFECTS_HIDER)) {
                if(!hider.isEmpty())
                    return ItemStack.EMPTY;

                hider = stack;
            } else if(InfusedFoods.isValidFood(stack) && stack.has(DataComponents.POTION_CONTENTS)) {
                if(!food.isEmpty())
                    return ItemStack.EMPTY;

                food = stack;
            }
        }

        var result = food.copyWithCount(1);
        result.set(RecipeSetup.HIDE_EFFECTS_COMPONENT, Unit.INSTANCE);
        return result;
    }

    @Override
    public RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return RecipeSetup.HIDE_EFFECTS.value();
    }
}
