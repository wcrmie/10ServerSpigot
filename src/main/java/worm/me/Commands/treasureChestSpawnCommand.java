package worm.me.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import worm.me.GameSystems.treasureChests;
import worm.me.server;

public class treasureChestSpawnCommand implements CommandExecutor {

    private final treasureChests treasureChestHandler = new treasureChests();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        treasureChestHandler.chestSpawn();
        return true;
    }
}