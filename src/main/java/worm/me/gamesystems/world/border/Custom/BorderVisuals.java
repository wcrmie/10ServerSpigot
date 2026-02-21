package worm.me.gamesystems.world.border.Custom;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import worm.me.Server;

import java.util.ArrayList;
import java.util.List;

public class BorderVisuals {
    private final Server plugin;
    private final ZoneGridManager gridManager;
    private BukkitRunnable activeTask;

    private final List<WallSegment> wallSegments = new ArrayList<>();

    public BorderVisuals(Server plugin, ZoneGridManager gridManager) {
        this.plugin = plugin;
        this.gridManager = gridManager;
    }

    public void start() {

        if (activeTask != null) {
            activeTask.cancel();
            activeTask = null;
        }

        wallSegments.clear();

        World world = Bukkit.getWorld("ul_Void");
        if (world != null) {
            calculateWalls();
        }

        activeTask = new BukkitRunnable() {
            @Override
            public void run() {
                World world = Bukkit.getWorld("ul_Void");
                if (world == null) return;

                List<Player> players = world.getPlayers();
                if (players.isEmpty()) return;

                for (WallSegment seg : wallSegments) {

                    boolean segmentNearby = false;
                    for (Player player : players) {
                        Location loc = player.getLocation();

                        double px = loc.getX();
                        double py = loc.getY();
                        double pz = loc.getZ();

                        double closestX = seg.axisX ? clamp(px, seg.start, seg.start + seg.length) : seg.fixed;
                        double closestZ = seg.axisX ? seg.fixed : clamp(pz, seg.start, seg.start + seg.length);

                        double dx = px - closestX;
                        double dz = pz - closestZ;

                        if (dx * dx + dz * dz <= 512 * 512) {
                            segmentNearby = true;
                            break;
                        }
                    }

                    if (!segmentNearby) continue;

                    if (seg.axisX) {
                        spawnWallX(world, seg.start, seg.fixed, seg.length, players);
                    } else {
                        spawnWallZ(world, seg.fixed, seg.start, seg.length, players);
                    }
                }
            }
        };

        activeTask.runTaskTimer(plugin, 0L, 3L);
    }

    private double clamp(double val, double min, double max) {
        return Math.max(min, Math.min(max, val));
    }

    public void refreshWalls() {
        wallSegments.clear();
        calculateWalls();
    }

    private void calculateWalls() {
        int[][] grid = gridManager.getGrid();
        int size = grid.length;
        int cell = gridManager.getCellSize();
        int start = -(size * cell) / 2;

        wallSegments.clear();

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                int currentVal = grid[row][col];

                if (row + 1 < size) {
                    if (currentVal != grid[row + 1][col]) {
                        int z = start + (row + 1) * cell;
                        int minX = start + col * cell;
                        wallSegments.add(new WallSegment(minX, z, cell, true));
                    }
                } else if (currentVal == 1) {
                    int z = start + (row + 1) * cell;
                    int minX = start + col * cell;
                    wallSegments.add(new WallSegment(minX, z, cell, true));
                }

                if (col + 1 < size) {
                    if (currentVal != grid[row][col + 1]) {
                        int x = start + (col + 1) * cell;
                        int minZ = start + row * cell;
                        wallSegments.add(new WallSegment(minZ, x, cell, false));
                    }
                } else if (currentVal == 1) {
                    int x = start + (col + 1) * cell;
                    int minZ = start + row * cell;
                    wallSegments.add(new WallSegment(minZ, x, cell, false));
                }

                if (row == 0 && currentVal == 1) {
                    int z = start;
                    int minX = start + col * cell;
                    wallSegments.add(new WallSegment(minX, z, cell, true));
                }

                if (col == 0 && currentVal == 1) {
                    int x = start;
                    int minZ = start + row * cell;
                    wallSegments.add(new WallSegment(minZ, x, cell, false));
                }
            }
        }
    }

    private void spawnWallX(World world, int startX, int z, int length, List<Player> players) {

        int endX = startX + length;
        for (int x = startX; x < endX; x += 20) {
            for (int y = 200; y <= 300; y += 20) {
                if (isPlayerNearby(players, x, y, z, 512)) {
                    world.spawnParticle(
                            Particle.TRIAL_SPAWNER_DETECTION_OMINOUS,
                            x, y, z,
                            1, 5, 5, 0, 0, null, true
                    );
                }
            }
        }

        for (int x = startX+2; x < endX-2; x += 2) {
            for (int y = 200; y <= 300; y += 3) {
                if (isPlayerNearby(players, x, y, z, 16)) {
                    world.spawnParticle(
                            Particle.TRIAL_SPAWNER_DETECTION,
                            x, y, z,
                            4, 2.5, 3, 0, 0, null, false
                    );
                }
            }
        }
    }

    private void spawnWallZ(World world, int x, int startZ, int length, List<Player> players) {

        int endZ = startZ + length;
        for (int z = startZ; z < endZ; z += 20) {
            for (int y = 200; y <= 300; y += 20) {
                if (isPlayerNearby(players, x, y, z, 512)) {
                    world.spawnParticle(
                            Particle.TRIAL_SPAWNER_DETECTION_OMINOUS,
                            x, y, z,
                            1, 0, 5, 5, 0, null, true
                    );
                }
            }
        }

        for (int z = startZ+2; z < endZ-2; z += 2) {
            for (int y = 200; y <= 300; y += 3) {
                if (isPlayerNearby(players, x, y, z, 16)) {
                    world.spawnParticle(
                            Particle.TRIAL_SPAWNER_DETECTION,
                            x, y, z,
                            4, 0, 3, 2.5, 0, null, false
                    );
                }
            }
        }
    }

    private boolean isPlayerNearby(List<Player> players, double px, double py, double pz, double distance) {
        double distanceSquared = distance * distance;
        for (Player player : players) {
            Location loc = player.getLocation();
            double dx = loc.getX() - px;
            double dy = loc.getY() - py;
            double dz = loc.getZ() - pz;
            if (dx * dx + dy * dy + dz * dz <= distanceSquared) return true;
        }
        return false;
    }


    public void stop() {
        if (activeTask != null) {
            activeTask.cancel();
            activeTask = null;
        }
        wallSegments.clear();
    }

    private static class WallSegment {
        final int start;
        final int fixed;
        final int length;
        final boolean axisX;

        WallSegment(int start, int fixed, int length, boolean axisX) {
            this.start = start;
            this.fixed = fixed;
            this.length = length;
            this.axisX = axisX;
        }
    }
}