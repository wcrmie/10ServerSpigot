package worm.me;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import worm.me.commands.*;
import worm.me.commands.admin.*;
import worm.me.commands.chat.*;
import worm.me.commands.gui.*;
import worm.me.commands.gui.OpenAbilitiesGUICommand;
import worm.me.commands.gui.OpenLevelsGUICommand;
import worm.me.gamesystems.recipes.ShapedRecipes;
import worm.me.gamesystems.recipes.ShapelessRecipes;
import worm.me.gamesystems.recipes.SmeltingRecipes;
import worm.me.gamesystems.world.WorldTimer;
import worm.me.gamesystems.world.generation.island.IslandManager;
import worm.me.gamesystems.world.border.Custom.BorderVisuals;
import worm.me.gamesystems.world.border.Custom.ZoneGridManager;
import worm.me.gamesystems.world.legacystructure.generation.Generate;
import worm.me.gamesystems.world.legacystructure.TreasureChests;
import worm.me.gamesystems.world.cleanup.WorldReset;
import worm.me.listeners.MainListener;
import worm.me.listeners.items.PotionListener;
import worm.me.listeners.items.ToolListener;
import worm.me.listeners.player.*;
import worm.me.listeners.world.WorldListener;
import worm.me.listeners.world.mobs.CustomMobsListener;
import worm.me.listeners.player.custom.AbilitiesListener;
import worm.me.listeners.player.custom.LevellingListener;
import worm.me.listeners.player.custom.QuestListener;
import worm.me.listeners.world.mobs.VillagerListener;
import worm.me.utils.managers.ConfigManager;
import worm.me.playersystems.abilities.Abilities;
import worm.me.playersystems.abilities.active.DoubleJump;
import worm.me.playersystems.abilities.active.FeatherDash;
import worm.me.playersystems.abilities.active.ShadowStep;
import worm.me.playersystems.abilities.active.SpeedStep;
import worm.me.data.levels.Reqs;
import worm.me.playersystems.generator.Generator;
import worm.me.playersystems.quests.QuestInitialize;
import worm.me.playersystems.PlayerSpawning;
import worm.me.gamesystems.world.border.WorldBorderSizeHandler;
import java.util.*;

public final class Server extends JavaPlugin {

    private static Server plugin;
    private ConfigManager configManager;

    private WorldBorderSizeHandler WorldBorderSizeHandler;
    private PlayerSpawning playerspawning;
    private FeatherDash featherDash = new FeatherDash();
    private TreasureChests treasureChests;
    //private static final long CHEST_SPAWN_INTERVAL = 2 * 60 * 60 * 20;
    //private static final String LAST_CHEST_SPAWN_KEY = "lastChestSpawn";
    public static ZoneGridManager zoneGridManager;

    public Map<Material, Long> miningBlocks = new HashMap<>();

    public Map<Material, Long> foragingBlocks = new HashMap<>();

    public Map<Material, Long> farmingBlocks = new HashMap<>();

    public Map<EntityType, Long> combatMobs = new HashMap<>();

    public Map<Material, Long> smithingItems = new HashMap<>();

    public static Map<Material, int[]> stones = new HashMap<>();
    public static Map<Material, int[]> dirts = new HashMap<>();
    public static Map<Material, int[]> planks = new HashMap<>();
    public static Map<Material, int[]> miscs = new HashMap<>();
    public List<Player> generating = new ArrayList<>();

    public static Map<String, List<Material>> tools = new HashMap<>();

    public WorldBorderSizeHandler getWorldBorderHandler() {
        return this.WorldBorderSizeHandler;
    }

    private SpeedStep speedStep = new SpeedStep();

    public TreasureChests getTreasureChests() {
        return this.treasureChests;
    }

    private ZoneGridManager gridManager;
    private BorderVisuals borderManager;

    @Override
    public void onEnable() {
        // Plugin startup logic

        plugin = this;

        saveDefaultConfig();
        getLogger().info("Plugin enabled");

        World world = Bukkit.getWorld("ul_Void");
        assert world != null;
        WorldBorderSizeHandler = new WorldBorderSizeHandler(this);
        playerspawning = new PlayerSpawning(this);
        //this.treasureChests = new TreasureChests(this);
        //treasureChests.onPluginEnable();

        // Add custom recipes

        WorldTimer.getInstance().start();

        IslandManager islandManager = new IslandManager();

        ShapedRecipes shapedRecipes = new ShapedRecipes();

        ShapelessRecipes shapelessRecipes = new ShapelessRecipes();

        SmeltingRecipes smeltingRecipes = new SmeltingRecipes();

        shapedRecipes.removeRecipes();
        shapedRecipes.addRecipes();
        shapelessRecipes.addRecipes();
        smeltingRecipes.initializeSmeltingRecipes();


        Generate generate = new Generate(this);

        // Register commands
        getCommand("generateisland").setExecutor(new IslandGenerationCommand());
        getCommand("handbook").setExecutor(new HandBookCommand());
        getCommand("spawntreasure").setExecutor(new TreasureChestCommand(treasureChests));
        getCommand("resetworld").setExecutor(new WorldResetCommand());
        getCommand("blockcount").setExecutor(new BlockCountCommand(WorldBorderSizeHandler));
        getCommand("expansionpercentage").setExecutor(new ExpansionPercentageCommand(WorldBorderSizeHandler));
        getCommand("structuresmake").setExecutor(new StructuresMakeCommand(generate));
        getCommand("structuresplace").setExecutor(new StructuresPlaceCommand(generate));
        getCommand("structuresplacetype").setExecutor(new StructuresPlaceTypeCommand());
        getCommand("structureslist").setExecutor(new StructuresListCommand(generate));
        getCommand("structuresremove").setExecutor(new StructuresRemoveCommand(generate));


        zoneGridManager = new ZoneGridManager(this);
        zoneGridManager.initialize();

        borderManager = new BorderVisuals(this, zoneGridManager);
        zoneGridManager.setBorderVisuals(borderManager);

        borderManager.start();

        getCommand("setbordergrid")
                .setExecutor(new SetBorderGrid(zoneGridManager));

        getCommand("getbordergrid")
                .setExecutor(new GetBorderGrid(zoneGridManager));

        // Allows listeners to receive events.
        getServer().getPluginManager().registerEvents(new MainListener(), this);

        // Player Listeners
        getServer().getPluginManager().registerEvents(new AbilitiesListener(), this);
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
        getServer().getPluginManager().registerEvents(new GameplayListener(), this);
        getServer().getPluginManager().registerEvents(new InitializationsListener(), this);
        getServer().getPluginManager().registerEvents(new LevellingListener(), this);

        getServer().getPluginManager().registerEvents(new WorldListener(this), this);
        getServer().getPluginManager().registerEvents(new QuestListener(), this);
        getServer().getPluginManager().registerEvents(new CustomMobsListener(), this);
        getServer().getPluginManager().registerEvents(new VillagerListener(), this);

        getServer().getPluginManager().registerEvents(new PotionListener(), this);
        getServer().getPluginManager().registerEvents(new ToolListener(), this);

        // Allows Command classes to register commands
        configManager = new ConfigManager();
        configManager.loadConfig();

        this.getCommand("generators").setExecutor(new SetGensCommand());
        this.getCommand("placetree").setExecutor(new PlaceTreeCommand());
        this.getCommand("rules").setExecutor(new RulesCommand());
        this.getCommand("sit").setExecutor(new SitCommand());
        this.getCommand("abilities").setExecutor(new OpenAbilitiesGUICommand());
        this.getCommand("levels").setExecutor(new OpenLevelsGUICommand());
        this.getCommand("menu").setExecutor(new MenuCommand());
        this.getCommand("profile").setExecutor(new OpenProfileGUICommand());
        this.getCommand("recipes").setExecutor(new OpenRecipesGUICommand());
        this.getCommand("suicide").setExecutor(new SuicideCommand());
        this.getCommand("togglegens").setExecutor(new ToggleGensCommand());
        this.getCommand("hat").setExecutor(new HatCommand());
        this.getCommand("givebook").setExecutor(new GiveClericBookCommand());
        this.getCommand("discord").setExecutor(new DiscordCommand());

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
        foragingBlocks.put(Material.MANGROVE_LOG, 10L);
        foragingBlocks.put(Material.PALE_OAK_LOG, 5L);
        foragingBlocks.put(Material.CHERRY_LOG, 3L);
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
        farmingBlocks.put(Material.RED_MUSHROOM_BLOCK, 2L);
        farmingBlocks.put(Material.BROWN_MUSHROOM_BLOCK, 2L);
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
        smithingItems.put(Material.IRON_NUGGET, 3L);
        smithingItems.put(Material.COAL, 1L);
        smithingItems.put(Material.COPPER_INGOT, 1L);

        // CREATE BLOCK KEYS FOR GENERATOR
        stones.put(Material.COBBLESTONE, new Reqs.RequirementBuilder().mining(1).build());
        stones.put(Material.TUFF, new Reqs.RequirementBuilder().combat(5).mining(10).build());
        stones.put(Material.ANDESITE, new Reqs.RequirementBuilder().mining(7).build());
        stones.put(Material.GRANITE, new Reqs.RequirementBuilder().mining(7).build());
        stones.put(Material.DIORITE, new Reqs.RequirementBuilder().mining(7).build());
        stones.put(Material.DRIPSTONE_BLOCK, new Reqs.RequirementBuilder().mining(25).build());
        stones.put(Material.PACKED_MUD, new Reqs.RequirementBuilder().farming(15).mining(15).foraging(10).build());
        stones.put(Material.COBBLED_DEEPSLATE, new Reqs.RequirementBuilder().smithing(10).mining(35).build());
        stones.put(Material.SMOOTH_BASALT, new Reqs.RequirementBuilder().smithing(15).mining(20).build());

        dirts.put(Material.DIRT, new Reqs.RequirementBuilder().farming(1).build());
        dirts.put(Material.COARSE_DIRT, new Reqs.RequirementBuilder().farming(5).mining(5).foraging(5).build());
        dirts.put(Material.SAND, new Reqs.RequirementBuilder().farming(5).mining(15).foraging(5).build());
        dirts.put(Material.GRAVEL, new Reqs.RequirementBuilder().mining(10).foraging(10).build());
        dirts.put(Material.RED_SAND, new Reqs.RequirementBuilder().smithing(5).mining(25).foraging(15).build());
        dirts.put(Material.CLAY, new Reqs.RequirementBuilder().farming(5).fishing(15).mining(5).foraging(15).build());
        dirts.put(Material.MUD, new Reqs.RequirementBuilder().farming(15).fishing(5).foraging(10).build());

        planks.put(Material.OAK_PLANKS, new Reqs.RequirementBuilder().foraging(1).build());
        planks.put(Material.SPRUCE_PLANKS, new Reqs.RequirementBuilder().combat(10).foraging(10).build());
        planks.put(Material.DARK_OAK_PLANKS, new Reqs.RequirementBuilder().fishing(15).foraging(15).build());
        planks.put(Material.BIRCH_PLANKS, new Reqs.RequirementBuilder().fishing(5).foraging(5).build());
        planks.put(Material.JUNGLE_PLANKS, new Reqs.RequirementBuilder().crafting(10).foraging(15).build());
        planks.put(Material.MANGROVE_PLANKS, new Reqs.RequirementBuilder().farming(5).fishing(5).foraging(20).build());
        planks.put(Material.ACACIA_PLANKS, new Reqs.RequirementBuilder().fishing(5).smithing(5).foraging(15).build());

        miscs.put(Material.COAL_ORE, new Reqs.RequirementBuilder().smithing(1).build());
        miscs.put(Material.QUARTZ, new Reqs.RequirementBuilder().smithing(25).mining(25).build());
        miscs.put(Material.REDSTONE, new Reqs.RequirementBuilder().crafting(10).smithing(25).mining(10).build());
        miscs.put(Material.GLOWSTONE_DUST, new Reqs.RequirementBuilder().smithing(15).build());

        // CREATE QUESTS
        QuestInitialize questInitialize = new QuestInitialize();
        questInitialize.questInitialize();

        // Initialize Ability GUI Components
        Abilities.abilitiesInit();

        for (Player p : Bukkit.getOnlinePlayers()){
            generating.add(p);
            Generator.generateItem(p);
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (playerspawning != null) {
            playerspawning.removeAllBlocks();
        }

        //treasureChests.onPluginDisable();

        WorldTimer.getInstance().saveData();

        //if (treasureChests != null) treasureChests.removeAllDisplays();

        FileConfiguration config = getConfig();
        WorldBorderSizeHandler WorldBorderSizeHandler = new WorldBorderSizeHandler(this);
        config.set("pluginEnabled", false);
        saveConfig();
        HandlerList.unregisterAll(this);

        getLogger().info("Plugin disabled");
        getServer().getScheduler().cancelTasks(this);

        ShadowStep.cancelAllGhosts();
        featherDash.cleanupAll();
        speedStep.cleanupAll();

        DoubleJump.cancelAllPlatforms();
    }


    public static void pluginMessage(String message, Player p){
        p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.BLUE + "10" + ChatColor.DARK_GRAY + "] " + message);
    }

    public static ZoneGridManager getZoneGridManager() {
        return zoneGridManager;
    }

    public static Server getPlugin() {
        return plugin;
    }

    public static ConfigManager getConfigManager() {
        return plugin.configManager;
    }

}
