package worm.me.PlayerSystems.PlayerData.Levels;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import worm.me.Server;

import java.util.Arrays;
import java.util.List;

public class Levels {

    private final Server plugin = Server.getPlugin();

    // Sets up the ability to create player levels

    public static final List<String> skills = Arrays.asList(
            "Combat",
            "Farming",
            "Crafting",
            "Fishing",
            "Smithing",
            "Mining",
            "Agility",
            "Foraging"
    );

    public static final NamespacedKey Levels = new NamespacedKey(Server.getPlugin(), "levels");

    public static final NamespacedKey Exp = new NamespacedKey(Server.getPlugin(), "exp");

    // Sets base experience requirement
    public static final int baseExp = 30;

    // Generates the required experience for the next level
    public static double expRequired(int level){
        if (level != 1) {
            return (10 * Math.pow(level, 2)) + 50;
        }
        else return baseExp;
    }

    // So that we can simply refer to the skill by name rather than memorizing its id
    public static int skillTranslation(String skill){
        return skills.indexOf(skill);
    }

    // Creates a public exp-adding method
    public static void addExp(long exp, int skill, Player player){

        PersistentDataContainer container = player.getPersistentDataContainer();

        // Call event
        GainExpEvent event = new GainExpEvent(player, exp, skill);
        Bukkit.getServer().getPluginManager().callEvent(event);

        long[] expArray = container.get(Exp, PersistentDataType.LONG_ARRAY);

        expArray[skill] += exp;

        container.set(Exp, PersistentDataType.LONG_ARRAY, expArray);

        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy(ChatColor.AQUA + "+ " + exp + " " + skills.get(skill) + " exp"));
    }

    public static long getExp(String skill, Player player){
        PersistentDataContainer container = player.getPersistentDataContainer();
        int skillIndex = skillTranslation(skill);

        return container.get(Exp, PersistentDataType.LONG_ARRAY)[skillIndex];
    }

    public static void levelUp(int skill, Player player ){
        PersistentDataContainer container = player.getPersistentDataContainer();

        int[] levels = container.get(Levels, PersistentDataType.INTEGER_ARRAY);
        int level = levels[skill];
        level++;

        LevelUpEvent event = new LevelUpEvent(skill, level, player);
        Bukkit.getServer().getPluginManager().callEvent(event);


        setLevel(skill, player, level);
        Server.getPlugin().broadcast(ChatColor.AQUA + player.getName() + " has reached level " + level + " in " + skills.get(skill) + "!");
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1F, 0.8F);
    }

    public static int getLevel(int skill, Player player){
        return player.getPersistentDataContainer().get(Levels, PersistentDataType.INTEGER_ARRAY)[skill];
    }

    public static void setLevel(int skill, Player player, Integer level){
        int[] levelArray = player.getPersistentDataContainer().get(Levels, PersistentDataType.INTEGER_ARRAY);
        levelArray[skill] = level;

        player.getPersistentDataContainer().set(Levels, PersistentDataType.INTEGER_ARRAY, levelArray);
    }

    public static void expHandler(Player p, Event event){

        if (event.getEventName().equals("BlockBreakEvent")) {

            BlockBreakEvent e = (BlockBreakEvent) event;

            if (Server.getPlugin().miningBlocks.containsKey(e.getBlock().getType())) {
                addExp(Server.getPlugin().miningBlocks.get(e.getBlock().getType()), skillTranslation("Mining"), p);
            } else if (Server.getPlugin().foragingBlocks.containsKey(e.getBlock().getType())) {
                addExp(Server.getPlugin().foragingBlocks.get(e.getBlock().getType()), skillTranslation("Foraging"), p);
            }
            
        } else if (event.getEventName().equals("EntityDeathEvent")) {

            EntityDeathEvent e = (EntityDeathEvent) event;

            addExp(Server.getPlugin().combatMobs.get(e.getEntityType()), skillTranslation("Combat"), p);

        }
    }

}
