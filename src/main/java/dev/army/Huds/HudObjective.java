package dev.army.Huds;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public interface HudObjective {
    void extract(GuiGraphicsExtractor guiGraphicsExtractor, DeltaTracker deltaTracker);
}
