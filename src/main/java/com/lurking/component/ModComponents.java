package com.lurking.component;

import com.lurking.TheLurking;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.minecraft.util.Identifier;

public class ModComponents implements EntityComponentInitializer {
    
    public static final ComponentKey<SanityComponent> SANITY =
        ComponentRegistry.getOrCreate(new Identifier(TheLurking.MOD_ID, "sanity"), SanityComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(SANITY, player -> new SanityComponent(), RespawnCopyStrategy.ALWAYS_COPY);
    }

    public static void register() {
        TheLurking.LOGGER.info("Registering components for " + TheLurking.MOD_ID);
    }
}