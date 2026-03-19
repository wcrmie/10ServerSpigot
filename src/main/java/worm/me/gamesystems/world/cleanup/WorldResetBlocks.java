package worm.me.gamesystems.world.cleanup;

import org.bukkit.*;
import org.bukkit.block.Block;
import worm.me.Server;
import worm.me.gamesystems.world.PlayArea;
import worm.me.gamesystems.world.border.ZoneGridManager;

public class WorldResetBlocks {

    private static final int FULL_SIZE = Server.getZoneGridManager().getCellSize() * Server.getZoneGridManager().getGridSize();

    private static final int GRID_HALF = (FULL_SIZE / 2) + 30;

    private final World world;
    private static boolean isResetting = false;
    private int taskId = -1;
    private int currentLayer;

    public WorldResetBlocks() {
        this.world = PlayArea.getPlayWorld();
    }

    public static boolean isResetting() {
        return isResetting;
    }

    public void startBlockReset() {
        if (isResetting) return;
        this.currentLayer = 320;
        isResetting = true;
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(
                Server.getPlugin(), this::processLayer, 0L, 1L);
    }

    private void processLayer() {
        if (currentLayer < -64) {
            completeReset();
            return;
        }

        final int EXCL_MIN_X = -3,  EXCL_MAX_X = 3;
        final int EXCL_MIN_Y = 210, EXCL_MAX_Y = 224;
        final int EXCL_MIN_Z = -3,  EXCL_MAX_Z = 3;

        for (int x = -GRID_HALF; x <= GRID_HALF; x++) {
            for (int z = -GRID_HALF; z <= GRID_HALF; z++) {

                if (currentLayer >= EXCL_MIN_Y && currentLayer <= EXCL_MAX_Y
                        && x >= EXCL_MIN_X && x <= EXCL_MAX_X
                        && z >= EXCL_MIN_Z && z <= EXCL_MAX_Z) {
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