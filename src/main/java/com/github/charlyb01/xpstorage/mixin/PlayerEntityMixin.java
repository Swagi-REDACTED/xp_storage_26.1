package com.github.charlyb01.xpstorage.mixin;
 
import com.github.charlyb01.xpstorage.Utils;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
 
@Mixin(Player.class)
public abstract class PlayerEntityMixin {
    @Shadow public int experienceLevel;
    @Shadow public float experienceProgress;
    @Shadow public int totalExperience;
 
    @Shadow public abstract void increaseScore(int score);
    @Shadow public abstract void giveExperienceLevels(int levels);
    @Shadow public abstract int getXpNeededForNextLevel();
 
    @Inject(method = "giveExperiencePoints", at = @At("HEAD"), cancellable = true)
    private void correctAddExperience(int experience, CallbackInfo ci) {
        if (experience == 0) return;
 
        this.increaseScore(experience);
        this.totalExperience = Mth.clamp(this.totalExperience + experience, 0, Integer.MAX_VALUE);
 
        int playerExperience = Utils.getPlayerExperience((Player) (Object) this);
        playerExperience += experience;
 
        this.experienceLevel = 0;
        this.giveExperienceLevels(Utils.getLevelFromExperience(playerExperience));
        int deltaExperience = playerExperience - Utils.getExperienceToLevel(this.experienceLevel);
        this.experienceProgress = deltaExperience / (float)this.getXpNeededForNextLevel();
 
        ci.cancel();
    }
}
