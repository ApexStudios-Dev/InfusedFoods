package dev.apexstudios.infusedfoods.data;

import dev.apexstudios.infusedfoods.common.CleansingRecipe;
import dev.apexstudios.infusedfoods.common.InfusedFoods;
import net.minecraft.advancements.Advancement;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.item.crafting.Recipe;

final class IFRecipesProvider extends RecipeProvider {
    IFRecipesProvider(BootstrapContext<Recipe<?>> recipeOutput, BootstrapContext<Advancement> advancementOutput) {
        super(recipeOutput, advancementOutput);
    }

    @Override
    protected void buildRecipes() {
        SpecialRecipeBuilder.special(() -> CleansingRecipe.INSTANCE).save(output, InfusedFoods.id("cleansing"));
    }
}
