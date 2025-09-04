package worm.me.commands.admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import worm.me.gamesystems.world.structure.Island;

public class StructuresPlaceTypeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Island.spawnIslandType(args[0]);
        return true;
    }
}
