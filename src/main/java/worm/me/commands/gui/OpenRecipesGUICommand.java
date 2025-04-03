package worm.me.commands.gui;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import worm.me.gamesystems.recipes.RecipesGUI;

public class OpenRecipesGUICommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player p){

            RecipesGUI recipesGUI = new RecipesGUI();
            recipesGUI.openMainGUI(p, 1);
            return true;

        }

        return false;
    }
}
