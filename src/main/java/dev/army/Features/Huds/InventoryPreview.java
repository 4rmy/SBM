package dev.army.Features.Huds;

import dev.army.Core.render.GuiGraphicsExtractorExt;
import dev.army.utils.ItemStackUtils;
import dev.army.utils.Rarity;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.ItemStack;

public class InventoryPreview implements HudObjective {
    private static final int SLOT_SIZE = 16;
    private static final int ITEM_SIZE = SLOT_SIZE - 2;
    private static final float scale = ITEM_SIZE / 16.0f;
    private static final int PADDING = 1;
    private static final Minecraft mc = Minecraft.getInstance();

    @Override
    public void extract(GuiGraphicsExtractor g, DeltaTracker deltaTracker) {
        GuiGraphicsExtractorExt gu = (GuiGraphicsExtractorExt) g;
        if (mc.level == null) return;
        if (mc.player == null) return;

        int width = PADDING * 2 + SLOT_SIZE * 9;
        int height = PADDING * 2 + SLOT_SIZE * 3;

        // Background
        g.fill(0, 0, width, height, 0xaa0d0d0d); //argb

        // Main inventory (3 rows)
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                int x = PADDING + col * SLOT_SIZE;
                int y = PADDING + row * SLOT_SIZE;
                g.fill(x, y, x + SLOT_SIZE - 1, y + SLOT_SIZE - 1, 0xaa333333);
                int slot = 9 + row * 9 + col;
                ItemStack item = mc.player.getInventory().getItem(slot);
                if (item.isEmpty()) continue;

                float offset = (SLOT_SIZE - ITEM_SIZE) / 2f;
                g.pose().pushMatrix();
                g.pose().translate(x + offset, y + offset);
                g.pose().scale(scale);
                Rarity r = ItemStackUtils.getRarity(item);
                if (r != null) {
                    // top
                    int border_alpha = 0xaa000000;
                    g.fill(0, 0, 16, 1, r.color() | border_alpha);
                    // bottom
                    g.fill(0, 15, 16, 16, r.color() | border_alpha);
                    // left
                    g.fill(0, 1, 1, 15, r.color() | border_alpha);
                    // right
                    g.fill(15, 1, 16, 15, r.color() | border_alpha);
                    int inner_alpha = 0x55000000;
                    g.fill(1, 1, 15, 15, r.color() | inner_alpha);
                }
                g.item(item, 0, 0);
                g.itemDecorations(mc.font, item, 0, 0);
                g.pose().popMatrix();
            }
        }
    }
}
