package worm.me.commands.admin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import worm.me.gamesystems.border.WorldBorderSizeHandler;
import worm.me.utils.chat.Message;
import worm.me.utils.chat.MessageType;

public class BlockCountCommand implements CommandExecutor {
    private final WorldBorderSizeHandler borderHandler;

    /**
     * Constructor for the BlockCountCommand
     * @param borderHandler The world border handler to interact with
     */
    public BlockCountCommand(WorldBorderSizeHandler borderHandler) {
        this.borderHandler = borderHandler;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            displayCurrentInfo(sender);
            return true;
        }

        String blockCountStr = args[0];

        if (!isValidNumber(blockCountStr)) {
            sender.sendMessage(Message.create(MessageType.ERROR, "Invalid number format! Please provide a valid number."));
            return false;
        }

        int newBlockCount = Integer.parseInt(blockCountStr);
        if (newBlockCount < 0) {
            sender.sendMessage(Message.create(MessageType.ERROR, "Block count cannot be negative!"));
            return false;
        }

        borderHandler.setBlockCount(newBlockCount);
        sender.sendMessage(Message.create(MessageType.SUCCESS, "Block count set to: " + newBlockCount));
        return true;
    }

    private void displayCurrentInfo(CommandSender sender) {
        long blockCount = borderHandler.getBlockCount();
        double worldBorderSize = borderHandler.getWorldBorderSize();

        sender.sendMessage(Message.create(MessageType.GENERIC, "Current block count: " + ChatColor.GOLD + blockCount));
        sender.sendMessage(Message.create(MessageType.GENERIC, "Current world border size: " + ChatColor.GOLD + worldBorderSize));
    }

    private boolean isValidNumber(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (i == 0 && c == '-') {
                if (str.length() == 1) {
                    return false;
                }
            } else if (!Character.isDigit(c)) {
                return false;
            }
        }

        return true;
    }
}