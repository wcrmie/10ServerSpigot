package worm.me.commands.gui;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import worm.me.playersystems.data.social.hats.Hats;
import worm.me.Server;

public class HatsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player p){

            if (args.length >= 1) {
                try {
                    int i = Integer.parseInt(args[0]);
                    if (i > 50*54/27) {
                        Server.pluginMessage(ChatColor.RED + "That page doesn't exist.", p);
                        return true;
                    }

                    else {
                        Hats hats = new Hats();
                        hats.openHatsGUI(p, i);
                        return true;
                    }


                } catch (NumberFormatException e) {
                    if (args[0].equals("set") && args.length >= 2) {

                        int i = Integer.parseInt(args[1]);
                        if (i > 2700) {
                            Server.pluginMessage(ChatColor.RED + "That hat doesn't exist.", p);
                            return true;
                        }

                        else {
                            Hats hats = new Hats();
                            hats.equipHat(p, i);
                            return true;

                        }
                    }
                }
            } else {
                Hats hats = new Hats();
                hats.openHatsGUI(p, 1);
                return true;
            }
        } else {
            sender.sendMessage("are we fr?");
            return false;
        }
        return false;
    }
}
