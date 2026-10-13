package dev.army.mixin;

import dev.army.utils.ItemStackUtils;
import dev.army.utils.Rarity;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Hud.class)
public class HudMixin {

    @Inject(
            method = "extractSlot",
            at = @At("HEAD")
    )
    private void smb$onHotbarSlot(
            GuiGraphicsExtractor graphics,
            int x,
            int y,
            DeltaTracker deltaTracker,
            Player player,
            ItemStack itemStack,
            int seed,
            CallbackInfo ci) {
        if (itemStack.isEmpty()) {
            return;
        }

        Rarity r = ItemStackUtils.getRarity(itemStack);
        if (r != null) {
            // top
            int border_alpha = 0xaa000000;
            graphics.fill(x, y, x + 16, y + 1, r.color() | border_alpha);
            // bottom
            graphics.fill(x, y + 15, x + 16, y + 16, r.color() | border_alpha);
            // left
            graphics.fill(x, y + 1, x + 1, y + 15, r.color() | border_alpha);
            // right
            graphics.fill(x + 15, y + 1, x + 16, y + 15, r.color() | border_alpha);
            int inner_alpha = 0x55000000;
            graphics.fill(x + 1, y + 1, x + 15, y + 15, r.color() | inner_alpha);
        }
    }
}
