package worm.me.Commands.GUICommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import worm.me.PlayerSystems.PlayerData.Social.Skin.SkinGui;

public class OpenSkinGUICommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player p){
            SkinGui.skinGuiOpen(p);
            return true;
        }

        return false;
    }
}
