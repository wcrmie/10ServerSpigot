package worm.me.utils.menu;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import worm.me.utils.Item;

public class GuiConstructor {

    public static Inventory chestInventory(String name, Integer size, InventoryHolder holder) {
        Inventory i = Bukkit.createInventory(holder, size, Component.text(name));
        for (int j = 0; j < size; j++) {
            i.setItem(j, Item.create(Material.GRAY_STAINED_GLASS_PANE, " "));
        }

        i.setItem(size - 5, Item.create(Material.SPECTRAL_ARROW, ChatColor.GRAY + "Go Back"));
        return i;
    }

    public static int[] determineCenteredSlots(int amount) {

        switch(amount){
            case 1 -> {
                return new int[]{4};
            }
            case 2 -> {
                return new int[]{3,5};
            }
            case 3 -> {
                return new int[]{2,4,6};
            }
            case 4 -> {
                return new int[]{1,3,5,7};
            }
            case 5 -> {
                return new int[]{2,3,4,5,6};
            }
            case 6 -> {
                return new int[]{1,2,3,5,6,7};
            }
            case 7 -> {
                return new int[]{1,2,3,4,5,6,7,};
            }
            case 8 -> {
                return new int[]{0,1,2,3,5,6,7,8};
            }
            case 9 -> {
                return new int[]{0,1,2,3,4,5,6,7,8};
            }
            default -> {
                return new int[]{};
            }
        }
    }
}
