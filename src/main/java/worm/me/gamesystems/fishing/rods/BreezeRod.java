package worm.me.gamesystems.fishing.rods;

import io.papermc.paper.datacomponent.DataComponentTypes;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import worm.me.Server;
import worm.me.utils.Item;

import java.util.List;

public class BreezeRod {

    public static ItemStack createBreezeRod() {

        ItemStack breezeRod = new Item.ItemBuilder(
                Item.create(
                        Material.FISHING_ROD,
                        "<aqua>Quick Rod",
                        "",
                        "<gray>Fish Caught: 0"
                )
        ).addPersistent(
                PersistentDataType.STRING,
                "breeze_rod",
                "rod"
        ).addPersistent(
                PersistentDataType.INTEGER,
                0,
                "fish_caught"
        ).setUnbreakable(true).build();

        return breezeRod;
    }

    public static void caught(ItemStack rod) {

        int caught;

        try {

            caught = Item.getPersistent(rod, PersistentDataType.INTEGER, "fish_caught");

        } catch (Exception e) {

            caught = 0;

        }

        caught++;

        Item.addPersistent(rod, PersistentDataType.INTEGER, caught, "fish_caught");

        ItemMeta meta = rod.getItemMeta();


        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        int fishCaught = pdc.getOrDefault(new NamespacedKey(Server.getPlugin(), "fish_caught"), PersistentDataType.INTEGER, 0);

        fishCaught++;

        pdc.set(new NamespacedKey(Server.getPlugin(), "fish_caught"), PersistentDataType.INTEGER, fishCaught);

        meta.setLore(List.of("", ChatColor.GRAY + "Fish Caught: " + caught));

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
