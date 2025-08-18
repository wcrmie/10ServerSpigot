package worm.me.gamesystems.structure.generation;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.WorldBorder;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class TreasureChests {

    private final World world = Bukkit.getWorld("ul_Void");
    private final WorldBorder worldBorder = world.getWorldBorder();
    private int xMoreThan, xLessThan, yMoreThan, yLessThan, zMoreThan, zLessThan;

    private final Map<Material, LootEntry> lootTable = new HashMap<>();

    private static class LootEntry {
        int weight;
        int maxQuantity;
        boolean isSpecialItem;

        public LootEntry(int weight, int maxQuantity) {
            this(weight, maxQuantity, false);
        }

        public LootEntry(int weight, int maxQuantity, boolean isSpecialItem) {
            this.weight = weight;
            this.maxQuantity = maxQuantity;
            this.isSpecialItem = isSpecialItem;
        }
    }

    public TreasureChests() {
        updateBorderValues();
        initializeLootTable();
    }

    private void initializeLootTable() {
        lootTable.put(Material.MOJANG_BANNER_PATTERN, new LootEntry(20, 1));
        lootTable.put(Material.SNIFFER_EGG, new LootEntry(20, 1));
        lootTable.put(Material.BREAD, new LootEntry(40, 10));
        lootTable.put(Material.ENCHANTED_GOLDEN_APPLE, new LootEntry(1, 1));
        lootTable.put(Material.LEATHER_BOOTS, new LootEntry(20, 1, true)); // This is for slime boots
        lootTable.put(Material.SNORT_POTTERY_SHERD, new LootEntry(5, 1));
        lootTable.put(Material.IRON_INGOT, new LootEntry(12, 1));
        lootTable.put(Material.IRON_NUGGET, new LootEntry(40, 5));
        lootTable.put(Material.PHANTOM_MEMBRANE, new LootEntry(20, 1));
        lootTable.put(Material.GOLDEN_CARROT, new LootEntry(20, 5));
        lootTable.put(Material.GOLD_INGOT, new LootEntry(20, 1));
        lootTable.put(Material.GOLD_NUGGET, new LootEntry(24, 5));
        lootTable.put(Material.COOKED_BEEF, new LootEntry(36, 15));
        lootTable.put(Material.GOLDEN_APPLE, new LootEntry(12, 2));
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
        Location location = findValidLocation(17, 17, 17, 45, 45, 45 ,3, "Estra Extra big used");
        if (location == null) {
            location = findValidLocation(11, 11, 11, 15, 15, 15 ,10, "Extra big used");
            if (location == null) {
                location = findValidLocation(7, 9, 7, 11, 9, 11, 20, "big used");
                if (location == null) {
                    location = findValidLocation(3, 5, 3, 0, 1, 0 , 500, "med used");
                    if (location == null) {
                        location = findValidLocation(0, 3, 0, 0, 1, 0 , 2000, "small used");
                        if (location == null) {
                            location = worldBorderGetRandomBlock();
                        }
                    }
                }
            }
        }
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

    private ItemStack createSlimeBoots() {
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS, 1);
        LeatherArmorMeta meta = (LeatherArmorMeta) boots.getItemMeta();

        meta.setDisplayName(ChatColor.GREEN + "Slime Boots");
        meta.setColor(Color.LIME);

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "You must have the slime boot ability to use this");
        meta.setLore(lore);

        meta.getPersistentDataContainer().set(
                new NamespacedKey(Bukkit.getPluginManager().getPlugin("YourPluginName"), "slimeboots"),
                PersistentDataType.STRING,
                "true"
        );

        boots.setItemMeta(meta);
        return boots;
    }

    private void addLootToChest(Inventory chestInventory) {
        int totalWeight = lootTable.values().stream().mapToInt(entry -> entry.weight).sum();

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

            for (Map.Entry<Material, LootEntry> entry : lootTable.entrySet()) {
                currentWeight += entry.getValue().weight;
                if (randomWeight < currentWeight) {
                    ItemStack item;

                    if (entry.getKey() == Material.LEATHER_BOOTS && entry.getValue().isSpecialItem) {
                        item = createSlimeBoots();
                    } else {
                        int quantity = ThreadLocalRandom.current().nextInt(1, entry.getValue().maxQuantity + 1);
                        item = new ItemStack(entry.getKey(), quantity);

                    }

                    chestInventory.setItem(slot, item);
                    break;
                }
            }
        }
    }
}