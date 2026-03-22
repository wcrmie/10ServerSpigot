package worm.me.gamesystems.world;

import org.bukkit.configuration.file.FileConfiguration;
import worm.me.Server;
import worm.me.gamesystems.world.cleanup.SpawnReset;
import worm.me.gamesystems.world.cleanup.WorldReset;
import worm.me.utils.chat.Message;
import worm.me.utils.chat.MessageType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class WorldTimer {

    private static WorldTimer instance;
    private final Server plugin;

    private static final long SPAWN_RESET_INTERVAL = 3L * 60 * 60; // 3 hours
    private static final long EXPANSION_INTERVAL = 6L * 60 * 60;   // 6 hours
    private static final long FULL_GRID_DELAY    = 12L * 60 * 60;  // 12 hours

    private long expansionSecs = 0;
    private long fullGridSecs  = 0;
    private long spawnResetSecs = 0;
    private boolean gridFull   = false;

    private boolean resetting = false;

    private boolean oneHourWarn = false;
    private boolean fiveMinWarn = false;
    private boolean oneMinWarn  = false;


    public static WorldTimer getInstance() {
        if (instance == null) {
            instance = new WorldTimer(Server.getPlugin());
        }
        return instance;
    }

    private WorldTimer(Server plugin) {
        this.plugin = plugin;
        loadData();
    }

    public void start() {
        loadData();

        plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {

            // ---------- SPAWN RESET ----------

            if (resetting) return;

            if (spawnResetSecs <= SPAWN_RESET_INTERVAL) {

                spawnResetSecs++;

            } else {

                spawnResetSecs = 0;
                SpawnReset.replaceSpawn();

            }

            // ---------- END SPAWN RESET ----------

            if (!gridFull) {
                expansionSecs++;

                if (expansionSecs >= EXPANSION_INTERVAL) {
                    expansionSecs = 0;
                    boolean nowFull = expandGrid();

                    if (nowFull) {
                        gridFull = true;
                        Message.sendAll(Message.create(MessageType.GENERIC,
                                "The border is now fully open! World resets in 12 hours."));
                    }
                    saveData();
                }

            } else {
                fullGridSecs++;
                long remaining = FULL_GRID_DELAY - fullGridSecs;

                if (remaining <= 60L * 60 && !oneHourWarn) {
                    Message.sendAll(Message.create(MessageType.GENERIC,
                            "The world is projected to reset in 1 hour!"));
                    oneHourWarn = true;
                    saveData();
                }
                if (remaining <= 60L * 5 && !fiveMinWarn) {
                    Message.sendAll(Message.create(MessageType.GENERIC,
                            "The world is projected to reset in 5 minutes!"));
                    fiveMinWarn = true;
                    saveData();
                }
                if (remaining <= 60L && !oneMinWarn) {
                    Message.sendAll(Message.create(MessageType.GENERIC,
                            "The world is projected to reset in 1 minute!"));
                    oneMinWarn = true;
                    saveData();
                }

                if (fullGridSecs >= FULL_GRID_DELAY) {
                    resetting = true;
                    saveData();
                    WorldReset.getInstance().triggerReset(null);
                    return;
                }

                saveData();
            }

        }, 0L, 20L);

        plugin.getServer().getScheduler().runTaskTimer(plugin, this::saveData, 0L, 15 * 60 * 20L);
    }

    private boolean expandGrid() {
        int[][] grid = Server.zoneGridManager.getGrid();
        int size = grid.length;
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        List<int[]> candidates = new ArrayList<>();
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (grid[row][col] != 0) continue;
                for (int[] d : directions) {
                    int nr = row + d[0];
                    int nc = col + d[1];
                    if (nr >= 0 && nr < size && nc >= 0 && nc < size && grid[nr][nc] == 1) {
                        candidates.add(new int[]{row, col});
                        break;
                    }
                }
            }
        }

        if (candidates.isEmpty()) {
            return isGridFull(grid);
        }

        int[] chosen = candidates.get(ThreadLocalRandom.current().nextInt(candidates.size()));
        Server.zoneGridManager.setGridValue(chosen[0], chosen[1], 1);

        int[][] updated = Server.zoneGridManager.getGrid();
        Message.sendAll(Message.create(MessageType.GENERIC,
                "The border has expanded! (" + countOpen(updated) + " of " + (size * size) + " zones open)"));

        return isGridFull(updated);
    }

    private boolean isGridFull(int[][] grid) {
        for (int[] row : grid)
            for (int v : row)
                if (v == 0) return false;
        return true;
    }

    private int countOpen(int[][] grid) {
        int count = 0;
        for (int[] row : grid)
            for (int v : row)
                if (v == 1) count++;
        return count;
    }

    public long secondsUntilNextExpansion() {
        return gridFull ? -1 : EXPANSION_INTERVAL - expansionSecs;
    }

    public long secondsUntilReset() {
        return gridFull ? FULL_GRID_DELAY - fullGridSecs : -1;
    }

    public boolean isGridFull() {
        return gridFull;
    }

    public boolean isResetting() {
        return resetting;
    }


    public void resetTimer() {
        this.expansionSecs = 0;
        this.fullGridSecs  = 0;
        this.gridFull      = false;
        this.resetting     = false;
        this.oneHourWarn   = false;
        this.fiveMinWarn   = false;
        this.oneMinWarn    = false;
        saveData();
    }

    public void saveData() {
        FileConfiguration config = plugin.getConfig();
        config.set("worldTimer.expansionSecs", expansionSecs);
        config.set("worldTimer.fullGridSecs",  fullGridSecs);
        config.set("worldTimer.gridFull",      gridFull);
        config.set("worldTimer.resetting",     resetting);
        config.set("worldTimer.oneHourWarn",   oneHourWarn);
        config.set("worldTimer.fiveMinWarn",   fiveMinWarn);
        config.set("worldTimer.oneMinWarn",    oneMinWarn);
        plugin.saveConfig();
    }

    public void loadData() {
        FileConfiguration config = plugin.getConfig();
        expansionSecs = config.getLong("worldTimer.expansionSecs", 0);
        fullGridSecs  = config.getLong("worldTimer.fullGridSecs",  0);
        gridFull      = config.getBoolean("worldTimer.gridFull",   false);
        resetting     = config.getBoolean("worldTimer.resetting",  false);
        oneHourWarn   = config.getBoolean("worldTimer.oneHourWarn", false);
        fiveMinWarn   = config.getBoolean("worldTimer.fiveMinWarn", false);
        oneMinWarn    = config.getBoolean("worldTimer.oneMinWarn",  false);
    }
}