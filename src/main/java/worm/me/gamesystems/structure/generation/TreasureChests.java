package worm.me.gamesystems.structure.generation;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import worm.me.Server;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class TreasureChests {
    private final Server plugin;
    private final World world;
    private BukkitRunnable spawnTask;
    private long nextSpawnTime;

    private final double SLOT_LOOT_CHANCE = 0.40;
    private final Map<Material, LootEntry> LOOT_TABLE = createLootTable();

    private static class LootEntry {
        final int weight;
        final int maxStack;
        final boolean isSlimeBoots;

        LootEntry(int weight, int maxStack) {
            this(weight, maxStack, false);
        }

        LootEntry(int weight, int maxStack, boolean isSlimeBoots) {
            this.weight = weight;
            this.maxStack = maxStack;
            this.isSlimeBoots = isSlimeBoots;
        }
    }

    public TreasureChests(Server plugin) {
        this.plugin = plugin;
        this.world = Bukkit.getWorld("ul_Void");
        initialize();
    }

    private void initialize() {
        this.nextSpawnTime = plugin.getConfig().getLong("treasure-next-spawn", 0);
        scheduleNextSpawn(shouldSpawnNow() ? 0 : getRemainingTicks());
    }

    private Map<Material, LootEntry> createLootTable() {
        Map<Material, LootEntry> table = new HashMap<>();
        table.put(Material.MOJANG_BANNER_PATTERN, new LootEntry(20, 1));
        table.put(Material.SNIFFER_EGG, new LootEntry(20, 1));
        table.put(Material.BREAD, new LootEntry(40, 10));
        table.put(Material.ENCHANTED_GOLDEN_APPLE, new LootEntry(1, 1));
        table.put(Material.LEATHER_BOOTS, new LootEntry(20, 1, true));
        table.put(Material.SNORT_POTTERY_SHERD, new LootEntry(5, 1));
        table.put(Material.IRON_INGOT, new LootEntry(12, 1));
        table.put(Material.IRON_NUGGET, new LootEntry(40, 5));
        table.put(Material.PHANTOM_MEMBRANE, new LootEntry(20, 1));
        table.put(Material.GOLDEN_CARROT, new LootEntry(20, 5));
        table.put(Material.GOLD_INGOT, new LootEntry(20, 1));
        table.put(Material.GOLD_NUGGET, new LootEntry(24, 5));
        table.put(Material.COOKED_BEEF, new LootEntry(36, 15));
        table.put(Material.GOLDEN_APPLE, new LootEntry(12, 2));
        return table;
    }

    private boolean shouldSpawnNow() {
        return nextSpawnTime == 0 || nextSpawnTime < System.currentTimeMillis();
    }

    private long getRemainingTicks() {
        return (nextSpawnTime - System.currentTimeMillis()) / 50;
    }

    private void scheduleNextSpawn(long delayTicks) {
        cancelExistingTask();

        spawnTask = new BukkitRunnable() {
            @Override
            public void run() {
                trySpawnChest();
                updateNextSpawnTime();
                scheduleNextSpawn(72000); // 1 hour in ticks
            }
        };
        spawnTask.runTaskTimer(plugin, delayTicks, 0);
    }

    private void trySpawnChest() {
        Location location = findValidLocation();
        if (location != null) {
            Block block = world.getBlockAt(location);
            block.setType(Material.CHEST);
            fillChest((Chest) block.getState());
        }
    }

    private Location findValidLocation() {
        WorldBorder border = world.getWorldBorder();
        double size = border.getSize();

        for (int i = 0; i < 50; i++) {
            double x = ThreadLocalRandom.current().nextDouble(-size/2, size/2);
            double z = ThreadLocalRandom.current().nextDouble(-size/2, size/2);
            int y = world.getHighestBlockYAt((int)x, (int)z);

            Location loc = new Location(world, x, y, z);
            if (isValidLocation(loc)) return loc;
        }
        return null;
    }

    private boolean isValidLocation(Location loc) {
        return loc.distanceSquared(new Location(world, 0, 0, 0)) > 400 &&
                loc.getBlock().getType().isAir() &&
                loc.clone().add(0, 1, 0).getBlock().getType().isAir();
    }

    private void fillChest(Chest chest) {
        Inventory inv = chest.getInventory();
        for (int slot = 0; slot < inv.getSize(); slot++) {
            if (ThreadLocalRandom.current().nextDouble() < SLOT_LOOT_CHANCE) {
                inv.setItem(slot, generateLootItem());
            }
        }
    }

    private ItemStack generateLootItem() {
        int totalWeight = LOOT_TABLE.values().stream().mapToInt(e -> e.weight).sum();
        int random = ThreadLocalRandom.current().nextInt(totalWeight);
        int current = 0;

        for (Map.Entry<Material, LootEntry> entry : LOOT_TABLE.entrySet()) {
            current += entry.getValue().weight;
            if (random < current) {
                LootEntry loot = entry.getValue();
                if (loot.isSlimeBoots) {
                    return createSlimeBoots();
                }
                return new ItemStack(entry.getKey(),
                        ThreadLocalRandom.current().nextInt(1, loot.maxStack + 1));
            }
        }
        return new ItemStack(Material.IRON_INGOT, 1);
    }

    private ItemStack createSlimeBoots() {
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS, 1);
        ItemMeta meta = boots.getItemMeta();

        meta.setDisplayName(ChatColor.GREEN + "Slime Boots");
        ((LeatherArmorMeta)meta).setColor(Color.LIME);

        meta.getPersistentDataContainer().set(
                new NamespacedKey(plugin, "slimeboots"),
                PersistentDataType.STRING,
                "true"
        );

        boots.setItemMeta(meta);
        return boots;
    }

    private void updateNextSpawnTime() {
        nextSpawnTime = System.currentTimeMillis() + 3600000;
        plugin.getConfig().set("treasure-next-spawn", nextSpawnTime);
        plugin.saveConfig();
    }

    private void cancelExistingTask() {
        if (spawnTask != null) {
            spawnTask.cancel();
        }
    }

    public void stop() {
        cancelExistingTask();
    }
}