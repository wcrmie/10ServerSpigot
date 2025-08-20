package worm.me.utils;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;
import worm.me.Server;
import worm.me.gamesystems.border.WorldBorderSizeHandler;

import java.util.concurrent.ThreadLocalRandom;

public class PlayArea {

    private static JavaPlugin plugin;
    private static World world;
    private static WorldBorder worldBorder;
    private static int xMoreThan, xLessThan, yMoreThan, yLessThan, zMoreThan, zLessThan;

    public static void init(Server serverPlugin) {
        plugin = serverPlugin;
    }

    public static void updateBorderValues(int edgeBufferX, int edgeBufferY, int edgeBufferZ) {
        world = Bukkit.getWorld("ul_Void");

        worldBorder = world.getWorldBorder();

        double borderSize = worldBorder.getSize();
        Location center = worldBorder.getCenter();

        xMoreThan = (int) (center.getX() - (borderSize / 2) + edgeBufferX);
        xLessThan = (int) (center.getX() + (borderSize / 2) - edgeBufferX);
        zMoreThan = (int) (center.getZ() - (borderSize / 2) + edgeBufferZ);
        zLessThan = (int) (center.getZ() + (borderSize / 2) - edgeBufferZ);

        yMoreThan = Math.max(world.getMinHeight(), 220 + edgeBufferY);
        yLessThan = Math.min(world.getMaxHeight(), 280 - edgeBufferY);
    }

    public static Location worldBorderGetRandomBlock(int xSize, int ySize, int zSize) {
        PlayArea.updateBorderValues(2,2,2);
        Location spawnCenter = new Location(world, 0.5, 240, 0.5);
        int excludeRadius = Math.max(xSize, zSize) + 30;

        Location randomLocation;
        int attempts = 0;
        do {
            int xCord = ThreadLocalRandom.current().nextInt(xMoreThan + xSize, xLessThan - xSize);
            int yCord = ThreadLocalRandom.current().nextInt(yMoreThan + ySize, yLessThan - ySize);
            int zCord = ThreadLocalRandom.current().nextInt(zMoreThan + zSize, zLessThan - zSize);

            randomLocation = new Location(world, xCord, yCord, zCord);
            attempts++;

            if (attempts > 1000) break;

        } while (randomLocation.distance(spawnCenter) < excludeRadius);

        return randomLocation;
    }


    private static Location findValidLocation(int xSize, int ySize, int zSize, int maxAttempts, String successMessage, boolean vc, int maxSizeNum) {
        PlayArea.updateBorderValues(2,2,2);
        int attempts = 0;
        while (attempts < maxAttempts) {
            Location baseLocation = worldBorderGetRandomBlock(xSize, ySize, zSize);
            int baseX = baseLocation.getBlockX();
            int baseY = baseLocation.getBlockY();
            int baseZ = baseLocation.getBlockZ();
            boolean isValid = true;

            for (int x = baseX - xSize; x <= baseX + xSize && isValid; x++) {
                for (int y = baseY - ySize; y <= baseY + ySize && isValid; y++) {
                    for (int z = baseZ - zSize; z <= baseZ + zSize && isValid; z++) {
                        if (y < world.getMinHeight() || y > world.getMaxHeight()) continue;
                        if (!isAir(x, y, z)) {
                            isValid = false;
                            break;
                        }
                    }
                }
            }

            if (!isValid) {
                attempts++;
                continue;
            }

            if (!vc) {
                for (int y = baseY - 1; y >= Math.max(baseY - 100, world.getMinHeight()) && isValid; y--) {
                    if (!isAir(baseX, y, baseZ)) {
                        isValid = false;
                        break;
                    }
                }
                for (int y = baseY + 1; y <= Math.min(baseY + 100, world.getMaxHeight()) && isValid; y++) {
                    if (!isAir(baseX, y, baseZ)) {
                        isValid = false;
                        break;
                    }
                }
                if (!isValid) {
                    attempts++;
                    continue;
                }
            }
            return new Location(world, baseX, baseY, baseZ);
        }
        return null;
    }

    private static boolean isAir(int x, int y, int z) {
        if (y < world.getMinHeight() || y > world.getMaxHeight()) {
            return true;
        }
        Block block = world.getBlockAt(x, y, z);
        Material type = block.getType();
        return type == Material.AIR || type == Material.CAVE_AIR || type == Material.VOID_AIR;
    }

    public static Location validSpawnLocation(int xsize, int ysize, int zsize, int maxSizeNum) {
        Location location;
        location = findValidLocation(6 * xsize, 6 * ysize, 6 * zsize, 10, "Extra Extra big", false, maxSizeNum);
        if (location != null) return location;

        location = findValidLocation(5 * xsize, 5 * ysize, 5 * zsize, 20, "Extra big", false, maxSizeNum);
        if (location != null) return location;

        location = findValidLocation(4 * xsize, 4 * ysize, 4 * zsize, 30, "Big", false, maxSizeNum);
        if (location != null) return location;

        location = findValidLocation(3 * xsize, 3 * ysize, 3 * zsize, 100, "Medium", false, maxSizeNum);
        if (location != null) return location;

        location = findValidLocation(2 * xsize, 2 * ysize, 2 * zsize, 500, "Small", false, maxSizeNum);
        if (location != null) return location;

        location = findValidLocation((int) (1.2 * xsize), (int) (1.2 * ysize), (int) (1.2 * zsize),
                2000, "Extra Small", true, maxSizeNum);

        return location;
    }
}
