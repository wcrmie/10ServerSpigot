package worm.me.commands.chat;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import worm.me.Server;
import worm.me.playersystems.social.MessageHead;

public class SayCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {

        if (strings.length == 0) {
            return false;
        }

        // Make the message play above the player's head
        if (commandSender instanceof Player player) {

            String message = String.join(" ", strings);

            PersistentDataContainer c = player.getPersistentDataContainer();

            MessageHead messageHead = new MessageHead(Server.getPlugin());

            if (message.length() > c.getOrDefault(messageHead.msgLength, PersistentDataType.INTEGER, 192)){
                commandSender.sendMessage(ChatColor.RED + String.format("Your messages must be %d characters or less.", c.getOrDefault(messageHead.msgLength, PersistentDataType.INTEGER, 192)));
                return false;
            }

            messageHead.messageQueue(player, message);

            return true;
        }

        return false;
    }
}
