package com.github.charlyb01.xpstorage.item;

import com.github.charlyb01.xpstorage.XpStorage;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public interface ItemKeys {
    ResourceKey<Item> CRYSTALLIZED_LAPIS_KEY = register("crystallized_lapis");
    ResourceKey<Item> XP_BOOK_UPGRADE_KEY = register("xp_book_upgrade_smithing_template");
    ResourceKey<Item> XP_BOOK_KEY = register("xp_book");
    ResourceKey<Item> XP_BOOK2_KEY = register("xp_book2");
    ResourceKey<Item> XP_BOOK3_KEY = register("xp_book3");

    private static ResourceKey<Item> register(String path) {
        return ResourceKey.create(Registries.ITEM, XpStorage.id(path));
    }
}
