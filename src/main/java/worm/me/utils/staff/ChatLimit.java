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

        if (getChatLimit(player) == 0) {
            player.getPersistentDataContainer().set(MessageHead.msgLength, PersistentDataType.INTEGER, limit);
            return;
        }

        player.getPersistentDataContainer().set(MessageHead.msgLength, PersistentDataType.INTEGER, limit);

        if (limit == 0) {
            return;

        }

        player.sendMessage(Message.create(MessageType.GENERIC, "Your character limit for messages has been set to " + limit));

    }

    public static void mute(Player player) {
        setChatLimit(player, 0);
        player.sendMessage(Message.create(MessageType.FAIL, "You have been muted."));
    }

    public static void unmute(Player player) {
        setChatLimit(player, 192);
        player.sendMessage(Message.create(MessageType.SUCCESS, "You have been unmuted."));
    }

    public static int getChatLimit(Player player) {
        return player.getPersistentDataContainer().getOrDefault(MessageHead.msgLength, PersistentDataType.INTEGER, 192);
    }

}
