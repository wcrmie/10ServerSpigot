package worm.me.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class HatCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {

        if (commandSender instanceof Player p) {

            ItemStack itemInHand = p.getInventory().getItemInMainHand();
            ItemStack currentHelmet = p.getInventory().getHelmet();

            p.getInventory().setHelmet(itemInHand);
            p.getInventory().setItemInMainHand(currentHelmet);

            return true;
        }

        return false;
    }
}
