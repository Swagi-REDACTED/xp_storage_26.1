package com.github.charlyb01.xpstorage;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;

public class Utils {

    public static int getExperienceFromLevel(final int level) {
        if (level >= 30) {
            return 112 + (level - 30) * 9;
        } else {
            return level >= 15 ? 37 + (level - 15) * 5 : 7 + level * 2;
        }
    }

    public static int getExperienceFromLevelToLevel(final int fromLevel, final int toLevel) {
        int experience = 0;
        for (int i = fromLevel; i < toLevel; i++) {
            experience += getExperienceFromLevel(i);
        }
        return experience;
    }

    public static int getExperienceToLevel(final int toLevel) {
        return getExperienceFromLevelToLevel(0, toLevel);
    }

    public static int getLevelFromExperience(final int experience) {
        if (experience <= 0)
            return 0;

        int level = 0;
        int xp = 0;
        while (xp < experience) {
            xp += getExperienceFromLevel(level++);
        }

        return xp == experience ? level : level - 1;
    }

    public static int getPlayerExperience(final Player player) {
        int playerExperience = Utils.getExperienceToLevel(player.experienceLevel);
        playerExperience += Math.round(player.experienceProgress * Utils.getExperienceFromLevel(player.experienceLevel));
        return playerExperience;
    }

    public static boolean isMundanePotion(ItemStack stack) {
        if (!stack.is(Items.POTION)) return false;
        if (!stack.has(DataComponents.POTION_CONTENTS)) return false;

        PotionContents potionContents = stack.get(DataComponents.POTION_CONTENTS);
        return potionContents != null && potionContents.is(Potions.MUNDANE);
    }
}
