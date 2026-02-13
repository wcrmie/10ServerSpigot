package worm.me.listeners.player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import worm.me.utils.chat.Message;
import worm.me.utils.chat.MessageType;

public class ChatListener implements Listener {

    // --------------------------MESSAGEHEAD--------------------------

    @EventHandler
    public void onChat(PlayerChatEvent event){

        if (event.isCancelled()) return;

        // initialize variables
        Player sender = event.getPlayer();
        String msg = event.getMessage();

        if (msg.toLowerCase().contains("hack") || msg.toLowerCase().contains("cheat") || msg.toLowerCase().contains("hax")) {
            sender.sendMessage(Message.create(MessageType.GENERIC, "If you think someone is hacking, please report them using /report <player>."));
            sender.playSound(sender, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
        }

        if (msg.toLowerCase().contains("how") && (msg.toLowerCase().contains("stone") || msg.toLowerCase().contains("tool") || msg.toLowerCase().contains("pick") || msg.toLowerCase().contains("axe") || msg.toLowerCase().contains("shovel") || msg.toLowerCase().contains("sword"))) {
            sender.sendMessage(Message.create(MessageType.GENERIC, "Hover your tools in your inventory to see how to upgrade them!"));
            sender.playSound(sender, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
        }


    }

    // --------------------------END MESSAGEHEAD--------------------------

}
