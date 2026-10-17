package dev.army.Core;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayDeque;
import java.util.Deque;

public class BlockBreakTracker {
    private static final long WINDOW_MS = 3000L;

    private static final Deque<Long> breakTimes = new ArrayDeque<>();

    private static float blocksPerSecond = 0.0f;

    public static void onBlockBroken(ClientLevel clientLevel, LocalPlayer localPlayer, BlockPos blockPos, BlockState blockState) {
        long now = System.nanoTime();

        breakTimes.addLast(now);
        update(now);
    }

    public static float getBPS() {
        update(System.nanoTime());
        return blocksPerSecond;
    }

    private static void update(long now) {
        long windowNanos = WINDOW_MS * 1_000_000L;

        while (!breakTimes.isEmpty()
                && now - breakTimes.peekFirst() >= windowNanos) {
            breakTimes.removeFirst();
        }

        blocksPerSecond = breakTimes.size()
                * (1_000_000_000.0f / windowNanos);
    }
}
