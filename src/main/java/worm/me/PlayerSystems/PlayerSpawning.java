package worm.me.PlayerSystems;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.WorldBorder;
import worm.me.Server;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class PlayerSpawning {
    private final JavaPlugin plugin;
    private final World world;
    private final WorldBorder worldBorder;
    private int xMoreThan, xLessThan, yMoreThan, yLessThan, zMoreThan, zLessThan;
    private static final Map<Location, Boolean> blocksToRemove = new HashMap<>();

    public PlayerSpawning(Server plugin) {
        this.plugin = plugin;
        this.world = Bukkit.getWorld("ul_Void");
        this.worldBorder = world.getWorldBorder();
        updateBorderValues();
    }

    private void updateBorderValues() {
        double borderSize = worldBorder.getSize();
        Location center = worldBorder.getCenter();

        xMoreThan = (int) (center.getX() - (borderSize / 2));
        xLessThan = (int) (center.getX() + (borderSize / 2)) - 1;
        zMoreThan = (int) (center.getZ() - (borderSize / 2));
        zLessThan = (int) (center.getZ() + (borderSize / 2)) - 1;

        yMoreThan = Math.max(world.getMinHeight(), 220);
        yLessThan = Math.min(world.getMaxHeight(), 280);
    }

    public Location worldBorderGetRandomBlock() {
        int attempts = 0;
        while (attempts < 50) {
            int xCord = ThreadLocalRandom.current().nextInt(xMoreThan + 1, xLessThan);
            int yCord = ThreadLocalRandom.current().nextInt(yMoreThan + 1, yLessThan);
            int zCord = ThreadLocalRandom.current().nextInt(zMoreThan + 1, zLessThan);

            if (xCord > xMoreThan && xCord < xLessThan && yCord > yMoreThan && yCord < yLessThan && zCord > zMoreThan && zCord < zLessThan) {
                //RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
                //RegionManager regions = container.get(BukkitAdapter.adapt(world));
                //ProtectedRegion spawn = regions.getRegion("spawn");
                //if (!spawn.contains(xCord, yCord, zCord)) {
                    return new Location(world, xCord, yCord, zCord);
                //}
            }
            attempts++;
        }

        return new Location(world, (xMoreThan + xLessThan) / 2, (yMoreThan + yLessThan) / 2, (zMoreThan + zLessThan) / 2);
    }

    private Location findValidLocation(int xSize, int ySize, int zSize, int xBSize, int yBSize, int zBSize, int randomBlocks, int maxAttempts, String successMessage, boolean vc) {
        int attempts = 0;

        while (attempts < maxAttempts) {
            Location baseLocation = worldBorderGetRandomBlock();
            int baseX = baseLocation.getBlockX();
            int baseY = baseLocation.getBlockY();
            int baseZ = baseLocation.getBlockZ();

            boolean isValid = true;

            for (int x = baseX - xSize; x <= baseX + xSize && isValid; x++) {
                for (int y = baseY - ySize; y <= baseY + ySize && isValid; y++) {
                    for (int z = baseZ - zSize; z <= baseZ + zSize && isValid; z++) {
                        if (y < world.getMinHeight() || y > world.getMaxHeight()) {
                            continue;
                        }

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

            for (int i = 0; i < randomBlocks && isValid; i++) {
                int x = ThreadLocalRandom.current().nextInt(baseX - xBSize, baseX + xBSize + 1);
                int y = ThreadLocalRandom.current().nextInt(baseY - yBSize, baseY + yBSize + 1);
                int z = ThreadLocalRandom.current().nextInt(baseZ - zBSize, baseZ + zBSize + 1);

                if (y < world.getMinHeight() || y > world.getMaxHeight()) {
                    continue;
                }

                if (!isAir(x, y, z)) {
                    isValid = false;
                    break;
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

            if (successMessage != null && !successMessage.isEmpty()) {
                plugin.getLogger().info(successMessage + " at location: " + baseX + ", " + baseY + ", " + baseZ);

                Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "Spawn location found using " +
                        ChatColor.GOLD + successMessage +
                        ChatColor.GREEN + " at: " + baseX + ", " + baseY + ", " + baseZ);
            }

            return new Location(world, baseX, baseY, baseZ);
        }

        return null;
    }

    public Location validSpawnLocation() {
        Location location = null;

        location = findValidLocation(17, 27, 17, 55, 75, 55, 2500, 10, "Estra Extra big", false);
        if (location != null) {
            return location;
        }

        location = findValidLocation(13, 17, 13, 35, 55, 35, 500, 20, "Extra big",false);
        if (location != null) {
            return location;
        }

        location = findValidLocation(9, 11, 9, 17, 33, 17, 50, 50, "Big",false);
        if (location != null) {
            return location;
        }

        location = findValidLocation(3, 5, 3, 11, 11, 11, 25, 100, "Medium",false);
        if (location != null) {
            return location;
        }

        location = findValidLocation(0, 3, 0, 5, 5, 5, 5, 1000, "Small",false);
        if (location != null) {
            return location;
        }

        location = findValidLocation(0, 3, 0, 1, 1, 1, 0, 2000, "enxtra Small",true);
        if (location != null) {
            return location;
        }

        Bukkit.getServer().broadcastMessage(ChatColor.RED + "No valid spawn area found! Using random location.");
        return worldBorderGetRandomBlock();
    }

    private boolean isAir(int x, int y, int z) {
        if (y < world.getMinHeight() || y > world.getMaxHeight()) {
            return true;
        }

        Block block = world.getBlockAt(x, y, z);
        Material type = block.getType();

        return type == Material.AIR ||
                type == Material.CAVE_AIR ||
                type == Material.VOID_AIR;
    }


    public void playerSpawn(Player player) {
        updateBorderValues();
        Location location = validSpawnLocation();

        if (location == null) {
            player.teleport(world.getSpawnLocation());
            return;
        }

        Block block = world.getBlockAt(location);
        block.setType(Material.BEDROCK);
        blocksToRemove.put(block.getLocation(), true);

        new BukkitRunnable() {
            @Override
            public void run() {
                player.teleport(location.clone().add(0.5, 1, 0.5));
            }
        }.runTaskLater(plugin, 5L);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (player.getLocation().distance(location) >= 5) {
                    Block block = world.getBlockAt(location);
                    if (block.getType() == Material.BEDROCK) {
                        block.setType(Material.AIR);
                        blocksToRemove.remove(location);
                    }
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 10 * 20L, 10 * 20L);
    }

    public void removeAllBlocks() {
        Bukkit.getServer().broadcastMessage("Removing " + blocksToRemove.size() + " blocks");
        for (Location location : blocksToRemove.keySet()) {
            Block block = world.getBlockAt(location);
            if (block.getType() == Material.BEDROCK) {
                block.setType(Material.VOID_AIR);
            }
        }
        blocksToRemove.clear();
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


