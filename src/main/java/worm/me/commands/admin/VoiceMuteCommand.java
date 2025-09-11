package worm.me.commands.admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.jetbrains.annotations.NotNull;
import worm.me.Server;
import worm.me.utils.chat.Message;
import worm.me.utils.chat.MessageType;
import worm.me.utils.player.Online;

public class VoiceMuteCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {

        Player p = Online.find(args[0]);

        if (p == null) {
            commandSender.sendMessage(Message.create(MessageType.ERROR, "Player not found!"));
            commandSender.sendMessage(Message.create(MessageType.ERROR, "Usage: /voicemute <player>"));
            return true;
        }

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
