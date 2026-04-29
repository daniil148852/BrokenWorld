package com.lurking.entity;

import com.lurking.TheLurking;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {
    public static final EntityType<ShadowLurkerEntity> SHADOW_LURKER = Registry.register(
        Registries.ENTITY_TYPE,
        new Identifier(TheLurking.MOD_ID, "shadow_lurker"),
        FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, ShadowLurkerEntity::new)
            .dimensions(EntityDimensions.fixed(0.6f, 2.4f))
            .trackRangeBlocks(64)
            .trackedUpdateRate(3)
            .build()
    );

    public static final EntityType<WhispererEntity> WHISPERER = Registry.register(
        Registries.ENTITY_TYPE,
        new Identifier(TheLurking.MOD_ID, "whisperer"),
        FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, WhispererEntity::new)
            .dimensions(EntityDimensions.fixed(0.8f, 1.8f))
            .trackRangeBlocks(64)
            .trackedUpdateRate(3)
            .build()
    );

    public static void register() {
        FabricDefaultAttributeRegistry.register(SHADOW_LURKER, ShadowLurkerEntity.createShadowLurkerAttributes());
        FabricDefaultAttributeRegistry.register(WHISPERER, WhispererEntity.createWhispererAttributes());
    }
}