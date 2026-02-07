package worm.me.commands.gui;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import worm.me.data.PlayerProfileGUI;
import worm.me.utils.chat.Message;
import worm.me.utils.chat.MessageType;

public class OpenProfileGUICommand implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player p){
            // Open the profile GUI

            PlayerProfileGUI playerProfileGUI = new PlayerProfileGUI();

            if (args.length > 0){
                try {
                    playerProfileGUI.openProfile(p.getServer().getPlayer(args[0]), p);
                } catch (NullPointerException e){
                    p.sendMessage(Message.create(MessageType.ERROR, "Player not found!"));
                    return false;
                }
                return true;
            } else playerProfileGUI.openProfile(p, p);
            return true;

        } else sender.sendMessage("This command is for the player, silly!");

        return false;
    }
}
