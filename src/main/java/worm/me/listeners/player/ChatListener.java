package worm.me.listeners.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import worm.me.Server;
import worm.me.playersystems.social.MessageHead;
import worm.me.utils.chat.Message;
import worm.me.utils.chat.MessageType;
import worm.me.utils.staff.ChatModeration;

import static worm.me.utils.staff.ChatModeration.*;

public class ChatListener implements Listener {

    // --------------------------MESSAGEHEAD--------------------------

    @EventHandler
    public void onChat(PlayerChatEvent event){

        // For messages above head
        MessageHead messageHead = new MessageHead(Server.getPlugin());

        // initialize variables
        Player p = event.getPlayer();
        String m = event.getMessage();

        // message character limit

        int msgLength = ChatModeration.getChatLimit(p);

        if (msgLength == 0) {
            p.sendMessage(Message.create(MessageType.FAIL, "You are currently muted. You may appeal on discord by making a ticket."));
            event.setCancelled(true);
            return;
        }

        if (blacklistFlagged(m)) {
            mute(p);
            notifyStaff(m, p);
            event.setCancelled(true);
            return;
        }

        if (m.length() > msgLength){
            p.sendMessage(Message.create(MessageType.FAIL, String.format("Your messages must be %d characters or less.", msgLength)));
            event.setCancelled(true);
            return;
        }

        // queue messages to be sent above head
        messageHead.messageQueue(p, ChatModeration.filteredMessage(m));
        messageHead.messageDisplay(p, ChatModeration.filteredMessage(m));
        event.setCancelled(true);


    }

    // --------------------------END MESSAGEHEAD--------------------------

}
