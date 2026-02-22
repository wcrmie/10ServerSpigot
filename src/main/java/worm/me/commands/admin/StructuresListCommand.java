package worm.me.commands.admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import worm.me.gamesystems.world.legacystructure.generation.Generate;

public class StructuresListCommand implements CommandExecutor {

    private final Generate generate;

    public StructuresListCommand(Generate generate) {
        this.generate = generate;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        generate.handleListStructures(sender);
        return true;
    }
}
