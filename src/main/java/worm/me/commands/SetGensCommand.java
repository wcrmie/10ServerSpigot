package worm.me.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import worm.me.playersystems.generator.GeneratorEdit;
import worm.me.Server;

public class SetGensCommand implements CommandExecutor {

    private Server plugin;


    public SetGensCommand(Server plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            Player p = (Player) sender;
            GeneratorEdit genInv = new GeneratorEdit();

            // This opens the GUI for the player
            genInv.genOptions(p);
            return true;
        } else sender.sendMessage("This command is for the player, silly!");
        return false;
    }
}
