package worm.me.utils.chat;

import org.bukkit.ChatColor;

public enum MessageType {
    FAIL(ChatColor.RED, "❌"),
    ERROR(ChatColor.GOLD, "⚠"),
    SUCCESS(ChatColor.GREEN, "✔"),
    GENERIC(ChatColor.BLUE, "ℹ"),
    ANNOUNCEMENT(ChatColor.YELLOW, "ANNOUNCEMENT");

    final ChatColor color;
    final String icon;

    MessageType(ChatColor color, String icon) {
        this.color = color;
        this.icon = icon;
    }

    public ChatColor getColor() {
        return color;
    }

    public String getIcon() {
        return icon;
    }
}
