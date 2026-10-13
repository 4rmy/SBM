package dev.army.Core;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import java.util.stream.Stream;

public class TablistParser {
    private static final Minecraft mc = Minecraft.getInstance();

    public static Stream<String> getLines() {
        if (mc.getConnection() == null) return Stream.empty();
        return mc.getConnection()
                .getListedOnlinePlayers()
                .stream()
                .map(entry -> {
                    Component displayname = entry.getTabListDisplayName();

                    if (displayname != null) {
                        return displayname.getString().strip();
                    }

                    return entry.getProfile().name().strip();
                });
    }

    public static Stream<String> getSection(String name) {
        return getLines()
                .dropWhile(line -> !line.toLowerCase().startsWith(name))
                .takeWhile(line -> !line.isEmpty());
    }

    public static String getArea() {
        return getLines()
                .filter(line -> line.startsWith("Area:"))
                .findFirst()
                .map(line -> line.substring(line.indexOf(':') + 1).trim().toLowerCase())
                .orElse("null");
    }
}
