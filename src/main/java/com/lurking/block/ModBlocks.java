package com.lurking.block;

import com.lurking.TheLurking;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlocks {
    
    public static final Block CURSED_STONE = registerBlock("cursed_stone",
        new Block(FabricBlockSettings.copyOf(Blocks.STONE).luminance(state -> 2)));

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, new Identifier(TheLurking.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, new Identifier(TheLurking.MOD_ID, name),
            new BlockItem(block, new FabricItemSettings()));
    }

    public static void register() {
        TheLurking.LOGGER.info("Registering blocks for " + TheLurking.MOD_ID);
    }
}