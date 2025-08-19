package worm.me.playersystems.social;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import worm.me.Server;
import worm.me.discord.bot.BotInteractions;
import worm.me.utils.chat.Message;
import worm.me.utils.chat.MessageType;

import java.util.*;

public class MessageHead {

    private final Server plugin;

    public MessageHead(Server plugin) {
        this.plugin = plugin;
    }

    public static final Map<Player, Queue<String>> map = new HashMap<>();

    public static final Map<Player, ArmorStand> playerArmorStand = new HashMap<>();

    public static final Map<ArmorStand, ArmorStand> newLines = new HashMap<>();

    public static final NamespacedKey armorStandExists = new NamespacedKey("10server", "armorstandexists");

    public static final NamespacedKey voice = new NamespacedKey("10server", "voice");

    public static final NamespacedKey voicePitch = new NamespacedKey("10server", "pitch");

    public static final NamespacedKey msgLength = new NamespacedKey("10server", "msglength");

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
        playerArmorStand.put(p, chat);
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
                PlayerReputation playerReputation = new PlayerReputation();
                char character = m.charAt(finalI[0]);
                chatMessage[0] = chatMessage[0] + character;
                chat.setCustomName(ChatColor.of(playerReputation.calcRep(p)) + chatMessage[0]);
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
                if (charCounter[0] >= 32 && ("" + character).equals(" ")){
                    messageNewLine(chat, world);
                    charCounter[0] = 0;
                    chatMessage[0] = "";
                } else if (charCounter[0] >= 50) {
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

    public void messageQueue(Player sender, String msg){

        // Create temp queue
        Queue<String> messageQ = new LinkedList<>();
        // If map doesn't have anything, add it
        if (map.get(sender) != null) {
            messageQ = map.get(sender);
        }
        messageQ.offer(msg);
        map.put(sender, messageQ);

        for (Player player : Bukkit.getOnlinePlayers()) {
            double distance = player.getLocation().distance(sender.getLocation());

            int color = distance == 0 ? 255 : (int) Math.round(255 - 255 * (distance / (player.getWorld().getWorldBorder().getSize() + 50)));
            if (color < 50) color = 50;
            String hexColor = String.format("#%02x%02x%02x", color, color, color);

            String message = msg;
            if (message.contains(player.getName())) {
                message = message.replace(player.getName(), ChatColor.YELLOW + "@" + player.getName() + ChatColor.RESET);
                player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
            }

            net.kyori.adventure.text.TextComponent component = Component.text(sender.getName() + ": " + message).color(TextColor.fromHexString(hexColor));

            if (message.contains("[i]") || message.contains("[item]")) {

                ItemStack item = sender.getInventory().getItemInMainHand();

                String itemName = item.getType().toString();

                if (itemName.isEmpty()) {
                    itemName = item.getType().toString();
                }

                message = message.replaceAll("\\[i]", ChatColor.AQUA + "[" + itemName + " x" + item.getAmount() + "]" + ChatColor.RESET).replaceAll("\\[item]", ChatColor.AQUA + "[" + itemName + " x" + item.getAmount() + "]" + ChatColor.RESET);

                component = Component.text(sender.getName() + ": " + message).color(TextColor.fromHexString(hexColor));

                component = component.hoverEvent(item.asHoverEvent());

            }

            player.sendMessage(component);

        }

        if (msg.toLowerCase().contains("hack") || msg.toLowerCase().contains("cheat") || msg.toLowerCase().contains("hax")) {
            sender.sendMessage(Message.create(MessageType.GENERIC, "If you think someone is hacking, please report them using /report <player>."));
            sender.playSound(sender, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
        }

        if (msg.toLowerCase().contains("how") && (msg.toLowerCase().contains("stone") || msg.toLowerCase().contains("tool") || msg.toLowerCase().contains("pick") || msg.toLowerCase().contains("axe") || msg.toLowerCase().contains("shovel") || msg.toLowerCase().contains("sword"))) {
            sender.sendMessage(Message.create(MessageType.GENERIC, "Hover your tools in your inventory to see how to upgrade them!"));
            sender.playSound(sender, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
        }

        BotInteractions botInteractions = new BotInteractions();

        msg = msg.replace("@", "◘");
        botInteractions.gameMessage(sender.getName() + ": " + msg);
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
