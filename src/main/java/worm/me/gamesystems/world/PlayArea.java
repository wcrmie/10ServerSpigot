package worm.me.gamesystems.world;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;
import worm.me.Server;

import java.util.concurrent.ThreadLocalRandom;

public class PlayArea {

    private static final World world = Bukkit.getWorld("ul_Void");
    private static final Location spawn = new Location(world, 0.5, 220, 0.5);
    private static final int maxHeight = 280;
    private static final int minHeight = 200;

    private static int xMoreThan, xLessThan, yMoreThan, yLessThan, zMoreThan, zLessThan;

    public static World getPlayWorld() {
        return world;
    }

    public static int getMaxHeight() {
        return maxHeight;
    }

    public static int getMinHeight() {
        return minHeight;
    }

    public static Location getSpawnLocation() {
        return spawn;
    }

    public static void updateBorderValues(int edgeBufferX, int edgeBufferY, int edgeBufferZ) {
        WorldBorder worldBorder = world.getWorldBorder();

        double borderSize = worldBorder.getSize();
        Location center = worldBorder.getCenter();

        xMoreThan = (int) (center.getX() - (borderSize / 2) + edgeBufferX);
        xLessThan = (int) (center.getX() + (borderSize / 2) - edgeBufferX);
        zMoreThan = (int) (center.getZ() - (borderSize / 2) + edgeBufferZ);
        zLessThan = (int) (center.getZ() + (borderSize / 2) - edgeBufferZ);

        yMoreThan = minHeight + edgeBufferY;
        yLessThan = maxHeight - edgeBufferY;
    }

    public static Location worldBorderGetRandomBlock(int xSize, int ySize, int zSize) {
        PlayArea.updateBorderValues(2,2,2);
        Location spawnCenter = PlayArea.getSpawnLocation();
        int excludeRadius = Math.max(ySize, Math.max(xSize, zSize)) + 10;

        Location randomLocation;
        int attempts = 0;
        do {
            try {
                int xCord = ThreadLocalRandom.current().nextInt(xMoreThan + (xSize/2+1), xLessThan - (xSize/2+1));
                int yCord = ThreadLocalRandom.current().nextInt(yMoreThan + (ySize/2+1), yLessThan - (ySize/2+1));
                int zCord = ThreadLocalRandom.current().nextInt(zMoreThan + (zSize/2+1), zLessThan - (zSize/2+1));

                randomLocation = new Location(world, xCord, yCord, zCord);
                attempts++;
            }catch (Exception e){
                return null;
            }

            if (attempts > 1000) break;

        } while (randomLocation.distance(spawnCenter) < excludeRadius);

        return randomLocation;
    }


    private static Location findValidLocation(int xSize, int ySize, int zSize, int maxAttempts, String successMessage) {
        PlayArea.updateBorderValues(2,2,2);
        int attempts = 0;
        while (attempts < maxAttempts) {
            Location baseLocation = worldBorderGetRandomBlock(xSize, ySize, zSize);
            if (baseLocation != null) {
                int baseX = baseLocation.getBlockX();
                int baseY = baseLocation.getBlockY();
                int baseZ = baseLocation.getBlockZ();
                boolean isValid = true;

                for (int x = baseX - xSize; x <= baseX + xSize && isValid; x++) {
                    for (int y = baseY - ySize; y <= baseY + ySize && isValid; y++) {
                        for (int z = baseZ - zSize; z <= baseZ + zSize && isValid; z++) {
                            if (!validBLock(x, y, z)) {
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
                return new Location(world, baseX, baseY, baseZ);
            }
            else {
                attempts += 10000000;
            }
        }
        return null;
    }

    private static boolean validBLock(int x, int y, int z) {
        if (y > yMoreThan && y < yLessThan && x > xMoreThan && x < xLessThan && z > zMoreThan && z < zLessThan) {
            Block block = world.getBlockAt(x, y, z);
            if (block.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public static Location validSpawnLocation(int xsize, int ysize, int zsize) {
        Location location;
        location = findValidLocation(6 * xsize, 6 * ysize, 6 * zsize, 10, "Extra Extra big");
        if (location != null) return location;

        location = findValidLocation(5 * xsize, 5 * ysize, 5 * zsize, 20, "Extra big");
        if (location != null) return location;

        location = findValidLocation(4 * xsize, 4 * ysize, 4 * zsize, 30, "Big");
        if (location != null) return location;

        location = findValidLocation(3 * xsize, 3 * ysize, 3 * zsize, 100, "Medium");
        if (location != null) return location;

        location = findValidLocation(2 * xsize, 2 * ysize, 2 * zsize, 500, "Small");
        if (location != null) return location;

        location = findValidLocation((int) (1.2 * xsize), (int) (1.2 * ysize), (int) (1.2 * zsize),
                2000, "Extra Small");

        return location;
    }
}
