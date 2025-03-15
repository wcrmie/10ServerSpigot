package worm.me.commands.AdminCommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import worm.me.GameSystems.StructureGeneration.TreasureChests;

public class TreasureChestSpawnCommand implements CommandExecutor {

    private final TreasureChests treasureChestHandler = new TreasureChests();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        treasureChestHandler.chestSpawn();
        return true;
    }
}