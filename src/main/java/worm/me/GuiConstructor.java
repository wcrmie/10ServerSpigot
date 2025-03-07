package worm.me;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import worm.me.PlayerSystems.MainGUIMenu;

import java.util.Arrays;

public class GuiConstructor {

    public static Inventory chestInventory(String name, Integer size){
        Inventory i = Bukkit.createInventory(null, size, name);
        for (int j = 0; j < size; j++){
            i.setItem(j, createGuiItem(Material.GRAY_STAINED_GLASS_PANE, " "));
        }

        i.setItem(size - 5, createGuiItem(Material.SPECTRAL_ARROW, ChatColor.GRAY + "Go Back"));
        return i;
    }

    public static ItemStack createGuiItem(Material material, String name, String... lore){
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();

        if (meta.getLore() != null) meta.getLore().clear();


        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ADDITIONAL_TOOLTIP);

        meta.setDisplayName(name);

        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);

        return item;
    }

    public void goBack(InventoryClickEvent e){

        Player p = (Player) e.getWhoClicked();

        String invName = e.getView().getTitle();

        if (invName.contains("Profile") || invName.contains("Levels") || invName.contains("Hats") || invName.contains("Skins") || invName.contains("Your Active Abilities") || invName.contains("Select Generated Blocks")){
            MainGUIMenu.openMainMenu(p);
        }


    }
}
