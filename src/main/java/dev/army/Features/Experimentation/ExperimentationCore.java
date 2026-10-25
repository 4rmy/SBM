package dev.army.Features.Experimentation;

import dev.army.Features.FeatureCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.Random;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExperimentationCore implements FeatureCore {
    private static final Minecraft mc = Minecraft.getInstance();

    private static final int ROUND_IDX = 4;
    private static final int STATE_IDX = 49;
    private static final int MIN_SLOT = 9;
    private static final int MAX_SLOT = 44;
    private static final int MAX_ROUND = 9;
    private static final int MAX_ROUND_META = 12;

    private static final Random random = new Random();
    private static long nextClickAt = -1L;

    private static final Set<Integer> frameSteps = new LinkedHashSet<>();
    private static final List<Integer> sequence = new LinkedList<>();
    private static boolean foundActive = false;
    private static boolean isTracking = false;
    private static boolean shouldAdd = true;

    @Nullable
    private static ContainerScreen isChronomatron() {
        if (!(mc.gui.screen() instanceof ContainerScreen screen)) {
            return null;
        }

        String title = screen.getTitle().getString();

        return title.contains("Chronomatron") && !title.contains("Stakes") ? screen : null;
    }
    private static boolean isActive(Slot s) {
        if (!s.hasItem()) return false;
        return s.getItem().getItemName()
                .toString()
                .contains("terracotta");
    }

    private static boolean shouldTrack(ContainerScreen screen) {
        return screen.getMenu()
                .getSlot(STATE_IDX)
                .getItem()
                .getItemName()
                .toString()
                .contains("glowstone");
    }
    private static int getRound(ContainerScreen screen) {
        String name = screen.getMenu()
                .getSlot(ROUND_IDX)
                .getItem()
                .getHoverName()
                .getString();

        Matcher matcher = Pattern.compile("Round: (\\d+)").matcher(name);

        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }

        return -1; // Round number not found
    }

    public static void preProcess(GuiGraphicsExtractor graphics, CallbackInfo ci) {
        ContainerScreen screen = isChronomatron();
        if (screen == null) return;

        foundActive = false;
        frameSteps.clear();
    }

    private static int normalizeSlot(int slotIndex) {
        return MIN_SLOT + (slotIndex - MIN_SLOT) % 9;
    }
    public static void extractSlot(GuiGraphicsExtractor graphics, Slot slot) {
        if (isChronomatron() == null) return;
        if (slot.index < MIN_SLOT || slot.index > MAX_SLOT) {
            return;
        }

        if (isActive(slot)) {
            foundActive = true;

            int step = normalizeSlot(slot.index);
            frameSteps.add(step);
        }
    }

    public static void postProcess(GuiGraphicsExtractor graphics, CallbackInfo ci) {
        ContainerScreen screen = isChronomatron();

        if (screen == null) {
            isTracking = false;
            sequence.clear();
            frameSteps.clear();
            nextClickAt = -1L;
            shouldAdd = true;
            return;
        }

        int max = (screen.getMenu()
                .getSlot(STATE_IDX)
                .getItem()
                .getItemName()
                .toString()
                .contains("Meta")) ? MAX_ROUND_META : MAX_ROUND;
        if (getRound(screen) > max) {
            assert mc.player != null;
            mc.player.closeContainer();
            return;
        }

        boolean trackingState = shouldTrack(screen);

        // Record each highlighted step once.
        if (isTracking && foundActive && shouldAdd) {
            sequence.addAll(frameSteps);
            shouldAdd = false;
        }

        if (!foundActive && !shouldAdd) {
            shouldAdd = true;
        }

        // Stop recording when the game enters playback mode.
        if (!isTracking) {
            if (trackingState && !foundActive) {
                sequence.clear();
                isTracking = true;
                nextClickAt = -1L;
            }
        } else if (!trackingState) {
            isTracking = false;
        }

        // Play the sequence when recording has ended.
        if (!trackingState && !isTracking && !foundActive && !sequence.isEmpty()) {
            long now = System.currentTimeMillis();

            // Schedule a randomized delay for the next click.
            if (nextClickAt == -1L) {
                nextClickAt = now + 200 + random.nextInt(126);
            }

            // Click once the delay has elapsed.
            if (now >= nextClickAt) {
                int slotIndex = sequence.getFirst() + 9;

                assert mc.gameMode != null;
                assert mc.player != null;
                mc.gameMode.handleContainerInput(
                        screen.getMenu().containerId,
                        slotIndex,
                        1,
                        ContainerInput.CLONE,
                        mc.player
                );

                sequence.removeFirst();
                nextClickAt = -1L;
            }
        } else {
            // Wait for a clear frame before scheduling another click.
            nextClickAt = -1L;
        }
    }

    public static void onClose() {
        sequence.clear();
        frameSteps.clear();
        isTracking = false;
    }

    @Override
    public void init() {
    }
}
