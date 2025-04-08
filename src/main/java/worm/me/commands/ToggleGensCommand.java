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

public class ToggleGensCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {

        if (commandSender instanceof Player p) {
            PersistentDataContainer container = p.getPersistentDataContainer();

            int currentVal = container.getOrDefault(new NamespacedKey(Server.getPlugin(), "togglegens"), PersistentDataType.INTEGER, 0);

            if (currentVal == 0) {
                container.set(new NamespacedKey(Server.getPlugin(), "togglegens"), PersistentDataType.INTEGER, 1);
                Server.pluginMessage(ChatColor.GREEN + "Generators are now DISABLED.", p);
            } else {
                container.set(new NamespacedKey(Server.getPlugin(), "togglegens"), PersistentDataType.INTEGER, 0);
                Server.pluginMessage(ChatColor.GREEN + "Generators are now ENABLED.", p);
            }
            return true;

        }

        return false;
    }
}
