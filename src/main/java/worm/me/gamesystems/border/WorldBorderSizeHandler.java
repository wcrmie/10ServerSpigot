package worm.me.gamesystems.border;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import worm.me.gamesystems.structure.generation.Island;
import worm.me.Server;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class WorldBorderSizeHandler {

    private static final String WORLD_NAME = "ul_Void";
    private static final String BLOCK_COUNT_CONFIG_KEY = "blockCount";
    private static final String EXPANSION_PERCENTAGE_CONFIG_KEY = "expansionPercentage";

    private final Server plugin = Server.getPlugin();
    private final WorldBorder worldBorder;

    private static long blockCount;
    private double expansionPercentage;

    public WorldBorderSizeHandler(Server plugin) {

        World world = Bukkit.getWorld(WORLD_NAME);
        if (world == null) {
            throw new IllegalStateException("World '" + WORLD_NAME + "' not found");
        }

        worldBorder = world.getWorldBorder();
        worldBorder.setCenter(0.5, 0.5);

        FileConfiguration config = plugin.getConfig();
        blockCount = config.getLong(BLOCK_COUNT_CONFIG_KEY, 0);
        this.expansionPercentage = config.getDouble(EXPANSION_PERCENTAGE_CONFIG_KEY, 0.25);
    }


    public void adjustWorldBorder() {
        long worldBlockSize = calculateWorldBlockSize();
        if ((calculateNewSize(worldBlockSize) >= 1)) {
            int newSize = (int) (worldBorder.getSize() + 1);
            if (newSize % 2 == 0) {
                newSize += 1;
            }
            worldBorder.setSize(newSize);
        }
    }

    int lastSpawnDone = 0;
    public void spawnthings() {
        if ((blockCount % 5000)==0 && blockCount != lastSpawnDone) {
            lastSpawnDone = (int) blockCount;

            Island.spawnIslandType("DEFAULT");
        }
    }

    public long calculateWorldBlockSize() {
        return (long) (worldBorder.getSize() * worldBorder.getSize() * 60);
    }

    private int calculateNewSize(long worldBlockSize) {

        return (int) (blockCount / (worldBlockSize * (expansionPercentage / 100.0)));
    }

    private static final Set<Material> bannedBlocks = new HashSet<>(Arrays.asList(
            Material.OAK_LOG,
            Material.SPRUCE_LOG,
            Material.BIRCH_LOG,
            Material.JUNGLE_LOG,
            Material.ACACIA_LOG,
            Material.DARK_OAK_LOG,
            Material.MANGROVE_LOG,
            Material.CHERRY_LOG,
            Material.STRIPPED_OAK_LOG,
            Material.STRIPPED_SPRUCE_LOG,
            Material.STRIPPED_BIRCH_LOG,
            Material.STRIPPED_JUNGLE_LOG,
            Material.STRIPPED_ACACIA_LOG,
            Material.STRIPPED_DARK_OAK_LOG,
            Material.STRIPPED_MANGROVE_LOG,
            Material.STRIPPED_CHERRY_LOG,

            Material.OAK_LEAVES,
            Material.SPRUCE_LEAVES,
            Material.BIRCH_LEAVES,
            Material.JUNGLE_LEAVES,
            Material.ACACIA_LEAVES,
            Material.DARK_OAK_LEAVES,
            Material.MANGROVE_LEAVES,
            Material.CHERRY_LEAVES,

            Material.SEAGRASS,
            Material.TALL_SEAGRASS,

            Material.AZALEA,
            Material.FLOWERING_AZALEA,
            Material.MOSS_CARPET,

            Material.BAMBOO,

            Material.SUGAR_CANE,
            Material.CACTUS,
            Material.VINE,

            Material.ICE,
            Material.SNOW,

            Material.DANDELION,
            Material.POPPY,
            Material.BLUE_ORCHID,
            Material.ALLIUM,
            Material.AZURE_BLUET,
            Material.RED_TULIP,
            Material.ORANGE_TULIP,
            Material.WHITE_TULIP,
            Material.PINK_TULIP,
            Material.OXEYE_DAISY,
            Material.CORNFLOWER,
            Material.LILY_OF_THE_VALLEY,
            Material.WITHER_ROSE,
            Material.SUNFLOWER,
            Material.LILAC,
            Material.ROSE_BUSH,
            Material.PEONY,

            Material.SHORT_GRASS,
            Material.TALL_GRASS,
            Material.FERN,
            Material.LARGE_FERN,

            Material.BROWN_MUSHROOM_BLOCK,
            Material.RED_MUSHROOM_BLOCK,
            Material.MUSHROOM_STEM,

            Material.POINTED_DRIPSTONE,

            Material.BROWN_MUSHROOM,
            Material.RED_MUSHROOM,

            Material.TWISTING_VINES,
            Material.TWISTING_VINES_PLANT,
            Material.WEEPING_VINES,
            Material.WEEPING_VINES_PLANT
    ));


    public void incrementBlockCount(BlockPlaceEvent event) {
        if (!bannedBlocks.contains(event.getBlock().getType())) {
            blockCount++;
            saveAndAdjustWorldBorder();
        }
    }

    public void decrementBlockCount(BlockBreakEvent event) {
        if (!bannedBlocks.contains(event.getBlock().getType())) {
            blockCount--;
            saveBlockCount();
        }
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