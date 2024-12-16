package worm.me.PlayerSystems.Skin;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.ItemMeta;
import worm.me.GuiConstructor;



public class SkinGui {
    public void skinGuiOpen(Player p){
        GuiConstructor guiConstructor = new GuiConstructor();
        Inventory GUI = guiConstructor.chestInventory(ChatColor.DARK_GRAY + "Skins", 9*3);

        ItemStack snowman = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta skullMeta = (SkullMeta) snowman.getItemMeta();
        skullMeta.setOwner("snowmaam");
        skullMeta.setDisplayName(ChatColor.BLUE + "Snowman");
        snowman.setItemMeta(skullMeta);
        GUI.setItem(11, snowman);

        ItemStack steve = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta skullMeta2 = (SkullMeta) steve.getItemMeta();
        skullMeta2.setOwner("worrm");
        skullMeta2.setDisplayName(ChatColor.BLUE + "Icy Steve");
        steve.setItemMeta(skullMeta2);
        GUI.setItem(13, steve);

        ItemStack alex = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta skullMeta3 = (SkullMeta) alex.getItemMeta();
        skullMeta3.setOwner("goobier");
        skullMeta3.setDisplayName(ChatColor.BLUE + "Icy Alex");
        alex.setItemMeta(skullMeta3);
        GUI.setItem(15, alex);

        ItemStack back =  new ItemStack(Material.ARROW, 1);
        ItemMeta meta = (ItemMeta) back.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY + "Go Back");
        back.setItemMeta(meta);
        GUI.setItem(22, back);

        p.openInventory(GUI);

    }

    public static void skinGuiDo(InventoryClickEvent e){
        int slot = e.getRawSlot();
        Player p = (Player) e.getWhoClicked();

        if (slot == 11){
            //set player skin to snowman
        }
        else if (slot == 13){
            //set player skin to steeve
        }
        else if (slot == 15){
            //set player skin to alex
        }
        else if (slot == 22){
            // menu back
        }

    }

}