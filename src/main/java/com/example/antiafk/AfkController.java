package com.example.antiafk;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

import java.util.Random;

/** Runs every client tick and simulates input + smooth camera movement. */
public final class AfkController {
    private static final Random RNG = new Random();

    private static int jumpCooldown = 60;
    private static int jumpHold = 0;
    private static int crouchCooldown = 300;
    private static int crouchHold = 0;
    private static int lookCooldown = 40;
    private static float remainingYaw = 0f;
    private static float remainingPitch = 0f;
    private static int hudTimer = 0;

    private AfkController() {}

    public static void tick(Minecraft mc) {
        LocalPlayer player = mc.player;
        if (player == null) {
            return;
        }
        if (!AfkConfig.enabled || mc.gui.screen() != null) {
            releaseKeys(mc);
            return;
        }

        // --- walk / sprint ---
        mc.options.keyUp.setDown(AfkConfig.walk);

        // --- crouch (short random bursts) ---
        boolean crouching = false;
        if (AfkConfig.crouch) {
            if (crouchHold > 0) {
                crouchHold--;
                crouching = true;
            } else if (--crouchCooldown <= 0) {
                crouchHold = 10 + RNG.nextInt(20);
                crouchCooldown = 200 + RNG.nextInt(300);
            }
        }
        mc.options.keyShift.setDown(crouching);
        mc.options.keySprint.setDown(AfkConfig.sprint && AfkConfig.walk && !crouching);

        // --- jump (short random taps) ---
        boolean jumping = false;
        if (AfkConfig.jump) {
            if (jumpHold > 0) {
                jumpHold--;
                jumping = true;
            } else if (--jumpCooldown <= 0) {
                jumpHold = 3;
                jumpCooldown = 40 + RNG.nextInt(80);
            }
        }
        mc.options.keyJump.setDown(jumping);

        // --- smooth camera movement ---
        if (AfkConfig.smoothLook) {
            if (--lookCooldown <= 0) {
                remainingYaw += (RNG.nextFloat() - 0.5f) * 120f;     // +-60 degrees
                float targetPitch = (RNG.nextFloat() - 0.4f) * 35f;  // slight look up/down
                remainingPitch += targetPitch - player.getXRot();
                lookCooldown = 60 + RNG.nextInt(100);
            }
            float stepYaw = remainingYaw * 0.08f;
            float stepPitch = remainingPitch * 0.08f;
            remainingYaw -= stepYaw;
            remainingPitch -= stepPitch;
            player.setYRot(player.getYRot() + stepYaw);
            player.setXRot(Mth.clamp(player.getXRot() + stepPitch, -60f, 60f));
        }

        // --- HUD (action bar text) ---
        if (AfkConfig.showHud && ++hudTimer >= 10) {
            hudTimer = 0;
            mc.gui.hud.setOverlayMessage(Component.literal("Anti-AFK: ON  [Z = settings]"), false);
        }
    }

    public static void releaseKeys(Minecraft mc) {
        if (mc.options == null) {
            return;
        }
        mc.options.keyUp.setDown(false);
        mc.options.keyShift.setDown(false);
        mc.options.keySprint.setDown(false);
        mc.options.keyJump.setDown(false);
        jumpHold = 0;
        crouchHold = 0;
        remainingYaw = 0f;
        remainingPitch = 0f;
    }
}
