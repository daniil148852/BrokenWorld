package com.lurking.client;

import com.lurking.client.hud.SanityHud;
import com.lurking.client.renderer.ShadowLurkerRenderer;
import com.lurking.client.renderer.WhispererRenderer;
import com.lurking.entity.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class TheLurkingClient implements ClientModInitializer {
    
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(ModEntities.SHADOW_LURKER, ShadowLurkerRenderer::new);
        EntityRendererRegistry.register(ModEntities.WHISPERER, WhispererRenderer::new);
        
        HudRenderCallback.EVENT.register(new SanityHud());
    }
}