package dev.army.Core;

import net.minecraft.client.multiplayer.ClientLevel;

public class TimingController {
    private static final float SMOOTHING = 0.1f;
    private static long lastTick = 0;
    private static float TPS = 20.0f;

    public static float getTPS() {
        return TPS;
    }

    public static void onTick(ClientLevel clientLevel) {
        long currentTime = System.currentTimeMillis();

        if (lastTick == 0) {
            lastTick = currentTime;
            return;
        }

        long elapsed = currentTime - lastTick;
        lastTick = currentTime;

        if (elapsed <= 0) return;

        float instantTPS = Math.min(20.0f, 1000.0f / elapsed);

        TPS += SMOOTHING * (instantTPS - TPS);
    }
}
