package com.lurking.client.renderer;

import com.lurking.TheLurking;
import com.lurking.entity.ShadowLurkerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.ZombieEntityModel;
import net.minecraft.util.Identifier;

public class ShadowLurkerRenderer extends MobEntityRenderer<ShadowLurkerEntity, ZombieEntityModel<ShadowLurkerEntity>> {
    
    private static final Identifier TEXTURE = new Identifier(TheLurking.MOD_ID, "textures/entity/shadow_lurker.png");

    public ShadowLurkerRenderer(EntityRendererFactory.Context context) {
        super(context, new ZombieEntityModel<>(context.getPart(EntityModelLayers.ZOMBIE)), 0.5f);
    }

    @Override
    public Identifier getTexture(ShadowLurkerEntity entity) {
        return TEXTURE;
    }
}