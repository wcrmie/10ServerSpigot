package worm.me.gamesystems.items.gear;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ThreadLocalRandom;

public class EnchantsDowngrade {

    public ItemStack downgradeEnchants(ItemStack item) {

        if (item == null || item.getEnchantments().isEmpty()) return item;

        Material mat = item.getType();

        for (Enchantment ench : item.getEnchantments().keySet().toArray(new Enchantment[0])) {
            int level = item.getEnchantmentLevel(ench);
            if (ench.equals(Enchantment.EFFICIENCY)) {
                int maxEff = 0;

                if (mat.name().startsWith("STONE_")) maxEff = 3;
                else if (mat.name().startsWith("IRON_")) maxEff = 4;

                if (maxEff > 0 && level > maxEff) {
                    item.removeEnchantment(ench);
                    item.addUnsafeEnchantment(ench, maxEff);
                }
            }


                int RNG = ThreadLocalRandom.current().nextInt(0, 10);
                if (RNG >= 2) continue;

                if (level > 1) {
                    item.removeEnchantment(ench);
                    item.addUnsafeEnchantment(ench, level - 1);
                } else {
                    item.removeEnchantment(ench);
                }
            }


        return item;
    }
}
