package com.github.charlyb01.xpstorage.recipe;
 
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
 
import java.util.LinkedHashMap;
import java.util.Map;
 
public class XpBookDeprecateRecipeJsonBuilder {
    private final RecipeCategory category;
    private final Ingredient base;
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
 
    public XpBookDeprecateRecipeJsonBuilder(RecipeCategory category, Ingredient base) {
        this.category = category;
        this.base = base;
    }
 
    public static XpBookDeprecateRecipeJsonBuilder create(RecipeCategory category, Ingredient base) {
        return new XpBookDeprecateRecipeJsonBuilder(category, base);
    }
 
    public XpBookDeprecateRecipeJsonBuilder unlockedBy(String name, Criterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }
 
    public void save(net.minecraft.data.recipes.RecipeOutput output, ResourceKey<Recipe<?>> recipeKey) {
        this.validate(recipeKey);
        net.minecraft.advancements.Advancement.Builder builder = output.advancement()
                .addCriterion("has_the_recipe", net.minecraft.advancements.criterion.RecipeUnlockedTrigger.unlocked(recipeKey))
                .rewards(AdvancementRewards.Builder.recipe(recipeKey))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(builder::addCriterion);
        XpBookDeprecateRecipe deprecateRecipe = new XpBookDeprecateRecipe(this.base);
        output.accept(recipeKey, deprecateRecipe, builder.build(recipeKey.identifier().withPrefix("recipes/" + this.category.getFolderName() + "/")));
    }
 
    private void validate(ResourceKey<Recipe<?>> recipeKey) {
        if (this.criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + recipeKey.identifier());
        }
    }
}
