package worm.me;

import net.skinsrestorer.api.SkinsRestorer;
import net.skinsrestorer.api.SkinsRestorerProvider;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import worm.me.Commands.*;
import worm.me.Commands.AdminCommands.*;
import worm.me.Commands.GUICommands.HatsCommand;
import worm.me.Commands.GUICommands.OpenAbilitiesGUICommand;
import worm.me.Commands.GUICommands.OpenLevelsGUICommand;
import worm.me.Commands.GUICommands.OpenSkinGUICommand;
import worm.me.GameSystems.Recipes;
import worm.me.GameSystems.ArtBlocks;
import worm.me.PlayerSystems.Abilities.Abilities;
import worm.me.PlayerSystems.Quests.QuestInitialize;
import worm.me.PlayerSystems.PlayerSpawning;
import worm.me.PlayerSystems.PlayerData.Social.Hats.HatsList;
import worm.me.PlayerSystems.SaddleRide;
import worm.me.WorldBorder.WorldBorderSizeHandler;

import java.util.*;

public final class Server extends JavaPlugin {

    private static Server plugin;

    public SkinsRestorer skinsRestorer;

    private WorldBorderSizeHandler WorldBorderSizeHandler;
    private PlayerSpawning playerspawning;
    private SaddleRide saddleRide;
    private ArtBlocks artBlocks;

    public Map<Material, Integer> miningBlocks = new HashMap<>();

    public List<Sound> sounds = new ArrayList<>();

    public static Map<Material, int[]> stones = new HashMap<>();
    public static Map<Material, int[]> dirts = new HashMap<>();
    public static Map<Material, int[]> planks = new HashMap<>();
    public static Map<Material, int[]> miscs = new HashMap<>();

    public static Map<String, List<Material>> tools = new HashMap<>();

    public WorldBorderSizeHandler getWorldBorderHandler() {
        return this.WorldBorderSizeHandler;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic

        plugin = this;

        saveDefaultConfig();
        FileConfiguration config = getConfig();
        config.set("pluginEnabled", true);

        if (!config.contains("expansionPercentage")) {
            config.set("expansionPercentage", 0.25);
        }

        if (!config.contains("blockCount")) {
            config.set("blockCount", 0);
        }

        saveConfig();
        getLogger().info("Plugin enabled");

        World world = Bukkit.getWorld("world");
        assert world != null;
        WorldBorderSizeHandler = new WorldBorderSizeHandler(this);
        playerspawning = new PlayerSpawning(this);
        saddleRide = new SaddleRide(this);
        artBlocks = new ArtBlocks(this);

        // Get rid of various vanilla recipes
        Recipes.removeRecipes();

        // Add custom recipes
        Recipes.addRecipes();

        // Register commands

        getCommand("blockcount").setExecutor(new BlockCountCommand(WorldBorderSizeHandler));
        getCommand("expansionpercentage").setExecutor(new ExpansionPercentageCommand(WorldBorderSizeHandler));



        // Allows ListenerClass to receive events.
        getServer().getPluginManager().registerEvents(new Listener(this), this);

        // Allows Command classes to register commands
        this.getCommand("setVoice").setExecutor(new SetVoiceCommand(this));
        this.getCommand("setGens").setExecutor(new SetGensCommand(this));
        this.getCommand("shout").setExecutor(new ShoutCommand(this));
        this.getCommand("rules").setExecutor(new RulesCommand());
        this.getCommand("sit").setExecutor(new SitCommand(this));
        this.getCommand("setCharLimit").setExecutor(new SetPlayerCharLimCommand(this));
        this.getCommand("spawnchest").setExecutor(new TreasureChestSpawnCommand());
        this.getCommand("abilities").setExecutor(new OpenAbilitiesGUICommand());
        this.getCommand("hat").setExecutor(new HatsCommand(this));
        this.getCommand("levels").setExecutor(new OpenLevelsGUICommand(this));
        this.getCommand("lostTools").setExecutor(new LostToolsCommand());
        this.getCommand("setSkin").setExecutor(new OpenSkinGUICommand());

        this.skinsRestorer = SkinsRestorerProvider.get();

        // Create Map for tools
        List<Material> pickaxes = new ArrayList<>();
        pickaxes.add(Material.WOODEN_PICKAXE);
        pickaxes.add(Material.STONE_PICKAXE);
        pickaxes.add(Material.IRON_PICKAXE);
        pickaxes.add(Material.DIAMOND_PICKAXE);

        List<Material> axes = new ArrayList<>();
        axes.add(Material.WOODEN_AXE);
        axes.add(Material.STONE_AXE);
        axes.add(Material.IRON_AXE);
        axes.add(Material.DIAMOND_AXE);

        List<Material> shovels = new ArrayList<>();
        shovels.add(Material.WOODEN_SHOVEL);
        shovels.add(Material.STONE_SHOVEL);
        shovels.add(Material.IRON_SHOVEL);
        shovels.add(Material.DIAMOND_SHOVEL);

        List<Material> swords = new ArrayList<>();
        swords.add(Material.WOODEN_SWORD);
        swords.add(Material.STONE_SWORD);
        swords.add(Material.IRON_SWORD);
        swords.add(Material.DIAMOND_SWORD);

        HatsList hats = new HatsList();
        hats.createHats();

        tools.put("pickaxe", pickaxes);
        tools.put("axe", axes);
        tools.put("shovel", shovels);
        tools.put("sword", swords);

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
        dirts.add(Material.MUD);

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

        // Initialize Ability GUI Components
        Abilities abilities = new Abilities();
        abilities.abilitiesInit();


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (playerspawning != null) {
            playerspawning.removeAllBlocks();
        }

        FileConfiguration config = getConfig();
        WorldBorderSizeHandler WorldBorderSizeHandler = new WorldBorderSizeHandler(this);
        config.set("pluginEnabled", false);
        saveConfig();
        HandlerList.unregisterAll(this);

        getLogger().info("Plugin disabled");
    }

    public void broadcast(String message){
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage(message);
        }
    }

    public static void pluginMessage(String message, Player p){
        p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.BLUE + "10" + ChatColor.DARK_GRAY + "] " + message);
    }

    public static Server getPlugin() {
        return plugin;
    }

}
