package com.github.charlyb01.xpstorage.recipe;

import com.github.charlyb01.xpstorage.XpStorage;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class RecipeRegistry {
    public static final RecipeSerializer<XpBookUpgradeRecipe> XP_BOOK_UPGRADE = XpBookUpgradeRecipe.SERIALIZER;
    public static final RecipeSerializer<XpBookDeprecateRecipe> XP_BOOK_DEPRECATE = XpBookDeprecateRecipe.SERIALIZER;

    public static void init() {
        Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, XpStorage.id("xp_book_upgrade"), XP_BOOK_UPGRADE);
        Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, XpStorage.id("xp_book_deprecate"), XP_BOOK_DEPRECATE);
    }
}
