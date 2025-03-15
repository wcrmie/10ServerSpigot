package worm.me;

import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;
import net.skinsrestorer.api.SkinsRestorer;
import net.skinsrestorer.api.SkinsRestorerProvider;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import worm.me.commands.*;
import worm.me.commands.admin.*;
import worm.me.commands.gui.*;
import worm.me.commands.gui.HatsCommand;
import worm.me.commands.gui.OpenAbilitiesGUICommand;
import worm.me.commands.gui.OpenLevelsGUICommand;
import worm.me.commands.gui.OpenSkinGUICommand;
import worm.me.gamesystems.recipes.Recipes;
import worm.me.gamesystems.ArtBlocks;
import worm.me.playersystems.abilities.Abilities;
import worm.me.playersystems.abilities.active.FrogGrab;
import worm.me.playersystems.quests.QuestInitialize;
import worm.me.playersystems.PlayerSpawning;
import worm.me.playersystems.saddle.SaddleSystem;
import worm.me.gamesystems.border.WorldBorderSizeHandler;

import java.io.File;
import java.util.*;

public final class Server extends JavaPlugin {

    private static Server plugin;

    public SkinsRestorer skinsRestorer;

    private WorldBorderSizeHandler WorldBorderSizeHandler;
    private PlayerSpawning playerspawning;
    private SaddleSystem saddleSystem;
    private ArtBlocks artBlocks;

    public Map<Material, Long> miningBlocks = new HashMap<>();

    public Map<Material, Long> foragingBlocks = new HashMap<>();

    public Map<Material, Long> farmingBlocks = new HashMap<>();

    public Map<EntityType, Long> combatMobs = new HashMap<>();

    public Map<Material, Long> smithingItems = new HashMap<>();

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
        getLogger().info("Plugin enabled");


        World world = Bukkit.getWorld("world");
        assert world != null;
        WorldBorderSizeHandler = new WorldBorderSizeHandler(this);
        playerspawning = new PlayerSpawning(this);
        saddleSystem = new SaddleSystem(this);
        artBlocks = new ArtBlocks(this);

        // Add custom recipes

        Recipes recipes = new Recipes();

        recipes.removeRecipes();
        recipes.addRecipes();


        // Register commands

        getCommand("blockcount").setExecutor(new BlockCountCommand(WorldBorderSizeHandler));
        getCommand("expansionpercentage").setExecutor(new ExpansionPercentageCommand(WorldBorderSizeHandler));



        // Allows ListenerClass to receive events.
        getServer().getPluginManager().registerEvents(new Listener(this), this);

        // Allows Command classes to register commands
        this.getCommand("islandspawn").setExecutor(new IslandSpawnCommand(this));
        this.getCommand("setVoice").setExecutor(new SetVoiceCommand(this));
        this.getCommand("setGens").setExecutor(new SetGensCommand(this));
        this.getCommand("shout").setExecutor(new ShoutCommand(this));
        this.getCommand("rules").setExecutor(new RulesCommand());
        this.getCommand("sit").setExecutor(new SitCommand(this));
        this.getCommand("setCharLimit").setExecutor(new SetPlayerCharLimCommand(this));
        this.getCommand("spawnchest").setExecutor(new TreasureChestSpawnCommand());
        this.getCommand("abilities").setExecutor(new OpenAbilitiesGUICommand());
        this.getCommand("hats").setExecutor(new HatsCommand());
        this.getCommand("levels").setExecutor(new OpenLevelsGUICommand(this));
        this.getCommand("lostTools").setExecutor(new LostToolsCommand());
        this.getCommand("setSkin").setExecutor(new OpenSkinGUICommand());
        this.getCommand("menu").setExecutor(new MenuCommand());
        this.getCommand("profile").setExecutor(new OpenProfileGUICommand());
        this.getCommand("hatstorage").setExecutor(new EditHatsCommand());
        this.getCommand("rename").setExecutor(new RenameCommand());
        this.getCommand("recipes").setExecutor(new OpenRecipesGUICommand());

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

        tools.put("pickaxe", pickaxes);
        tools.put("axe", axes);
        tools.put("shovel", shovels);
        tools.put("sword", swords);

        // Set blocks and their given mining exp.
        miningBlocks.put(Material.COBBLESTONE, 1L);
        miningBlocks.put(Material.STONE, 3L);
        miningBlocks.put(Material.IRON_ORE, 10L);
        miningBlocks.put(Material.COAL_ORE, 5L);

        // Set blocks and their given foraging exp.
        foragingBlocks.put(Material.OAK_LOG, 5L);
        foragingBlocks.put(Material.BIRCH_LOG, 10L);
        foragingBlocks.put(Material.SPRUCE_LOG, 5L);
        foragingBlocks.put(Material.JUNGLE_LOG, 15L);
        foragingBlocks.put(Material.ACACIA_LOG, 10L);
        foragingBlocks.put(Material.DARK_OAK_LOG, 5L);
        foragingBlocks.put(Material.SHORT_GRASS, 1L);
        foragingBlocks.put(Material.TALL_GRASS, 2L);
        foragingBlocks.put(Material.MOSS_BLOCK, 2L);
        foragingBlocks.put(Material.DANDELION, 1L);
        foragingBlocks.put(Material.POPPY, 1L);
        foragingBlocks.put(Material.BLUE_ORCHID, 1L);
        foragingBlocks.put(Material.ALLIUM, 1L);
        foragingBlocks.put(Material.AZURE_BLUET, 1L);
        foragingBlocks.put(Material.RED_TULIP, 1L);
        foragingBlocks.put(Material.ORANGE_TULIP, 1L);
        foragingBlocks.put(Material.WHITE_TULIP, 1L);
        foragingBlocks.put(Material.PINK_TULIP, 1L);
        foragingBlocks.put(Material.OXEYE_DAISY, 1L);
        foragingBlocks.put(Material.CORNFLOWER, 1L);
        foragingBlocks.put(Material.LILY_OF_THE_VALLEY, 1L);
        foragingBlocks.put(Material.DIRT, 2L);
        foragingBlocks.put(Material.SEAGRASS, 1L);

        // Set blocks and their given farming exp.
        farmingBlocks.put(Material.WHEAT, 2L);
        farmingBlocks.put(Material.CARROTS, 3L);
        farmingBlocks.put(Material.POTATOES, 3L);
        farmingBlocks.put(Material.BEETROOTS, 5L);
        farmingBlocks.put(Material.MELON, 8L);
        farmingBlocks.put(Material.PUMPKIN, 8L);
        farmingBlocks.put(Material.SUGAR_CANE, 8L);
        farmingBlocks.put(Material.COCOA, 10L);
        farmingBlocks.put(Material.RED_MUSHROOM_BLOCK, 15L);
        farmingBlocks.put(Material.BROWN_MUSHROOM_BLOCK, 15L);
        farmingBlocks.put(Material.NETHER_WART, 15L);
        farmingBlocks.put(Material.CACTUS, 8L);
        farmingBlocks.put(Material.KELP, 1L);

        // Set mobs and their given combat exp.
        combatMobs.put(EntityType.ZOMBIE, 3L);
        combatMobs.put(EntityType.SKELETON, 3L);
        combatMobs.put(EntityType.CREEPER, 5L);
        combatMobs.put(EntityType.SPIDER, 3L);
        combatMobs.put(EntityType.CAVE_SPIDER, 5L);
        combatMobs.put(EntityType.ENDERMAN, 10L);
        combatMobs.put(EntityType.WITCH, 10L);
        combatMobs.put(EntityType.ZOMBIFIED_PIGLIN, 5L);
        combatMobs.put(EntityType.PIGLIN, 5L);
        combatMobs.put(EntityType.PIGLIN_BRUTE, 30L);
        combatMobs.put(EntityType.BLAZE, 15L);
        combatMobs.put(EntityType.GHAST, 10L);
        combatMobs.put(EntityType.MAGMA_CUBE, 5L);
        combatMobs.put(EntityType.ENDER_DRAGON, 100L);
        combatMobs.put(EntityType.WITHER, 150L);
        combatMobs.put(EntityType.BOGGED, 5L);
        combatMobs.put(EntityType.COW, 1L);
        combatMobs.put(EntityType.SHEEP, 1L);
        combatMobs.put(EntityType.PIG, 1L);
        combatMobs.put(EntityType.CHICKEN, 1L);
        combatMobs.put(EntityType.RABBIT, 3L);
        combatMobs.put(EntityType.WOLF, 5L);
        combatMobs.put(EntityType.OCELOT, 5L);
        combatMobs.put(EntityType.FOX, 1L);
        combatMobs.put(EntityType.PANDA, 5L);
        combatMobs.put(EntityType.POLAR_BEAR, 10L);
        combatMobs.put(EntityType.LLAMA, 5L);
        combatMobs.put(EntityType.PARROT, 1L);
        combatMobs.put(EntityType.DOLPHIN, 2L);
        combatMobs.put(EntityType.TURTLE, 2L);
        combatMobs.put(EntityType.BAT, 1L);
        combatMobs.put(EntityType.SQUID, 1L);
        combatMobs.put(EntityType.PUFFERFISH, 3L);
        combatMobs.put(EntityType.SALMON, 1L);
        combatMobs.put(EntityType.COD, 1L);
        combatMobs.put(EntityType.TROPICAL_FISH, 1L);
        combatMobs.put(EntityType.DROWNED, 5L);
        combatMobs.put(EntityType.STRAY, 5L);
        combatMobs.put(EntityType.HUSK, 5L);
        combatMobs.put(EntityType.PHANTOM, 5L);
        combatMobs.put(EntityType.PILLAGER, 10L);
        combatMobs.put(EntityType.VINDICATOR, 15L);
        combatMobs.put(EntityType.EVOKER, 20L);
        combatMobs.put(EntityType.ILLUSIONER, 25L);
        combatMobs.put(EntityType.RAVAGER, 50L);
        combatMobs.put(EntityType.VEX, 10L);
        combatMobs.put(EntityType.WITHER_SKELETON, 10L);
        combatMobs.put(EntityType.ENDERMITE, 5L);
        combatMobs.put(EntityType.GUARDIAN, 10L);
        combatMobs.put(EntityType.ELDER_GUARDIAN, 20L);
        combatMobs.put(EntityType.SHULKER, 10L);
        combatMobs.put(EntityType.STRIDER, 5L);
        combatMobs.put(EntityType.ZOGLIN, 5L);
        combatMobs.put(EntityType.HOGLIN, 5L);
        combatMobs.put(EntityType.PLAYER, 3L);
        combatMobs.put(EntityType.IRON_GOLEM, 7L);
        combatMobs.put(EntityType.SNOW_GOLEM, 1L);
        combatMobs.put(EntityType.BREEZE, 5L);
        combatMobs.put(EntityType.SILVERFISH, 3L);
        combatMobs.put(EntityType.ZOMBIE_VILLAGER, 3L);
        combatMobs.put(EntityType.ZOMBIE_HORSE, 3L);

        // Set the exp things give when taken from a furnace
        smithingItems.put(Material.IRON_INGOT, 3L);
        smithingItems.put(Material.GOLD_NUGGET, 2L);
        smithingItems.put(Material.GOLD_INGOT, 5L);
        smithingItems.put(Material.IRON_NUGGET, 1L);
        smithingItems.put(Material.COAL, 1L);



        // Set voices
        sounds.add(Sound.BLOCK_STONE_BUTTON_CLICK_OFF); // 0
        sounds.add(Sound.BLOCK_WOODEN_BUTTON_CLICK_OFF); // 1
        sounds.add(Sound.BLOCK_DISPENSER_DISPENSE); // 2
        sounds.add(Sound.BLOCK_STONE_BREAK); // 3
        sounds.add(Sound.BLOCK_BONE_BLOCK_BREAK); // 4
        sounds.add(Sound.ENTITY_ARMOR_STAND_HIT); // 5
        sounds.add(Sound.BLOCK_WOOL_HIT); // 6
        sounds.add(Sound.BLOCK_BAMBOO_HIT); // 7
        sounds.add(Sound.BLOCK_AMETHYST_BLOCK_BREAK); // 8
        sounds.add(Sound.ENTITY_ENDER_EYE_DEATH); // 9
        sounds.add(Sound.ENTITY_PLAYER_BURP); // 10
        sounds.add(Sound.ENTITY_CAT_AMBIENT); // 11
        sounds.add(Sound.ENTITY_WOLF_AMBIENT); // 12
        sounds.add(Sound.ENTITY_SHEEP_DEATH); // 13
        sounds.add(Sound.ENTITY_BEE_HURT); // 14
        sounds.add(Sound.ENTITY_ARMADILLO_BRUSH); // 15
        sounds.add(Sound.ENTITY_CAMEL_DASH); // 16
        sounds.add(Sound.ENTITY_VILLAGER_TRADE); // 17
        sounds.add(Sound.ENTITY_VILLAGER_NO); // 18
        sounds.add(Sound.ENTITY_CREEPER_HURT); // 19
        sounds.add(Sound.ENTITY_ZOMBIE_HURT); // 20
        sounds.add(Sound.ENTITY_SKELETON_HURT); // 21
        sounds.add(Sound.ENTITY_ENDERMAN_HURT); // 22
        sounds.add(Sound.ENTITY_ENDER_DRAGON_HURT); // 23
        sounds.add(Sound.ENTITY_WITCH_THROW); // 24

        // CREATE BLOCK KEYS FOR GENERATOR
        stones.put(Material.COBBLESTONE, new int[]{0,0,0,0,0,1,0,0});
        stones.put(Material.TUFF, new int[]{5,0,0,0,0,10,0,0});
        stones.put(Material.ANDESITE, new int[]{0,0,0,0,0,7,0,0});
        stones.put(Material.GRANITE, new int[]{0,0,0,0,0,7,0,0});
        stones.put(Material.DIORITE, new int[]{0,0,0,0,0,7,0,0});
        stones.put(Material.DRIPSTONE_BLOCK, new int[]{0,0,0,0,0,25,0,0});
        stones.put(Material.PACKED_MUD, new int[]{0,15,0,0,0,15,0,10});
        stones.put(Material.COBBLED_DEEPSLATE, new int[]{0,0,0,0,10,35,0,0});
        stones.put(Material.SMOOTH_BASALT, new int[]{0,0,0,0,15,20,0,0});

        dirts.put(Material.DIRT, new int[]{0,1,0,0,0,0,0,0});
        dirts.put(Material.COARSE_DIRT, new int[]{0,5,0,0,0,5,0,5});
        dirts.put(Material.SAND, new int[]{0,5,0,0,0,15,0,5});
        dirts.put(Material.GRAVEL, new int[]{0,0,0,0,0,10,0,10});
        dirts.put(Material.RED_SAND, new int[]{0,0,0,0,5,25,0,15});
        dirts.put(Material.CLAY, new int[]{0,5,0,15,0,5,0,15});
        dirts.put(Material.MUD, new int[]{0,15,0,5,0,0,0,10});

        planks.put(Material.OAK_PLANKS, new int[]{0,0,0,0,0,0,0,1});
        planks.put(Material.SPRUCE_PLANKS, new int[]{10,0,0,0,0,0,0,10});
        planks.put(Material.DARK_OAK_PLANKS, new int[]{0,0,0,15,0,0,0,15});
        planks.put(Material.BIRCH_PLANKS, new int[]{0,0,0,5,0,0,0,5});
        planks.put(Material.JUNGLE_PLANKS, new int[]{0,0,10,0,0,0,0,15});
        planks.put(Material.MANGROVE_PLANKS, new int[]{0,5,0,5,0,0,0,20});
        planks.put(Material.ACACIA_PLANKS, new int[]{0,0,0,5,5,0,0,15});

        miscs.put(Material.COAL, new int[]{0,0,0,0,1,0,0,0});
        miscs.put(Material.QUARTZ, new int[]{0,0,0,0,25,25,0,0});
        miscs.put(Material.REDSTONE, new int[]{0,0,10,0,25,10,0,0});
        miscs.put(Material.GLOWSTONE_DUST, new int[]{0,0,0,0,15,0,0,0});

        // CREATE QUESTS
        QuestInitialize questInitialize = new QuestInitialize();
        questInitialize.questInitialize();

        // Initialize Ability GUI Components
        Abilities abilities = new Abilities();
        abilities.abilitiesInit();

        abilities.initializeRequirements();


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
        getServer().getScheduler().cancelTasks(this);

        FrogGrab.cancelAllGrabs();
        FrogGrab.clearAllMovementRestrictions();


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

    public CoreProtectAPI getCoreProtect() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CoreProtect");

        if (plugin == null || !(plugin instanceof CoreProtect)) {
            return null;
        }

        CoreProtectAPI CoreProtect = ((CoreProtect) plugin).getAPI();
        if (CoreProtect.isEnabled() == false) {
            return null;
        }

        if (CoreProtect.APIVersion() < 10) {
            return null;
        }

        return CoreProtect;
    }

    public FileConfiguration getHatsStorage() {

        File hatStorage = new File(Server.getPlugin().getDataFolder(), "hatStorage.yml");

        return YamlConfiguration.loadConfiguration(hatStorage);
    }
}
