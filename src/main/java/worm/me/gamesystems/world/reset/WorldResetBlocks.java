package worm.me.gamesystems.world.reset;

import org.bukkit.*;
import org.bukkit.block.Block;
import worm.me.Server;

public class WorldResetBlocks {
    private final World world;
    private int originalBorderSize;
    private final int worldHeightTop = 280+40;
    private final int worldHeightBottom = -64;
    private int currentLayer;
    private static boolean isResetting = false;
    private int taskId = -1;
    private Location originalCenter;

    private final int minExcludeX = -4;
    private final int maxExcludeX = 4;
    private final int minExcludeY = 218;
    private final int maxExcludeY = 224;
    private final int minExcludeZ = -4;
    private final int maxExcludeZ = 4;

    public WorldResetBlocks() {
        this.world = Bukkit.getWorld("ul_Void");
    }

    public void startBlockReset(int originalSize, Location originalCenter) {
        if (isResetting) return;

        this.originalBorderSize = originalSize;
        this.originalCenter = originalCenter;
        this.currentLayer = worldHeightTop;
        this.isResetting = true;

        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Server.getPlugin(), this::processLayer, 0L, 1L);


    }

    private void processLayer() {
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