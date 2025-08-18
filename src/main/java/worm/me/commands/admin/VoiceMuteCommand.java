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
import worm.me.utils.chat.Message;
import worm.me.utils.chat.MessageType;

public class VoiceMuteCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getName().equals(strings[0])) {



                 if (p.hasPermission("voicechat.speak")) {

                     PermissionAttachment attachment = p.addAttachment(Server.getPlugin(), "voicechat.speak", false);
                     p.recalculatePermissions();
                     commandSender.sendMessage(Message.create(MessageType.SUCCESS, p.getName() + " has been voice muted."));
                     p.sendMessage(Message.create(MessageType.GENERIC, "You have been voice muted."));
                     return true;

                 } else {

                     PermissionAttachment attachment = p.addAttachment(Server.getPlugin(), "voicechat.speak", true);
                     p.recalculatePermissions();
                     p.sendMessage(Message.create(MessageType.GENERIC, "You are no longer voice muted."));
                     commandSender.sendMessage(Message.create(MessageType.SUCCESS, p.getName() + " is no longer voice muted."));
                     return true;

                 }
            }
        }
        return false;
    }
}
