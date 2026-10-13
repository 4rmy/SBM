package dev.army.Core;

import com.mojang.blaze3d.platform.InputConstants;
import dev.army.SkyblockMod;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;

import java.util.HashMap;

public class KeyMappingController {
    static KeyMapping.Category DEBUG_CATEGORY = new KeyMapping.Category(SkyblockMod.id("debug-category"));

    static KeyMapping debug = KeyMappingHelper.registerKeyMapping(
            new KeyMapping(
                    "debug",
                    InputConstants.Type.KEYSYM,
                    InputConstants.KEY_F2,
                    KeyMappingController.DEBUG_CATEGORY
            )
    );

    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(KeyMappingController::processMappings);
    }

    private static void processMappings(Minecraft mc) {
        while (debug.consumeClick()) {
            // debug contents
        }
    }
}
