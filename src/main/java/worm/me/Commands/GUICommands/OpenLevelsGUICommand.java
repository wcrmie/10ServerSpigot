package worm.me.Commands.GUICommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import worm.me.PlayerSystems.PlayerData.Levels.LevelsGUI;
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
            levelsGUI.openLevelsGUI(p);
            return true;
        } else sender.sendMessage("This command is for the player, silly!");
        return false;
    }
}
