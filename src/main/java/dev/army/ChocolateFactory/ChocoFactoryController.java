package dev.army.ChocolateFactory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;

import java.util.ArrayList;
import java.util.List;

public class ChocoFactoryController {
    private static final Minecraft mc = Minecraft.getInstance();

    private static final int IMAGE_WIDTH = 176;
    private static final int IMAGE_HEIGHT = 114 + 6 * 18;

    private static final List<Employee> employees = new ArrayList<>(7);

    public static void extractRenderState(ContainerScreen screen, GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        Slot cho = screen.getMenu().slots.get(13);
        if (cho.getItem().isEmpty()) return;

        int chocolate = Integer.parseInt(String.join(
                "",
                screen.getMenu()
                        .slots.get(13)
                        .getItem()
                        .getDisplayName()
                        .getString()
                        .strip().split(" ")[0]
                        .split(",")
        ).substring(1));

        // get list of eligible upgrades
        employees.clear();
        for (Slot s : screen.getMenu().slots.subList(28, 35)) { // just the rabits
            if (s.getItem().isEmpty()) continue;
            List<String> tooltip = s.getItem().getTooltipLines(
                            Item.TooltipContext.EMPTY,
                            mc.player,
                            TooltipFlag.NORMAL
                    ).stream()
                    .dropWhile(l -> !l.getString().contains("---"))
                    .skip(2)
                    .map(l -> l.getString().strip())
                    .filter(l -> l.contains("Chocolate"))
                    .toList();
            if (!tooltip.isEmpty()) {
                employees.add(
                        new Employee(
                                s,
                                s.index - 27,
                                Integer.parseInt(String.join("", tooltip.getLast().split("\\s")[0].split(",")))
                        )
                );
            }

        }

        // rendering
        int xoff = (screen.width - IMAGE_WIDTH) / 2;
        int yoff = (screen.height - IMAGE_HEIGHT) / 2;

        boolean first = true;
        for (Employee e : employees.stream().sorted().toList()) {
            if (first) {
                graphics.fill(
                        e.slot.x + xoff,
                        e.slot.y + yoff,
                        e.slot.x + xoff + 16,
                        e.slot.y + yoff + 16,
                        (chocolate >= e.cost) ? 0xaa55FF55 : 0xaaFFAA55
                );
                if (chocolate >= e.cost)
                    break;
            } else if (chocolate >= e.cost) {
                graphics.fill(
                        e.slot.x + xoff,
                        e.slot.y + yoff,
                        e.slot.x + xoff + 16,
                        e.slot.y + yoff + 16,
                        0xaaFF5555
                );
                break;
            }
            first = false;
        }
    }

    private record Employee(Slot slot, int delta, int cost) implements Comparable<Employee> {
        @Override
        public int compareTo(ChocoFactoryController.Employee o) {
            return Double.compare((double) o.delta / o.cost, (double) this.delta / this.cost);
        }
    }
}
