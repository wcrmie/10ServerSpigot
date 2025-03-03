package worm.me.PlayerSystems.Levels;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import worm.me.server;

public class Level {

    private final server plugin;

    public Level(server plugin) {
        this.plugin = plugin;
    }

    // Sets up the ability to create player levels
    public final NamespacedKey combat = new NamespacedKey("10plugin", "combat");

    public final NamespacedKey farming = new NamespacedKey("10plugin", "farming");

    public final NamespacedKey survival = new NamespacedKey("10plugin", "survival");

    public final NamespacedKey crafting = new NamespacedKey("10plugin", "crafting");

    public final NamespacedKey fishing = new NamespacedKey("10plugin", "fishing");

    public final NamespacedKey smithing = new NamespacedKey("10plugin", "smithing");

    public final NamespacedKey mining = new NamespacedKey("10plugin", "mining");

    public final NamespacedKey agility = new NamespacedKey("10plugin", "agility");

    public final NamespacedKey foraging = new NamespacedKey("10plugin", "foraging");

    public final NamespacedKey foragingLevel = new NamespacedKey("10plugin", "foraginglevel");

    public final NamespacedKey agilityLevel = new NamespacedKey("10plugin", "agilitylevel");

    public final NamespacedKey miningLevel = new NamespacedKey("10plugin", "mininglevel");

    public final NamespacedKey smithingLevel = new NamespacedKey("10plugin", "smithinglevel");

    public final NamespacedKey fishingLevel = new NamespacedKey("10plugin", "fishinglevel");

    public final NamespacedKey craftingLevel = new NamespacedKey("10plugin", "craftinglevel");

    public final NamespacedKey survivalLevel = new NamespacedKey("10plugin", "survivallevel");

    public final NamespacedKey farmingLevel = new NamespacedKey("10plugin", "farminglevel");

    public final NamespacedKey combatLevel = new NamespacedKey("10plugin", "combatlevel");

    // Sets base experience requirement
    public final int baseExp = 30;

    // Generates the required experience for the next level
    public double expRequired(int level){
        if (level != 1) {
            return (10 * Math.pow(level, 2)) + 50;
        }
        else return baseExp;
    }

    // Creates a public exp-adding method
    public void addExp(int exp, NamespacedKey skill, Player player){
        PersistentDataContainer container = player.getPersistentDataContainer();
        if (container.get(skill, PersistentDataType.INTEGER) != null){
            int currentExp = getExp(skill, player);
            container.set(skill, PersistentDataType.INTEGER, currentExp + exp);

            if (getExp(skill, player) >= expRequired(container.get(plugin.expToLevel.get(skill), PersistentDataType.INTEGER)) + 1) {
                levelUp(plugin.expToLevel.get(skill), player);
            }
        } else {
            container.set(skill, PersistentDataType.INTEGER, exp);
        }
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy(ChatColor.AQUA + "+ " + exp + " " + skill.getKey() + " exp"));
    }

    public int getExp(NamespacedKey skill, Player player){
        PersistentDataContainer container = player.getPersistentDataContainer();
        return container.get(skill, PersistentDataType.INTEGER);
    }

    public void setExp(NamespacedKey skill, Player player, Integer exp){
        PersistentDataContainer container = player.getPersistentDataContainer();
        container.set(skill, PersistentDataType.INTEGER, exp);
    }

    public void levelUp(NamespacedKey skill, Player player ){
        PersistentDataContainer container = player.getPersistentDataContainer();
        int level = getLevel(skill, player) + 1;
        setLevel(skill, player, level);
        plugin.broadcast(ChatColor.AQUA + "" + player.getName() + " has reached level " + level + " in " + skill.getKey().replace("level", "").toUpperCase() + "!");
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1F, 0.8F);
    }

    public int getLevel(NamespacedKey skill, Player player){
        return player.getPersistentDataContainer().get(skill,PersistentDataType.INTEGER);
    }

    public void setLevel(NamespacedKey skill, Player player, Integer level){
        player.getPersistentDataContainer().set(skill, PersistentDataType.INTEGER, level);
    }

    public void initLevel(Player p) {
        PersistentDataContainer c = p.getPersistentDataContainer();
        c.set(combat, PersistentDataType.INTEGER, 0);
        c.set(mining, PersistentDataType.INTEGER, 0);
        c.set(fishing, PersistentDataType.INTEGER, 0);
        c.set(farming, PersistentDataType.INTEGER, 0);
        c.set(crafting, PersistentDataType.INTEGER, 0);
        c.set(smithing, PersistentDataType.INTEGER, 0);
        c.set(survival, PersistentDataType.INTEGER, 0);
        c.set(agility, PersistentDataType.INTEGER, 0);
        c.set(foraging, PersistentDataType.INTEGER, 0);
        c.set(foragingLevel, PersistentDataType.INTEGER, 0);
        c.set(agilityLevel, PersistentDataType.INTEGER, 0);
        c.set(combatLevel, PersistentDataType.INTEGER, 0);
        c.set(miningLevel, PersistentDataType.INTEGER, 0);
        c.set(fishingLevel, PersistentDataType.INTEGER, 0);
        c.set(farmingLevel, PersistentDataType.INTEGER, 0);
        c.set(craftingLevel, PersistentDataType.INTEGER, 0);
        c.set(smithingLevel, PersistentDataType.INTEGER, 0);
        c.set(survivalLevel, PersistentDataType.INTEGER, 0);
    }

}
