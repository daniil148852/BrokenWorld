package com.lurking.entity;

import com.lurking.component.ModComponents;
import com.lurking.sound.ModSounds;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;

public class ShadowLurkerEntity extends HostileEntity {
    private int stalkingTicks = 0;
    private static final int MAX_STALKING_TIME = 600;

    public ShadowLurkerEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        this.experiencePoints = 20;
    }

    public static DefaultAttributeContainer.Builder createShadowLurkerAttributes() {
        return HostileEntity.createHostileAttributes()
            .add(EntityAttributes.GENERIC_MAX_HEALTH, 40.0)
            .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.28)
            .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 8.0)
            .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 64.0)
            .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.5);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new MeleeAttackGoal(this, 1.0, false));
        this.goalSelector.add(3, new WanderAroundFarGoal(this, 0.8));
        this.goalSelector.add(4, new LookAtEntityGoal(this, PlayerEntity.class, 32.0f));
        this.goalSelector.add(5, new LookAroundGoal(this));
        
        this.targetSelector.add(1, new RevengeGoal(this));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
    }

    @Override
    public void tick() {
        super.tick();
        
        if (!this.getWorld().isClient) {
            PlayerEntity nearestPlayer = this.getWorld().getClosestPlayer(this, 16.0);
            
            if (nearestPlayer != null) {
                stalkingTicks++;
                
                if (stalkingTicks % 40 == 0) {
                    ModComponents.getSanity(nearestPlayer.getUuid()).decreaseSanity(2);
                }
                
                if (stalkingTicks > MAX_STALKING_TIME / 2) {
                    this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED)
                        .setBaseValue(0.35);
                }
            } else {
                stalkingTicks = Math.max(0, stalkingTicks - 1);
            }
        }
        
        if (this.getWorld().isClient && this.random.nextFloat() < 0.1f) {
            spawnShadowParticles();
        }
    }

    private void spawnShadowParticles() {
        for (int i = 0; i < 2; i++) {
            double d = this.random.nextGaussian() * 0.02;
            double e = this.random.nextGaussian() * 0.02;
            double f = this.random.nextGaussian() * 0.02;
            this.getWorld().addParticle(
                net.minecraft.particle.ParticleTypes.SMOKE,
                this.getParticleX(1.0),
                this.getRandomBodyY(),
                this.getParticleZ(1.0),
                d, e, f
            );
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.LURKER_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return ModSounds.LURKER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.LURKER_DEATH;
    }

    @Override
    protected float getSoundVolume() {
        return 0.8f;
    }
}
