package worm.me.commands.gui;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import worm.me.menu.SkillsMenu;

public class OpenLevelsGUICommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player p){


            if (args.length >= 1){
                try {

                    SkillsMenu skillsMenu = new SkillsMenu(p, Bukkit.getPlayer(args[0]));
                    skillsMenu.open();
                    return true;

                } catch (Exception e){
                    p.sendMessage("Player not found");
                    return false;
                }

            } else {

                SkillsMenu skillsMenu = new SkillsMenu(p, p);
                skillsMenu.open();

            }
            return true;

        } else sender.sendMessage("This command is for the player, silly!");
        return false;
    }
}
