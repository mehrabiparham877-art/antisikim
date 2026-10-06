package com.example.antiafk;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;

public class AntiAfkMod implements ClientModInitializer {
    private static KeyMapping settingsKey;
    private static KeyMapping toggleKey;

    @Override
    public void onInitializeClient() {
        KeyMapping.Category category =
                KeyMapping.Category.register(Identifier.fromNamespaceAndPath("antiafk", "main"));

        settingsKey = KeyMappingHelper.registerKeyMapping(
                new KeyMapping("key.antiafk.settings", InputConstants.Type.KEYSYM, InputConstants.KEY_Z, category));
        toggleKey = KeyMappingHelper.registerKeyMapping(
                new KeyMapping("key.antiafk.toggle", InputConstants.Type.KEYSYM, InputConstants.KEY_X, category));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (settingsKey.consumeClick()) {
                if (client.screen == null) {
                    client.setScreen(new AfkSettingsScreen());
                }
            }
            while (toggleKey.consumeClick()) {
                AfkConfig.enabled = !AfkConfig.enabled;
                if (!AfkConfig.enabled) {
                    AfkController.releaseKeys(client);
                }
            }
            AfkController.tick(client);
        });
    }
}
