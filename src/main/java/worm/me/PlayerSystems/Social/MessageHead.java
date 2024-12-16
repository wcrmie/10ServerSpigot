package worm.me.PlayerSystems;

import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import worm.me.server;

import java.util.*;

public class MessageHead {

    private final server plugin;

    public MessageHead(server plugin) {
        this.plugin = plugin;
    }

    public static final Map<Player, Queue<String>> map = new HashMap<>();

    public static final Map<Player, ArmorStand> playerArmorStand = new HashMap<>();

    public static final Map<ArmorStand, ArmorStand> newLines = new HashMap<>();

    public final NamespacedKey armorStandExists = new NamespacedKey("10server", "armorstandexists");

    public final NamespacedKey voice = new NamespacedKey("10server", "voice");

    public final NamespacedKey voicePitch = new NamespacedKey("10server", "pitch");

    public void messageDisplay(Player p, String m) {

        PersistentDataContainer container = p.getPersistentDataContainer();

        if (Boolean.TRUE.equals(container.get(armorStandExists, PersistentDataType.BOOLEAN))) {
            return;
        }

        // Set variables to be used
        Location loc = p.getLocation();
        World world = p.getWorld();
        ArmorStand chat = world.spawn(loc.add(0, 2, 0), ArmorStand.class);

        // Associate a specific armor stand with the player
        map.get(p).remove();
        this.playerArmorStand.put(p, chat);
        container.set(armorStandExists, PersistentDataType.BOOLEAN, true);

        // Set armor stand properties
        chat.setVisible(false);
        chat.setGravity(false);

        // Final shenanigans to make it work in the runnable
        final String[] chatMessage = {""};
        final int[] finalI = new int[1];
        final int[] charCounter = new int[1];

        // Repeating task instead of a loop, so I can add delay
        new BukkitRunnable() {
            @Override
            public void run() {

                // Display message character by character
                char character = m.charAt(finalI[0]);
                chatMessage[0] = chatMessage[0] + character;
                chat.setCustomName(chatMessage[0]);
                chat.setCustomNameVisible(true);
                chat.setInvulnerable(true);
                chat.setMarker(true);

                // Play sound as if player has a voice
                Location newLoc = p.getLocation().add(0, 2, 0);
                if (!("" + character).equals(" ")) world.playSound(newLoc, VoiceTranslate(getVoice(p)), 0.5F, getVoicePitch(p));

                // Teleport armor stand to player
                messageTeleport(p);

                // When loop is finished
                if (finalI[0] + 1 >= m.length()) {

                    // Get rid of display
                    messageFade(chat, p);
                    cancel();
                }

                // New line
                if (charCounter[0] >= 47 && ("" + character).equals(" ")){
                    messageNewLine(chat, world);
                    charCounter[0] = 0;
                    chatMessage[0] = "";
                }
                charCounter[0]++;
                finalI[0]++;
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    public void messageNewLine(ArmorStand a, World world){
        if (!messageHasLines(a)){
            ArmorStand newLine = world.spawn(a.getLocation(), ArmorStand.class);
            newLine.setVisible(false);
            newLine.setGravity(false);
            newLine.setCustomName(a.getCustomName());
            newLine.setCustomNameVisible(true);
            newLine.setInvulnerable(true);
            newLine.setMarker(true);
            newLines.put(a, newLine);
        } else {
            messageNewLine(newLines.get(a), world);
            newLines.get(a).setCustomName(a.getCustomName());
        }

    }

    public void messageNewLineTp(ArmorStand a, ArmorStand nl){
        Location loc = a.getLocation().add(0, 0.3, 0);
        nl.teleport(loc);
        if (messageHasLines(nl)) {
            messageNewLineTp(nl, newLines.get(nl));
        }
    }

    public void lineDel(ArmorStand a){
        ArmorStand nl = newLines.get(a);
        if (messageHasLines(nl)) {
            lineDel(nl);
        }
        if (nl != null) nl.remove();
    }

    public boolean messageHasLines(ArmorStand a){
        return newLines.get(a) != null;
    }

    public void messageQueue(Player p, String m){

        // Create temp queue
        Queue<String> messageQ = new LinkedList<>();
        // If map doesn't have anything, add it
        if (map.get(p) != null) {
            messageQ = map.get(p);
        }
        messageQ.offer(m);
        map.put(p, messageQ);
    }

    public void messageTeleport(Player p) {
        if (!(map.get(p) == null)){
            ArmorStand a = playerArmorStand.get(p);
            a.teleport(p.getLocation().add(0, 2, 0));
            if (messageHasLines(a)) {
                messageNewLineTp(a, newLines.get(a));
            }
        }
    }

    public void messageFade(ArmorStand a, Player p){
        BukkitTask balls = new BukkitRunnable(){
            @Override
            public void run(){
                messageTeleport(p);
            }
        }.runTaskTimer(plugin, 1, 1);
        new BukkitRunnable(){
            @Override
            public void run(){
                balls.cancel();
                lineDel(a);
                a.remove();
                playerArmorStand.remove(p);
                PersistentDataContainer container = p.getPersistentDataContainer();
                container.set(armorStandExists, PersistentDataType.BOOLEAN, false);
                if (map.get(p).peek() != null) messageDisplay(p, map.get(p).peek());
            }
        }.runTaskLater(plugin, 60L);
    }

    public int getVoice(Player p){
        PersistentDataContainer container = p.getPersistentDataContainer();
        return Objects.requireNonNullElse(container.get(voice, PersistentDataType.INTEGER), 0);
    }

    public float getVoicePitch(Player p){
        PersistentDataContainer container = p.getPersistentDataContainer();
        return Objects.requireNonNullElse(container.get(voicePitch, PersistentDataType.FLOAT), 1).floatValue();
    }

    public void setVoice(Player p, Integer v){
        PersistentDataContainer container = p.getPersistentDataContainer();
        container.set(voice, PersistentDataType.INTEGER, v);
    }

    public void setVoicePitch(Player p, float v){
        PersistentDataContainer container = p.getPersistentDataContainer();
        container.set(voicePitch, PersistentDataType.FLOAT, v);
    }

    public Sound VoiceTranslate(Integer v){
        return plugin.sounds.get(v);
    }
}
