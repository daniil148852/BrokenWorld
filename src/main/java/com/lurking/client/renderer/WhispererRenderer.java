package com.lurking.client.renderer;

import com.lurking.TheLurking;
import com.lurking.entity.WhispererEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.SkeletonEntityModel;
import net.minecraft.util.Identifier;

public class WhispererRenderer extends MobEntityRenderer<WhispererEntity, SkeletonEntityModel<WhispererEntity>> {
    
    private static final Identifier TEXTURE = new Identifier(TheLurking.MOD_ID, "textures/entity/whisperer.png");

    public WhispererRenderer(EntityRendererFactory.Context context) {
        super(context, new SkeletonEntityModel<>(context.getPart(EntityModelLayers.SKELETON)), 0.5f);
    }

    @Override
    public Identifier getTexture(WhispererEntity entity) {
        return TEXTURE;
    }
}