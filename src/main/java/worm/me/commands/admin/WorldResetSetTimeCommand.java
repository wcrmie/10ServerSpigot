package worm.me.commands.admin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import worm.me.gamesystems.world.reset.WorldReset;

public class WorldResetSetTimeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!sender.isOp()) {
            sender.sendMessage(ChatColor.RED + "you dont have permission to use this command");
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /setresettimer <seconds>");
            return true;
        }

        long newTime;

        try {
            newTime = Long.parseLong(args[0]);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Invalid number: " + args[0]);
            return true;
        }

        if (newTime <= 0) {
            sender.sendMessage(ChatColor.RED + "Time must be greater than zero.");
            return true;
        }

        WorldReset wr = WorldReset.getInstance();
        wr.scoreNeededForReset = newTime;
        wr.saveData();

        sender.sendMessage(ChatColor.GREEN + "World reset timer set to " + newTime + " seconds.");
        sender.sendMessage(ChatColor.GREEN + "That's " + (newTime / 3600.0) + " hours.");

        return true;
    }
}
