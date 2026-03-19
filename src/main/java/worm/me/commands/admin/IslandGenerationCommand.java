package worm.me.commands.admin;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import worm.me.gamesystems.world.generation.island.IslandGenerator;

import java.util.List;

public class IslandGenerationCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (sender instanceof Player player) {

            Location location = player.getLocation();

            IslandGenerator.generateIsland(location, args.length < 1 ? null : Integer.parseInt(args[0]));

            player.sendMessage("Attempting to generate an island at your location... get rady");

            return true;
        } else {
            sender.sendMessage("This command can only be executed by a player.");
        }

        return false;
    }
}
