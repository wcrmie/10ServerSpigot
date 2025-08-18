package worm.me.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import worm.me.utils.chat.Message;
import worm.me.utils.chat.MessageType;
import worm.me.utils.staff.Spectate;

public class SpectateCommand implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {

        if (commandSender.hasPermission("10VS.spectate") && commandSender instanceof Player moderator) {

            if (strings.length == 0) {
                commandSender.sendMessage(Message.create(MessageType.ERROR, "Usage: /spectate <player/stop>"));
                return true;
            }

            String arg = strings[0];
            if (!arg.equalsIgnoreCase("stop")) {
                if (!Spectate.stop(moderator)) {
                    moderator.sendMessage(Message.create(MessageType.ERROR, "You are not currently spectating anyone."));
                    return true;
                }
                moderator.sendMessage(Message.create(MessageType.SUCCESS, "You have been returned to your original location."));
                return true;
            }

            for (Player suspect : Bukkit.getOnlinePlayers()) {

                if (suspect.getName().equalsIgnoreCase(arg)) {
                    moderator.sendMessage(Message.create(MessageType.SUCCESS, "Spectating player: " + arg));
                    Spectate.start(moderator, suspect);
                    return true;
                }
            }

            moderator.sendMessage(Message.create(MessageType.ERROR, "Player not found: " + arg));
            return true;
        }

        return false;
    }
}
