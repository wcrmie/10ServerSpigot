package worm.me.utils.chat;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Message {
    public static Component create(MessageType type, String message) {

        return MiniMessage.miniMessage().deserialize(

                String.format(

                        "<dark_gray>[%s]</dark_gray> <%s>%s",
                        type.icon,
                        type.messageColor,
                        message

                )

        );
    }

    public static void sendAll(Component message) {
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            p.sendMessage(message);
        }
    }
}
