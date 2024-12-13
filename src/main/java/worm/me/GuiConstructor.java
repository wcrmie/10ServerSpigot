package worm.me;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class GuiConstructor {

    public Inventory chestInventory(String name, Integer size){
        Inventory i = Bukkit.createInventory(null, size, name);
        for (int j = 0; j < size; j++){
            i.setItem(j, createGuiItem(Material.GRAY_STAINED_GLASS_PANE, " "));
        }
        return i;
    }

    public ItemStack createGuiItem(Material material, String name, String... lore){
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(name);

        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);

        return item;
    }
}
