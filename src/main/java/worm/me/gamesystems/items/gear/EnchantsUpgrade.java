package worm.me.gamesystems.items.gear;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ThreadLocalRandom;

public class EnchantsUpgrade {

    public ItemStack upgradeEnchants(ItemStack item) {

        if (item == null) return item;
        if (!item.getEnchantments().isEmpty()) {
            return item;
        }

        if (ThreadLocalRandom.current().nextInt(0, 5) != 0) {
            return item;
        }

        Material mat = item.getType();

        if (isTool(mat)) {
            item.addUnsafeEnchantment(Enchantment.EFFICIENCY, 1);
        } else if (isSword(mat)) {
            item.addUnsafeEnchantment(Enchantment.SHARPNESS, 1);
        }

        return item;
    }

    private boolean isTool(Material mat) {
        String name = mat.name();
        return name.endsWith("_PICKAXE")
                || name.endsWith("_AXE")
                || name.endsWith("_SHOVEL");
    }

    private boolean isSword(Material mat) {
        return mat.name().endsWith("_SWORD");
    }
}
