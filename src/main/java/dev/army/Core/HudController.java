package dev.army.Core;

import dev.army.Features.Huds.HudObjective;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;

import java.util.ArrayList;
import java.util.List;

public class HudController {
    private static final List<HudObjective> hudObjectiveList = new ArrayList<>();

    public static void registerObjective(HudObjective objective) {
        hudObjectiveList.add(objective);
    }

    public static void extractAll(GuiGraphicsExtractor guiGraphicsExtractor, DeltaTracker deltaTracker) {
        for (HudObjective objective : hudObjectiveList)
            objective.extract(guiGraphicsExtractor, deltaTracker);
    }
}
