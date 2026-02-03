package dev.apexstudios.infusedfoods.common;

import com.mojang.serialization.MapCodec;
import dev.apexstudios.infusedfoods.common.util.InfusionEntries;
import dev.apexstudios.infusedfoods.common.util.InfusionTags;
import dev.apexstudios.infusedfoods.common.util.InfusionUtil;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.ApiStatus;

public final class CleansingRecipe extends CustomRecipe {
    public static final CleansingRecipe INSTANCE = new CleansingRecipe();
    public static final MapCodec<CleansingRecipe> CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, CleansingRecipe> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @ApiStatus.Internal
    private CleansingRecipe() {

    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        var cleansingAgent = ItemStack.EMPTY;
        var food = ItemStack.EMPTY;

        for(var stack : input.items()) {
            if(stack.is(InfusionTags.CLEANSING_AGENT)) {
                if(!cleansingAgent.isEmpty())
                    return false;

                cleansingAgent = stack;
            } else if(InfusionUtil.isValidFood(stack) && stack.has(DataComponents.POTION_CONTENTS)) {
                if(!food.isEmpty())
                    return false;

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
            if(stack.is(InfusionTags.CLEANSING_AGENT)) {
                if(!cleansingAgent.isEmpty())
                    return ItemStack.EMPTY;

                cleansingAgent = stack;
            } else if(InfusionUtil.isValidFood(stack) && stack.has(DataComponents.POTION_CONTENTS)) {
                if(!food.isEmpty())
                    return ItemStack.EMPTY;

                food = stack;
            }
        }

        var result = food.copyWithCount(1);
        result.remove(DataComponents.POTION_CONTENTS);
        result.remove(InfusionEntries.HIDE_EFFECTS_COMPONENT);
        return result;
    }

    @Override
    public RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return InfusionEntries.CLEANSING_RECIPE.value();
    }
}
