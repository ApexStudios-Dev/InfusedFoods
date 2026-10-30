package dev.apexstudios.infusedfoods.api;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.Tags;

public final class CleansingRecipe extends CustomRecipe {
    public static final CleansingRecipe INSTANCE = new CleansingRecipe();
    public static final MapCodec<CleansingRecipe> MAP_CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, CleansingRecipe> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    private CleansingRecipe() {
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        var cleansingAgent = ItemStack.EMPTY;
        var food = ItemStack.EMPTY;

        for(var stack : input.items()) {
            if(stack.is(InfusedFoods.CLEANSING_AGENTS)) {
                if(!cleansingAgent.isEmpty()) {
                    return false;
                }

                cleansingAgent = stack;
            } else if(stack.is(Tags.Items.FOODS) && stack.has(DataComponents.POTION_CONTENTS)) {
                if(!food.isEmpty()) {
                    return false;
                }

                food = stack;
            }
        }

        return !cleansingAgent.isEmpty() && !food.isEmpty();
    }

    @Override
    public ItemStack assemble(CraftingInput input) {
        var cleansingAgent = ItemStack.EMPTY;
        var food = ItemStack.EMPTY;

        for(var stack : input.items()) {
            if(stack.is(InfusedFoods.CLEANSING_AGENTS)) {
                if(!cleansingAgent.isEmpty()) {
                    return ItemStack.EMPTY;
                }

                cleansingAgent = stack;
            } else if(stack.is(Tags.Items.FOODS) && stack.has(DataComponents.POTION_CONTENTS)) {
                if(!food.isEmpty()) {
                    return ItemStack.EMPTY;
                }

                food = stack;
            }
        }

        var result = food.copyWithCount(1);
        result.remove(DataComponents.POTION_CONTENTS);
        return result;
    }

    @Override
    public RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return InfusedFoods.CLEANSING_RECIPE.value();
    }
}
