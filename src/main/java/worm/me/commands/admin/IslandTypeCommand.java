package worm.me.commands.admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import worm.me.gamesystems.structure.generation.Island;
import worm.me.gamesystems.structure.generation.IslandType;

import java.util.concurrent.ThreadLocalRandom;

public class IslandTypeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {


        String typeArg = args[0].toUpperCase();

        Island.spawnIslandType(typeArg);

        return true;
    }
}
