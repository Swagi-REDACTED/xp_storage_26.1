package com.github.charlyb01.xpstorage.data;

import com.github.charlyb01.xpstorage.XpStorage;
import com.github.charlyb01.xpstorage.item.ItemRegistry;
import com.github.charlyb01.xpstorage.recipe.XpBookDeprecateRecipeJsonBuilder;
import com.github.charlyb01.xpstorage.recipe.XpBookUpgradeRecipeJsonBuilder;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.concurrent.CompletableFuture;

public class RecipeProvider extends FabricRecipeProvider {
    public RecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected net.minecraft.data.recipes.RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        return new net.minecraft.data.recipes.RecipeProvider(registries, output) {
            @Override
            public void buildRecipes() {
                this.shaped(RecipeCategory.MISC, ItemRegistry.CRYSTALLIZED_LAPIS, 2)
                        .pattern(" l ")
                        .pattern("lal")
                        .pattern(" l ")
                        .define('l', Items.LAPIS_LAZULI)
                        .define('a', Items.AMETHYST_SHARD)
                        .unlockedBy(getHasName(Items.LAPIS_LAZULI),
                                this.has(MinMaxBounds.Ints.atLeast(4), Items.LAPIS_LAZULI))
                        .save(this.output);

                this.shaped(RecipeCategory.TOOLS, ItemRegistry.XP_BOOK)
                        .pattern(" c ")
                        .pattern("cbc")
                        .pattern(" c ")
                        .define('c', ItemRegistry.CRYSTALLIZED_LAPIS)
                        .define('b', Items.BOOK)
                        .unlockedBy(getHasName(ItemRegistry.CRYSTALLIZED_LAPIS),
                                this.has(ItemRegistry.CRYSTALLIZED_LAPIS))
                        .save(this.output);

                this.shaped(RecipeCategory.MISC, ItemRegistry.XP_BOOK_UPGRADE)
                        .pattern("lll")
                        .pattern("ldl")
                        .pattern("lll")
                        .define('l', ItemRegistry.CRYSTALLIZED_LAPIS)
                        .define('d', Items.DIAMOND)
                        .unlockedBy(getHasName(ItemRegistry.CRYSTALLIZED_LAPIS),
                                this.has(ItemRegistry.CRYSTALLIZED_LAPIS))
                        .save(this.output);

                this.shaped(RecipeCategory.MISC, ItemRegistry.XP_BOOK_UPGRADE, 2)
                        .pattern(" l ")
                        .pattern("lul")
                        .pattern(" l ")
                        .define('l', ItemRegistry.CRYSTALLIZED_LAPIS)
                        .define('u', ItemRegistry.XP_BOOK_UPGRADE)
                        .unlockedBy(getHasName(ItemRegistry.CRYSTALLIZED_LAPIS),
                                this.has(ItemRegistry.CRYSTALLIZED_LAPIS))
                        .save(this.output, XpStorage.id(getItemName(ItemRegistry.XP_BOOK_UPGRADE) + "_duplication").toString());

                offerXpBookUpgradeRecipe(this.output, Items.DIAMOND, 0, 30, 90, 3, Integer.parseInt("a1fbe8", 16));
                offerXpBookUpgradeRecipe(this.output, Items.NETHERITE_INGOT, 1, 50, 95, 5, Integer.parseInt("5a575a", 16));
                offerXpBookUpgradeRecipe(this.output, Items.NETHER_STAR, 2, 100, 100, 10, Integer.parseInt("e0e277", 16));

                offerXpBookDeprecateRecipe(this.output, ItemRegistry.XP_BOOK);
                offerXpBookDeprecateRecipe(this.output, ItemRegistry.XP_BOOK2);
                offerXpBookDeprecateRecipe(this.output, ItemRegistry.XP_BOOK3);
            }

            public void offerXpBookUpgradeRecipe(RecipeOutput output, Item ingredient, int baseLevel, int resultCapacity,
                                                 int resultXpFromUsing, int resultXpFromBrewing, int resultBarColor) {
                XpBookUpgradeRecipeJsonBuilder
                        .create(
                                RecipeCategory.MISC,
                                Ingredient.of(ItemRegistry.XP_BOOK_UPGRADE),
                                Ingredient.of(ItemRegistry.XP_BOOK),
                                Ingredient.of(ingredient),
                                baseLevel,
                                resultCapacity,
                                resultXpFromUsing,
                                resultXpFromBrewing,
                                resultBarColor
                        )
                        .unlockedBy("has_xp_book_upgrade_ingredient", this.has(ingredient))
                        .save(output, ResourceKey.create(Registries.RECIPE, XpStorage.id(getItemName(ingredient) + "_xp_book_upgrade")));
            }

            public void offerXpBookDeprecateRecipe(RecipeOutput output, Item ingredient) {
                XpBookDeprecateRecipeJsonBuilder
                        .create(
                                RecipeCategory.MISC,
                                Ingredient.of(ingredient)
                        )
                        .unlockedBy("has_deprecated_xp_book", this.has(ingredient))
                        .save(output, ResourceKey.create(Registries.RECIPE, XpStorage.id(getItemName(ingredient) + "_deprecate")));
            }
        };
    }

    @Override
    public String getName() {
        return "Recipes";
    }
}
