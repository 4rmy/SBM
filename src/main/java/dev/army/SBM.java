package dev.army;

import dev.army.Core.*;
import dev.army.Features.Farming.FarmingCore;
import dev.army.Features.Huds.InventoryPreview;
import dev.army.Features.Huds.StatsHud;
import dev.army.Features.Mining.MiningCore;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.fabricmc.fabric.api.event.client.player.ClientPlayerBlockBreakEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SBM implements ModInitializer {
    public static final String MOD_ID = "skyblockmod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Minecraft mc = Minecraft.getInstance();

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    @Override
    public void onInitialize() {
        LOGGER.info("SETTING UP");

        // attach timing controller
        ClientTickEvents.START_LEVEL_TICK.register(TimingController::onTick);
        ClientPlayerBlockBreakEvents.AFTER.register(BlockBreakTracker::onBlockBroken);

        // attach to hud rendering
        HudElementRegistry.attachElementBefore(
                VanillaHudElements.CHAT,
                SBM.id("before_chat"),
                HudController::extractAll
        );
        HudController.registerObjective(new InventoryPreview());
        HudController.registerObjective(new StatsHud());

        // client side commands
        CommandController.init();

        // Key Mapping Controller init
        KeyMappingController.init();

        // Farming Core init
        FarmingCore.init();
        MiningCore.init();

        LOGGER.info("FINISHED SETTING UP");
    }
}
