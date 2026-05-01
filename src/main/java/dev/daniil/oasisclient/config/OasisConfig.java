package dev.daniil.oasisclient.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.daniil.oasisclient.OasisClientMod;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.Path;

public class OasisConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance()
            .getConfigDir().resolve("oasis-client.json");

    // Settings
    public static String serverUrl = "http://localhost:5000";
    public static String prompt = "photorealistic, ultra detailed, 8k";
    public static float captureIntervalSeconds = 3.0f;
    public static boolean autoCaptureEnabled = true;
    public static float overlayOpacity = 1.0f;
    public static int captureWidth = 512;
    public static int captureHeight = 512;

    public static void load() {
        File configFile = CONFIG_PATH.toFile();
        if (configFile.exists()) {
            try (Reader reader = new FileReader(configFile)) {
                ConfigData data = GSON.fromJson(reader, ConfigData.class);
                if (data != null) {
                    if (data.serverUrl != null) serverUrl = data.serverUrl;
                    if (data.prompt != null) prompt = data.prompt;
                    if (data.captureIntervalSeconds > 0) captureIntervalSeconds = data.captureIntervalSeconds;
                    autoCaptureEnabled = data.autoCaptureEnabled;
                    overlayOpacity = data.overlayOpacity;
                    if (data.captureWidth > 0) captureWidth = data.captureWidth;
                    if (data.captureHeight > 0) captureHeight = data.captureHeight;
                }
            } catch (IOException e) {
                OasisClientMod.LOGGER.error("Failed to load config", e);
            }
        } else {
            save(); // Create default config
        }
    }

    public static void save() {
        try (Writer writer = new FileWriter(CONFIG_PATH.toFile())) {
            ConfigData data = new ConfigData();
            data.serverUrl = serverUrl;
            data.prompt = prompt;
            data.captureIntervalSeconds = captureIntervalSeconds;
            data.autoCaptureEnabled = autoCaptureEnabled;
            data.overlayOpacity = overlayOpacity;
            data.captureWidth = captureWidth;
            data.captureHeight = captureHeight;
            GSON.toJson(data, writer);
        } catch (IOException e) {
            OasisClientMod.LOGGER.error("Failed to save config", e);
        }
    }

    private static class ConfigData {
        String serverUrl = "http://localhost:5000";
        String prompt = "photorealistic, ultra detailed, 8k";
        float captureIntervalSeconds = 3.0f;
        boolean autoCaptureEnabled = true;
        float overlayOpacity = 1.0f;
        int captureWidth = 512;
        int captureHeight = 512;
    }
}
