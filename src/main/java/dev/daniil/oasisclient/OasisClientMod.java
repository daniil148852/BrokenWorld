package dev.daniil.oasisclient;

import dev.daniil.oasisclient.config.OasisConfig;
import dev.daniil.oasisclient.network.FrameSender;
import dev.daniil.oasisclient.render.OasisOverlayRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OasisClientMod implements ClientModInitializer {

    public static final String MOD_ID = "oasisclient";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    // Keybindings
    public static KeyBinding toggleKey;
    public static KeyBinding captureKey;

    public static boolean overlayEnabled = false;
    private int tickCounter = 0;

    @Override
    public void onInitializeClient() {
        LOGGER.info("Oasis Client initializing...");

        // Load config
        OasisConfig.load();

        // Register keybindings
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.oasisclient.toggle",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_V,
                "category.oasisclient"
        ));

        captureKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.oasisclient.capture",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                "category.oasisclient"
        ));

        // Register HUD overlay
        HudRenderCallback.EVENT.register((context, tickDelta) -> {
            if (overlayEnabled) {
                OasisOverlayRenderer.render(context);
            }
        });

        // Tick handler for auto-capture
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            // Handle toggle key
            while (toggleKey.wasPressed()) {
                overlayEnabled = !overlayEnabled;
                LOGGER.info("Oasis overlay: {}", overlayEnabled ? "ON" : "OFF");
            }

            // Handle manual capture key
            while (captureKey.wasPressed()) {
                if (overlayEnabled) {
                    FrameSender.captureAndSend(client);
                }
            }

            // Auto-capture every N ticks (20 ticks = 1 second)
            if (overlayEnabled && OasisConfig.autoCaptureEnabled) {
                tickCounter++;
                int intervalTicks = (int)(OasisConfig.captureIntervalSeconds * 20);
                if (tickCounter >= intervalTicks) {
                    tickCounter = 0;
                    FrameSender.captureAndSend(client);
                }
            }
        });

        LOGGER.info("Oasis Client ready! Server: {}", OasisConfig.serverUrl);
    }
}
