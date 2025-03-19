package worm.me.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import worm.me.playersystems.data.VoidPlayer;
import worm.me.Server;

public class LostToolsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player p){
            Inventory inv = p.getInventory();

            for (ItemStack item : VoidPlayer.getTools(p)){
                if (inv.firstEmpty() != -1){
                    if (!inv.contains(item)) inv.addItem(item);
                } else {
                    Server.pluginMessage(ChatColor.RED + "You have no space in your inventory to receive your tools!", p);
                    break;
                }
            } return true;

        }

        return false;
    }
}
