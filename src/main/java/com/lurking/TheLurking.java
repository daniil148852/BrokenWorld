package com.lurking;

import com.lurking.block.ModBlocks;
import com.lurking.component.ModComponents;
import com.lurking.entity.ModEntities;
import com.lurking.item.ModItems;
import com.lurking.network.ModNetworking;
import com.lurking.sound.ModSounds;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TheLurking implements ModInitializer {
    public static final String MOD_ID = "thelurking";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("The darkness awakens...");
        
        ModComponents.register();
        ModSounds.register();
        ModItems.register();
        ModBlocks.register();
        ModEntities.register();
        ModNetworking.register();
        
        LOGGER.info("The Lurking has been initialized!");
    }
}