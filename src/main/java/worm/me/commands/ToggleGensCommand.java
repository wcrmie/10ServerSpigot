package worm.me.commands;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.units.qual.N;
import org.jetbrains.annotations.NotNull;
import worm.me.Server;
import worm.me.utils.chat.Message;
import worm.me.utils.chat.MessageType;

public class ToggleGensCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {

        if (commandSender instanceof Player p) {
            PersistentDataContainer container = p.getPersistentDataContainer();

            int currentVal = container.getOrDefault(new NamespacedKey(Server.getPlugin(), "togglegens"), PersistentDataType.INTEGER, 0);

            if (currentVal == 0) {
                container.set(new NamespacedKey(Server.getPlugin(), "togglegens"), PersistentDataType.INTEGER, 1);
                p.sendMessage(Message.create(MessageType.SUCCESS, "Generators are now DISABLED."));
            } else {
                container.set(new NamespacedKey(Server.getPlugin(), "togglegens"), PersistentDataType.INTEGER, 0);
                p.sendMessage(Message.create(MessageType.SUCCESS, "Generators are now ENABLED."));

            }
            return true;

        }

        return false;
    }
}
