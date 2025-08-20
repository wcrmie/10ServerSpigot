package worm.me.commands.admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import worm.me.gamesystems.structure.generation.Generate;

public class StructuresPlaceCommand implements CommandExecutor {

    private final Generate generate;

    public StructuresPlaceCommand(Generate generate) {
        this.generate = generate;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        generate.handlePlaceStructure(sender, args);
        return true;
    }
}