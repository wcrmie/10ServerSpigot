package worm.me;
import net.dv8tion.jda.api.JDA;
import net.skinsrestorer.api.SkinsRestorer;
import net.skinsrestorer.api.SkinsRestorerProvider;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import worm.me.commands.*;
import worm.me.commands.admin.*;
import worm.me.commands.chat.*;
import worm.me.commands.discord.DiscordCommand;
import worm.me.commands.discord.DiscordLinkCommand;
import worm.me.commands.gui.*;
import worm.me.commands.gui.OpenAbilitiesGUICommand;
import worm.me.commands.gui.OpenLevelsGUICommand;
import worm.me.discord.bot.BotInitialize;
import worm.me.gamesystems.recipes.ShapedRecipes;
import worm.me.gamesystems.recipes.ShapelessRecipes;
import worm.me.gamesystems.recipes.SmeltingRecipes;
import worm.me.gamesystems.structure.generation.TreasureChests;
import worm.me.listeners.MainListener;
import worm.me.listeners.QuestListener;
import worm.me.listeners.WorldListener;
import worm.me.listeners.player.PlayerListener;
import worm.me.managers.ConfigManager;
import worm.me.playersystems.abilities.Abilities;
import worm.me.playersystems.abilities.active.AgilityDoubleJump;
import worm.me.playersystems.abilities.active.FeatherDash;
import worm.me.playersystems.abilities.active.ShadowStep;
import worm.me.playersystems.abilities.active.SpeedStep;
import worm.me.playersystems.data.levels.Reqs;
import worm.me.playersystems.generator.Generator;
import worm.me.playersystems.quests.QuestInitialize;
import worm.me.playersystems.PlayerSpawning;
import worm.me.gamesystems.border.WorldBorderSizeHandler;
import java.util.*;

public final class Server extends JavaPlugin {

    private static Server plugin;
    private ConfigManager configManager;

    public SkinsRestorer skinsRestorer;

    private WorldBorderSizeHandler WorldBorderSizeHandler;
    private PlayerSpawning playerspawning;
    private FeatherDash featherDash = new FeatherDash();
    private TreasureChests treasureChests;
    private static final long CHEST_SPAWN_INTERVAL = 2 * 60 * 60 * 20;
    private static final String LAST_CHEST_SPAWN_KEY = "lastChestSpawn";

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
    public List<Player> generating = new ArrayList<>();

    public static Map<String, List<Material>> tools = new HashMap<>();

    public WorldBorderSizeHandler getWorldBorderHandler() {
        return this.WorldBorderSizeHandler;
    }

    private SpeedStep speedStep = new SpeedStep();

    @Override
    public void onEnable() {
        // Plugin startup logic

        plugin = this;

        saveDefaultConfig();
        getLogger().info("Plugin enabled");

        BotInitialize.initialize();


        World world = Bukkit.getWorld("world");
        assert world != null;
        WorldBorderSizeHandler = new WorldBorderSizeHandler(this);
        playerspawning = new PlayerSpawning(this);
        this.treasureChests = new TreasureChests();
        setupChestSpawnSchedule();

        // Add custom recipes

        ShapedRecipes shapedRecipes = new ShapedRecipes();

        ShapelessRecipes shapelessRecipes = new ShapelessRecipes();

        SmeltingRecipes smeltingRecipes = new SmeltingRecipes();

        shapedRecipes.removeRecipes();
        shapedRecipes.addRecipes();
        shapelessRecipes.addRecipes();
        smeltingRecipes.initializeSmeltingRecipes();


        // Register commands

        getCommand("resetworld").setExecutor(new WorldResetCommand());
        getCommand("blockcount").setExecutor(new BlockCountCommand(WorldBorderSizeHandler));
        getCommand("expansionpercentage").setExecutor(new ExpansionPercentageCommand(WorldBorderSizeHandler));



        // Allows listeners to receive events.
        getServer().getPluginManager().registerEvents(new MainListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getServer().getPluginManager().registerEvents(new WorldListener(this), this);
        getServer().getPluginManager().registerEvents(new QuestListener(), this);

        // Allows Command classes to register commands
        configManager = new ConfigManager(this);

        this.getCommand("report").setExecutor(new ReportCommand(this));
        this.getCommand("islandspawn").setExecutor(new IslandSpawnCommand(this));
        this.getCommand("setVoice").setExecutor(new SetVoiceCommand(this));
        this.getCommand("setGens").setExecutor(new SetGensCommand(this));
        this.getCommand("shout").setExecutor(new ShoutCommand(this));
        this.getCommand("rules").setExecutor(new RulesCommand());
        this.getCommand("sit").setExecutor(new SitCommand(this));
        this.getCommand("setCharLimit").setExecutor(new SetPlayerCharLimCommand(this));
        this.getCommand("abilities").setExecutor(new OpenAbilitiesGUICommand());
        //this.getCommand("hats").setExecutor(new HatsCommand());
        this.getCommand("levels").setExecutor(new OpenLevelsGUICommand(this));
        //this.getCommand("setSkin").setExecutor(new OpenSkinGUICommand());
        this.getCommand("menu").setExecutor(new MenuCommand());
        this.getCommand("profile").setExecutor(new OpenProfileGUICommand());
        //this.getCommand("hatstorage").setExecutor(new EditHatsCommand());
        this.getCommand("rename").setExecutor(new RenameCommand());
        this.getCommand("recipes").setExecutor(new OpenRecipesGUICommand());
        this.getCommand("suicide").setExecutor(new SuicideCommand());
        this.getCommand("discord").setExecutor(new DiscordCommand());
        this.getCommand("link").setExecutor(new DiscordLinkCommand());
        this.getCommand("togglegens").setExecutor(new ToggleGensCommand());
        this.getCommand("hat").setExecutor(new HatCommand());
        this.getCommand("say").setExecutor(new SayCommand());
        this.getCommand("voicemute").setExecutor(new VoiceMuteCommand());

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
        sounds.add(Sound.BLOCK_DECORATED_POT_INSERT); // 25
        sounds.add(Sound.BLOCK_DECORATED_POT_INSERT_FAIL); // 26

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
        Abilities abilities = new Abilities();
        abilities.abilitiesInit();

        abilities.initializeRequirements();

        for (Player p : Bukkit.getOnlinePlayers()){
            generating.add(p);
            Generator generator = new Generator(this);
            generator.generateItem(p);
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (playerspawning != null) {
            playerspawning.removeAllBlocks();
        }

        JDA jda = BotInitialize.api;

        BotInitialize.channel.sendMessage("`Server is shutting down.`").queue();

        jda.shutdown();

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

        AgilityDoubleJump.cancelAllPlatforms();
    }

    private void setupChestSpawnSchedule() {
        long lastSpawn = getConfig().getLong(LAST_CHEST_SPAWN_KEY, 0);
        long currentTime = System.currentTimeMillis();
        long timeSinceLastSpawn = currentTime - lastSpawn;

        long intervalMillis = 2 * 60 * 60 * 1000;

        long initialDelay;
        if (lastSpawn == 0) {
            initialDelay = 0;
        } else if (timeSinceLastSpawn >= intervalMillis) {
            initialDelay = 0;
        } else {
            initialDelay = intervalMillis - timeSinceLastSpawn;
        }

        long initialDelayTicks = initialDelay / 50;

        getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
            treasureChests.chestSpawn();
            getConfig().set(LAST_CHEST_SPAWN_KEY, System.currentTimeMillis());
            saveConfig();
        }, initialDelayTicks, CHEST_SPAWN_INTERVAL);
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

    public static ConfigManager getConfigManager() {
        return plugin.configManager;
    }

}
