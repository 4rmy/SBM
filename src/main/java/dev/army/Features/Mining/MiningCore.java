package dev.army.Features.Mining;

import dev.army.Core.HudController;

public class MiningCore {
    public static void init() {
        // create Hud
        HudController.registerObjective(new MiningHud());
    }
}
