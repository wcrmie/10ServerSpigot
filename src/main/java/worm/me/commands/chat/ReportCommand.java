package worm.me.commands.chat;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import worm.me.Server;
import worm.me.playersystems.social.MessageHead;

public class ReportCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {

        if (commandSender instanceof Player player && args.length == 1) {

            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getName().equals(args[0])) {

                    Server.pluginMessage(NamedTextColor.GREEN + "Report submitted! Be sure to only report players you REALLY think are cheating. If you are found to be abusing or spamming the report command, permission to do so will be taken away.", player);

                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        if (onlinePlayer.hasPermission("10.report.receive")) {
                            Server.pluginMessage(NamedTextColor.YELLOW + player.getName() + NamedTextColor.RED + " has reported " + NamedTextColor.YELLOW + p.getName() + NamedTextColor.RED + "!", onlinePlayer);
                        }
                    }

                    return true;

                }
            }
            Server.pluginMessage(ChatColor.RED + "Player not found!", player);
            return true;

        }

        return false;
    }
}
