package dev.army;

import dev.army.Core.CommandController;
import dev.army.Core.HudController;
import dev.army.Core.KeyMappingController;
import dev.army.Farming.FarmingCore;
import dev.army.Huds.InventoryPreview;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SkyblockMod implements ModInitializer {
    public static final String MOD_ID = "skyblockmod";
    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Minecraft mc = Minecraft.getInstance();
    public static ItemStack s1 = null;
    public static ItemStack s2 = null;
    public static ItemStack s3 = null;
    public static ItemStack s4 = null;
    public static Slot hoveredSlot;

    private static final boolean wasF2Down = false;

    private static void extract(GuiGraphicsExtractor guiGraphicsExtractor, DeltaTracker deltaTracker) {
        // TODO: AUTO FISHING MOD
		/*
		if (mc.level == null) return;
		if (mc.player == null) return;
		LocalPlayer lp = mc.player;
		if (lp.fishing == null) return;
		FishingHook bobber = lp.fishing;

		int y = 2;
		for (Entity e : mc.level.getEntitiesOfClass(
				Entity.class,
				new AABB(bobber.position().subtract(5,1,5), bobber.position().add(5,1,5))
		)) {
			String name = e.getDisplayName().getString();
			if (!name.contains("?") && !name.contains("!")) continue;
			guiGraphicsExtractor.text(
					mc.font,
					e.getClass().getName() + ": " + e.getDisplayName().getString(),
					2,
					y,
					0xFFFFFFFF
			);
			y += mc.font.lineHeight+2;
		}
		*/
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    @Override
    public void onInitialize() {
        LOGGER.info("SETTING UP");

        // attach to hud rendering
        HudElementRegistry.attachElementBefore(
                VanillaHudElements.CHAT,
                SkyblockMod.id("before_chat"),
                HudController::extractAll
        );
        HudController.registerObjective(new InventoryPreview());

        // client side commands
        CommandController.init();

        // Key Mapping Controller init
        KeyMappingController.init();

        // Farming Core init
        FarmingCore.init();

        LOGGER.info("FINISHED SETTING UP");
    }
}

// quick debug mapping
/*
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
boolean isF2Down = GLFW.glfwGetKey(
        mc.getWindow().handle(),
        GLFW.GLFW_KEY_F2
) == GLFW.GLFW_PRESS;
            if (isF2Down && !wasF2Down) {
        // Print item nbt data
        // should prob make this copy the item
        // to a testing world
                if (!(mc.gui.screen() instanceof AbstractContainerScreen<?> screen)) {
                    return;
                }

                Slot slot = hoveredSlot;

                if (slot == null || !slot.hasItem()) {
                    return;
                }

                ItemStack stack = slot.getItem();

                System.out.println("=== ITEM ===");
                System.out.println(stack.getDisplayName().getString());

                // Components (26.2)
                System.out.println(stack.getComponents());

                // Full stack
                System.out.println(stack);
        }
        wasF2Down = isF2Down;
    });
*/