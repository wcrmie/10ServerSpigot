package worm.me.commands.admin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import worm.me.gamesystems.worldreset.WorldReset;

public class WorldResetCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player && !sender.isOp()) {
            sender.sendMessage(ChatColor.RED + "you dont have permission to use this command");
            return true;
        }

        WorldReset.getInstance().triggerReset(sender instanceof Player ? (Player) sender : null);
        return true;
    }
}