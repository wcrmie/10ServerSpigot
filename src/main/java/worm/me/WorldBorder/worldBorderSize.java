package worm.me.WorldBorder;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;

public class worldBorderSize {
    private final JavaPlugin plugin;
    private final WorldBorder worldBorder;
    private long blockCount;
    private final int baseSize;
    private double expansionPercentage;

    public worldBorderSize(JavaPlugin plugin, World world) {
        this.plugin = plugin;
        this.worldBorder = world.getWorldBorder();
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