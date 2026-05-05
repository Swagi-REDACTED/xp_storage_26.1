package com.github.charlyb01.xpstorage.component;

import com.github.charlyb01.xpstorage.XpStorage;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownExperienceBottle;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.item.ItemComponentInitializer;
import org.ladysnake.cca.api.v3.item.ItemComponentMigrationRegistry;

public final class MyComponents implements EntityComponentInitializer, ItemComponentInitializer {
    private static final Identifier BOOK_COMPONENT_ID = Identifier.fromNamespaceAndPath(XpStorage.MOD_ID, "book_component");
    private static final Identifier XP_COMPONENT_ID = Identifier.fromNamespaceAndPath(XpStorage.MOD_ID, "xp_component");
    public static final ComponentKey<ExperienceComponent> XP_COMPONENT_CC =
            ComponentRegistry.getOrCreate(XP_COMPONENT_ID, ExperienceComponent.class);

    public static final DataComponentType<BookData> BOOK_COMPONENT = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            BOOK_COMPONENT_ID,
            DataComponentType.<BookData>builder().persistent(BookData.CODEC).build()
    );

    public static final DataComponentType<XpAmountData> XP_COMPONENT = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            XP_COMPONENT_ID,
            DataComponentType.<XpAmountData>builder().persistent(XpAmountData.CODEC).build()
    );

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerFor(ThrownExperienceBottle.class, XP_COMPONENT_CC, entity -> new XpAmountComponent());
    }

    @Override
    public void registerItemComponentMigrations(ItemComponentMigrationRegistry registry) {
        registry.registerMigration(XP_COMPONENT_ID, XP_COMPONENT);
    }
}
