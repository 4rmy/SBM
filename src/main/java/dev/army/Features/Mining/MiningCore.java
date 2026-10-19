package dev.army.Features.Mining;

import dev.army.Core.HudController;
import dev.army.Features.FeatureCore;
import dev.army.SBM;

public class MiningCore implements FeatureCore {
    public void init() {
        // create Hud
        HudController.registerObjective(new MiningHud());
    }
}
