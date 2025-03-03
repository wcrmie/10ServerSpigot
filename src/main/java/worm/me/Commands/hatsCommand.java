package worm.me.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import worm.me.PlayerSystems.Social.Hats.Hats;
import worm.me.PlayerSystems.Social.Hats.HatsList;
import worm.me.server;

import java.util.List;

public class hatsCommand implements CommandExecutor {

    private final server plugin;

    public hatsCommand(server plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player p){

            if (args.length > 1) {
                try {
                    int i = Integer.parseInt(args[1]);
                    List<ItemStack> hats = HatsList.hats;
                    if (i > hats.size()) {
                        plugin.pluginMessage(ChatColor.RED + "That hat doesn't exist.", p);
                        return true;
                    }

                    else {
                        p.getInventory().setHelmet(hats.get(i));
                        plugin.pluginMessage(ChatColor.GREEN + "Hat set!", p);
                        return true;
                    }


                } catch (NumberFormatException e) {
                    Hats hats = new Hats();
                    hats.openHatsGUI(p);
                    return true;
                }
            } else {
                Hats hats = new Hats();
                hats.openHatsGUI(p);
                return true;
            }
        } else {
            sender.sendMessage("are we fr?");
            return false;
        }
    }
}
