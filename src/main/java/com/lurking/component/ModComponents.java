package com.lurking.component;

import com.lurking.TheLurking;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ModComponents {
    private static final Map<UUID, SanityComponent> SANITY_MAP = new HashMap<>();

    public static SanityComponent getSanity(UUID playerId) {
        return SANITY_MAP.computeIfAbsent(playerId, id -> new SanityComponent());
    }

    public static void register() {
        TheLurking.LOGGER.info("Registering components for " + TheLurking.MOD_ID);
    }
}
