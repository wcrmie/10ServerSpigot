package worm.me.commands.admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import worm.me.gamesystems.world.legacystructure.generation.Generate;

public class StructuresRemoveCommand implements CommandExecutor {

    private final Generate generate;

    public StructuresRemoveCommand(Generate generate) {
        this.generate = generate;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        generate.handleRemoveStructure(sender, args);
        return true;
    }
}
