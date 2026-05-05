package com.github.charlyb01.xpstorage.mixin;
 
import com.github.charlyb01.xpstorage.component.MyComponents;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownExperienceBottle;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
 
@Mixin(ThrownExperienceBottle.class)
public abstract class XpBottleEntityMixin extends ThrowableItemProjectile {
    public XpBottleEntityMixin(EntityType<? extends ThrowableItemProjectile> entityType, Level world) {
        super(entityType, world);
    }
 
    @WrapOperation(method = "onHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ExperienceOrb;awardWithDirection(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/Vec3;I)V"))
    private void changeXpAmount(ServerLevel world, Vec3 pos, Vec3 direction, int randomAmount, Operation<Void> original) {
        final int xpAmount = MyComponents.XP_COMPONENT_CC.get(this).getAmount();
        original.call(world, pos, direction, xpAmount > 0 ? xpAmount : randomAmount);
    }
}
