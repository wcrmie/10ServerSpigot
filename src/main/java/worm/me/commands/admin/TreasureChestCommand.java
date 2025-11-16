package worm.me.commands.admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import worm.me.gamesystems.world.structure.TreasureChests;
import worm.me.utils.chat.Message;
import worm.me.utils.chat.MessageType;

public class TreasureChestCommand implements CommandExecutor {

    private final TreasureChests treasureChests;

    public TreasureChestCommand(TreasureChests treasureChests) {
        this.treasureChests = treasureChests;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        treasureChests.chestSpawn();

        sender.sendMessage(Message.create(MessageType.SUCCESS, "Treasure chest spawned!"));

        return true;
    }
}
