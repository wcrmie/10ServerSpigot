package worm.me.playersystems.playerdata.levels;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.event.player.PlayerHarvestBlockEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import worm.me.Server;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author worm
 */
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

    /**
     * Adds experience to a player's skill
     * @param exp The amount of experience to add
     * @param skill The skill to add experience to
     * @param player The player to add experience to
     */
    public static void addExp(long exp, int skill, Player player){

        PersistentDataContainer container = player.getPersistentDataContainer();

        // Call event
        GainExpEvent event = new GainExpEvent(player, exp, skill);
        Bukkit.getServer().getPluginManager().callEvent(event);

        long[] expArray = container.get(Exp, PersistentDataType.LONG_ARRAY);

        expArray[skill] += exp;

        container.set(Exp, PersistentDataType.LONG_ARRAY, expArray);

        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy(ChatColor.AQUA + "+ " + exp + " " + skills.get(skill) + " EXP"));
        player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.3F, ThreadLocalRandom.current().nextFloat(1.6F, 1.8F));
    }

    public static long getExp(String skill, Player player){
        PersistentDataContainer container = player.getPersistentDataContainer();
        int skillIndex = skillTranslation(skill);

        return container.getOrDefault(Exp, PersistentDataType.LONG_ARRAY, new long[0])[skillIndex];
    }

    public static void levelUp(int skill, Player player){
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
        return player.getPersistentDataContainer().getOrDefault(Levels, PersistentDataType.INTEGER_ARRAY, new int[0])[skill];
    }

    public static void setLevel(int skill, Player player, Integer level){
        int[] levelArray = player.getPersistentDataContainer().get(Levels, PersistentDataType.INTEGER_ARRAY);
        levelArray[skill] = level;

        player.getPersistentDataContainer().set(Levels, PersistentDataType.INTEGER_ARRAY, levelArray);
    }

    public static void expHandler(Player p, Event e){

        if (e.getEventName().equals("BlockBreakEvent")) {

            BlockBreakEvent event = (BlockBreakEvent) e;

            if (Server.getPlugin().miningBlocks.containsKey(event.getBlock().getType())) {
                addExp(Server.getPlugin().miningBlocks.get(event.getBlock().getType()), skillTranslation("Mining"), p);



            } else if (Server.getPlugin().foragingBlocks.containsKey(event.getBlock().getType())) {
                addExp(Server.getPlugin().foragingBlocks.get(event.getBlock().getType()), skillTranslation("Foraging"), p);



            } else if (Server.getPlugin().farmingBlocks.containsKey(event.getBlock().getType())) {

                if (event.getBlock().getBlockData() instanceof Ageable crop) {
                    if (crop.getAge() != crop.getMaximumAge()) {
                        return;
                    }

                    addExp(Server.getPlugin().farmingBlocks.get(event.getBlock().getType()), skillTranslation("Farming"), p);

                } else {
                    addExp(Server.getPlugin().farmingBlocks.get(event.getBlock().getType()), skillTranslation("Farming"), p);
                }
            }




        } else if (e.getEventName().equals("EntityDeathEvent")) {

            EntityDeathEvent event = (EntityDeathEvent) e;

            if (Server.getPlugin().combatMobs.containsKey(event.getEntityType())) {

                addExp(Server.getPlugin().combatMobs.get(event.getEntityType()), skillTranslation("Combat"), p);
            }




        } else if (e.getEventName().equals("FurnaceExtractEvent")) {

            FurnaceExtractEvent event = (FurnaceExtractEvent) e;

            if (Server.getPlugin().smithingItems.containsKey(event.getItemType())) {

                addExp(Server.getPlugin().smithingItems.get(event.getItemType()) * event.getItemAmount(), skillTranslation("Smithing"), p);
            }




        } else if (e.getEventName().equals("PlayerHarvestEvent")) {

            PlayerHarvestBlockEvent event = (PlayerHarvestBlockEvent) e;

            if (Server.getPlugin().farmingBlocks.containsKey(event.getHarvestedBlock().getType())){
                addExp(Server.getPlugin().farmingBlocks.get(event.getHarvestedBlock().getType()), skillTranslation("Farming"), p);
            }




        } else if (e.getEventName().equals("PlayerFishEvent")) {


        }
    }

}
