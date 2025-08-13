package worm.me.gamesystems.fishing.rods;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import worm.me.Server;

import java.util.List;

public class BreezeRod {

    public static ItemStack createBreezeRod() {
        ItemStack breezeRod = new ItemStack(Material.FISHING_ROD);
        ItemMeta meta = breezeRod.getItemMeta();

        meta.setDisplayName(ChatColor.AQUA + "Quick Rod");
        meta.setLore(List.of("", ChatColor.GRAY + "Fish Caught: 0"));

        meta.setUnbreakable(true);

        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        pdc.set(new NamespacedKey(Server.getPlugin(), "rod"), PersistentDataType.STRING, "breeze_rod");
        pdc.set(new NamespacedKey(Server.getPlugin(), "fish_caught"), PersistentDataType.INTEGER, 0);

        breezeRod.setItemMeta(meta);

        return breezeRod;
    }

    public static void caught(ItemStack rod) {
        ItemMeta meta = rod.getItemMeta();


        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        int fishCaught = pdc.getOrDefault(new NamespacedKey(Server.getPlugin(), "fish_caught"), PersistentDataType.INTEGER, 0);

        fishCaught++;

        pdc.set(new NamespacedKey(Server.getPlugin(), "fish_caught"), PersistentDataType.INTEGER, fishCaught);

        meta.setLore(List.of("", ChatColor.GRAY + "Fish Caught: " + fishCaught));

        rod.setItemMeta(meta);

        improve(rod);

    }

    public static void improve(ItemStack rod) {

        ItemMeta meta = rod.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        int fishCaught = pdc.getOrDefault(new NamespacedKey(Server.getPlugin(), "fish_caught"), PersistentDataType.INTEGER, 0);

        int digits = String.valueOf(fishCaught).length();

        if (rod.containsEnchantment(Enchantment.LURE)) {
            rod.removeEnchantment(Enchantment.LURE);
        }

        rod.addUnsafeEnchantment(Enchantment.LURE, digits);

    }

}
