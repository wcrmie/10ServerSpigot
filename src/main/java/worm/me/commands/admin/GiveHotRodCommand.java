package worm.me.commands.admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import worm.me.gamesystems.fishing.rods.HotRod;
import worm.me.utils.Item;
import worm.me.utils.chat.Message;
import worm.me.utils.chat.MessageType;

public class GiveHotRodCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {

        if (sender instanceof Player player && player.isOp()) {

            Item.giveOrDrop(HotRod.createItem(), player);

            return true;
        }

        sender.sendMessage(Message.create(MessageType.ERROR, "You do not have permission to use this command."));

        return false;
    }
}
