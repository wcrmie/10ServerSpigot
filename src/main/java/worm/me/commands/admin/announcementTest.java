package worm.me.commands.admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import worm.me.discord.bot.BotInteractions;

public class announcementTest implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {

        if (commandSender.isOp()) {

            BotInteractions botInteractions = new BotInteractions();
            botInteractions.announcementTest();

            return true;
        }

        return false;
    }
}
