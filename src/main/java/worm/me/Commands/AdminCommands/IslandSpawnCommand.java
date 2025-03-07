package worm.me.Commands.AdminCommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import worm.me.GameSystems.StructureGeneration.IslandSpawning;
import worm.me.Server;

public class IslandSpawnCommand implements CommandExecutor {
    private final IslandSpawning islandSpawningHandler;

    public IslandSpawnCommand(Server plugin) {
        this.islandSpawningHandler = new IslandSpawning(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String IslandName = args.length > 0 ? args[0] : null;
        islandSpawningHandler.IslandSpawn(IslandName);
        return true;
    }
}