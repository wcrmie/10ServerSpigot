package worm.me.gamesystems.world.cleanup;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.*;
import org.bukkit.block.Block;
import worm.me.Server;
import worm.me.gamesystems.world.PlayArea;
import worm.me.gamesystems.world.border.ZoneGridManager;

import java.util.ArrayList;
import java.util.List;

public class WorldResetBlocks {

    private static final int[][][] PHASES = {
            {
                    {1, 1, 1, 1, 1},
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0}
            },
            {
                    {0, 0, 0, 0, 0},
                    {1, 1, 1, 1, 1},
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0}
            },
            {
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0},
                    {1, 1, 1, 1, 1},
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0}
            },
            {
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0},
                    {1, 1, 1, 1, 1},
                    {0, 0, 0, 0, 0}
            },
            {
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0},
                    {1, 1, 1, 1, 1}
            }
    };

    private final World world;
    private static boolean isResetting = false;
    private int taskId = -1;
    private int currentPhase;
    private int currentLayer;
    private List<int[]> phaseCellBounds;
    private ProtectedRegion spawnRegion;

    public WorldResetBlocks() {
        this.world = PlayArea.getPlayWorld();
    }

    public static boolean isResetting() { return isResetting; }

    private ProtectedRegion fetchSpawnRegion() {
        RegionManager rm = WorldGuard.getInstance()
                .getPlatform()
                .getRegionContainer()
                .get(BukkitAdapter.adapt(world));
        if (rm == null) return null;
        return rm.getRegion("spawn");
    }

    private boolean isInSpawnRegion(int x, int y, int z) {
        return spawnRegion != null && spawnRegion.contains(x, y, z);
    }

    public void startBlockReset() {
        if (isResetting) return;
        spawnRegion  = fetchSpawnRegion();
        currentPhase = 0;
        isResetting  = true;
        loadPhase(currentPhase);
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(
                Server.getPlugin(), this::tick, 0L, 1L);
    }

    private void tick() {
        if (currentLayer < 190) {
            currentPhase++;
            if (currentPhase >= PHASES.length) {
                finishReset();
                return;
            }
            loadPhase(currentPhase);
        }

        int layersThisTick = 1;
        for (int i = 0; i < layersThisTick; i++) {
            if (currentLayer < 190) break;
            for (int[] bounds : phaseCellBounds) {
                int minX = bounds[0], maxX = bounds[1];
                int minZ = bounds[2], maxZ = bounds[3];
                for (int x = minX; x <= maxX; x++) {
                    for (int z = minZ; z <= maxZ; z++) {
                        if (!world.isChunkLoaded(x >> 4, z >> 4)) world.loadChunk(x >> 4, z >> 4);
                        if (isInSpawnRegion(x, currentLayer, z)) continue;
                        Block block = world.getBlockAt(x, currentLayer, z);
                        if (!block.getType().isAir()) {
                            block.setType(Material.AIR, false);
                        }
                    }
                }
            }
            currentLayer--;
        }
    }

    private void loadPhase(int phase) {
        ZoneGridManager zgm = Server.getZoneGridManager();
        int cellSize        = zgm.getCellSize();
        int gridSize        = zgm.getGridSize();
        int worldStart      = -(gridSize * cellSize) / 2;
        int[][] phaseGrid   = PHASES[phase];
        phaseCellBounds     = new ArrayList<>();

        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                if (phaseGrid[row][col] != 1) continue;
                int minX = worldStart + col * cellSize;
                int maxX = minX + cellSize - 1;
                int minZ = worldStart + row * cellSize;
                int maxZ = minZ + cellSize - 1;
                phaseCellBounds.add(new int[]{minX, maxX, minZ, maxZ});
            }
        }
        currentLayer = 320;
    }

    private void finishReset() {
        Bukkit.getScheduler().cancelTask(taskId);
        isResetting  = false;
        spawnRegion  = null;
        WorldReset.getInstance().completeReset();
    }
}