package worm.me.Commands.GUICommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import worm.me.PlayerSystems.Abilities.AbilitiesGUI;

public class OpenAbilitiesGUICommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player p){
            AbilitiesGUI abilitiesGUI = new AbilitiesGUI();
            abilitiesGUI.abilitiesActive(p);
            return true;
        } else sender.sendMessage("This command is for the player, silly!");
        return false;
    }
}
