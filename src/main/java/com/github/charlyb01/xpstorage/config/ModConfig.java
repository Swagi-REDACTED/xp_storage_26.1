package com.github.charlyb01.xpstorage.config;

import me.fzzyhmstrs.fzzy_config.annotations.Version;
import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import net.minecraft.resources.Identifier;

@Version(version = 1)
public class ModConfig extends me.fzzyhmstrs.fzzy_config.config.Config {
    public BookConfig books = new BookConfig();
    public BottleConfig bottles = new BottleConfig();
    public CosmeticConfig cosmetic = new CosmeticConfig();

    public ModConfig() {
        super(Identifier.fromNamespaceAndPath("xp_storage", "main"));
    }

    private static ModConfig instance = null;

    public static ModConfig get() {
        if (instance == null) {
            instance = ConfigApiJava.registerAndLoadConfig(ModConfig::new);
        }
        return instance;
    }

    @Override
    public int defaultPermLevel() {
        return 2;
    }
}
