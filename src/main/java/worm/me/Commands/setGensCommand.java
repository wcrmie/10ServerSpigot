package worm.me.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import worm.me.PlayerSystems.Generator.GeneratorEdit;
import worm.me.server;

public class setGensCommand implements CommandExecutor {

    private server plugin;


    public setGensCommand(server plugin) {
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
