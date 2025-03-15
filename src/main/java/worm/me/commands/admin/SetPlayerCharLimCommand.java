package worm.me.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import worm.me.playersystems.playerdata.social.MessageHead;
import worm.me.Server;

public class SetPlayerCharLimCommand implements CommandExecutor {

    private final Server plugin;

    public SetPlayerCharLimCommand(Server plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getName().equals(args[0])) {
                if (Integer.parseInt(args[1]) > 0) {
                    MessageHead messageHead = new MessageHead(this.plugin);
                    p.getPersistentDataContainer().set(messageHead.msgLength, PersistentDataType.INTEGER, Integer.parseInt(args[1]));
                    plugin.pluginMessage(ChatColor.RED + "Your character limit for messages has been set to " + args[1], p);
                    plugin.pluginMessage(ChatColor.GREEN + "Character limit for " + args[0] + " has been set to " + args[1], (Player) sender);
                    return true;
                } else {
                    plugin.pluginMessage(ChatColor.RED + "Character limit must be greater than 0!", (Player) sender);
                    return false;
                }
            }
        }
        plugin.pluginMessage(ChatColor.RED + "Player not found!", (Player) sender);
        return false;
    }
}
