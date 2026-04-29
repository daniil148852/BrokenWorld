package com.lurking.item;

import com.lurking.TheLurking;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    
    public static final Item SHADOW_LURKER_SPAWN_EGG = registerItem("shadow_lurker_spawn_egg",
        new SpawnEggItem(com.lurking.entity.ModEntities.SHADOW_LURKER, 0x1a1a1a, 0x440044,
            new FabricItemSettings()));
    
    public static final Item WHISPERER_SPAWN_EGG = registerItem("whisperer_spawn_egg",
        new SpawnEggItem(com.lurking.entity.ModEntities.WHISPERER, 0x2a2a2a, 0x00aa00,
            new FabricItemSettings()));

    public static final Item CURSED_CRYSTAL = registerItem("cursed_crystal",
        new Item(new FabricItemSettings().maxCount(16)));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(TheLurking.MOD_ID, name), item);
    }

    public static void register() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register(entries -> {
            entries.add(SHADOW_LURKER_SPAWN_EGG);
            entries.add(WHISPERER_SPAWN_EGG);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.add(CURSED_CRYSTAL);
        });
    }
}