package dev.army.Features.Huds;

import dev.army.Core.HudController;
import dev.army.Core.TimingController;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public class StatsHud implements HudObjective {
    private static final Minecraft mc = Minecraft.getInstance();

    @Override
    public void extract(GuiGraphicsExtractor g, DeltaTracker deltaTracker) {
        g.text(mc.font, "FPS:", 1, 1, 0xFF4169E1);
        g.text(mc.font, String.valueOf(mc.getFps()), 1 + mc.font.width("FPS: "), 1, -1);
        g.text(mc.font, "TPS:", 1, 1 + mc.font.lineHeight, 0xFF4169E2);
        g.text(mc.font, String.valueOf(Math.round(TimingController.getTPS())), 1 + mc.font.width("TPS: "), 1 + mc.font.lineHeight, -1);
    }
}
