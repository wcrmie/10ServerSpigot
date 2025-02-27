package worm.me.WorldBorder;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.configuration.file.FileConfiguration;
import worm.me.server;

public class worldBorderSizeHandler {
    private final server plugin;
    private final World world = Bukkit.getWorld("ul_Void");
    private final WorldBorder worldBorder = world.getWorldBorder();
    private long blockCount;
    private final int baseSize;
    private double expansionPercentage;

    public worldBorderSizeHandler(server plugin) {
        this.plugin = plugin;
        this.baseSize = 20;
        this.blockCount = 0;

        // Load expansion percentage from config, default to 0.25%
        FileConfiguration config = plugin.getConfig();
        this.expansionPercentage = config.getDouble("expansionPercentage", 0.25);

        worldBorder.setCenter(0.0, 0.0);
        worldBorder.setSize(baseSize);
    }

    public void adjustWorldBorder() {
        long worldBlockSize = (long) (worldBorder.getSize() * worldBorder.getSize() * 60); // 60 is world height


        int newSize = (int) (baseSize + (blockCount / (worldBlockSize * (expansionPercentage / 100.0))));

        if (newSize > worldBorder.getSize() && newSize % 2 == 0) {
            worldBorder.setSize(newSize);
        } else if (newSize > worldBorder.getSize() && newSize % 2 != 0) {
            worldBorder.setSize(newSize - 1);
        }

    }

    public void incrementBlockCount() {
        Bukkit.getScheduler().runTask(plugin, () -> {
            blockCount++;
            adjustWorldBorder();
        });
    }

    public void decrementBlockCount() {
        if (blockCount > 0) {
            blockCount--;
        }
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