package com.github.charlyb01.xpstorage.component;

import com.github.charlyb01.xpstorage.Utils;
import org.ladysnake.cca.api.v3.component.Component;

@SuppressWarnings("deprecation")
class XpAmountComponent implements Component, ExperienceComponent {
    private int level = 0;
    private int amount = 0;

    @Override
    public int getAmount() { return this.amount; }

    @Override
    public void setAmount(final int amount) {
        this.amount = amount;
        this.level = Utils.getLevelFromExperience(amount);
    }

    @Override
    public void setLevel(final int level) {
        this.amount = Utils.getExperienceToLevel(level);
        this.level = level;
    }

    @Override
    public void readData(net.minecraft.world.level.storage.ValueInput input) {
        this.amount = input.getIntOr("xp_amount", 0);
        this.level = input.getIntOr("xp_level", 0);
    }

    @Override
    public void writeData(net.minecraft.world.level.storage.ValueOutput output) {
        output.putInt("xp_amount", this.amount);
        output.putInt("xp_level", this.level);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof XpAmountComponent &&
                ((XpAmountComponent) obj).amount == this.amount;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(this.amount);
    }
}
