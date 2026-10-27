package dev.apexstudios.infusedfoods.data;

import dev.apexstudios.infusedfoods.api.InfusedFoods;
import dev.apexstudios.infusedfoods.core.CleansingRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.item.crafting.Recipe;

final class RecipeProvider extends dev.apexstudios.apexcore.api.data.RecipeProvider {
    RecipeProvider(BootstrapContext<Recipe<?>> recipeOutput, BootstrapContext<Advancement> advancementOutput) {
        super(recipeOutput, advancementOutput);
    }

    @Override
    protected void buildRecipes() {
        SpecialRecipeBuilder.special(() -> CleansingRecipe.INSTANCE)
                .save(output, InfusedFoods.REGISTREE.id("cleansing"));
    }
}
