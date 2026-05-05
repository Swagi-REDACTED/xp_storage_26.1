package com.github.charlyb01.xpstorage.client;

import com.github.charlyb01.xpstorage.component.BookData;
import com.github.charlyb01.xpstorage.component.MyComponents;
import com.github.charlyb01.xpstorage.component.XpAmountData;
import com.github.charlyb01.xpstorage.config.ExperienceTooltip;
import com.github.charlyb01.xpstorage.config.ModConfig;
import com.github.charlyb01.xpstorage.item.ItemRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

@Environment(EnvType.CLIENT)
public class XpStorageClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ItemTooltipCallback.EVENT.register((stack, context, type, lines) -> {
            if (stack.is(Items.EXPERIENCE_BOTTLE)) {
                int level = stack.getOrDefault(MyComponents.XP_COMPONENT, XpAmountData.EMPTY).level();
                int amount = stack.getOrDefault(MyComponents.XP_COMPONENT, XpAmountData.EMPTY).amount();
                if (amount <= 0) return;
 
                lines.add(Component.translatable("item.xp_storage.tooltip")
                            .withStyle(ChatFormatting.GRAY));
                if (!ModConfig.get().cosmetic.bottleTooltip.equals(ExperienceTooltip.POINT)) {
                    lines.add(Component.translatable("item.xp_storage.xp_bottle.tooltip.level", level)
                            .withStyle(ChatFormatting.BLUE));
                }
                if (!ModConfig.get().cosmetic.bottleTooltip.equals(ExperienceTooltip.LEVEL)) {
                    lines.add(Component.translatable("item.xp_storage.xp_bottle.tooltip.point", amount)
                            .withStyle(ChatFormatting.BLUE));
                }
            } else if (isDeprecated(stack)) {
                lines.add(Component.translatable("item.xp_storage.tooltip.deprecated")
                        .withStyle(ChatFormatting.RED));
                lines.add(Component.translatable("item.xp_storage.tooltip.deprecated_how")
                        .withStyle(ChatFormatting.DARK_RED, ChatFormatting.ITALIC));
            }
        });
    }

    private static boolean isDeprecated(ItemStack stack) {
        if (!stack.is(ItemRegistry.XP_BOOK)
                && !stack.is(ItemRegistry.XP_BOOK2)
                && !stack.is(ItemRegistry.XP_BOOK3)) {
            return false;
        }

        XpAmountData xpAmountData = stack.get(MyComponents.XP_COMPONENT);
        if (xpAmountData == null) return false;

        BookData bookData = stack.get(MyComponents.BOOK_COMPONENT);
        return  bookData == null || bookData.capacity() < xpAmountData.level();
    }
}
