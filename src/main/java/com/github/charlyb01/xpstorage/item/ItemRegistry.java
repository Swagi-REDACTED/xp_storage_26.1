package com.github.charlyb01.xpstorage.item;

import com.github.charlyb01.xpstorage.XpStorage;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SmithingTemplateItem;

import java.util.List;

public class ItemRegistry {
    private static final ChatFormatting DESCRIPTION_FORMATTING = ChatFormatting.BLUE;
    private static final Component XP_BOOK_UPGRADE_APPLIES_TO_TEXT = Component.translatable(
                    "item.xp_storage.smithing_template.xp_book_upgrade.applies_to"
            )
            .withStyle(DESCRIPTION_FORMATTING);
    private static final Component XP_BOOK_UPGRADE_INGREDIENTS_TEXT = Component.translatable(
                    "item.xp_storage.smithing_template.xp_book_upgrade.ingredients"
            )
            .withStyle(DESCRIPTION_FORMATTING);
    private static final Component XP_BOOK_UPGRADE_BASE_SLOT_DESCRIPTION_TEXT = Component.translatable(
            "item.xp_storage.smithing_template.xp_book_upgrade.base_slot_description"
    );
    private static final Component XP_BOOK_UPGRADE_ADDITIONS_SLOT_DESCRIPTION_TEXT = Component.translatable(
            "item.xp_storage.smithing_template.xp_book_upgrade.additions_slot_description"
    );

    public static final Item CRYSTALLIZED_LAPIS = new Item(new Item.Properties().setId(ItemKeys.CRYSTALLIZED_LAPIS_KEY));
    public static final SmithingTemplateItem XP_BOOK_UPGRADE = new SmithingTemplateItem(
            XP_BOOK_UPGRADE_APPLIES_TO_TEXT,
            XP_BOOK_UPGRADE_INGREDIENTS_TEXT,
            XP_BOOK_UPGRADE_BASE_SLOT_DESCRIPTION_TEXT,
            XP_BOOK_UPGRADE_ADDITIONS_SLOT_DESCRIPTION_TEXT,
            List.of(XpStorage.id("container/slot/book")),
            List.of(Identifier.withDefaultNamespace("container/slot/diamond"),
                    Identifier.withDefaultNamespace("container/slot/ingot")),
            new Item.Properties().setId(ItemKeys.XP_BOOK_UPGRADE_KEY)
    );
    public static final XpBook XP_BOOK = new XpBook(new Item.Properties().setId(ItemKeys.XP_BOOK_KEY));
    public static final Item XP_BOOK2 = new Item(new Item.Properties().setId(ItemKeys.XP_BOOK2_KEY));
    public static final Item XP_BOOK3 = new Item(new Item.Properties().setId(ItemKeys.XP_BOOK3_KEY));

    public static void init() {
        Registry.register(BuiltInRegistries.ITEM, XpStorage.id("crystallized_lapis"), CRYSTALLIZED_LAPIS);
        Registry.register(BuiltInRegistries.ITEM, XpStorage.id("xp_book_upgrade_smithing_template"), XP_BOOK_UPGRADE);
        Registry.register(BuiltInRegistries.ITEM, XpStorage.id("xp_book"), XP_BOOK);
        Registry.register(BuiltInRegistries.ITEM, XpStorage.id("xp_book2"), XP_BOOK2);
        Registry.register(BuiltInRegistries.ITEM, XpStorage.id("xp_book3"), XP_BOOK3);

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS).register(output -> {
            output.insertAfter(Items.LAPIS_LAZULI, CRYSTALLIZED_LAPIS);
            output.insertAfter(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE, XP_BOOK_UPGRADE);
        });
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(output ->
                output.insertAfter(Items.WRITABLE_BOOK, XP_BOOK));
    }
}
