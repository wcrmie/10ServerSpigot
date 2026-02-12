package worm.me.gamesystems.world.structure;

import org.bukkit.Material;

public enum TreasureChestLootType {

    SNIFFER_EGG(Material.SNIFFER_EGG, 5, 1),
    BREAD(Material.BREAD, 40, 10),
    SNORT_POTTERY_SHERD(Material.SNORT_POTTERY_SHERD, 1, 1),
    PHANTOM_MEMBRANE(Material.PHANTOM_MEMBRANE, 10, 1),
    IRON_INGOT(Material.IRON_INGOT, 12, 1),
    IRON_NUGGET(Material.IRON_NUGGET, 40, 5),
    GOLDEN_CARROT(Material.GOLDEN_CARROT, 20, 5),
    GOLD_INGOT(Material.GOLD_INGOT, 12, 1),
    GOLD_NUGGET(Material.GOLD_NUGGET, 20, 5),
    COOKED_BEEF(Material.COOKED_BEEF, 36, 15),
    GOLDEN_APPLE(Material.GOLDEN_APPLE, 10, 2);

    private final Material material;
    private final int weight;
    private final int maxQuantity;
    private final boolean special;

    TreasureChestLootType(Material material, int weight, int maxQuantity) {
        this(material, weight, maxQuantity, false);
    }

    TreasureChestLootType(Material material, int weight, int maxQuantity, boolean special) {
        this.material = material;
        this.weight = weight;
        this.maxQuantity = maxQuantity;
        this.special = special;
    }

    public Material getMaterial() {
        return material;
    }

    public int getWeight() {
        return weight;
    }

    public int getMaxQuantity() {
        return maxQuantity;
    }

    public boolean isSpecial() {
        return special;
    }
}
