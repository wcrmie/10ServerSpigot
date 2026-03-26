package worm.me.commands.admin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import worm.me.gamesystems.world.mob.PiglinTraders.PiglinTradeManager;
import worm.me.gamesystems.world.mob.PiglinTraders.PiglinTrades;
import worm.me.utils.Item;

import java.util.Arrays;
import java.util.stream.Collectors;

public class GivePiglinTomeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command is for players only.");
            return true;
        }

        if (!player.isOp()) {
            player.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "Usage: /" + label + " <type>");
            sendAvailableTypes(player);
            return true;
        }

        String input = args[0].toUpperCase();

        try {
            PiglinTrades type = PiglinTrades.valueOf(input);

            if (!type.requiresTome()) {
                player.sendMessage(ChatColor.RED + "The type '" + input + "' cannot be given via tome.");
                return true;
            }

            ItemStack tome = PiglinTradeManager.generateTome(type);
            if (tome != null) {
                Item.giveOrDrop(tome, player);
                player.sendMessage(ChatColor.GREEN + "You have been given a " + ChatColor.GOLD + type.getName() + ChatColor.GREEN + " Tome.");
            }

        } catch (IllegalArgumentException e) {
            player.sendMessage(ChatColor.RED + "Unknown Piglin type: " + input);
            sendAvailableTypes(player);
        }

        return true;
    }

    private void sendAvailableTypes(Player player) {
        String available = Arrays.stream(PiglinTrades.values())
                .filter(PiglinTrades::requiresTome)
                .map(Enum::name)
                .collect(Collectors.joining(ChatColor.GRAY + ", " + ChatColor.WHITE));

        player.sendMessage(ChatColor.YELLOW + "Available types: " + ChatColor.WHITE + available);
    }
}