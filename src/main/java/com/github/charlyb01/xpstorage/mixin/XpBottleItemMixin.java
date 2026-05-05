package com.github.charlyb01.xpstorage.mixin;
 
import com.github.charlyb01.xpstorage.component.MyComponents;
import com.github.charlyb01.xpstorage.component.XpAmountData;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ExperienceBottleItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
 
@Mixin(ExperienceBottleItem.class)
public class XpBottleItemMixin extends Item {
    public XpBottleItemMixin(Properties properties) {
        super(properties);
    }
 
    @WrapOperation(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/Projectile;spawnProjectileFromRotation(Lnet/minecraft/world/entity/projectile/Projectile$ProjectileFactory;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;FFF)Lnet/minecraft/world/entity/projectile/Projectile;"))
    private <T extends Projectile> T setExperienceToEntity(Projectile.ProjectileFactory<T> creator, ServerLevel world,
                                                                   ItemStack projectileStack, LivingEntity shooter,
                                                                   float pitchOffset, float velocity, float uncertainty,
                                                                   Operation<T> original) {
        T experienceBottleEntity = original.call(creator, world, projectileStack, shooter, pitchOffset, velocity, uncertainty);
        final int level = projectileStack.getOrDefault(MyComponents.XP_COMPONENT, XpAmountData.EMPTY).level();
        if (level > 0) {
            MyComponents.XP_COMPONENT_CC.get(experienceBottleEntity).setLevel(level);
        }
        return experienceBottleEntity;
    }
}
