package worm.me.gamesystems.world.structure;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.WorldBorder;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.plugin.Plugin;
import worm.me.utils.chat.Message;
import worm.me.utils.chat.MessageType;
import org.bukkit.entity.Display;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;


import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class TreasureChests {

    private final World world = Bukkit.getWorld("ul_Void");
    private final WorldBorder worldBorder = world.getWorldBorder();
    private final Plugin plugin;

    private int xMoreThan, xLessThan, yMoreThan, yLessThan, zMoreThan, zLessThan;

    private final Map<Location, BlockDisplay> chestDisplays = new HashMap<>();

    private final List<TreasureChestLootType> lootTable =
            Arrays.asList(TreasureChestLootType.values());

    private static final long CHEST_SPAWN_INTERVAL = 30 * 60 * 20L;
    public TreasureChests(Plugin plugin) {
        this.plugin = plugin;
        updateBorderValues();
    }

    private void updateBorderValues() {
        double borderSize = worldBorder.getSize();
        xMoreThan = (int) ((borderSize / 2) * -1);
        xLessThan = (int) (borderSize / 2) - 1;
        zMoreThan = (int) ((borderSize / 2) * -1);
        zLessThan = (int) (borderSize / 2) - 1;
        yMoreThan = 219;
        yLessThan = 280;
    }

    public void onPluginEnable() {


        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(
                plugin,
                this::chestSpawn,
                CHEST_SPAWN_INTERVAL,
                CHEST_SPAWN_INTERVAL
        );
    }

    public void onPluginDisable() {
        removeAllDisplays();
    }

    public Location worldBorderGetRandomBlock() {
        Location spawnCenter = new Location(world, 0.5, 220, 0.5);
        int excludeRadius = 20;

        Location randomLocation;
        do {
            int xCord = ThreadLocalRandom.current().nextInt(xMoreThan, xLessThan);
            int yCord = ThreadLocalRandom.current().nextInt(yMoreThan, yLessThan);
            int zCord = ThreadLocalRandom.current().nextInt(zMoreThan, zLessThan);
            randomLocation = new Location(world, xCord, yCord, zCord);
        } while (randomLocation.distance(spawnCenter) < excludeRadius);

        return randomLocation;
    }

    public Location validLocation() {
        Location location = findValidLocation(17, 17, 17, 45, 45, 45, 3, "Estra Extra big used");
        if (location == null) {
            location = findValidLocation(11, 11, 11, 15, 15, 15, 10, "Extra big used");
            if (location == null) {
                location = findValidLocation(7, 9, 7, 11, 9, 11, 20, "big used");
                if (location == null) {
                    location = findValidLocation(3, 5, 3, 0, 1, 0, 500, "med used");
                    if (location == null) {
                        location = findValidLocation(0, 3, 0, 0, 1, 0, 2000, "small used");
                        if (location == null) {
                            location = worldBorderGetRandomBlock();
                        }
                    }
                }
            }
        }
        return location;
    }

    private Location findValidLocation(int xSize, int ySize, int zSize,
                                       int xBSize, int yBSize, int zBSize,
                                       int maxAttempts, String successMessage) {

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

            // Full interior check
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

            // X walls
            for (int y = yStart; y <= yEnd; y++) {
                for (int z = zStart; z <= zEnd; z++) {
                    if (!isAir(xStart, y, z) || !isAir(xEnd, y, z)) {
                        validLocation = false;
                        break;
                    }
                }
                if (!validLocation) break;
            }

            // Y walls
            for (int x = xStart; x <= xEnd; x++) {
                for (int z = zStart; z <= zEnd; z++) {
                    if (!isAir(x, yStart, z) || !isAir(x, yEnd, z)) {
                        validLocation = false;
                        break;
                    }
                }
                if (!validLocation) break;
            }

            // Z walls
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

        return validLocation ? location : null;
    }

    private boolean isAir(int x, int y, int z) {
        return world.getBlockAt(x, y, z).getType() == Material.AIR;
    }

    public void chestSpawn() {
        updateBorderValues();

        Location location = validLocation();
        Block block = world.getBlockAt(location);
        block.setType(Material.CHEST);
        Chest chest = (Chest) block.getState();
        addLootToChest(chest.getInventory());

        BlockDisplay display = world.spawn(location.clone(), BlockDisplay.class);
        display.setBlock(block.getBlockData());
        display.setInterpolationDuration(0);
        display.setShadowStrength(0);
        display.setGlowing(true);

        chestDisplays.put(location, display);

        String msg = Message.create(MessageType.GENERIC,
                "A treasure chest has spawned at " +
                        location.getBlockX() + ", " +
                        location.getBlockY() + ", " +
                        location.getBlockZ() + "!"
        );
        Message.sendAll(msg);
    }

    private void addLootToChest(Inventory chestInventory) {
        int totalWeight = lootTable.stream().mapToInt(TreasureChestLootType::getWeight).sum();

        int chestSize = chestInventory.getSize();
        int minSlots = (int) Math.ceil(chestSize * 0.10);
        int maxSlots = (int) Math.ceil(chestSize * 0.20);
        int slotsToFill = ThreadLocalRandom.current().nextInt(minSlots, maxSlots + 1);

        Set<Integer> filledSlots = new HashSet<>();

        for (int i = 0; i < slotsToFill; i++) {

            int slot;
            do {
                slot = ThreadLocalRandom.current().nextInt(chestSize);
            } while (filledSlots.contains(slot));

            filledSlots.add(slot);

            int randomWeight = ThreadLocalRandom.current().nextInt(totalWeight);
            int currentWeight = 0;

            for (TreasureChestLootType loot : lootTable) {
                currentWeight += loot.getWeight();

                if (randomWeight < currentWeight) {

                    int qty = ThreadLocalRandom.current().nextInt(1, loot.getMaxQuantity() + 1);
                    ItemStack item = new ItemStack(loot.getMaterial(), qty);

                    chestInventory.setItem(slot, item);
                    break;
                }
            }
        }
    }


    public BlockDisplay getDisplayAt(Location loc) {
        return chestDisplays.get(loc);
    }

    public void removeDisplay(Location loc) {
        BlockDisplay display = chestDisplays.remove(loc);
        if (display != null && !display.isDead()) display.remove();
    }

    public void removeAllDisplays() {
        for (BlockDisplay display : chestDisplays.values()) {
            if (display != null && !display.isDead()) display.remove();
        }
        chestDisplays.clear();
    }

    public void removeClosestDisplay(Location loc, double radius) {
        BlockDisplay closest = null;
        double closestDistance = radius;

        for (Map.Entry<Location, BlockDisplay> entry : chestDisplays.entrySet()) {
            double distance = entry.getKey().distance(loc);
            if (distance <= closestDistance) {
                closest = entry.getValue();
                closestDistance = distance;
            }
        }

        if (closest != null) {
            closest.remove();
            chestDisplays.values().remove(closest);
        }
    }

    public void chestOpened(Location loc, Player player) {
        Block block = loc.getBlock();
        removeClosestDisplay(loc, 2.0);
    }

}