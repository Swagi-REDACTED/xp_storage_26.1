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
import java.util.Optional;
 
public class XpBookUpgradeRecipeJsonBuilder {
    private final RecipeCategory category;
    private final Ingredient template;
    private final Ingredient base;
    private final Ingredient addition;
    private final int baseLevel;
    private final int resultCapacity;
    private final int resultXpFromUsing;
    private final int resultXpFromBrewing;
    private final int resultBarColor;
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
 
    public XpBookUpgradeRecipeJsonBuilder(RecipeCategory category, Ingredient template, Ingredient base, Ingredient addition,
                                          int baseLevel, int resultCapacity, int resultXpFromUsing, int resultXpFromBrewing,
                                          int resultBarColor) {
        this.category = category;
        this.template = template;
        this.base = base;
        this.addition = addition;
        this.baseLevel = baseLevel;
        this.resultCapacity = resultCapacity;
        this.resultXpFromUsing = resultXpFromUsing;
        this.resultXpFromBrewing = resultXpFromBrewing;
        this.resultBarColor = resultBarColor;
    }
 
    public static XpBookUpgradeRecipeJsonBuilder create(RecipeCategory category, Ingredient template, Ingredient base,
                                                        Ingredient addition, int baseLevel, int resultCapacity,
                                                        int resultXpFromUsing, int resultXpFromBrewing, int resultBarColor) {
        return new XpBookUpgradeRecipeJsonBuilder(category, template, base, addition, baseLevel,
                resultCapacity, resultXpFromUsing, resultXpFromBrewing, resultBarColor);
    }
 
    public XpBookUpgradeRecipeJsonBuilder unlockedBy(String name, Criterion<?> criterion) {
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
        XpBookUpgradeRecipe upgradeRecipe = new XpBookUpgradeRecipe(Optional.of(this.template), Optional.of(this.base),
                Optional.of(this.addition), this.baseLevel, this.resultCapacity, this.resultXpFromUsing,
                this.resultXpFromBrewing, this.resultBarColor);
        output.accept(recipeKey, upgradeRecipe, builder.build(recipeKey.identifier().withPrefix("recipes/" + this.category.getFolderName() + "/")));
    }
 
    private void validate(ResourceKey<Recipe<?>> recipeKey) {
        if (this.criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + recipeKey.identifier());
        }
    }
}
