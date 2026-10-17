package dev.army.Features.Misc;

import dev.army.utils.RomanNumerals;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;


public class CollectionsNumbers {
    private static final Minecraft mc = Minecraft.getInstance();

    public static void extractSlot(GuiGraphicsExtractor graphics, Slot slot) {
        // in a collections screen
        assert mc.gui.screen() != null;
        if (!mc.gui.screen().getTitle().getString().strip().contains(" Collections"))
            return;

        // in the use-able range
        if (slot.index < 10 || slot.index > 43 || slot.index % 9 == 0 || (slot.index + 1) % 9 == 0)
            return;

        // no collection
        if (!slot.hasItem())
            return;

        // collection not found
        if (slot.getItem().getItem() == Items.DYE.gray())
            return;

        int tier = -1;

        if (!firstTier(slot)) {
            String[] name = slot.getItem().getDisplayName().getString().strip().split("\\s");
            String numerals = name[name.length-1];
            tier = RomanNumerals.convert(numerals);
        }

        String text = String.valueOf(tier+1);

        graphics.text(
                mc.font,
                text,
                slot.x+17-mc.font.width(text),
                slot.y+18-mc.font.lineHeight,
                (isMaxed(slot)) ? 0xFF00FF00 : ((tier == -1) ? 0xFF555555 : 0xFFFFFF00)
        );
    }

    private static boolean isMaxed(Slot slot) {
        List<Component> tooltip = slot.getItem()
                .getTooltipLines(Item.TooltipContext.EMPTY, mc.player, TooltipFlag.NORMAL);
        for (Component line : tooltip)
            if (line.getString().contains("MAXED"))
                return true;
        return false;
    }

    private static boolean firstTier(Slot slot) {
        List<Component> tooltip = slot.getItem()
                .getTooltipLines(Item.TooltipContext.EMPTY, mc.player, TooltipFlag.NORMAL);
        for (Component line : tooltip)
            if (line.getString().contains("Progress to"))
                return line.getString().contains("I:");
        return false;
    }
}
