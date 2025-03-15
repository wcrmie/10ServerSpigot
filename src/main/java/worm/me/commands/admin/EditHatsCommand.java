package worm.me.commands.AdminCommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import worm.me.PlayerSystems.PlayerData.Social.Hats.HatsStorage;
import worm.me.Server;

public class EditHatsCommand implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {



        if (sender instanceof Player p){

            if (args.length == 0) {
                Server.pluginMessage("Usage: /edithats <page no.>", p);
                return false;
            }

            HatsStorage hs = new HatsStorage();
            hs.addHat(Integer.parseInt(args[0]), p);
        }

        return false;
    }
}
