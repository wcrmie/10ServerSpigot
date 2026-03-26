package worm.me.gamesystems.world.mob.PiglinTraders;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public enum PiglinTrades {


    DEFAULT("Default", null, () -> {
        List<LootEntry> loot = new ArrayList<>();
        loot.add(new LootEntry(new ItemStack(Material.CRYING_OBSIDIAN, 1),  15));
        return loot;
    }),
    HELM("sirhelm", "will give you helm", () -> {
        List<LootEntry> loot = new ArrayList<>();
        loot.add(new LootEntry(new ItemStack(Material.GOLDEN_HELMET, 1),    25));
        return loot;
    });


    public static class LootEntry {
        private final ItemStack item;
        private final int weight;

        public LootEntry(ItemStack item, int weight) {
            this.item   = item;
            this.weight = weight;
        }

        public ItemStack getItem()  { return item.clone(); }
        public int       getWeight() { return weight; }
    }

    @FunctionalInterface
    public interface LootSupplier { List<LootEntry> get(); }

    private final String       name;
    private final String       description;
    private final LootSupplier lootSupplier;

    PiglinTrades(String name, String description, LootSupplier lootSupplier) {
        this.name         = name;
        this.description  = description;
        this.lootSupplier = lootSupplier;
    }

    public String getName()        { return name; }
    public String getDescription() { return description; }
    public List<LootEntry> getLoot() { return lootSupplier.get(); }
    public boolean requiresTome()  { return this != DEFAULT; }

    public ItemStack rollLoot() {
        List<LootEntry> entries = getLoot();
        int totalWeight = entries.stream().mapToInt(LootEntry::getWeight).sum();
        int roll = ThreadLocalRandom.current().nextInt(totalWeight);
        int cumulative = 0;
        for (LootEntry entry : entries) {
            cumulative += entry.getWeight();
            if (roll < cumulative) return entry.getItem();
        }
        return entries.get(0).getItem();
    }
}