package worm.me.WorldBorder;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.configuration.file.FileConfiguration;
import worm.me.Server;


public class WorldBorderSizeHandler {
    private final Server plugin;
    private World world;
    private WorldBorder worldBorder;
    private long blockCount;
    private final int baseSize;
    private double expansionPercentage;

    public WorldBorderSizeHandler(Server plugin) {
        this.plugin = plugin;
        this.baseSize = 20;
        this.world = Bukkit.getWorld("ul_Void");
        this.worldBorder = world.getWorldBorder();
        this.worldBorder.setCenter(0.5, 0.5);

        FileConfiguration config = plugin.getConfig();
        this.expansionPercentage = config.getDouble("expansionPercentage", 0.25);
        this.blockCount = config.getLong("blockCount", 0);
    }

    public void adjustWorldBorder() {
        long worldBlockSize = (long) (worldBorder.getSize() * worldBorder.getSize() * 75); // 75 is world height


        int newSize = (int) (baseSize + (blockCount / (worldBlockSize * (expansionPercentage / 100.0))));

        if (newSize > worldBorder.getSize() && newSize % 2 == 0) {
            worldBorder.setSize(newSize);
        } else if (newSize > worldBorder.getSize() && newSize % 2 != 0) {
            worldBorder.setSize(newSize - 1);
        }

    }

    public void incrementBlockCount() {
        blockCount++;
        saveBlockCount();
        adjustWorldBorder();
    }

    public void decrementBlockCount() {
        blockCount--;
        saveBlockCount();
    }

    public void saveBlockCount() {
        FileConfiguration config = plugin.getConfig();
        config.set("blockCount", blockCount);
        plugin.saveConfig();
    }

    public long getBlockCount() {
        return blockCount;
    }

    public double getWorldBorderSize() {
        return worldBorder.getSize();
    }

    public void setBlockCount(long blockCount) {
        this.blockCount = blockCount;
        adjustWorldBorder();
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
        config.set("expansionPercentage", percentage);
        plugin.saveConfig();

        adjustWorldBorder();
    }
}