package com.github.charlyb01.xpstorage.mixin.brewing;
 
import com.github.charlyb01.xpstorage.config.ModConfig;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
 
@Mixin(targets = "net.minecraft.world.inventory.BrewingStandMenu$PotionSlot")
public class BrewingPotionSlotMixin {
    @ModifyReturnValue(method = "mayPlace", at = @At("RETURN"))
    private static boolean makeXpBottlesMatches(boolean original, ItemStack stack) {
        return original || ModConfig.get().bottles.enableBrewing && stack.is(Items.EXPERIENCE_BOTTLE);
    }
}
