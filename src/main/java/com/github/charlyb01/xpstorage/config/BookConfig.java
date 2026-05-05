package com.github.charlyb01.xpstorage.config;

import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;

public class BookConfig extends ConfigSection {
    public static class BookTierConfig extends ConfigSection {
        public int capacity;
        public ValidatedInt xpFromUsing;
        public ValidatedInt xpFromBrewing;
        public int barColor;

        public BookTierConfig() {}
        public BookTierConfig(int capacity, int xpFromUsing, int xpFromBrewing, int barColor) {
            this.capacity = capacity;
            this.xpFromUsing = new ValidatedInt(xpFromUsing, 130, 85);
            this.xpFromBrewing = new ValidatedInt(xpFromBrewing, 100, 1);
            this.barColor = barColor;
        }
    }

    public BookTierConfig level0 = new BookTierConfig(15, 85, 1, Integer.parseInt("1c53a8", 16));
    public BookTierConfig level1 = new BookTierConfig(30, 90, 3, 10615784);
    public BookTierConfig level2 = new BookTierConfig(50, 95, 5, 5920602);
    public BookTierConfig level3 = new BookTierConfig(100, 100, 10, 14738039);

    public BookTierConfig getTier(int level) {
        return switch (level) {
            case 1 -> level1;
            case 2 -> level2;
            case 3 -> level3;
            default -> level0;
        };
    }
}
