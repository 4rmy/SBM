package dev.army.Core;

import com.mojang.blaze3d.platform.InputConstants;
import dev.army.SBM;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;

import javax.swing.text.html.parser.Entity;
import java.util.List;

public class KeyMappingController {
    private static final List<Entity> entities = null;
    static final KeyMapping.Category DEBUG_CATEGORY = new KeyMapping.Category(SBM.id("debug-category"));
    static final KeyMapping debug = KeyMappingHelper.registerKeyMapping(
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
            assert mc.player != null;
            LivingEntity player = mc.player.asLivingEntity();
            assert mc.level != null;
            assert player != null;
            System.out.println("Entities ==========");
            mc.level.getEntities(player, player.getBoundingBox().inflate(20))
                    .stream()
                    .filter(entity -> entity.distanceToSqr(player) <= 20 * 20)
                    .filter(entity -> entity instanceof ArmorStand)
                    .forEach(entity -> System.out.println(((ArmorStand) entity).getItemBySlot(EquipmentSlot.HEAD)));
        }
    }
}
