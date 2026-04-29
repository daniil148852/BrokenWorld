package com.lurking.sound;

import com.lurking.TheLurking;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds {
    
    public static final SoundEvent LURKER_AMBIENT = registerSound("entity.shadow_lurker.ambient");
    public static final SoundEvent LURKER_HURT = registerSound("entity.shadow_lurker.hurt");
    public static final SoundEvent LURKER_DEATH = registerSound("entity.shadow_lurker.death");
    public static final SoundEvent WHISPER = registerSound("entity.whisperer.whisper");
    public static final SoundEvent SANITY_LOW = registerSound("sanity.low");

    private static SoundEvent registerSound(String name) {
        Identifier id = new Identifier(TheLurking.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void register() {
        TheLurking.LOGGER.info("Registering sounds for " + TheLurking.MOD_ID);
    }
}