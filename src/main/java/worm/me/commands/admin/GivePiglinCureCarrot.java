package worm.me.commands.admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import worm.me.gamesystems.world.mob.PiglinTraders.PiglinCure;

public class GivePiglinCureCarrot implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage(">:(");
            return true;
        }

        if (!player.hasPermission("worm.admin")) {
            player.sendMessage("<red>no permission >:(");
            return true;
        }

        int amount = 1;
        if (args.length > 0) {
            try {
                amount = Integer.parseInt(args[0]);
                amount = Math.max(1, Math.min(64, amount));
            } catch (NumberFormatException e) {
                player.sendMessage("<red>Usage: /givecurecarrot [amount]");
                return true;
            }
        }

        ItemStack carrot = PiglinCure.createCureCarrot();
        carrot.setAmount(amount);

        player.getInventory().addItem(carrot);
        return true;
    }
}