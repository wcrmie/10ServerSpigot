package worm.me.commands.admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import worm.me.utils.chat.Message;
import worm.me.utils.chat.MessageType;
import worm.me.utils.player.Online;
import worm.me.utils.staff.ChatModeration;

public class SetPlayerCharLimCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length < 2) {
            sender.sendMessage(Message.create(MessageType.ERROR, "Usage: /setcharlimit <player> <limit>"));
            return true;
        }

        Player player = Online.find(args[0]);

        if (player == null) {
            sender.sendMessage(Message.create(MessageType.ERROR, "Player not found!"));
            sender.sendMessage(Message.create(MessageType.ERROR, "Usage: /setcharlimit <player> <limit>"));
            return true;
        }

        int limit = Integer.parseInt(args[1]);

        if (limit > 0) {

            ChatModeration.setChatLimit(player, limit);
            sender.sendMessage(Message.create(MessageType.SUCCESS, "Character limit for " + player.getName() + " has been set to " + args[1]));
            return true;

        } else if (limit == 0) {

            ChatModeration.setChatLimit(player, limit);
            sender.sendMessage(Message.create(MessageType.SUCCESS, player.getName() + " has been muted."));
            return true;

        } else {
            sender.sendMessage(Message.create(MessageType.ERROR, "Character limits cannot be negative."));
            return true;
        }
    }
}
