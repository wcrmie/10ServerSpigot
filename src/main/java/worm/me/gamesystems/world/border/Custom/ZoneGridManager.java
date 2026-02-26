package worm.me.gamesystems.world.border.Custom;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.flags.StateFlag;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import worm.me.Server;
import worm.me.utils.world.WGUtil;

import java.util.ArrayList;
import java.util.List;

public class ZoneGridManager {

    private final Server plugin;

    private BorderVisuals borderVisuals;

    public void setBorderVisuals(BorderVisuals borderVisuals) {
        this.borderVisuals = borderVisuals;
    }

    private final int GRID_SIZE = 5;
    private final int CELL_SIZE = 50;

    private final int[][] grid = {
            {0,0,1,0,0},
            {0,0,1,0,0},
            {0,0,1,0,0},
            {0,0,0,0,0},
            {0,0,0,0,0}
    };

    private final List<String> gridRegionIds = new ArrayList<>();

    public ZoneGridManager(Server plugin) {
        this.plugin = plugin;
    }

    public void initialize() {
        World world = Bukkit.getWorld("ul_Void");
        if (world == null) return;

        RegionManager manager = WGUtil.getRegionManager(world);
        if (manager == null) return;

        createMainRegion(manager);
        createGrid(manager);

        try { manager.save(); } catch (Exception ignored) {}
    }

    private void applyFlags(ProtectedRegion region, boolean allowed) {
        StateFlag.State state = allowed ? StateFlag.State.ALLOW : StateFlag.State.DENY;
        region.setFlag(com.sk89q.worldguard.protection.flags.Flags.BLOCK_PLACE, state);
        region.setFlag(com.sk89q.worldguard.protection.flags.Flags.BLOCK_BREAK, state);
        region.setFlag(com.sk89q.worldguard.protection.flags.Flags.USE, state);
    }

    private void createMainRegion(RegionManager manager) {
        if (manager.getRegion("world_main") != null) return;

        ProtectedCuboidRegion region = new ProtectedCuboidRegion(
                "world_main",
                BlockVector3.at(-10000, -64, -10000),
                BlockVector3.at(10000, 320, 10000)
        );
        region.setPriority(0);
        applyFlags(region, false);
        manager.addRegion(region);
    }

    private void createGrid(RegionManager manager) {
        int start = -(GRID_SIZE * CELL_SIZE) / 2;

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                String id = "grid_" + row + "_" + col;
                if (manager.getRegion(id) != null) continue;

                gridRegionIds.add(id);

                int minX = start + col * CELL_SIZE;
                int minZ = start + row * CELL_SIZE;

                ProtectedCuboidRegion region = new ProtectedCuboidRegion(
                        id,
                        BlockVector3.at(minX, -64, minZ),
                        BlockVector3.at(minX + CELL_SIZE, 320, minZ + CELL_SIZE)
                );

                region.setPriority(1);
                applyFlags(region, grid[row][col] == 1);
                manager.addRegion(region);
            }
        }
    }

    public void removeGridRegions() {
        World world = Bukkit.getWorld("ul_Void");
        if (world == null) return;

        RegionManager manager = WGUtil.getRegionManager(world);
        if (manager == null) return;

        for (String id : gridRegionIds) {
            manager.removeRegion(id);
        }

        gridRegionIds.clear();

        try { manager.save(); } catch (Exception ignored) {}
    }


    public boolean isInAllowedGrid(Location loc) {
        if (loc.getWorld() == null || !loc.getWorld().getName().equals("ul_void")) {
            return true;
        }

        int start = -(GRID_SIZE * CELL_SIZE) / 2;
        int end = start + (GRID_SIZE * CELL_SIZE);

        if (loc.getBlockX() < start || loc.getBlockX() >= end ||
                loc.getBlockZ() < start || loc.getBlockZ() >= end) {
            return false;
        }

        int relativeX = loc.getBlockX() - start;
        int relativeZ = loc.getBlockZ() - start;

        int col = relativeX / CELL_SIZE;
        int row = relativeZ / CELL_SIZE;

        if (row < 0 || row >= GRID_SIZE || col < 0 || col >= GRID_SIZE) {
            return false;
        }

        return grid[row][col] == 1;
    }

    public void setGridValue(int row, int col, int value) {
        if (row < 0 || row >= GRID_SIZE || col < 0 || col >= GRID_SIZE) return;
        grid[row][col] = value;

        World world = Bukkit.getWorld("ul_Void");
        if (world != null) {
            RegionManager manager = WGUtil.getRegionManager(world);
            if (manager != null) {
                String id = "grid_" + row + "_" + col;
                ProtectedRegion region = manager.getRegion(id);
                if (region != null) {
                    applyFlags(region, value == 1);
                    try { manager.save(); } catch (Exception ignored) {}
                }
            }
        }

        if (borderVisuals != null) {
            borderVisuals.refreshWalls();
        }
    }

    public int getGridValue(int row, int col) {
        if (row < 0 || row >= GRID_SIZE || col < 0 || col >= GRID_SIZE) return -1;
        return grid[row][col];
    }

    public int[][] getGrid() { return grid; }
    public int getCellSize() { return CELL_SIZE; }

    public void handleSetGrid(CommandSender sender, String[] args) {

        if (args.length != 3) {
            sender.sendMessage("Usage: /setbordergrid <row> <col> <0|1>");
            return;
        }

        try {
            int row = Integer.parseInt(args[0]);
            int col = Integer.parseInt(args[1]);
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
                if (grid[row][col] == 1) {
                    line.append("§a1 ");
                } else {
                    line.append("§c0 ");
                }
            }

            sender.sendMessage(line.toString());
        }
    }

    public Location getClosestAllowedLocation(Location loc) {
        int start = -(GRID_SIZE * CELL_SIZE) / 2;
        double bestDist = Double.MAX_VALUE;
        Location bestLoc = null;

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (grid[row][col] != 1) continue;

                double cellCenterX = start + col * CELL_SIZE + CELL_SIZE / 2.0;
                double cellCenterZ = start + row * CELL_SIZE + CELL_SIZE / 2.0;

                double clampedX = Math.max(start + col * CELL_SIZE, Math.min(start + col * CELL_SIZE + CELL_SIZE, loc.getX()));
                double clampedZ = Math.max(start + row * CELL_SIZE, Math.min(start + row * CELL_SIZE + CELL_SIZE, loc.getZ()));

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
}