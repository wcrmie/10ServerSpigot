package worm.me.commands.admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import worm.me.gamesystems.world.mob.PiglinTraders.PiglinTradeManager;
import worm.me.utils.Item;

public class GivePiglinTomeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (commandSender instanceof Player player && player.isOp()) {
            Item.giveOrDrop(PiglinTradeManager.generateRandomTome(), player);
        }
        return false;
    }
}