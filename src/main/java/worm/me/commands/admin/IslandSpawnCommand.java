package worm.me.commands.admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import worm.me.gamesystems.structure.generation.Island;
import worm.me.Server;

public class IslandSpawnCommand implements CommandExecutor {
    private final Island islandSpawningHandler;

    public IslandSpawnCommand(Server plugin) {
        this.islandSpawningHandler = new Island(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String IslandName = args.length > 0 ? args[0] : null;
        islandSpawningHandler.IslandSpawn(IslandName);
        return true;
    }
}