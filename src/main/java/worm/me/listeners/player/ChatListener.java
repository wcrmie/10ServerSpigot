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

        // queue messages to be sent above head
        event.getRecipients().clear();

        for (Player player : Bukkit.getOnlinePlayers()) {
            double distance = player.getLocation().distance(sender.getLocation());

            int color = distance == 0 ? 255 : (int) Math.round(255 - 255 * (distance / (player.getWorld().getWorldBorder().getSize() + 50)));
            if (color < 50) color = 50;
            String hexColor = String.format("#%02x%02x%02x", color, color, color);

            String message = msg;
            if (message.contains(player.getName())) {
                message = message.replace(player.getName(), ChatColor.YELLOW + "@" + player.getName() + ChatColor.RESET);
                player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
            }

            net.kyori.adventure.text.TextComponent component = Component.text(sender.getName() + ": " + message).color(TextColor.fromHexString(hexColor));

            if (message.contains("[i]") || message.contains("[item]")) {

                ItemStack item = sender.getInventory().getItemInMainHand();

                String itemName = item.getType().toString();

                if (itemName.isEmpty()) {
                    itemName = item.getType().toString();
                }

                message = message.replaceAll("\\[i]", ChatColor.AQUA + "[" + itemName + " x" + item.getAmount() + "]" + ChatColor.RESET).replaceAll("\\[item]", ChatColor.AQUA + "[" + itemName + " x" + item.getAmount() + "]" + ChatColor.RESET);

                component = Component.text(sender.getName() + ": " + message).color(TextColor.fromHexString(hexColor));

                component = component.hoverEvent(item.asHoverEvent());

            }

            player.sendMessage(component);

        }

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
