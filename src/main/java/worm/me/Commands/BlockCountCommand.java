package worm.me.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BlockCountCommand implements CommandExecutor {

    private final worm.me.WorldBorder.worldBorderSizeHandler worldBorderSizeHandler;

    public BlockCountCommand(worm.me.WorldBorder.worldBorderSizeHandler worldBorderSizeHandler) {
        this.worldBorderSizeHandler = worldBorderSizeHandler;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            try {
                int newBlockCount = Integer.parseInt(args[0]);
                if (newBlockCount < 0) {
                    sender.sendMessage(ChatColor.RED + "Block count cannot be negative!");
                    return false;
                }

                worldBorderSizeHandler.setBlockCount(newBlockCount);
                sender.sendMessage(ChatColor.GREEN + "Block count set to: " + ChatColor.GOLD + newBlockCount);
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "Invalid number format! Please provide a valid number.");
                return false;
            }
        } else {
            long blockCount = worldBorderSizeHandler.getBlockCount();
            double worldBorderSize = worldBorderSizeHandler.getWorldBorderSize();

            sender.sendMessage(ChatColor.GREEN + "Current block count: " + ChatColor.GOLD + blockCount);
            sender.sendMessage(ChatColor.GREEN + "Current world border size: " + ChatColor.GOLD + worldBorderSize);
        }

        return true;
    }
}