package worm.me.PlayerSystems;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.WorldBorder;
import worm.me.server;


public class PlayerSpawning {

    private final JavaPlugin plugin;
    private final World world = Bukkit.getWorld("ul_Void");
    private final WorldBorder worldBorder = world.getWorldBorder();
    private int xMoreThan, xLessThan, yMoreThan, yLessThan, zMoreThan, zLessThan;

    public PlayerSpawning(server plugin) {
        this.plugin = plugin;
        updateBorderValues();
    }

    private void updateBorderValues() {
        double borderSize = worldBorder.getSize();
        xMoreThan = (int) ((borderSize / 2) * -1);
        xLessThan = (int) (borderSize / 2) - 1;
        zMoreThan = (int) ((borderSize / 2) * -1);
        zLessThan = (int) (borderSize / 2) - 1;
        yMoreThan = 220;
        yLessThan = 280;
    }

    public Location worldBorderGetRandomBlock() {
        int xCord = (int) (Math.random() * (((xLessThan-1) - (xMoreThan+1)) + 1)) + (xMoreThan+1);
        int yCord = (int) (Math.random() * (((yLessThan-1) - (yMoreThan+1)) + 1)) + (yMoreThan+1);
        int zCord = (int) (Math.random() * (((zLessThan-1) - (zMoreThan+1)) + 1)) + (zMoreThan+1);
        return new Location(world, xCord, yCord, zCord);
    }

    public Location validSpawnLocation() {
        Location location =           findValidLocation(17, 17, 17, 45, 45, 45 ,3,    "Estra Extra big used");
        if (location == null) {
            location =                findValidLocation(11,  11,  11,  15, 15, 15 ,10,   "Extra big used");
            if (location == null) {
                location =            findValidLocation(7,  9,  7,  11, 9,  11, 20,   "big used");
                if (location == null) {
                    location =        findValidLocation(3,  5,  3,  0,  1,  0 , 500,  "med used");
                    if (location == null) {
                        location =    findValidLocation(0,  3,  0,  0,  1,  0 , 2000, "small used");
                        if (location == null) {
                            location = worldBorderGetRandomBlock();
                        }}}}}
        return location;
    }

    private Location findValidLocation(int xSize, int ySize, int zSize, int xBSize, int yBSize, int zBSize, int maxAttempts, String successMessage) {
        int yOffSetUp = 1;
        int locationAttempts = 0;
        Location location;
        boolean validLocation;
        do {
            locationAttempts++;
            validLocation = true;
            location = worldBorderGetRandomBlock();
            int xStart = (int) (location.getX() - (xBSize / 2.0));
            int xEnd = (int) (location.getX() + (xBSize / 2.0));
            int yStart = (int) (location.getY() - (yBSize / 2.0)) + yOffSetUp;
            int yEnd = (int) (location.getY() + (yBSize / 2.0)) + yOffSetUp;
            int zStart = (int) (location.getZ() - (zBSize / 2.0));
            int zEnd = (int) (location.getZ() + (zBSize / 2.0));
            //full
            for (int x = (int) (location.getX() - (xSize / 2.0)); x <= (int) (location.getX() + (xSize / 2.0)); x++) {
                for (int y = (int) (location.getY() - (ySize / 2.0)) + yOffSetUp; y <= (int) (location.getY() + (ySize / 2.0)) + yOffSetUp; y++) {
                    for (int z = (int) (location.getZ() - (zSize / 2.0)); z <= (int) (location.getZ() + (zSize / 2.0)); z++) {
                        if (!isAir(x, y, z)) {
                            validLocation = false;
                            break;
                        }
                    }
                    if (!validLocation) break;
                }
                if (!validLocation) break;
            }
            //x sides
            for (int y = yStart; y <= yEnd; y++) {
                for (int z = zStart; z <= zEnd; z++) {
                    if (!isAir(xStart, y, z) || !isAir(xEnd, y, z)) {
                        validLocation = false;
                        break;
                    }
                }
                if (!validLocation) break;
            }
            //y sides
            for (int x = xStart; x <= xEnd; x++) {
                for (int z = zStart; z <= zEnd; z++) {
                    if (!isAir(x, yStart, z) || !isAir(x, yEnd, z)) {
                        validLocation = false;
                        break;
                    }
                }
                if (!validLocation) break;
            }
            //z sides
            for (int x = xStart; x <= xEnd; x++) {
                for (int y = yStart; y <= yEnd; y++) {
                    if (!isAir(x, y, zStart) || !isAir(x, y, zEnd)) {
                        validLocation = false;
                        break;
                    }
                }
                if (!validLocation) break;
            }
        } while (!validLocation && locationAttempts < maxAttempts);
        if (validLocation) {
            return location;
        }
        return null;
    }

    private boolean isAir(int x, int y, int z) {
        Block block = world.getBlockAt(x, y, z);
        return block.getType() == Material.AIR;
    }

    public void playerSpawn(Player player) {
        updateBorderValues();
        Location location = validSpawnLocation();
        Block block = world.getBlockAt(location);
        block.setType(Material.BEDROCK);
        new BukkitRunnable() {
            @Override
            public void run() {
                location.add(0.5, 1, 0.5);
                player.teleport(location);
            }
        }.runTaskLater(plugin, 1L);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (player.getLocation().distance(location) >= 5) {
                    location.subtract(0.5, 1, 0.5);
                    Block block = world.getBlockAt(location);
                    block.setType(Material.AIR);
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 10 * 20L, 10 * 20L);
    }


    //stress testing
    /*
    public void ArmorStandSpawn(ArmorStand armorStand) {
        updateBorderValues();
        Location location = validSpawnLocation();
        Block block = world.getBlockAt(location);
        block.setType(Material.BEDROCK);
        new BukkitRunnable() {
            @Override
            public void run() {
                location.add(0.5, 1, 0.5);
                armorStand.teleport(location);
            }
        }.runTaskLater(plugin, 1L);
        new BukkitRunnable() {
            @Override
            public void run() {
                location.subtract(0.5, 1, 0.5);
                Block block = world.getBlockAt(location);
                block.setType(Material.AIR);
            }
        }.runTaskLater(plugin, 30 * 20L);
    }

     */

}
