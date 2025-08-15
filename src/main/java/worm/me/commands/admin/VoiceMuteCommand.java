package worm.me.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import worm.me.Server;
import worm.me.playersystems.social.MessageHead;

public class VoiceMuteCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getName().equals(strings[0])) {



                 if (p.hasPermission("voicechat.speak")) {

                     PermissionAttachment attachment = p.addAttachment(Server.getPlugin(), "voicechat.speak", false);
                     p.recalculatePermissions();
                     commandSender.sendMessage(ChatColor.RED + p.getName() + " has been voice muted.");
                     Server.pluginMessage(ChatColor.RED + "You have been voice muted.", p);

                 } else {

                     PermissionAttachment attachment = p.addAttachment(Server.getPlugin(), "voicechat.speak", true);
                     p.recalculatePermissions();
                     Server.pluginMessage(ChatColor.GREEN + "You are no longer voice muted.", p);
                     commandSender.sendMessage(ChatColor.GREEN + p.getName() + " is no longer voice muted.");

                 }
            }
        }
        return false;
    }
}
