package com.lurking.entity;

import com.lurking.component.ModComponents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class WhispererEntity extends HostileEntity {
    
    public WhispererEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        this.experiencePoints = 15;
    }

    public static DefaultAttributeContainer.Builder createWhispererAttributes() {
        return HostileEntity.createHostileAttributes()
            .add(EntityAttributes.GENERIC_MAX_HEALTH, 25.0)
            .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25)
            .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 5.0)
            .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 48.0);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new FleeEntityGoal<>(this, PlayerEntity.class, 6.0f, 1.0, 1.2));
        this.goalSelector.add(3, new WanderAroundFarGoal(this, 0.8));
        this.goalSelector.add(4, new LookAtEntityGoal(this, PlayerEntity.class, 48.0f));
        this.goalSelector.add(5, new LookAroundGoal(this));
    }

    @Override
    public void tick() {
        super.tick();
        
        if (!this.getWorld().isClient) {
            this.getWorld().getPlayers().forEach(player -> {
                if (this.squaredDistanceTo(player) < 576.0) {
                    if (this.age % 60 == 0) {
                        ModComponents.getSanity(player.getUuid()).decreaseSanity(1);
                    }
                }
            });
        }
    }
}
