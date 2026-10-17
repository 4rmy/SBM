package dev.army.Features.Farming;

import dev.army.Core.TablistParser;

import java.util.Arrays;
import java.util.List;

public class FarmingTablist extends TablistParser {
    public static List<Integer> getPestPlots() {

        String plots = TablistParser.getLines()
                .filter(l -> l.toLowerCase().startsWith("plots"))
                .findFirst().orElse(null);
        if (plots == null) return List.of();

        return Arrays.stream(plots.substring(plots.indexOf(':') + 1).split(","))
                .map(String::strip)
                .map(Integer::parseInt)
                .toList();
    }
}
