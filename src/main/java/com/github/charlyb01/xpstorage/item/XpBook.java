package com.github.charlyb01.xpstorage.item;

import com.github.charlyb01.xpstorage.Utils;
import com.github.charlyb01.xpstorage.component.BookData;
import com.github.charlyb01.xpstorage.component.MyComponents;
import com.github.charlyb01.xpstorage.component.XpAmountData;
import com.github.charlyb01.xpstorage.config.ExperienceTooltip;
import com.github.charlyb01.xpstorage.config.ModConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.function.Consumer;

public class XpBook extends Item {
    public XpBook(Properties properties) {
        super(properties
                .stacksTo(1)
                .rarity(Rarity.UNCOMMON)
                .component(MyComponents.BOOK_COMPONENT, BookData.getDefault())
                .component(MyComponents.XP_COMPONENT, XpAmountData.EMPTY)
        );
    }

    @Override
    @SuppressWarnings("deprecation")
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, net.minecraft.world.item.component.TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag type) {
        final int bookLevel = getBookLevel(stack);
        XpAmountData xpAmountData = stack.getOrDefault(MyComponents.XP_COMPONENT, XpAmountData.EMPTY);
        final int xpLevel = xpAmountData.level();
        final int xpAmount = xpAmountData.amount();

        tooltip.accept(Component.translatable("item.xp_storage.xp_book.tooltip.upgrade", bookLevel));
        tooltip.accept(Component.translatable("item.xp_storage.tooltip")
                .withStyle(ChatFormatting.GRAY));
        if (!ModConfig.get().cosmetic.bookTooltip.equals(ExperienceTooltip.POINT)) {
            tooltip.accept(Component.translatable("item.xp_storage.xp_book.tooltip.level", xpLevel, getMaxXpLevel(stack))
                    .withStyle(ChatFormatting.BLUE));
        }
        if (!ModConfig.get().cosmetic.bookTooltip.equals(ExperienceTooltip.LEVEL)) {
            tooltip.accept(Component.translatable("item.xp_storage.xp_book.tooltip.point", xpAmount, getMaxXpAmount(stack))
                    .withStyle(ChatFormatting.BLUE));
        }
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        XpAmountData xpAmountData = stack.getOrDefault(MyComponents.XP_COMPONENT, XpAmountData.EMPTY);
        final int bookExperience = xpAmountData.amount();
        return (bookExperience / (float) getMaxXpAmount(stack)) * 100 >= ModConfig.get().cosmetic.glint;
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return stack.getOrDefault(MyComponents.BOOK_COMPONENT, BookData.getDefault()).barColor();
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        XpAmountData xpAmountData = stack.getOrDefault(MyComponents.XP_COMPONENT, XpAmountData.EMPTY);
        final int bookExperience = xpAmountData.amount();
        return Math.round((bookExperience * 13) / (float) getMaxXpAmount(stack));
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        XpAmountData xpAmountData = stack.getOrDefault(MyComponents.XP_COMPONENT, XpAmountData.EMPTY);
        return xpAmountData.amount() > 0;
    }

    @Override
    public InteractionResult use(Level level, Player user, InteractionHand hand) {
        ItemStack stack = (user.getMainHandItem().getItem() instanceof XpBook) ? user.getMainHandItem() : user.getOffhandItem();
        XpAmountData xpAmountData = stack.getOrDefault(MyComponents.XP_COMPONENT, XpAmountData.EMPTY);
        final int bookExperience = xpAmountData.amount();
        int playerExperience = Utils.getPlayerExperience(user);

        final int maxExperience = getMaxXpAmount(stack);
        final int maxLevel = getMaxXpLevel(stack);

        if (level.isClientSide()) {
            // Play sound when filling
            if (!user.isShiftKeyDown() && playerExperience > 0 && bookExperience < maxExperience) {
                user.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
            }
        } else {
            // Empty / Fill
            if (user.isShiftKeyDown()) {
                final int retrievedExperience = Math.round(bookExperience * (getXpFromUsing(stack) / 100.0F));
                ExperienceOrb.award((ServerLevel) level, user.position(), retrievedExperience);
                stack.set(MyComponents.XP_COMPONENT, XpAmountData.EMPTY);
            } else {
                // Check max value
                if (maxExperience - bookExperience < playerExperience) {
                    user.giveExperiencePoints(bookExperience - maxExperience);
                    stack.set(MyComponents.XP_COMPONENT, new XpAmountData(maxExperience, maxLevel));
                } else {
                    int amount = bookExperience + playerExperience;
                    stack.set(MyComponents.XP_COMPONENT, new XpAmountData(amount, Utils.getLevelFromExperience(amount)));
                    user.giveExperiencePoints(-playerExperience);
                }
            }
        }

        return InteractionResult.SUCCESS;
    }

    public static int getBookLevel(ItemStack stack) {
        return stack.getOrDefault(MyComponents.BOOK_COMPONENT, BookData.getDefault()).level();
    }

    public static int getMaxXpLevel(ItemStack stack) {
        return stack.getOrDefault(MyComponents.BOOK_COMPONENT, BookData.getDefault()).capacity();
    }

    public static int getMaxXpAmount(ItemStack stack) {
        return Utils.getExperienceToLevel(getMaxXpLevel(stack));
    }

    public static int getXpFromUsing(ItemStack stack) {
        return stack.getOrDefault(MyComponents.BOOK_COMPONENT, BookData.getDefault()).xpFromUsing();
    }

    public static int getXpFromBrewing(ItemStack stack) {
        return stack.getOrDefault(MyComponents.BOOK_COMPONENT, BookData.getDefault()).xpFromBrewing();
    }
}
