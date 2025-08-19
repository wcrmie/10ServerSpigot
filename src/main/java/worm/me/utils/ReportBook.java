package worm.me.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import worm.me.utils.managers.ConfigManager;

import java.util.*;

public class ReportBook {

    private static final Map<UUID, String> playerTokens = new HashMap<>();

    public static void openReportBook(Player player, String reportedName, ConfigManager configManager) {
        String token = UUID.randomUUID().toString();
        playerTokens.put(player.getUniqueId(), token);

        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();

        meta.author(Component.text("🌟 Saturn Report System 🌟", NamedTextColor.DARK_PURPLE, TextDecoration.BOLD));
        meta.title(Component.text("📒 Report: " + reportedName, NamedTextColor.GOLD, TextDecoration.BOLD));

        List<Component> pageComponents = new ArrayList<>();

        pageComponents.add(Component.text("🚨 REPORT MENU 🚨\n\n", NamedTextColor.GOLD, TextDecoration.BOLD, TextDecoration.UNDERLINED));
        pageComponents.add(Component.text("Why are you reporting ", NamedTextColor.GOLD)
                .append(Component.text(reportedName, NamedTextColor.RED, TextDecoration.BOLD))
                .append(Component.text("?\n\n", NamedTextColor.WHITE)));

        List<Map<String, Object>> reasons = configManager.getReasons();

        for (int i = 0; i < reasons.size(); i++) {
            Map<String, Object> reason = reasons.get(i);
            String name = (String) reason.get("name");
            String description = (String) reason.get("description");

            TextComponent reasonText = Component.text("➡ " + (i + 1) + ". " + name, NamedTextColor.DARK_RED)
                    .hoverEvent(HoverEvent.showText(Component.text(description, NamedTextColor.YELLOW)))
                    .clickEvent(ClickEvent.runCommand("/report " + reportedName + " " + stripColorCodes(name)));

            pageComponents.add(reasonText);
            pageComponents.add(Component.text("\n"));
        }

        Component page = Component.text().append(pageComponents).build();

        meta.pages(List.of(page));
        book.setItemMeta(meta);

        player.openBook(book);
    }

    private static String stripColorCodes(String input) {
        return input.replaceAll("(?i)§[0-9A-FK-OR]", "").trim();
    }
}
