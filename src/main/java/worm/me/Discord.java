package worm.me;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class Discord {

    public static void sendDiscordMessage(Player p) {
        // Send message to Player
        Server.pluginMessage(ChatColor.GRAY + "Join our discord here for updates: " + ChatColor.BLUE + "https://discord.gg/p5mhNdYvEm", p);
        p.playSound(p, Sound.BLOCK_NOTE_BLOCK_CHIME, 1, 1);
    }

}
