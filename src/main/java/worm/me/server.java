package worm.me;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import net.skinsrestorer.api.SkinsRestorer;
import net.skinsrestorer.api.SkinsRestorerProvider;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import worm.me.Commands.*;
import worm.me.PlayerSystems.Level;

import java.util.*;

public final class server extends JavaPlugin {

    public SkinsRestorer skinsRestorer;

    public Map<Material, Integer> miningBlocks = new HashMap<>();

    public static  final Map<NamespacedKey, NamespacedKey> expToLevel = new HashMap<>();

    Level level = new Level(this);

    public List<Sound> sounds = new ArrayList<>();

    public List<Material> stones = new ArrayList<>();
    public List<Material> dirts = new ArrayList<>();
    public List<Material> planks = new ArrayList<>();
    public List<Material> miscs = new ArrayList<>();

    public List<String> quests = new ArrayList<>();

    @Override
    public void onEnable() {
        // Plugin startup logic

        // Allows ListenerClass to receive events.
        getServer().getPluginManager().registerEvents(new Listener(this), this);

        // Allows Command classes to register commands
        this.getCommand("getExp").setExecutor(new GetLevel(this));
        this.getCommand("setVoice").setExecutor(new setVoiceCommand(this));
        this.getCommand("setGens").setExecutor(new setGensCommand(this));
        this.getCommand("shout").setExecutor(new shoutCommand(this));
        this.getCommand("rules").setExecutor(new RulesCommand());
        this.getCommand("sit").setExecutor(new SitCommand());

        this.skinsRestorer = SkinsRestorerProvider.get();

        expToLevel.put(level.mining, level.miningLevel);
        expToLevel.put(level.combat, level.combatLevel);
        expToLevel.put(level.crafting, level.craftingLevel);
        expToLevel.put(level.farming, level.farmingLevel);
        expToLevel.put(level.fishing, level.fishingLevel);
        expToLevel.put(level.smithing, level.smithingLevel);
        expToLevel.put(level.survival, level.survivalLevel);

        // Set blocks and their given mining exp.
        miningBlocks.put(Material.COBBLESTONE, 1);
        miningBlocks.put(Material.STONE, 3);

        // Set voices
        sounds.add(Sound.BLOCK_STONE_BUTTON_CLICK_OFF);
        sounds.add(Sound.BLOCK_WOODEN_BUTTON_CLICK_OFF);
        sounds.add(Sound.BLOCK_DISPENSER_DISPENSE);
        sounds.add(Sound.BLOCK_STONE_BREAK);
        sounds.add(Sound.BLOCK_BONE_BLOCK_BREAK);
        sounds.add(Sound.ENTITY_ARMOR_STAND_HIT);
        sounds.add(Sound.BLOCK_WOOL_HIT);
        sounds.add(Sound.BLOCK_BAMBOO_HIT);
        sounds.add(Sound.BLOCK_AMETHYST_BLOCK_BREAK);
        sounds.add(Sound.ENTITY_ENDER_EYE_DEATH);
        sounds.add(Sound.ENTITY_PLAYER_BURP);
        sounds.add(Sound.ENTITY_CAT_AMBIENT);
        sounds.add(Sound.ENTITY_WOLF_AMBIENT);
        sounds.add(Sound.ENTITY_SHEEP_DEATH);
        sounds.add(Sound.ENTITY_BEE_HURT);
        sounds.add(Sound.ENTITY_ARMADILLO_BRUSH);
        sounds.add(Sound.ENTITY_CAMEL_DASH);
        sounds.add(Sound.ENTITY_VILLAGER_TRADE);
        sounds.add(Sound.ENTITY_VILLAGER_NO);
        sounds.add(Sound.ENTITY_CREEPER_HURT);
        sounds.add(Sound.ENTITY_ZOMBIE_HURT);
        sounds.add(Sound.ENTITY_SKELETON_HURT);
        sounds.add(Sound.ENTITY_ENDERMAN_HURT);
        sounds.add(Sound.ENTITY_ENDER_DRAGON_HURT);
        sounds.add(Sound.ENTITY_WITCH_THROW);

        // CREATE BLOCK KEYS FOR GENERATOR
        stones.add(Material.COBBLESTONE);
        stones.add(Material.TUFF);
        stones.add(Material.ANDESITE);
        stones.add(Material.GRANITE);
        stones.add(Material.DIORITE);
        stones.add(Material.DRIPSTONE_BLOCK);
        stones.add(Material.PACKED_MUD);
        stones.add(Material.COBBLED_DEEPSLATE);
        stones.add(Material.SMOOTH_BASALT);

        dirts.add(Material.DIRT);
        dirts.add(Material.COARSE_DIRT);
        dirts.add(Material.SAND);
        dirts.add(Material.GRAVEL);
        dirts.add(Material.RED_SAND);
        dirts.add(Material.CLAY);
        dirts.add(Material.PACKED_MUD);

        planks.add(Material.OAK_PLANKS);
        planks.add(Material.SPRUCE_PLANKS);
        planks.add(Material.DARK_OAK_PLANKS);
        planks.add(Material.BIRCH_PLANKS);
        planks.add(Material.JUNGLE_PLANKS);
        planks.add(Material.MANGROVE_PLANKS);
        planks.add(Material.ACACIA_PLANKS);

        miscs.add(Material.COAL);
        miscs.add(Material.QUARTZ);
        miscs.add(Material.REDSTONE);
        miscs.add(Material.GLOWSTONE_DUST);

        // CREATE QUESTS
        QuestInitialize questInitialize = new QuestInitialize(this);
        questInitialize.questInitialize();


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void broadcast(String message){
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage(message);
        }
    }

}
