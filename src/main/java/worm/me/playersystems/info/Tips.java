package worm.me.playersystems.info;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import worm.me.Server;
import worm.me.utils.chat.Message;
import worm.me.utils.chat.MessageType;

import java.util.concurrent.ThreadLocalRandom;

public class Tips {

    public String getTip(int i){
        return switch (i) {
            case 1 -> "View the rules with /rules!";
            case 2 -> "Endermen may sometimes drop emeralds.";
            case 3 -> "You can use /recipes to see the custom recipes!";
            case 4 -> "Hover over your tools to see how to upgrade them!";
            case 5 -> "This server uses SimpleVoiceChat. Download it here: https://modrinth.com/plugin/simple-voice-chat/";
            case 6 -> "Blast furnaces may yield more material than regular furnaces.";
            case 7 -> "Most things on the server can be accessed from /menu";
            default -> "Join our discord with /discord";
        };
    }

    public int createTip() {
        return ThreadLocalRandom.current().nextInt(0,7);
    }

    public void sendTip(int i, Player p){
        String tip = getTip(i);
        p.sendMessage(Message.create(MessageType.GENERIC, tip));
    }

    public void beginTipSending(Player p){

        new BukkitRunnable() {
            @Override
            public void run() {

                if (!p.isOnline()) {
                    this.cancel();
                    return;
                }

                sendTip(createTip(), p);

            }
        }.runTaskTimer(Server.getPlugin(), 6000, 6000);

    }


}
