package dev.army.Mixins;

import dev.army.Core.render.GuiGraphicsExtractorExt;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static dev.army.SBM.mc;

@Mixin(GuiGraphicsExtractor.class)
public abstract class GuiGraphicsExtractorMixin implements GuiGraphicsExtractorExt {
    @Unique
    private static final Pattern PESTS_PATTERN = Pattern.compile("Vacuum Bag: (\\d+) Pests?");

    @Unique
    private static @Nullable String getVacuumCount(ItemStack stack) {
        for (Component line : stack.getTooltipLines(
                Item.TooltipContext.EMPTY,
                mc.player,
                TooltipFlag.NORMAL
        )) {
            Matcher matcher = PESTS_PATTERN.matcher(line.getString());

            if (matcher.find()) {
                return matcher.group(1);
            }
        }

        return null;
    }

    @Shadow
    public abstract void fill(int x0, int y0, int x1, int y1, int col);

    @ModifyVariable(
            method = "itemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
            at = @At("HEAD"),
            argsOnly = true,
            name = "countText")
    private String modifyCountText(
            String countText,
            Font font,
            ItemStack itemStack,
            int x,
            int y
    ) {
        if (itemStack.getDisplayName().getString().contains("Vacuum")) {
            String c = getVacuumCount(itemStack);
            if (c != null) return c;
            return "";
        }

        return countText;
    }
}
