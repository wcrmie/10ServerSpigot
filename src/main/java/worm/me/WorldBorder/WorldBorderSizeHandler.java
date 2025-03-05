package worm.me.WorldBorder;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.configuration.file.FileConfiguration;
import worm.me.GameSystems.IslandSpawning;
import worm.me.GameSystems.TreasureChests;
import worm.me.Server;

import java.util.concurrent.ThreadLocalRandom;

public class WorldBorderSizeHandler {
    private static final int BASE_SIZE = 20;
    private static final String WORLD_NAME = "ul_Void";
    private static final String BLOCK_COUNT_CONFIG_KEY = "blockCount";
    private static final String EXPANSION_PERCENTAGE_CONFIG_KEY = "expansionPercentage";

    private final Server plugin;
    private final World world;
    private final WorldBorder worldBorder;

    private static long blockCount;
    private double expansionPercentage;

    public WorldBorderSizeHandler(Server plugin) {
        this.plugin = plugin;

        this.world = Bukkit.getWorld(WORLD_NAME);
        if (this.world == null) {
            throw new IllegalStateException("World '" + WORLD_NAME + "' not found");
        }

        this.worldBorder = world.getWorldBorder();
        this.worldBorder.setCenter(0.5, 0.5);

        FileConfiguration config = plugin.getConfig();
        this.blockCount = config.getLong(BLOCK_COUNT_CONFIG_KEY, 0);
        this.expansionPercentage = config.getDouble(EXPANSION_PERCENTAGE_CONFIG_KEY, 0.25);
    }

    public void adjustWorldBorder() {
        long worldBlockSize = calculateWorldBlockSize();
        int newSize = calculateNewSize(worldBlockSize);

        newSize = (newSize % 2 == 0) ? newSize : newSize - 1;

        if (newSize > worldBorder.getSize()) {

            worldBorder.setSize(newSize);
        }
    }

    int lastSpawnDone = 0;
    public void spawnthings() {
        if ((blockCount % 5000)==0 && blockCount != lastSpawnDone) {
            lastSpawnDone = (int) blockCount;
            TreasureChests TreasureChestsH = new TreasureChests();
            TreasureChestsH.chestSpawn();

            IslandSpawning IslandSpawningH = new IslandSpawning(plugin);
            IslandSpawningH.IslandSpawn(null);
        }
    }

    private long calculateWorldBlockSize() {
        return (long) (worldBorder.getSize() * worldBorder.getSize() * 60);
    }

    private int calculateNewSize(long worldBlockSize) {
        return (int) (BASE_SIZE + (blockCount / (worldBlockSize * (expansionPercentage / 100.0))));
    }

    public void incrementBlockCount() {
        blockCount++;
        saveAndAdjustWorldBorder();
    }

    public void decrementBlockCount() {
        blockCount--;
        saveBlockCount();
    }

    public void addBlockCount(int amount) {
        blockCount += amount;
        Bukkit.getLogger().info("Adding blocks: " + amount + ", New block count: " + blockCount);
        saveAndAdjustWorldBorder();
    }

    public void removeBlockCount(int amount) {
        blockCount -= amount;
        saveBlockCount();
    }

    private void saveAndAdjustWorldBorder() {
        saveBlockCount();
        adjustWorldBorder();
    }

    private void saveBlockCount() {
        FileConfiguration config = plugin.getConfig();
        config.set(BLOCK_COUNT_CONFIG_KEY, blockCount);
        plugin.saveConfig();
        spawnthings();
    }

    public long getBlockCount() {
        return blockCount;
    }

    public void setBlockCount(long blockCount) {
        this.blockCount = blockCount;
        saveAndAdjustWorldBorder();
    }

    public double getWorldBorderSize() {
        return worldBorder.getSize();
    }

    public double getExpansionPercentage() {
        return expansionPercentage;
    }

    public void setExpansionPercentage(double percentage) {
        if (percentage <= 0) {
            throw new IllegalArgumentException("Percentage must be greater than 0");
        }

        this.expansionPercentage = percentage;

        FileConfiguration config = plugin.getConfig();
        config.set(EXPANSION_PERCENTAGE_CONFIG_KEY, percentage);
        plugin.saveConfig();

        adjustWorldBorder();
    }
}