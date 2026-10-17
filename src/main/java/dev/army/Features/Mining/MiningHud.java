package dev.army.Features.Mining;

import dev.army.Core.TablistParser;
import dev.army.Features.Huds.HudObjective;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public class MiningHud implements HudObjective {
    public MiningHud() {
        if (TablistParser.getArea().equals("dwarven mines")) {}
    }

    @Override
    public void extract(GuiGraphicsExtractor guiGraphicsExtractor, DeltaTracker deltaTracker) {

    }
}
