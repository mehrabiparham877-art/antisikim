package com.example.antiafk;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public class AfkSettingsScreen extends Screen {
    public AfkSettingsScreen() {
        super(Component.literal("Smooth Anti-AFK Settings"));
    }

    @Override
    protected void init() {
        int w = 200;
        int h = 20;
        int x = this.width / 2 - w / 2;
        int y = this.height / 2 - 90;
        int gap = 24;

        addToggle(x, y, w, h, "Anti-AFK", () -> AfkConfig.enabled, v -> AfkConfig.enabled = v);
        addToggle(x, y + gap, w, h, "Walk forward", () -> AfkConfig.walk, v -> AfkConfig.walk = v);
        addToggle(x, y + gap * 2, w, h, "Sprint", () -> AfkConfig.sprint, v -> AfkConfig.sprint = v);
        addToggle(x, y + gap * 3, w, h, "Jump", () -> AfkConfig.jump, v -> AfkConfig.jump = v);
        addToggle(x, y + gap * 4, w, h, "Crouch", () -> AfkConfig.crouch, v -> AfkConfig.crouch = v);
        addToggle(x, y + gap * 5, w, h, "Smooth camera", () -> AfkConfig.smoothLook, v -> AfkConfig.smoothLook = v);
        addToggle(x, y + gap * 6, w, h, "Show HUD text", () -> AfkConfig.showHud, v -> AfkConfig.showHud = v);

        addRenderableWidget(Button.builder(Component.literal("Done"), b -> onClose())
                .bounds(x, y + gap * 7 + 6, w, h).build());
    }

    private void addToggle(int x, int y, int w, int h, String label,
                           BooleanSupplier getter, Consumer<Boolean> setter) {
        Button button = Button.builder(label(label, getter.getAsBoolean()), b -> {
            boolean now = !getter.getAsBoolean();
            setter.accept(now);
            b.setMessage(label(label, now));
        }).bounds(x, y, w, h).build();
        addRenderableWidget(button);
    }

    private static Component label(String name, boolean on) {
        return Component.literal(name + ": " + (on ? "ON" : "OFF"));
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
