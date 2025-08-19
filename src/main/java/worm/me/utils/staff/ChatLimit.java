package worm.me.utils.staff;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import worm.me.Server;
import worm.me.playersystems.social.MessageHead;
import worm.me.utils.chat.Message;
import worm.me.utils.chat.MessageType;

public class ChatLimit {

    public static void setChatLimit(Player player, int limit) {

        if (player.getPersistentDataContainer().getOrDefault(MessageHead.msgLength, PersistentDataType.INTEGER, 192) == 0) {
            player.getPersistentDataContainer().set(MessageHead.msgLength, PersistentDataType.INTEGER, limit);
            player.sendMessage(Message.create(MessageType.SUCCESS, "You have been unmuted."));
            return;
        }

        player.getPersistentDataContainer().set(MessageHead.msgLength, PersistentDataType.INTEGER, limit);

        if (limit == 0) {

            player.sendMessage(Message.create(MessageType.FAIL, "You have been muted."));
            return;

        }

        player.sendMessage(Message.create(MessageType.GENERIC, "Your character limit for messages has been set to " + limit));

    }

}
