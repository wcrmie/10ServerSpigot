package worm.me.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import worm.me.PlayerSystems.Social.MessageHead;
import worm.me.server;

public class setPlayerCharLimCommand implements CommandExecutor {

    private final server plugin;

    public setPlayerCharLimCommand(server plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (Bukkit.getOnlinePlayers().contains(args[0])) {
            Player p = Bukkit.getPlayer(args[0]);
            if (Integer.parseInt(args[1]) > 0) {
                MessageHead messageHead = new MessageHead(this.plugin);
                p.getPersistentDataContainer().set(messageHead.msgLength, PersistentDataType.INTEGER, Integer.parseInt(args[1]));
                p.sendMessage(ChatColor.RED + "Your character limit for messages has been set to " + args[1]);
                sender.sendMessage(ChatColor.GREEN + "Character limit for " + args[0] + " has been set to " + args[1]);
            }
        }
        return false;
    }
}
