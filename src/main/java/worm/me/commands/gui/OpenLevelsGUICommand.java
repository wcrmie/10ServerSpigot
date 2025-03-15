package worm.me.commands.gui;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import worm.me.playersystems.playerdata.levels.LevelsGUI;
import worm.me.Server;

public class OpenLevelsGUICommand implements CommandExecutor {

    private final Server plugin;

    public OpenLevelsGUICommand(Server plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player p){

            LevelsGUI levelsGUI = new LevelsGUI();
            if (args.length > 0){
                try {
                    plugin.getServer().getPlayer(args[0]);
                } catch (Exception e){
                    p.sendMessage("Player not found");
                    return false;
                }
                levelsGUI.openLevelsGUI(p.getServer().getPlayer(args[0]), p);
                return true;
            } else levelsGUI.openLevelsGUI(p, p);
            return true;

        } else sender.sendMessage("This command is for the player, silly!");
        return false;
    }
}
