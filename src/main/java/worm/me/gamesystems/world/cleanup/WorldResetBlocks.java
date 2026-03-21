package worm.me.gamesystems.world.cleanup;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.Vector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.world.biome.BiomeType;
import org.bukkit.*;
import org.bukkit.block.Block;
import worm.me.Server;
import worm.me.gamesystems.world.PlayArea;
import worm.me.gamesystems.world.border.ZoneGridManager;

import java.util.ArrayList;
import java.util.List;

public class WorldResetBlocks {

    private static final int EXCL_MIN_X = -3,  EXCL_MAX_X = 3;
    private static final int EXCL_MIN_Y = 210, EXCL_MAX_Y = 224;
    private static final int EXCL_MIN_Z = -3,  EXCL_MAX_Z = 3;

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
            },            {
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0},
                    {1, 1, 1, 1, 1},
                    {0, 0, 0, 0, 0}
            },            {
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

    public WorldResetBlocks() {
        this.world = PlayArea.getPlayWorld();
    }

    public static boolean isResetting() { return isResetting; }

    public void startBlockReset() {
        if (isResetting) return;

        currentPhase = 0;
        isResetting  = true;

        resetBiome();

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

                        if (currentLayer >= EXCL_MIN_Y && currentLayer <= EXCL_MAX_Y
                                && x >= EXCL_MIN_X && x <= EXCL_MAX_X
                                && z >= EXCL_MIN_Z && z <= EXCL_MAX_Z) continue;

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

    private void resetBiome() {

        Bukkit.getAsyncScheduler().runNow(Server.getPlugin(), $ -> {

                CuboidRegion globalRegion = new CuboidRegion(BlockVector3.at(1300, 180, 1300), BlockVector3.at(-1300, 300, -1300));

                World world = PlayArea.getPlayWorld();

                try (EditSession editSession = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(world))) {

                    for (BlockVector3 block : globalRegion) {

                        editSession.setBiome(block, BiomeType.REGISTRY.get("minecraft:meadow"));

                    }

                    editSession.commit();

                }


            }
        );
    }

    private void finishReset() {
        Bukkit.getScheduler().cancelTask(taskId);
        isResetting = false;
        WorldReset.getInstance().completeReset();
    }
}