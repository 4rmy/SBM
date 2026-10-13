package dev.army.mixin;

import dev.army.ChocolateFactory.ChocoFactoryController;
import dev.army.SkyblockMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(Screen.class)
public abstract class ScreenMixin {
    @Shadow
    public abstract Component getTitle();

    @Inject(
            method = "extractRenderState",
            at = @At("TAIL")
    )
    private void sbm$ScreenExtractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a, CallbackInfo ci) {
        if (!(Minecraft.getInstance().gui.screen() instanceof ContainerScreen)) return;
        if (this.getTitle().getString().strip().equalsIgnoreCase("chocolate factory")) {
            ChocoFactoryController.extractRenderState((ContainerScreen) Minecraft.getInstance().gui.screen(), graphics, mouseX, mouseY, a);

        }
    }

    @Inject(
            method = "keyPressed",
            at = @At("HEAD")
    )
    private void sbm$KeyPress(KeyEvent event, CallbackInfoReturnable<Boolean> cir) {
        if (event.key() == GLFW.GLFW_KEY_F2) {
            System.out.println(Minecraft.getInstance().gui.screen());
        }
    }
}
