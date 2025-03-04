package worm.me.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class RulesCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        sender.sendMessage(ChatColor.UNDERLINE + "" + ChatColor.RED + "RULES" + ChatColor.RESET + ChatColor.RED + "\n1. No Hacking\n2. Don't be excessively toxic\n3. No racism/bigotry\n4. DON'T SPAM! WE CAN SEE YOUR MESSAGES!\n5. Don't advertise your Server; nobody here wants to join it anyway");
        return true;
    }
}
