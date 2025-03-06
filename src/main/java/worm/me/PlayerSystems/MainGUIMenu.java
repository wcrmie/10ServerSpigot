package worm.me.PlayerSystems;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import worm.me.GuiConstructor;
import worm.me.PlayerSystems.Abilities.Abilities;
import worm.me.PlayerSystems.Abilities.AbilitiesGUI;
import worm.me.PlayerSystems.Generator.GeneratorEdit;
import worm.me.PlayerSystems.PlayerData.PlayerProfileGUI;
import worm.me.PlayerSystems.PlayerData.Social.Hats.Hats;
import worm.me.PlayerSystems.PlayerData.Social.Skin.SkinGui;
import worm.me.Server;

public class MainGUIMenu {

    public static void openMainMenu(Player p){
        // Open the main menu GUI

        Inventory gui = GuiConstructor.chestInventory(ChatColor.DARK_GRAY + "Main Menu", 36);

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

        gui.setItem(31, GuiConstructor.createGuiItem(Material.BARRIER, ChatColor.RED + "Close Menu", " ", ChatColor.GRAY + "Close this menu."));

        p.openInventory(gui);
    }

    public void eventHandler(InventoryClickEvent e){
        e.setCancelled(true);

        Player p = (Player) e.getWhoClicked();

        if (e.getSlot() == 11){
            GuideBook.guideBook(p);
        } else if (e.getSlot() == 12){
            SkinGui.skinGuiOpen(p);
        } else if (e.getSlot() == 13){
            PlayerProfileGUI PlayerProfileGUI = new PlayerProfileGUI();
            PlayerProfileGUI.openProfile(p, p);
        } else if (e.getSlot() == 14){
            Hats hats = new Hats();
            hats.openHatsGUI(p, 1);
        } else if (e.getSlot() == 15){
            Server.pluginMessage(ChatColor.RED + "This feature is not yet available.", p);
        } else if (e.getSlot() == 21){
            GeneratorEdit generatorEdit = new GeneratorEdit();
            generatorEdit.genOptions(p);
        } else if (e.getSlot() == 22){
            AbilitiesGUI abilitiesGUI = new AbilitiesGUI();
            abilitiesGUI.abilitiesActive(p);
        } else if (e.getSlot() == 23){
            Server.pluginMessage(ChatColor.RED + "This feature is not yet available.", p);
        }

    }

}
