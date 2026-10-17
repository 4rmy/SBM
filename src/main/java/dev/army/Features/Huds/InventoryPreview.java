package dev.army.Features.Huds;

import dev.army.utils.ItemStackUtils;
import dev.army.utils.Rarity;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.ItemStack;

public class InventoryPreview implements HudObjective {
    private static final int SLOT_SIZE = 16;
    private static final int ITEM_SIZE = SLOT_SIZE - 1;
    private static final float scale = ITEM_SIZE / 16.0f;
    private static final int PADDING = 1;
    private static final Minecraft mc = Minecraft.getInstance();

    @Override
    public void extract(GuiGraphicsExtractor g, DeltaTracker deltaTracker) {
        if (mc.level == null || mc.player == null) return;

        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();

        int width = PADDING + SLOT_SIZE * 9;
        int height = PADDING + SLOT_SIZE * 3;

        // Default vanilla hotbar dimensions
        int hotbarWidth = 182;
        int hotbarHeight = 22;
        int gap = 2;

        int hotbarX = (screenWidth - hotbarWidth) / 2;
        int hotbarY = screenHeight - hotbarHeight;

        // Position preview to the right of the hotbar
        int originX = hotbarX + hotbarWidth + gap;
        int originY = hotbarY + (hotbarHeight - height) - gap;

        // Background
        //g.fill(originX, originY, originX + width, originY + height, 0xaa0d0d0d);

        // Main inventory (3 rows)
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                int x = originX + PADDING * 2 + col * SLOT_SIZE;
                int y = originY + PADDING * 2 + row * SLOT_SIZE;

                g.fill(
                        x, y,
                        x + SLOT_SIZE - PADDING, y + SLOT_SIZE - PADDING,
                        0x77000000
                );

                int slot = 9 + row * 9 + col;
                ItemStack item = mc.player.getInventory().getItem(slot);

                if (item.isEmpty()) continue;

                g.pose().pushMatrix();
                g.pose().translate(x, y);
                g.pose().scale(scale);

                Rarity r = ItemStackUtils.getRarity(item);

                if (r != null) {
                    int borderAlpha = 0xaa000000;
                    int innerAlpha = 0x55000000;

                    // Rarity border
                    g.fill(0, 0, 16, 1, r.color() | borderAlpha);
                    g.fill(0, 15, 16, 16, r.color() | borderAlpha);
                    g.fill(0, 1, 1, 15, r.color() | borderAlpha);
                    g.fill(15, 1, 16, 15, r.color() | borderAlpha);

                    // Rarity background
                    g.fill(1, 1, 15, 15, r.color() | innerAlpha);
                }

                g.item(item, 0, 0);
                g.itemDecorations(mc.font, item, 0, 0);

                g.pose().popMatrix();
            }
        }
    }
}
