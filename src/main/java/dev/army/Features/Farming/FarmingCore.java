package dev.army.Features.Farming;

import com.mojang.blaze3d.platform.InputConstants;
import dev.army.Core.HudController;
import dev.army.Core.TablistParser;
import dev.army.Features.ChocolateFactory.ChocoFactoryController;
import dev.army.Features.FeatureCore;
import dev.army.SBM;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelExtractionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.minecraft.client.KeyMapping;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;

public class FarmingCore implements FeatureCore {
    public static final HashMap<Integer, Cord> grid = new HashMap<>() {{
        put(21, new Cord(0, 0)); // top left plot
        put(13, new Cord(1, 0));
        put(9, new Cord(2, 0));
        put(14, new Cord(3, 0));
        put(22, new Cord(4, 0));

        put(15, new Cord(0, 1));
        put(5, new Cord(1, 1));
        put(1, new Cord(2, 1));
        put(6, new Cord(3, 1));
        put(16, new Cord(4, 1));

        put(10, new Cord(0, 2));
        put(2, new Cord(1, 2));
        put(0, new Cord(2, 2)); // barn
        put(3, new Cord(3, 2));
        put(11, new Cord(4, 2));

        put(17, new Cord(0, 3));
        put(7, new Cord(1, 3));
        put(4, new Cord(2, 3));
        put(8, new Cord(3, 3));
        put(18, new Cord(4, 3));

        put(23, new Cord(0, 4));
        put(19, new Cord(1, 4));
        put(12, new Cord(2, 4));
        put(20, new Cord(3, 4));
        put(24, new Cord(4, 4)); // bottom right plot
    }};
    public static FarmingCore instance;

    private final KeyMapping.Category FARMING_CAT = KeyMapping.Category.register(
            SBM.id("farming_category")
    );
    private final KeyMapping setspawn = KeyMappingHelper.registerKeyMapping(
            new KeyMapping(
                    "key.skyblockmod.setspawn",
                    InputConstants.Type.MOUSE,
                    InputConstants.MOUSE_BUTTON_5,
                    this.FARMING_CAT
            )
    );
    private final KeyMapping warpgarden = KeyMappingHelper.registerKeyMapping(
            new KeyMapping(
                    "key.skyblockmod.warpgarden",
                    InputConstants.Type.MOUSE,
                    InputConstants.MOUSE_BUTTON_4,
                    this.FARMING_CAT
            )
    );

    public FarmingCore() {
        // rendering
        FarmingRenderer renderer = new FarmingRenderer();

        LevelExtractionEvents.END_EXTRACTION.register(renderer::plotBorderExtraction);
        LevelRenderEvents.BEFORE_GIZMOS.register(renderer::plotBorderRender);

        LevelExtractionEvents.END_EXTRACTION.register(renderer::pestsOutlineExtraction);
        LevelRenderEvents.AFTER_TRANSLUCENT_TERRAIN.register(renderer::pestsOutlineRender);

        // keybinds
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (this.setspawn.consumeClick()) {
                if (client.player == null) return;
                if (!TablistParser.getArea().equals("garden")) return;
                client.player.connection.sendCommand("setspawn");
            }
            while (this.warpgarden.consumeClick()) {
                if (client.player == null) return;
                if (!TablistParser.getArea().equals("garden")) return;
                client.player.connection.sendCommand("warp garden");
            }
        });
    }

    @Override
    public void init() {
        instance = this;
        HudController.registerObjective(new FarmingHud());
    }

    public static PosRange cordToPos(Cord c) {
        int size = 96;

        int dx = c.x - 2;
        int dz = c.y - 2;

        double minX = -48 + dx * size;
        double maxX = 48 + dx * size;

        double minZ = -48 + dz * size;
        double maxZ = 48 + dz * size;

        return new PosRange(
                new Vec3(minX, 66, minZ),
                new Vec3(maxX, 86, maxZ)
        );
    }

    public static Cord posToCord(Vec3 pos) {
        int size = 96;

        int x = (int) Math.floor((pos.x + 48) / size) + 2;
        int y = (int) Math.floor((pos.z + 48) / size) + 2;

        return new Cord(x, y);
    }

    public record Cord(int x, int y) {
    }

    public record PosRange(Vec3 start, Vec3 end) {
    }
}


// === NOTES ===
/*
    Barn Range:
    <-48, 66, 47> // bottom left
          to
    <47, 72, -48> // top right

    Plot 1 Range:
    <-48, 66, -49> // bottom left
    to
    <47, 72, -142> // top right

    Garden layout:
    21 13 09 14 22
    15 05 01 06 16
    10 02 xx 03 11
    17 07 04 08 18
    23 19 12 20 24
*/