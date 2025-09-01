package worm.me.gamesystems.items.gear;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ThreadLocalRandom;

public class EnchantsDowngrade {

    public ItemStack downgradeEnchants(ItemStack item) {

        if (item.getEnchantments().isEmpty()) return item;

        Enchantment[] enchantments = item.getEnchantments().keySet().toArray(new Enchantment[0]);

        for (Enchantment enchant : enchantments) {

            int RNG = ThreadLocalRandom.current().nextInt(0, 10);

            if (RNG >= 2) continue;

            int level = item.getEnchantmentLevel(enchant);
            if (level > 1) {
                item.removeEnchantment(enchant);
                item.addUnsafeEnchantment(enchant, level - 1);
            } else if (level == 1) {
                item.removeEnchantment(enchant);
            }

        }

        return item;

    }


}
