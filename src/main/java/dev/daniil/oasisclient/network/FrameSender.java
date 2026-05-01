package dev.daniil.oasisclient.network;

import dev.daniil.oasisclient.OasisClientMod;
import dev.daniil.oasisclient.config.OasisConfig;
import dev.daniil.oasisclient.render.OasisOverlayRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

public class FrameSender {

    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    // Prevent multiple simultaneous requests
    private static final AtomicBoolean isSending = new AtomicBoolean(false);

    public static void captureAndSend(MinecraftClient client) {
        if (isSending.get()) {
            return; // Skip if previous request still in flight
        }

        if (client.getFramebuffer() == null || client.world == null) {
            return;
        }

        try {
            // Capture current framebuffer
            byte[] imageBytes = captureFramebuffer(client);
            if (imageBytes == null) return;

            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

            // Send async so we don't freeze game
            isSending.set(true);
            CompletableFuture.runAsync(() -> sendToServer(base64Image))
                    .whenComplete((v, e) -> isSending.set(false));

        } catch (Exception e) {
            OasisClientMod.LOGGER.error("Failed to capture frame", e);
            isSending.set(false);
        }
    }

    private static byte[] captureFramebuffer(MinecraftClient client) {
        try {
            Framebuffer fb = client.getFramebuffer();
            int width = fb.textureWidth;
            int height = fb.textureHeight;

            // Read pixels from OpenGL framebuffer
            ByteBuffer buffer = ByteBuffer.allocateDirect(width * height * 4);
            fb.beginRead();

            // Use GL to read pixels
            org.lwjgl.opengl.GL11.glReadPixels(
                    0, 0, width, height,
                    org.lwjgl.opengl.GL11.GL_RGBA,
                    org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE,
                    buffer
            );
            fb.endRead();

            // Convert to BufferedImage (flip vertically — OpenGL is bottom-up)
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int i = (y * width + x) * 4;
                    int r = buffer.get(i) & 0xFF;
                    int g = buffer.get(i + 1) & 0xFF;
                    int b = buffer.get(i + 2) & 0xFF;
                    image.setRGB(x, height - 1 - y, (r << 16) | (g << 8) | b);
                }
            }

            // Scale down to configured size for faster processing
            java.awt.image.BufferedImage scaled = new java.awt.image.BufferedImage(
                    OasisConfig.captureWidth, OasisConfig.captureHeight,
                    BufferedImage.TYPE_INT_RGB
            );
            java.awt.Graphics2D g2d = scaled.createGraphics();
            g2d.setRenderingHint(java.awt.RenderingHints.KEY_INTERPOLATION,
                    java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.drawImage(image, 0, 0, OasisConfig.captureWidth, OasisConfig.captureHeight, null);
            g2d.dispose();

            // Encode to PNG bytes
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(scaled, "PNG", baos);
            return baos.toByteArray();

        } catch (Exception e) {
            OasisClientMod.LOGGER.error("Framebuffer capture failed", e);
            return null;
        }
    }

    private static void sendToServer(String base64Image) {
        try {
            // Build JSON payload
            String json = String.format(
                    "{\"image\":\"%s\",\"prompt\":\"%s\"}",
                    base64Image,
                    OasisConfig.prompt.replace("\"", "\\\"")
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(OasisConfig.serverUrl + "/transform"))
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofSeconds(30))
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = HTTP_CLIENT.send(request,
                    HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                // Parse response JSON: {"image": "<base64>"}
                String body = response.body();
                String resultBase64 = extractBase64FromJson(body);
                if (resultBase64 != null) {
                    byte[] imageBytes = Base64.getDecoder().decode(resultBase64);
                    BufferedImage resultImage = ImageIO.read(new ByteArrayInputStream(imageBytes));
                    OasisOverlayRenderer.updateFrame(resultImage);
                    OasisClientMod.LOGGER.debug("Frame updated successfully");
                }
            } else {
                OasisClientMod.LOGGER.warn("Server returned: {} — {}", response.statusCode(), response.body());
            }

        } catch (Exception e) {
            OasisClientMod.LOGGER.error("Failed to send frame to server", e);
        }
    }

    private static String extractBase64FromJson(String json) {
        // Simple extraction without extra deps: {"image":"<data>"}
        int start = json.indexOf("\"image\":\"");
        if (start == -1) return null;
        start += 9;
        int end = json.indexOf("\"", start);
        if (end == -1) return null;
        return json.substring(start, end);
    }
}
