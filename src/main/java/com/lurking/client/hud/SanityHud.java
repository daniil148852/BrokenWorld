package com.lurking.client.hud;

import com.lurking.component.ModComponents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class SanityHud implements HudRenderCallback {
    
    @Override
    public void onHudRender(DrawContext drawContext, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        
        if (client.player == null) return;
        
        var sanity = ModComponents.getSanity(client.player.getUuid());
        
        int x = 10;
        int y = 10;
        int barWidth = 100;
        int barHeight = 10;
        
        // Background
        drawContext.fill(x, y, x + barWidth, y + barHeight, 0x88000000);
        
        // Sanity bar
        int sanityWidth = (int) (barWidth * (sanity.getSanity() / 100.0));
        int color = getColorForSanity(sanity.getSanity());
        drawContext.fill(x, y, x + sanityWidth, y + barHeight, color);
        
        // Border
        drawContext.drawBorder(x, y, barWidth, barHeight, 0xFFFFFFFF);
        
        // Text
        String text = "Sanity: " + sanity.getSanity();
        drawContext.drawText(client.textRenderer, text, x, y - 10, 0xFFFFFFFF, true);
    }
    
    private int getColorForSanity(int sanity) {
        if (sanity > 66) {
            return 0xFF00FF00;
        } else if (sanity > 33) {
            return 0xFFFFFF00;
        } else {
            return 0xFFFF0000;
        }
    }
}
