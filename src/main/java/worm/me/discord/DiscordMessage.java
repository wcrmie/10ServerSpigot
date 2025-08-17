package worm.me.discord;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import worm.me.Server;
import worm.me.utils.chat.Message;
import worm.me.utils.chat.MessageType;

public class DiscordMessage {

    public static void sendDiscordMessage(Player p) {
        // Send message to Player
        p.sendMessage(Message.create(MessageType.GENERIC, "Join our discord here for updates: " + ChatColor.BLUE + "https://discord.gg/p5mhNdYvEm"));
        p.playSound(p, Sound.BLOCK_NOTE_BLOCK_CHIME, 1, 1);
    }

}
