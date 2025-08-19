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

public class UnmuteCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {

        if (args.length < 1) {
            sender.sendMessage(Message.create(MessageType.ERROR, "Usage: /unmute <player>"));
            return true;
        }

        Player player = Online.find(args[0]);

        if (player == null) {
            sender.sendMessage(Message.create(MessageType.ERROR, "Player not found!"));
            sender.sendMessage(Message.create(MessageType.ERROR, "Usage: /unmute <player>"));
            return true;
        }

        if (ChatLimit.getChatLimit(player) != 0) {
            sender.sendMessage(Message.create(MessageType.FAIL, player.getName() + " is not currently muted."));
            return true;
        }

        ChatLimit.unmute(player);
        sender.sendMessage(Message.create(MessageType.SUCCESS, player.getName() + " has been unmuted."));
        return true;
    }
}
