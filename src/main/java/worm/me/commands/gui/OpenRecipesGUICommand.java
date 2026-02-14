package worm.me.commands.gui;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import worm.me.menu.recipes.RecipeBrowserMenu;

public class OpenRecipesGUICommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player p){

            RecipeBrowserMenu recipeBrowserMenu = new RecipeBrowserMenu(p, 1);
            recipeBrowserMenu.open();
            return true;

        }

        return false;
    }
}
