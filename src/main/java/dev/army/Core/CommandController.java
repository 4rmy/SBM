package dev.army.Core;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import com.mojang.brigadier.arguments.FloatArgumentType;
import dev.army.Features.Farming.FarmingCore;
import dev.army.SBM;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class CommandController {
    private static final Minecraft mc = Minecraft.getInstance();

    public static void init() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            // all of skyblock
            dispatcher.register(
                    ClientCommands.literal("s").executes(_ -> {
                        if (mc.getConnection() == null) return 0;
                        mc.getConnection().sendCommand("skyblock");
                        return 1;
                    })
            );
            // farming
            dispatcher.register(
                    ClientCommands.literal("garden").executes(_ -> {
                        if (mc.getConnection() == null) return 0;
                        mc.getConnection().sendCommand("warp garden");
                        return 1;
                    })
            );
            dispatcher.register(
                    ClientCommands.literal("gd").executes(_ -> {
                        if (mc.getConnection() == null) return 0;
                        mc.getConnection().sendCommand("warp garden");
                        return 1;
                    })
            );
            dispatcher.register(
                    ClientCommands.literal("barn").executes(_ -> {
                        if (mc.getConnection() == null) return 0;
                        mc.getConnection().sendCommand("tptoplot barn");
                        return 1;
                    })
            );
            dispatcher.register(
                    ClientCommands.literal("setangle")
                            .then(ClientCommands.argument("pitch", FloatArgumentType.floatArg())
                            .then(ClientCommands.argument("yaw", FloatArgumentType.floatArg())
                            .executes(cmd -> {
                                        float pitch = FloatArgumentType.getFloat(cmd, "pitch");
                                        float yaw = FloatArgumentType.getFloat(cmd, "yaw");

                                        LocalPlayer player = mc.player;
                                        if (player == null) return 0;

                                        player.setXRot(yaw);
                                        player.setYRot(pitch);

                                        return 1;
                                    })
                            )
                        )
            );

            // debug
            dispatcher.register(
                    ClientCommands.literal("debugtab").executes(_ -> {
                        assert mc.player != null;
                        FarmingCore.PosRange rng = FarmingCore.cordToPos(
                                FarmingCore.posToCord(
                                        mc.player.position()
                                )
                        );
                        assert mc.level != null;
                        List<Entity> entities = mc.level.getEntities(mc.player, new AABB(rng.start(), rng.end())).stream()
                                .filter(e -> e.getCustomName() != null)
                                .toList();

                        System.out.println(" === ENTITIES");
                        for (Entity e : entities)
                            System.out.println(e.getCustomName().getString());

                        return 1;
                    })
            );
            dispatcher.register(
                    ClientCommands.literal("setheads")
                            .executes(ctx -> {
                                LocalPlayer player = Minecraft.getInstance().player;

                                if (player == null) {
                                    return 0;
                                }

                                player.getInventory().setItem(0, SBM.s1.copy());
                                player.getInventory().setItem(1, SBM.s2.copy());
                                player.getInventory().setItem(2, SBM.s3.copy());
                                player.getInventory().setItem(3, SBM.s4.copy());

                                return 1;
                            })
            );


        });
    }
}
