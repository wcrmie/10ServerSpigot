package worm.me.gamesystems.world.cleanup;

import org.bukkit.*;
import org.bukkit.block.Block;
import worm.me.Server;

public class WorldResetBlocks {
    private final World world;
    private int originalBorderSize;
    private int currentLayer;
    private static boolean isResetting = false;
    private int taskId = -1;
    private Location originalCenter;

    public WorldResetBlocks() {
        this.world = Bukkit.getWorld("ul_Void");
    }

    public void startBlockReset(int originalSize, Location originalCenter) {
        if (isResetting) return;

        this.originalBorderSize = originalSize;
        this.originalCenter = originalCenter;
        this.currentLayer = 280 + 40;
        isResetting = true;

        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Server.getPlugin(), this::processLayer, 0L, 1L);


    }

    private void processLayer() {
        int worldHeightBottom = -64;
        if (currentLayer < worldHeightBottom) {
            completeReset();
            return;
        }

        int clearanceRadius = (originalBorderSize / 2) + 50;
        int minX = (int) (originalCenter.getX() - clearanceRadius);
        int maxX = (int) (originalCenter.getX() + clearanceRadius);
        int minZ = (int) (originalCenter.getZ() - clearanceRadius);
        int maxZ = (int) (originalCenter.getZ() + clearanceRadius);

        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                int minExcludeX = -3;
                int maxExcludeX = 3;
                int minExcludeY = 210;
                int maxExcludeY = 224;
                int minExcludeZ = -3;
                int maxExcludeZ = 3;
                if (currentLayer >= minExcludeY && currentLayer <= maxExcludeY &&
                        x >= minExcludeX && x <= maxExcludeX &&
                        z >= minExcludeZ && z <= maxExcludeZ) {
                    continue;
                }

                Block block = world.getBlockAt(x, currentLayer, z);
                if (!block.getType().isAir()) {
                    block.setType(Material.AIR, false);
                }
            }
        }

        currentLayer--;
    }

    private void completeReset() {
        Bukkit.getScheduler().cancelTask(taskId);
        isResetting = false;
        WorldReset.getInstance().completeReset();
    }
}