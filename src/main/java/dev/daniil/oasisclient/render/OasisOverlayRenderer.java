package dev.daniil.oasisclient.render;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.daniil.oasisclient.OasisClientMod;
import dev.daniil.oasisclient.config.OasisConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;

import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicReference;

public class OasisOverlayRenderer {

    private static final Identifier OVERLAY_TEXTURE_ID = Identifier.of("oasisclient", "overlay");

    // Thread-safe frame holder
    private static final AtomicReference<BufferedImage> pendingFrame = new AtomicReference<>(null);
    private static NativeImageBackedTexture overlayTexture = null;
    private static boolean hasFrame = false;

    // Called from network thread
    public static void updateFrame(BufferedImage image) {
        pendingFrame.set(image);
    }

    // Called from render thread every frame
    public static void render(DrawContext context) {
        MinecraftClient client = MinecraftClient.getInstance();

        // Upload pending frame to GPU (must be on render thread)
        BufferedImage newFrame = pendingFrame.getAndSet(null);
        if (newFrame != null) {
            uploadTexture(client, newFrame);
            hasFrame = true;
        }

        if (!hasFrame) return;

        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();

        // Draw fullscreen overlay
        RenderSystem.setShaderColor(1f, 1f, 1f, OasisConfig.overlayOpacity);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        context.drawTexture(
                OVERLAY_TEXTURE_ID,
                0, 0,           // x, y
                screenWidth, screenHeight,  // width, height
                0, 0,           // u, v
                1, 1,           // regionWidth, regionHeight
                1, 1            // textureWidth, textureHeight
        );

        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.disableBlend();

        // Draw status text
        String status = String.format("§a[Oasis] §fAI overlay ON | §7%.1fs interval | §eR§f=capture §eV§f=toggle",
                OasisConfig.captureIntervalSeconds);
        context.drawTextWithShadow(client.textRenderer, status, 4, 4, 0xFFFFFF);
    }

    private static void uploadTexture(MinecraftClient client, BufferedImage image) {
        try {
            int width = image.getWidth();
            int height = image.getHeight();

            NativeImage nativeImage = new NativeImage(NativeImage.Format.RGBA, width, height, false);

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int argb = image.getRGB(x, y);
                    int a = (argb >> 24) & 0xFF;
                    int r = (argb >> 16) & 0xFF;
                    int g = (argb >> 8) & 0xFF;
                    int b = argb & 0xFF;
                    // NativeImage uses ABGR format
                    nativeImage.setColor(x, y, (a << 24) | (b << 16) | (g << 8) | r);
                }
            }

            if (overlayTexture == null) {
                overlayTexture = new NativeImageBackedTexture(nativeImage);
                client.getTextureManager().registerTexture(OVERLAY_TEXTURE_ID, overlayTexture);
            } else {
                overlayTexture.setImage(nativeImage);
                overlayTexture.upload();
            }

        } catch (Exception e) {
            OasisClientMod.LOGGER.error("Failed to upload texture", e);
        }
    }
}
