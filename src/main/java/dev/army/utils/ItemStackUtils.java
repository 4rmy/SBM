package dev.army.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemStackUtils {
    public static @Nullable Rarity getRarity(ItemStack stack) {
        Minecraft mc = Minecraft.getInstance();

        List<Component> tooltip = stack.getTooltipLines(
                Item.TooltipContext.EMPTY,
                mc.player,
                TooltipFlag.NORMAL
        );

        if (tooltip.isEmpty()) {
            return null;
        }

        for (Component c : tooltip) {
            for (Rarity rarity : Rarity.values()) {
                if (c.getString().startsWith(rarity.name().toUpperCase())) {
                    return rarity;
                }
            }
        }

        return null;
    }
}
