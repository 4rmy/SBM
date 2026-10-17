package dev.army.utils;

import dev.army.Mixins.PlayerTabOverlayAccessor;
import net.minecraft.client.Minecraft;

public class MiscUtils {
    private static final Minecraft mc = Minecraft.getInstance();

    public static boolean isOnHypixel() {
        if (mc.getCurrentServer() == null) return false;
        return ((PlayerTabOverlayAccessor) mc.gui.hud.getTabList())
                .getHeader()
                .getString()
                .toLowerCase()
                .contains("hypixel.net");
    }
}
