package worm.me.utils.staff;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import worm.me.Server;
import worm.me.playersystems.social.MessageHead;
import worm.me.utils.chat.Message;
import worm.me.utils.chat.MessageType;
import worm.me.utils.managers.ConfigManager;
import worm.me.utils.managers.WebhookSender;

import java.util.logging.Logger;

public class ChatModeration {

    public static void setChatLimit(Player player, int limit) {

        if (getChatLimit(player) == 0) {
            player.getPersistentDataContainer().set(new NamespacedKey(Server.getPlugin(), "msglength"), PersistentDataType.INTEGER, limit);
            player.sendMessage(Message.create(MessageType.GENERIC, "You have been unmuted."));
            return;
        }

        player.getPersistentDataContainer().set(new NamespacedKey(Server.getPlugin(), "msglength"), PersistentDataType.INTEGER, limit);

        if (limit == 0) {
            return;

        }

        player.sendMessage(Message.create(MessageType.GENERIC, "Your character limit for messages has been set to " + limit));

    }

    public static void mute(Player player) {
        setChatLimit(player, 0);
        player.sendMessage(Message.create(MessageType.FAIL, "You have been muted. An appeal for this can be made by opening a ticket in Discord."));
    }

    public static void unmute(Player player) {
        setChatLimit(player, 192);
        player.sendMessage(Message.create(MessageType.SUCCESS, "You have been unmuted."));
    }

    public static int getChatLimit(Player player) {
        return player.getPersistentDataContainer().getOrDefault(new NamespacedKey(Server.getPlugin(), "msglength"), PersistentDataType.INTEGER, 192);
    }

    public static String filteredMessage(String message) {

        for (String blacklistedWord : Server.getConfigManager().getFilteredWords()) {
            message = message.replaceAll(blacklistedWord, "*".repeat(blacklistedWord.length()));
        }

        return message;
    }

    public static boolean blacklistFlagged(String message) {

        boolean filtered = false;

        ConfigManager config = Server.getConfigManager();

        for (String blacklistedWord : config.getBlacklistedWords()) {
            if (checkMessage(message).contains(blacklistedWord.toLowerCase())) {

                filtered = true;
                break;
            }
        }

        return filtered;
    }

    public static String checkMessage(String message) {

        return message.toLowerCase().replaceAll("1", "i").replaceAll("3", "e").replaceAll("4", "a").replaceAll("5", "s").replaceAll("8", "b").replaceAll("!", "i").replaceAll("9", "g").replaceAll("0", "o"); // Return the filtered message
    }

    public static void notifyStaff(String message, Player player) {

        String notif = Message.create(MessageType.ERROR, "Staff Notification: " + player.getName() + " was automatically muted for typing this: " + message);

        for (Player staff : player.getServer().getOnlinePlayers()) {
            if (staff.hasPermission("10VS.filter.notify")) {
                staff.sendMessage(notif);
                staff.playSound(staff, Sound.BLOCK_NOTE_BLOCK_BASEDRUM, 1.0f, 1.0f);
            }
        }

        Logger logger = Server.getPlugin().getLogger();

        WebhookSender.sendMessage(Server.getConfigManager().getModeratorWebhook(), ChatColor.stripColor(notif), logger);

    }

}
