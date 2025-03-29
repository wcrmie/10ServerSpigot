package worm.me.gamesystems.worldreset;

import org.bukkit.*;
import org.bukkit.block.Block;
import worm.me.Server;

public class WorldResetBlocks {
    private final World world;
    private int originalBorderSize;
    private final int worldHeightTop = 280+40;
    private final int worldHeightBottom = 220-10;
    private int currentLayer;
    private boolean isResetting = false;
    private int taskId = -1;
    private Location originalCenter;

    private final int minExcludeX = -6;
    private final int maxExcludeX = 6;
    private final int minExcludeY = 238;
    private final int maxExcludeY = 245;
    private final int minExcludeZ = -6;
    private final int maxExcludeZ = 6;

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

    public void cancelReset() {
        if (isResetting) {
            Bukkit.getScheduler().cancelTask(taskId);
            isResetting = false;
        }
    }

    public boolean isResetting() {
        return isResetting;
    }
}