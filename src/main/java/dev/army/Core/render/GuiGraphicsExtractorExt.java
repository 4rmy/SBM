package dev.army.Core.render;

import net.minecraft.world.item.ItemStack;

public interface GuiGraphicsExtractorExt {
    void item(ItemStack stack, int x, int y, boolean drawRarity);
}
