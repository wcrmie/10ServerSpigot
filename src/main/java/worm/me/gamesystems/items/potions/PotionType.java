package worm.me.gamesystems.items.potions;

import org.bukkit.Material;

public enum PotionType {
    SHRINK(220, 235, 45, "Potion of Shrinking", "Decreases your size by 20% (stacking) until death.", Material.DANDELION),
    GROW(215,70,40, "Potion of Growing", "Increases your size by 20% (stacking) until death.", Material.POPPY);

    final int r, g, b;
    final String name, lore;
    final Material material;

    PotionType(int r, int g, int b, String name, String lore, Material material) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.name = name;
        this.lore = lore;
        this.material = material;
    }

    public int getR() {
        return r;
    }

    public int getG() {
        return g;
    }

    public int getB() {
        return b;
    }

    public String getName() {
        return name;
    }

    public String getLore() {
        return lore;
    }

    public Material getMaterial() {
        return material;
    }
}
