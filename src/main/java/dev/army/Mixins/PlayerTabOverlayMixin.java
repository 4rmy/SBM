package dev.army.Mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.army.utils.MiscUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;
import org.jspecify.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;


@Mixin(PlayerTabOverlay.class)
public class PlayerTabOverlayMixin {
    @Unique
    private static final Minecraft mc = Minecraft.getInstance();

    @Shadow
    private @Nullable Component footer;
    @Unique
    @Nullable
    private Component sbm$originalFooter;

    // hide the Header text
    @ModifyExpressionValue(
            method = "extractRenderState",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/gui/components/PlayerTabOverlay;header:Lnet/minecraft/network/chat/Component;",
                    opcode = Opcodes.GETFIELD,
                    ordinal = 0
            )
    )
    private Component sbm$hideHeader(Component original) {
        if (!MiscUtils.isOnHypixel()) return original;
        return null;
    }

    // Compact the footer text
    @Inject(method = "extractRenderState", at = @At("HEAD"))
    private void sbm$modifyFooter(GuiGraphicsExtractor graphics, int screenWidth, Scoreboard scoreboard, Objective displayObjective, CallbackInfo ci) {
        if (!MiscUtils.isOnHypixel()) return;

        sbm$originalFooter = this.footer;

        if (this.footer == null) {
            return;
        }

        List<Component> lines = sbm$splitStyledLines(this.footer);

        if (lines.isEmpty()
                || !lines.getLast().getString().contains("STORE.HYPIXEL.NET")) {
            sbm$originalFooter = null;
            return;
        }

        List<Component> modified = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            Component line = lines.get(i);
            String text = line.getString();

            if (text.contains("No effects active")) {
                modified.add(Component.literal("No effects active")
                        .withStyle(line.getStyle().withColor(TextColor.DARK_GRAY)));
                i++;
            } else if (text.contains("Not active!")) {
                modified.add(Component.literal("Not active!")
                        .withStyle(line.getStyle().withColor(TextColor.DARK_GRAY)));
                i++;
            } else {
                modified.add(line);
            }
        }

        for (int i = 0; i < 2 && !modified.isEmpty(); i++) {
            modified.removeLast();
        }

        this.footer = sbm$joinStyledLines(modified);

    }

    @Inject(method = "extractRenderState", at = @At("RETURN"))
    private void sbm$restoreFooter(GuiGraphicsExtractor graphics, int screenWidth, Scoreboard scoreboard, Objective displayObjective, CallbackInfo ci) {
        if (!MiscUtils.isOnHypixel()) return;
        footer = sbm$originalFooter;
        sbm$originalFooter = null;
    }

    @Unique
    private List<Component> sbm$splitStyledLines(Component component) {
        List<Component> lines = new ArrayList<>();
        lines.add(Component.empty());

        component.visit((style, text) -> {
            String[] parts = text.split("\n", -1);

            for (int i = 0; i < parts.length; i++) {
                if (!parts[i].isEmpty()) {
                    lines.set(
                            lines.size() - 1,
                            lines.getLast().copy().append(
                                    Component.literal(parts[i]).setStyle(style)
                            )
                    );
                }

                if (i < parts.length - 1) {
                    lines.add(Component.empty());
                }
            }

            return java.util.Optional.empty();
        }, Style.EMPTY);

        return lines;
    }

    @Unique
    private Component sbm$joinStyledLines(List<Component> lines) {
        MutableComponent result = Component.empty();

        for (int i = 0; i < lines.size(); i++) {
            if (i > 0) {
                result.append(Component.literal("\n"));
            }

            result.append(lines.get(i));
        }

        return result;
    }

    // Hide ping icon
    @Inject(method = "extractPingIcon(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIILnet/minecraft/client/multiplayer/PlayerInfo;)V", at = @At("HEAD"), cancellable = true)
    private void sbm$extractPingIcon(GuiGraphicsExtractor graphics, int slotWidth, int xo, int yo, PlayerInfo info, CallbackInfo ci) {
        if (MiscUtils.isOnHypixel()) ci.cancel();
    }
}
