package worm.me.PlayerSystems;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import worm.me.GuiConstructor;

public class MainGUIMenu {

    public static void openMainMenu(Player p){
        // Open the main menu GUI

        Inventory gui = GuiConstructor.chestInventory("Main Menu", 36);

        // Add items to the GUI
        gui.setItem(11, GuiConstructor.createGuiItem(Material.BOOK, ChatColor.AQUA + "Tutorial Book", " ", ChatColor.GRAY + "Read this book to learn about the server!"));
        gui.setItem(12, GuiConstructor.createGuiItem(Material.LEATHER_CHESTPLATE, ChatColor.AQUA + "Skin Selection", " ", ChatColor.GRAY + "Open the skin selection menu."));

        // Set player head item to the player's head
        ItemStack skull = GuiConstructor.createGuiItem(Material.PLAYER_HEAD, ChatColor.AQUA + "Your Profile", " ", ChatColor.GRAY + "View your stats and progress.");
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        skullMeta.setOwningPlayer(p);
        skull.setItemMeta(skullMeta);

        gui.setItem(13, skull);
        gui.setItem(14, GuiConstructor.createGuiItem(Material.GOLDEN_HELMET, ChatColor.AQUA + "Hats", " ", ChatColor.GRAY + "Open the hats menu."));
        gui.setItem(15, GuiConstructor.createGuiItem(Material.SCULK_SHRIEKER, ChatColor.AQUA + "Voices", " ", ChatColor.GRAY + "Open the voices menu."));
        gui.setItem(21, GuiConstructor.createGuiItem(Material.COBBLESTONE, ChatColor.AQUA + "Generators", " ", ChatColor.GRAY + "Open the generators menu."));
        gui.setItem(22, GuiConstructor.createGuiItem(Material.HEART_OF_THE_SEA, ChatColor.AQUA + "Abilities", " ", ChatColor.GRAY + "Open the abilities menu."));
        gui.setItem(23, GuiConstructor.createGuiItem(Material.CRAFTING_TABLE, ChatColor.AQUA + "Recipes", " ", ChatColor.GRAY + "Open the recipes menu."));

        gui.setItem(0, GuiConstructor.createGuiItem(Material.KNOWLEDGE_BOOK, ChatColor.BLUE + "\uD83D\uDEC8", " ", ChatColor.GRAY + "This is the main menu of the server;", ChatColor.GRAY + "from here you can access all the other menus.", ChatColor.GRAY + "If you're unsure where to start, try" , ChatColor.GRAY + "reading the tutorial book!"));

        p.openInventory(gui);
    }

}
