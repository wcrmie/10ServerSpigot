package worm.me.GameSystems;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.WorldBorder;
import worm.me.server;

import java.util.concurrent.ThreadLocalRandom;

public class treasureChests {

    private final World world = Bukkit.getWorld("ul_Void");
    private final WorldBorder worldBorder = world.getWorldBorder();
    private int xMoreThan, xLessThan, yMoreThan, yLessThan, zMoreThan, zLessThan;

    public treasureChests() {
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


    public Location validLocation() {
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

    public void chestSpawn() {
        updateBorderValues();
        Location location = validLocation();
        Block block = world.getBlockAt(location);
        block.setType(Material.CHEST);
        Chest chest = (Chest) block.getState();
        Inventory inventory = chest.getInventory();
        addLootToChest(inventory);
    }

    private void addLootToChest(Inventory chestInventory) {
        Object[][] lootTable = {
                {Material.COD, 5, 20, 20.0},
                {Material.SALMON, 5, 15, 10.0},
                {Material.PUFFERFISH, 1, 5, 5.0},
                {Material.BREAD, 1, 3, 5.0},
                {Material.APPLE, 1, 1, 5.0}
        };

        for (int i = 0; i < chestInventory.getSize(); i++) {
            if (chestInventory.getItem(i) != null) {
                continue;
            }

            for (Object[] loot : lootTable) {
                double rng = ThreadLocalRandom.current().nextDouble(0.0, 100.0);

                Double chance = (Double) loot[3];

                if (rng <= chance) {
                    Material material = (Material) loot[0];
                    int minAmount = (int) loot[1];
                    int maxAmount = (int) loot[2];

                    int amount = ThreadLocalRandom.current().nextInt(minAmount, maxAmount + 1);
                    ItemStack item = new ItemStack(material, amount);

                    chestInventory.setItem(i, item);
                    break;
                }
            }
        }
    }
}

