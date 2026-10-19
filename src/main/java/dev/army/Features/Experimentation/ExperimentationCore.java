package dev.army.Features.Experimentation;

import dev.army.Features.ChocolateFactory.ChocoFactoryController;
import dev.army.Features.FeatureCore;
import dev.army.SBM;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class ExperimentationCore implements FeatureCore {
    private static final Minecraft mc = Minecraft.getInstance();

    public static void extractSlot(GuiGraphicsExtractor graphics, Slot slot) {
        assert mc.gui.screen() != null;
        if (!(mc.gui.screen() instanceof ContainerScreen screen)) return;
        if (!mc.gui.screen().getTitle().getString().contains("Chronomatron") ||
                mc.gui.screen().getTitle().getString().contains("Stakes")) return;

        // state of the game
        Slot s49 = screen.getMenu().getSlot(49);
        if (!s49.hasItem()) return;
        if (s49.getItem().is(Items.GLOWSTONE)) {
            graphics.text(mc.font, "learning...", 0, 0, 0xFFFFFFFF);
        }
        if (s49.getItem().is(Items.CLOCK)) {
            graphics.text(mc.font, "Playing.", 0, 0, 0xFFFFFFFF);
        }

        graphics.text(
                mc.font,
                String.valueOf(slot.index),
                slot.x,
                slot.y,
                0xFFFFFFFF
        );
    }

    public static void preProcess(GuiGraphicsExtractor graphics, CallbackInfo ci) {
        assert mc.gui.screen() != null;
        if (!(mc.gui.screen() instanceof ContainerScreen screen)) return;
        if (!mc.gui.screen().getTitle().getString().contains("Chronomatron") ||
                mc.gui.screen().getTitle().getString().contains("Stakes")) return;

        // round
        Slot s4 = screen.getMenu().getSlot(4);
        String[] name = s4.getItem().getDisplayName().getString().strip().split("\\s");
        String num = name[name.length - 1];
        int round = Integer.parseInt(num.substring(0, num.length() - 1));

        if (round == 13) {
            mc.gui.screen().onClose();
            ci.cancel();
        }
    }

    @Override
    public void init() {}
}
