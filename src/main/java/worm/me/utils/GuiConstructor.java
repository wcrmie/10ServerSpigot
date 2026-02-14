package worm.me.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import worm.me.menu.MainGUIMenu;

public class GuiConstructor {

    public static Inventory chestInventory(String name, Integer size, InventoryHolder holder){
        Inventory i = Bukkit.createInventory(holder, size, Component.text(name));
        for (int j = 0; j < size; j++){
            i.setItem(j, Item.create(Material.GRAY_STAINED_GLASS_PANE, " "));
        }

        i.setItem(size - 5, Item.create(Material.SPECTRAL_ARROW, ChatColor.GRAY + "Go Back"));
        return i;
    }
}
