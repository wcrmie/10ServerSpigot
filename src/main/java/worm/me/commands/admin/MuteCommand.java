package worm.me.commands.admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import worm.me.utils.chat.Message;
import worm.me.utils.chat.MessageType;
import worm.me.utils.player.Online;
import worm.me.utils.staff.ChatLimit;

public class MuteCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {

        if (args.length < 1) {
            sender.sendMessage(Message.create(MessageType.ERROR, "Usage: /mute <player>"));
            return true;
        }

        Player player = Online.find(args[0]);

        if (player == null) {
            sender.sendMessage(Message.create(MessageType.ERROR, "Player not found!"));
            sender.sendMessage(Message.create(MessageType.ERROR, "Usage: /mute <player>"));
            return true;
        }

        ChatLimit.setChatLimit(player, 0);
        sender.sendMessage(Message.create(MessageType.SUCCESS, player.getName() + " has been muted."));
        return true;
    }
}
