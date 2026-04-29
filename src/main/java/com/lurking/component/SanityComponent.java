package com.lurking.component;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.nbt.NbtCompound;

public class SanityComponent implements Component {
    private int sanity = 100;
    private static final int MAX_SANITY = 100;
    private static final int MIN_SANITY = 0;

    public int getSanity() {
        return sanity;
    }

    public void setSanity(int sanity) {
        this.sanity = Math.max(MIN_SANITY, Math.min(MAX_SANITY, sanity));
    }

    public void decreaseSanity(int amount) {
        setSanity(sanity - amount);
    }

    public void increaseSanity(int amount) {
        setSanity(sanity + amount);
    }

    public boolean isLowSanity() {
        return sanity < 30;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        sanity = tag.getInt("sanity");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putInt("sanity", sanity);
    }
}