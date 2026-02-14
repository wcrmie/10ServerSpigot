package worm.me.commands.gui;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import worm.me.menu.generators.GensMainMenu;

public class SetGensCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player){
            GensMainMenu genInv = new GensMainMenu(player);

            // This opens the GUI for the player
            genInv.genOptions(player);
            return true;
        } else sender.sendMessage("This command is for the player, silly!");
        return false;
    }
}
