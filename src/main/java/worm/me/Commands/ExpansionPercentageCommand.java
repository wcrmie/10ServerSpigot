package worm.me.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import worm.me.worldborderstuffs.worldBorderSizeClass;

public class ExpansionPercentageCommand implements CommandExecutor {

    private final worldBorderSizeClass worldBorderSizeHandler;

    public ExpansionPercentageCommand(worldBorderSizeClass worldBorderSizeHandler) {
        this.worldBorderSizeHandler = worldBorderSizeHandler;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            try {
                double newPercentage = Double.parseDouble(args[0]);

                if (newPercentage <= 0) {
                    sender.sendMessage(ChatColor.RED + "Percentage must be greater than 0!");
                    return false;
                }

                worldBorderSizeHandler.setExpansionPercentage(newPercentage);
                sender.sendMessage(ChatColor.GREEN + "Expansion percentage set to: " +
                        ChatColor.GOLD + newPercentage + "%" +
                        ChatColor.GREEN + " of world blocks needed for border expansion");

                // Show current stats after change
                long blockCount = worldBorderSizeHandler.getBlockCount();
                double worldBorderSize = worldBorderSizeHandler.getWorldBorderSize();
                sender.sendMessage(ChatColor.GREEN + "Current block count: " + ChatColor.GOLD + blockCount);
                sender.sendMessage(ChatColor.GREEN + "Current world border size: " + ChatColor.GOLD + worldBorderSize);

            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "Invalid number format! Please provide a valid percentage.");
                return false;
            } catch (IllegalArgumentException e) {
                sender.sendMessage(ChatColor.RED + e.getMessage());
                return false;
            }
        } else {
            // Display current percentage
            double currentPercentage = worldBorderSizeHandler.getExpansionPercentage();
            sender.sendMessage(ChatColor.GREEN + "Current expansion percentage: " +
                    ChatColor.GOLD + currentPercentage + "%" +
                    ChatColor.GREEN + " of world blocks needed for border expansion");
        }

        return true;
    }
}
