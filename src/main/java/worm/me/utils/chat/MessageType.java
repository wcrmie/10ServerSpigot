package worm.me.utils.chat;

import org.bukkit.ChatColor;

public enum MessageType {
    FAIL("<red>❌</red>", "red"),
    ERROR("<gold>⚠</gold>", "yellow"),
    SUCCESS("<green>✔</green>", "green"),
    GENERIC("<blue>ℹ</blue>", "white"),
    ANNOUNCEMENT("<gold>ANNOUNCEMENT</gold>", "yellow");

    final String icon;
    final String messageColor;

     MessageType(String icon, String messageColor) {
        this.messageColor = messageColor;
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }

    public String getMsgColor() {
        return messageColor;
    }
}
