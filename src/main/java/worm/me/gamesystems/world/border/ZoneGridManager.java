package worm.me.gamesystems.world.border;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import worm.me.Server;
import worm.me.gamesystems.world.PlayArea;

import java.util.ArrayList;
import java.util.List;

public class ZoneGridManager {

    private BorderVisuals borderVisuals;

    private final int GRID_SIZE = 5;
    private final int CELL_SIZE = 500;

    private final int[][] grid = {
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 1, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0}
    };

    private final List<String> gridRegionIds = new ArrayList<>();

    public ZoneGridManager() {
    }

    public void setBorderVisuals(BorderVisuals borderVisuals) {
        this.borderVisuals = borderVisuals;
    }
    public void initialize() {
        loadGrid();
    }

    public void setGridValue(int row, int col, int value) {
        if (row < 0 || row >= GRID_SIZE || col < 0 || col >= GRID_SIZE) return;
        grid[row][col] = value;
        saveGrid();

        if (borderVisuals != null) {
            borderVisuals.refreshWalls();
        }
    }

    public int getGridValue(int row, int col) {
        if (row < 0 || row >= GRID_SIZE || col < 0 || col >= GRID_SIZE) return -1;
        return grid[row][col];
    }

    public int[][] getGrid()  { return grid; }
    public int getCellSize()  { return CELL_SIZE; }

    public int getGridSize() { return GRID_SIZE; }

    public void saveGrid() {
        FileConfiguration config = Server.getPlugin().getConfig();
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                config.set("zoneGrid." + row + "_" + col, grid[row][col]);
            }
        }
        Server.getPlugin().saveConfig();
    }

    public void loadGrid() {
        FileConfiguration config = Server.getPlugin().getConfig();
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                String key = "zoneGrid." + row + "_" + col;
                if (config.contains(key)) {
                    grid[row][col] = config.getInt(key);
                }
            }
        }
    }

    public boolean isInAllowedGrid(Location loc) {
        if (loc.getWorld() != PlayArea.getPlayWorld()) {
            return true;
        }
        int start = -(GRID_SIZE * CELL_SIZE) / 2;
        int end   = start + (GRID_SIZE * CELL_SIZE);
        if (loc.getBlockX() < start || loc.getBlockX() >= end ||
                loc.getBlockZ() < start || loc.getBlockZ() >= end) {
            return false;
        }
        int col = (loc.getBlockX() - start) / CELL_SIZE;
        int row = (loc.getBlockZ() - start) / CELL_SIZE;
        if (row < 0 || row >= GRID_SIZE || col < 0 || col >= GRID_SIZE) return false;
        return grid[row][col] == 1;
    }

    public Location getClosestAllowedLocation(Location loc) {
        int start = -(GRID_SIZE * CELL_SIZE) / 2;
        double bestDist = Double.MAX_VALUE;
        Location bestLoc = null;
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (grid[row][col] != 1) continue;
                double clampedX = Math.max(start + col * CELL_SIZE,
                        Math.min(start + col * CELL_SIZE + CELL_SIZE, loc.getX()));
                double clampedZ = Math.max(start + row * CELL_SIZE,
                        Math.min(start + row * CELL_SIZE + CELL_SIZE, loc.getZ()));
                double dx = loc.getX() - clampedX;
                double dz = loc.getZ() - clampedZ;
                double dist = dx * dx + dz * dz;
                if (dist < bestDist) {
                    bestDist = dist;
                    bestLoc = new Location(loc.getWorld(), clampedX, loc.getY(), clampedZ);
                }
            }
        }
        return bestLoc;
    }

    public void handleSetGrid(CommandSender sender, String[] args) {
        if (args.length != 3) {
            sender.sendMessage("Usage: /setbordergrid <row> <col> <0|1>");
            return;
        }
        try {
            int row   = Integer.parseInt(args[0]);
            int col   = Integer.parseInt(args[1]);
            int value = Integer.parseInt(args[2]);
            if (value != 0 && value != 1) {
                sender.sendMessage("Value must be 0 or 1.");
                return;
            }
            if (getGridValue(row, col) == -1) {
                sender.sendMessage("Invalid grid position.");
                return;
            }
            setGridValue(row, col, value);
            sender.sendMessage("Set grid (" + row + ", " + col + ") to " + value);
        } catch (NumberFormatException e) {
            sender.sendMessage("Row, column, and value must be numbers.");
        }
    }

    public void handleGetGrid(CommandSender sender) {
        sender.sendMessage("Current Border Grid:");
        for (int row = 0; row < grid.length; row++) {
            StringBuilder line = new StringBuilder();
            for (int col = 0; col < grid[row].length; col++) {
                line.append(grid[row][col] == 1 ? "§a1 " : "§c0 ");
            }
            sender.sendMessage(line.toString());
        }
    }
}