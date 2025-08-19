package worm.me.utils.staff;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import worm.me.playersystems.social.MessageHead;
import worm.me.utils.chat.Message;
import worm.me.utils.chat.MessageType;
import worm.me.utils.managers.ConfigManager;

public class ChatModeration {

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

    public static boolean filterFlagged(String message) {

        boolean filtered = false;

        for (String blacklistedWord : ConfigManager.getBlacklistedWords()) {
            if (filterMessage(message).contains(blacklistedWord.toLowerCase())) {

                filtered = true;
                break;
            }
        }

        return filtered;
    }

    public static String filterMessage(String message) {

        return message.toLowerCase().replaceAll("1", "i").replaceAll("3", "e").replaceAll("4", "a").replaceAll("5", "s").replaceAll("8", "b").replaceAll("!", "i").replaceAll("9", "g"); // Return the filtered message
    }

    public static void notifyStaff(String message, Player player) {

        for (Player staff : player.getServer().getOnlinePlayers()) {
            if (staff.hasPermission("10VS.filter.notify")) {
                staff.sendMessage(Message.create(MessageType.GENERIC, "Staff Notification: " + player.getName() + " was automatically muted for typing this: " + message));
                staff.playSound(staff, Sound.BLOCK_NOTE_BLOCK_BASEDRUM, 1.0f, 1.0f);
            }
        }

    }

}
