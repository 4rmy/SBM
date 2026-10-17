package dev.army.Mixins;

import dev.army.Features.Experimentation.ExperimentationCore;
import dev.army.Features.Misc.CollectionsNumbers;
import dev.army.utils.ItemStackUtils;
import dev.army.utils.Rarity;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin {

    @Shadow
    @Final
    protected Component playerInventoryTitle;

    @Shadow
    protected abstract boolean isHovering(Slot slot, double xm, double ym);

    @Inject(method = "extractSlot(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/world/inventory/Slot;II)V", at = @At("HEAD"))
    private void smb$onExtractSlot(GuiGraphicsExtractor graphics, Slot slot, int mouseX, int mouseY, CallbackInfo ci) {
        // handle all rarity highlighting
        ItemStack stack = slot.getItem();
        if (!stack.isEmpty())
            sum$highlightStack(graphics, slot, stack);
    }

    @Unique
    private void sum$highlightStack(GuiGraphicsExtractor graphics, Slot slot, ItemStack stack) {
        Rarity r = ItemStackUtils.getRarity(stack);
        if (r != null) {
            // top
            int border_alpha = 0xaa000000;
            graphics.fill(slot.x, slot.y, slot.x + 16, slot.y + 1, r.color() | border_alpha);
            // bottom
            graphics.fill(slot.x, slot.y + 15, slot.x + 16, slot.y + 16, r.color() | border_alpha);
            // left
            graphics.fill(slot.x, slot.y + 1, slot.x + 1, slot.y + 15, r.color() | border_alpha);
            // right
            graphics.fill(slot.x + 15, slot.y + 1, slot.x + 16, slot.y + 15, r.color() | border_alpha);
            int inner_alpha = 0x55000000;
            graphics.fill(slot.x + 1, slot.y + 1, slot.x + 15, slot.y + 15, r.color() | inner_alpha);
        }
    }

    @Inject(method = "extractRenderState(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIF)V", at = @At("HEAD"), cancellable = true)
    private void sbm$onExtractState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a, CallbackInfo ci) {
        ExperimentationCore.preProcess(graphics, ci);
    }

    @Inject(method = "extractSlot(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/world/inventory/Slot;II)V", at = @At("TAIL"))
    private void sbm$onExtractSlotTail(GuiGraphicsExtractor graphics, Slot slot, int mouseX, int mouseY, CallbackInfo ci) {
        CollectionsNumbers.extractSlot(graphics, slot);
        ExperimentationCore.extractSlot(graphics, slot);
    }


    @Inject(method = "extractFloatingItem", at = @At("HEAD"))
    private void smb$onExtractFloatingItem(GuiGraphicsExtractor graphics, ItemStack stack, int x, int y, String text, CallbackInfo ci) {
        if (stack.isEmpty()) {
            return;
        }

        Rarity r = ItemStackUtils.getRarity(stack);
        if (r != null) {
            graphics.fill(x, y, x + 16, y + 16, r.color());
        }
    }
}