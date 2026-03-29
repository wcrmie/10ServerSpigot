package worm.me.gamesystems.world.mob.PiglinTraders;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import worm.me.utils.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public enum PiglinTrades {


    DEFAULT("Default", null, () -> {
        List<LootEntry> loot = new ArrayList<>();
        loot.add(new LootEntry(new ItemStack(Material.NETHERRACK, 16),  25));
        loot.add(new LootEntry(new ItemStack(Material.BLACKSTONE, 16),  25));
        loot.add(new LootEntry(new ItemStack(Material.SOUL_SAND, 8),  25));
        loot.add(new LootEntry(new ItemStack(Material.LEATHER, 8),  25));
        loot.add(new LootEntry(new ItemStack(Material.IRON_NUGGET, 8),  25));
        loot.add(new LootEntry(new ItemStack(Material.GRAVEL, 8),  25));
        loot.add(new LootEntry(new ItemStack(Material.NETHER_BRICK, 16),  25));
        loot.add(new LootEntry(new ItemStack(Material.SPECTRAL_ARROW, 16),  15));
        loot.add(new LootEntry(new ItemStack(Material.ARROW, 16),  15));
        loot.add(new LootEntry(new ItemStack(Material.ENDER_PEARL, 2),  15));
        loot.add(new LootEntry(new ItemStack(Material.DRIED_GHAST, 1),  5));
        loot.add(new LootEntry(new ItemStack(Material.PIGLIN_HEAD, 1),  5));
        loot.add(new LootEntry(new Item.PotionBuilder(new ItemStack(Material.SPLASH_POTION)).addEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 3600, 0)).build(), 15));
        loot.add(new LootEntry(new Item.PotionBuilder(new ItemStack(Material.POTION)).addEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 3600, 0)).build(), 15));
        loot.add(new LootEntry(new Item.PotionBuilder(new ItemStack(Material.ENCHANTED_BOOK)).addEnchantment(Enchantment.SOUL_SPEED, 1).build(), 2));
        loot.add(new LootEntry(new Item.PotionBuilder(new ItemStack(Material.ENCHANTED_BOOK)).addEnchantment(Enchantment.SOUL_SPEED, 2).build(), 2));
        loot.add(new LootEntry(new Item.PotionBuilder(new ItemStack(Material.ENCHANTED_BOOK)).addEnchantment(Enchantment.SOUL_SPEED, 3).build(), 2));
        loot.add(new LootEntry(new Item.PotionBuilder(new ItemStack(Material.IRON_BOOTS)).addEnchantment(Enchantment.SOUL_SPEED, 1).build(), 5));
        loot.add(new LootEntry(new Item.PotionBuilder(new ItemStack(Material.IRON_BOOTS)).addEnchantment(Enchantment.SOUL_SPEED, 2).build(), 5));
        loot.add(new LootEntry(new Item.PotionBuilder(new ItemStack(Material.IRON_BOOTS)).addEnchantment(Enchantment.SOUL_SPEED, 3).build(), 5));
        loot.add(new LootEntry(new Item.PotionBuilder(new ItemStack(Material.BOW)).addEnchantment(Enchantment.FLAME, 1).build(), 5));
        loot.add(new LootEntry(new Item.PotionBuilder(new ItemStack(Material.GOLDEN_SPEAR)).addEnchantment(Enchantment.LUNGE, 1).build(), 10));
        loot.add(new LootEntry(PiglinCure.createCureCarrot(), 5));
        return loot;
    }),
    Jouster("jouster", null, () -> {
        List<LootEntry> loot = new ArrayList<>();
        loot.add(new LootEntry(new ItemStack(Material.NETHERRACK, 16),  20));
        loot.add(new LootEntry(new ItemStack(Material.BLACKSTONE, 16),  20));
        loot.add(new LootEntry(new ItemStack(Material.SOUL_SAND, 8),  20));
        loot.add(new LootEntry(new ItemStack(Material.LEATHER, 8),  20));
        loot.add(new LootEntry(new ItemStack(Material.IRON_NUGGET, 8),  20));
        loot.add(new LootEntry(new ItemStack(Material.GRAVEL, 8),  20));
        loot.add(new LootEntry(new ItemStack(Material.NETHER_BRICK, 16),  20));
        loot.add(new LootEntry(new ItemStack(Material.SPECTRAL_ARROW, 16),  15));
        loot.add(new LootEntry(new ItemStack(Material.ARROW, 16),  15));
        loot.add(new LootEntry(new ItemStack(Material.ENDER_PEARL, 2),  15));
        loot.add(new LootEntry(new ItemStack(Material.DRIED_GHAST, 1),  5));
        loot.add(new LootEntry(new ItemStack(Material.PIGLIN_HEAD, 1),  5));
        loot.add(new LootEntry(new Item.PotionBuilder(new ItemStack(Material.SPLASH_POTION)).addEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 3600, 0)).build(), 10));
        loot.add(new LootEntry(new Item.PotionBuilder(new ItemStack(Material.POTION)).addEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 3600, 0)).build(), 10));
        loot.add(new LootEntry(new Item.PotionBuilder(new ItemStack(Material.SPLASH_POTION)).addEffect(new PotionEffect(PotionEffectType.SPEED, 3600, 0)).build(), 10));
        loot.add(new LootEntry(new Item.PotionBuilder(new ItemStack(Material.POTION)).addEffect(new PotionEffect(PotionEffectType.SPEED, 3600, 0)).build(), 10));
        loot.add(new LootEntry(new Item.PotionBuilder(new ItemStack(Material.ENCHANTED_BOOK)).addEnchantment(Enchantment.SOUL_SPEED, 1).build(), 1));
        loot.add(new LootEntry(new Item.PotionBuilder(new ItemStack(Material.ENCHANTED_BOOK)).addEnchantment(Enchantment.SOUL_SPEED, 2).build(), 1));
        loot.add(new LootEntry(new Item.PotionBuilder(new ItemStack(Material.ENCHANTED_BOOK)).addEnchantment(Enchantment.SOUL_SPEED, 3).build(), 4));
        loot.add(new LootEntry(new Item.PotionBuilder(new ItemStack(Material.IRON_BOOTS)).addEnchantment(Enchantment.SOUL_SPEED, 1).build(), 3));
        loot.add(new LootEntry(new Item.PotionBuilder(new ItemStack(Material.IRON_BOOTS)).addEnchantment(Enchantment.SOUL_SPEED, 2).build(), 3));
        loot.add(new LootEntry(new Item.PotionBuilder(new ItemStack(Material.IRON_BOOTS)).addEnchantment(Enchantment.SOUL_SPEED, 3).build(), 10));
        loot.add(new LootEntry(new Item.PotionBuilder(new ItemStack(Material.BOW)).addEnchantment(Enchantment.FLAME, 1).build(), 10));
        loot.add(new LootEntry(new Item.PotionBuilder(new ItemStack(Material.GOLDEN_SPEAR)).addEnchantment(Enchantment.LUNGE, 1).build(), 10));
        loot.add(new LootEntry(new Item.PotionBuilder(new ItemStack(Material.GOLDEN_SPEAR)).addEnchantment(Enchantment.LUNGE, 2).build(), 5));
        loot.add(new LootEntry(new Item.PotionBuilder(new ItemStack(Material.GOLDEN_SPEAR)).addEnchantment(Enchantment.SHARPNESS, 1).build(), 10));
        loot.add(new LootEntry(new Item.PotionBuilder(new ItemStack(Material.GOLDEN_SPEAR)).addEnchantment(Enchantment.SHARPNESS, 2).build(), 5));
        loot.add(new LootEntry(new Item.PotionBuilder(new ItemStack(Material.GOLDEN_SPEAR)).addEnchantment(Enchantment.LOOTING, 1).build(), 5));
        loot.add(new LootEntry(new Item.PotionBuilder(new ItemStack(Material.GOLDEN_SPEAR)).addEnchantment(Enchantment.LOOTING, 2).build(), 3));
        loot.add(new LootEntry(new Item.PotionBuilder(new ItemStack(Material.GOLDEN_SPEAR)).addEnchantment(Enchantment.UNBREAKING, 1).build(), 10));
        loot.add(new LootEntry(new Item.PotionBuilder(new ItemStack(Material.GOLDEN_SPEAR)).addEnchantment(Enchantment.UNBREAKING, 2).build(), 5));
        loot.add(new LootEntry(new Item.PotionBuilder(new ItemStack(Material.GOLDEN_SPEAR)).addEnchantment(Enchantment.FIRE_ASPECT, 1).build(), 5));
        loot.add(new LootEntry(new Item.PotionBuilder(new ItemStack(Material.GOLDEN_SPEAR)).addEnchantment(Enchantment.KNOCKBACK, 1).build(), 5));
        loot.add(new LootEntry(new Item.PotionBuilder(new ItemStack(Material.GOLDEN_SPEAR)).addEnchantment(Enchantment.MENDING, 2).build(), 5));
        loot.add(new LootEntry(new Item.PotionBuilder(new ItemStack(Material.DIAMOND_SPEAR)).addEnchantment(Enchantment.LUNGE, 1).build(), 5));
        loot.add(new LootEntry(PiglinCure.createCureCarrot(), 5));
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