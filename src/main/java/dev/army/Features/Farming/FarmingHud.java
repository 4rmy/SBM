package dev.army.Features.Farming;

import dev.army.Core.BlockBreakTracker;
import dev.army.Features.Huds.HudObjective;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class FarmingHud implements HudObjective {
    private static final Minecraft mc = Minecraft.getInstance();

    private ItemStack held = null;
    private boolean isFarmingTool = false;

    @Override
    public void extract(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
        assert mc.player != null;
        if (held == null || held != mc.player.getActiveItem()) {
            isFarmingTool = false;
            held = mc.player.getActiveItem();
            if (held.getTooltipLines(
                    Item.TooltipContext.EMPTY,
                    mc.player,
                    TooltipFlag.NORMAL
            ).getLast().getString().contains("FARMING TOOL")) {
                isFarmingTool = true;
            }
        }

        if (!isFarmingTool) return;

        graphics.text(
                mc.font,
                String.format("%.3f / %.3f",
                        Mth.wrapDegrees(mc.player.getYRot()),
                        Mth.wrapDegrees(mc.player.getXRot())
                ),
                mc.getWindow().getGuiScaledWidth() / 2 + 2,
                mc.getWindow().getGuiScaledHeight() / 2 + 2,
                0xFFFFFF00
        );

        graphics.text(
                mc.font,
                String.format("BPS: %.2f", BlockBreakTracker.getBPS()),
                mc.getWindow().getGuiScaledWidth() / 2 + 2,
                mc.getWindow().getGuiScaledHeight() / 2 + mc.font.lineHeight + 1,
                0xFF4169E2
        );
    }
}
