package com.github.charlyb01.xpstorage;

import com.github.charlyb01.xpstorage.config.ModConfig;
import com.github.charlyb01.xpstorage.item.ItemRegistry;
import com.github.charlyb01.xpstorage.recipe.RecipeRegistry;

import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;

public class XpStorage implements ModInitializer {
    public static final String MOD_ID = "xp_storage";

    @Override
    public void onInitialize() {
        ModConfig.get();

        ItemRegistry.init();
        RecipeRegistry.init();
    }

    public static Identifier id(final String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
