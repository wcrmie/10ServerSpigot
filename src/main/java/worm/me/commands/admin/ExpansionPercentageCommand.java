package worm.me.commands.admin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import worm.me.gamesystems.world.border.WorldBorderSizeHandler;
import worm.me.utils.chat.Message;
import worm.me.utils.chat.MessageType;

public class ExpansionPercentageCommand implements CommandExecutor {

    private final WorldBorderSizeHandler worldBorderSizeHandler;

    public ExpansionPercentageCommand(WorldBorderSizeHandler worldBorderSizeHandler) {
        this.worldBorderSizeHandler = worldBorderSizeHandler;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            try {
                double newPercentage = Double.parseDouble(args[0]);

                if (newPercentage <= 0) {
                    sender.sendMessage(Message.create(MessageType.ERROR, "Percentage must be greater than 0!"));
                    return false;
                }

                worldBorderSizeHandler.setExpansionPercentage(newPercentage);
                sender.sendMessage(Message.create(MessageType.GENERIC, "Expansion percentage set to: " +
                        ChatColor.GOLD + newPercentage + "%" +
                        ChatColor.YELLOW + " of world blocks needed for border expansion." +
                        ChatColor.GOLD + " (" + Math.round(worldBorderSizeHandler.calculateWorldBlockSize() * (newPercentage / 100.0)) + ")"));

                // Show current stats after change
                long blockCount = worldBorderSizeHandler.getBlockCount();
                double worldBorderSize = worldBorderSizeHandler.getWorldBorderSize();
                sender.sendMessage(Message.create(MessageType.GENERIC, "Current block count: " + ChatColor.GOLD + blockCount));
                sender.sendMessage(Message.create(MessageType.GENERIC, "Current world border size: " + ChatColor.GOLD + worldBorderSize));

            } catch (NumberFormatException e) {
                sender.sendMessage(Message.create(MessageType.ERROR, "Invalid number format! Please provide a valid percentage."));
                return false;
            } catch (IllegalArgumentException e) {
                sender.sendMessage(Message.create(MessageType.ERROR, e.getMessage()));
                return false;
            }
        } else {
            // Display current percentage
            double currentPercentage = worldBorderSizeHandler.getExpansionPercentage();
            sender.sendMessage(Message.create(MessageType.GENERIC, ChatColor.YELLOW + "Current expansion percentage: " +
                    ChatColor.GOLD + currentPercentage + "%" +
                    ChatColor.YELLOW + " of world blocks needed for border expansion." + ChatColor.GOLD + " (" + Math.round(worldBorderSizeHandler.calculateWorldBlockSize() * (currentPercentage / 100.0)) + ")"));
        }

        return true;
    }
}
