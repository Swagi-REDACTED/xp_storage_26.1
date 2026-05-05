package com.github.charlyb01.xpstorage.mixin.brewing;
 
import com.github.charlyb01.xpstorage.Utils;
import com.github.charlyb01.xpstorage.item.XpBook;
import com.github.charlyb01.xpstorage.component.MyComponents;
import com.github.charlyb01.xpstorage.component.XpAmountData;
import com.github.charlyb01.xpstorage.config.ModConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
 
@Mixin(BrewingStandBlockEntity.class)
public abstract class BrewingStandMixin extends BaseContainerBlockEntity {
 
    protected BrewingStandMixin(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }
 
    @Inject(method = "isBrewable", at = @At("HEAD"), cancellable = true)
    private static void canUseXpBooks(PotionBrewing potionBrewing, NonNullList<ItemStack> slots, CallbackInfoReturnable<Boolean> cir) {
        ItemStack ingredient = slots.get(3);
        if (ingredient.isEmpty() || !(ingredient.getItem() instanceof XpBook)
                || !ingredient.has(MyComponents.XP_COMPONENT))
            return;
 
        final int bookExperience = ingredient.get(MyComponents.XP_COMPONENT).amount();
        int levelIncrement = XpBook.getXpFromBrewing(ingredient);
 
        for (int i = 0; i < 3; ++i) {
            ItemStack potion = slots.get(i);
            // Due to a minecraft edge case, you can stack potion with quick move.
            // getMaxItemCount is not taken into account when item is stackable.
            // The fix would be trivial, but not with mixin
            if (potion.isEmpty() || potion.getCount() > 1)
                continue;
 
            if (potion.is(Items.EXPERIENCE_BOTTLE) && potion.has(MyComponents.XP_COMPONENT)) {
                final int currentLevel = potion.get(MyComponents.XP_COMPONENT).level();
                final int nextLevel = currentLevel + levelIncrement;
                final int xpForNextLevel = Utils.getExperienceFromLevelToLevel(currentLevel, nextLevel);
                if (bookExperience >= xpForNextLevel && nextLevel <= ModConfig.get().bottles.maxLevel) {
                    cir.setReturnValue(true);
                }
            } else if (Utils.isMundanePotion(potion)) {
                if (bookExperience >= Utils.getExperienceFromLevel(levelIncrement)
                        && levelIncrement <= ModConfig.get().bottles.maxLevel) {
                    cir.setReturnValue(true);
                }
            }
        }
    }
 
    @Inject(method = "doBrew", at = @At("HEAD"), cancellable = true)
    private static void craftXpBottles(Level world, BlockPos pos, NonNullList<ItemStack> slots, CallbackInfo ci) {
        ItemStack ingredient = slots.get(3);
        if (ingredient.isEmpty() || !(ingredient.getItem() instanceof XpBook)
                || !ingredient.has(MyComponents.XP_COMPONENT))
            return;
 
        int levelIncrement = XpBook.getXpFromBrewing(ingredient);
 
        for (int i = 0; i < 3; ++i) {
            ItemStack potion = slots.get(i);
            // See above
            if (potion.isEmpty() || potion.getCount() > 1)
                continue;
 
            final int bookExperience = ingredient.getOrDefault(MyComponents.XP_COMPONENT, XpAmountData.EMPTY).amount();
 
            if (potion.is(Items.EXPERIENCE_BOTTLE) && potion.has(MyComponents.XP_COMPONENT)) {
                final int currentLevel = potion.get(MyComponents.XP_COMPONENT).level();
                final int nextLevel = currentLevel + levelIncrement;
                final int xpForNextLevel = Utils.getExperienceFromLevelToLevel(currentLevel, nextLevel);
                if (bookExperience >= xpForNextLevel && nextLevel <= ModConfig.get().bottles.maxLevel) {
                    potion.set(MyComponents.XP_COMPONENT, new XpAmountData(Utils.getExperienceToLevel(nextLevel), nextLevel));
                    int amount = bookExperience - xpForNextLevel;
                    ingredient.set(MyComponents.XP_COMPONENT, new XpAmountData(amount, Utils.getLevelFromExperience(amount)));
                }
            } else if (Utils.isMundanePotion(potion)) {
                final int xpForFirstLevels = Utils.getExperienceToLevel(levelIncrement);
                if (bookExperience >= xpForFirstLevels && levelIncrement <= ModConfig.get().bottles.maxLevel) {
                    ItemStack xpBottle = new ItemStack(Items.EXPERIENCE_BOTTLE);
                    slots.set(i, xpBottle);
                    xpBottle.set(MyComponents.XP_COMPONENT, new XpAmountData(xpForFirstLevels, levelIncrement));
                    int amount = bookExperience - xpForFirstLevels;
                    ingredient.set(MyComponents.XP_COMPONENT, new XpAmountData(amount, Utils.getLevelFromExperience(amount)));
                }
            }
        }
 
        if (world != null) {
            world.levelEvent(1035, pos, 0);
        }
        ci.cancel();
    }
}
