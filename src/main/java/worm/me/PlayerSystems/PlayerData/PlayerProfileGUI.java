package worm.me.PlayerSystems.PlayerData;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import worm.me.GuiConstructor;
import worm.me.PlayerSystems.PlayerData.Levels.LevelsGUI;

public class PlayerProfileGUI {

    public void openProfile(Player p, Player viewer){
        // Open the player's profile GUI

        Inventory gui = GuiConstructor.chestInventory(ChatColor.DARK_GRAY + p.getName() + "'s Profile", 36);

        // Set player head item to the player's head

        String whose;

        if (p == viewer) {

            whose = "Your ";

        } else {

            whose = p.getName() + "'s ";

        }
        ItemStack skull = GuiConstructor.createGuiItem(Material.PLAYER_HEAD, ChatColor.AQUA + whose + "Profile");


        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        skullMeta.setOwningPlayer(p);
        skull.setItemMeta(skullMeta);

        gui.setItem(4, skull);

        gui.setItem(11, GuiConstructor.createGuiItem(Material.SKELETON_SKULL, ChatColor.DARK_AQUA + "☠ Deaths: ", " ", ChatColor.GRAY + "" + p.getStatistic(Statistic.DEATHS)));
        gui.setItem(12, GuiConstructor.createGuiItem(Material.DIAMOND_SWORD, ChatColor.DARK_AQUA + "⚔ Players Killed: ", " ", ChatColor.GRAY + "" + p.getStatistic(Statistic.PLAYER_KILLS)));

        gui.setItem(13, GuiConstructor.createGuiItem(Material.TOTEM_OF_UNDYING, ChatColor.AQUA + "Skill Levels", " ", ChatColor.GRAY + "Click here to view " + whose + "skill levels."));

        gui.setItem(14, GuiConstructor.createGuiItem(Material.IRON_PICKAXE, ChatColor.DARK_AQUA + "⛏ Blocks Broken: ", " ", ChatColor.GRAY + "" + VoidPlayer.getStat(p, 1)));
        gui.setItem(15, GuiConstructor.createGuiItem(Material.COBBLESTONE, ChatColor.DARK_AQUA + "☐ Blocks Placed: ", " ", ChatColor.GRAY + "" + VoidPlayer.getStat(p, 0)));

        gui.setItem(21, GuiConstructor.createGuiItem(Material.BOW, ChatColor.DARK_AQUA + "\uD83C\uDFF9 Bow Accuracy: ", " ", ChatColor.GRAY + "" + (VoidPlayer.getStat(p, 2)*100/VoidPlayer.getStat(p, 3)) + "%"));

        int totalSecs = p.getStatistic(Statistic.PLAY_ONE_MINUTE) / 20;

        int hours = totalSecs / 3600;
        int minutes = (totalSecs % 3600) / 60;
        int seconds = totalSecs % 60;

        String timeString = String.format("%02d hours, %02d minutes, and %02d seconds", hours, minutes, seconds);

        gui.setItem(22, GuiConstructor.createGuiItem(Material.CLOCK, ChatColor.DARK_AQUA + "⌛ Time Played: ", " ", ChatColor.GRAY + timeString, ChatColor.GRAY + "First Join: " + new java.util.Date(p.getFirstPlayed())));

        gui.setItem(23, GuiConstructor.createGuiItem(Material.FISHING_ROD, ChatColor.DARK_AQUA + "\uD83C\uDFA3 Caught Fish: ", " ", ChatColor.GRAY + "" + p.getStatistic(Statistic.FISH_CAUGHT)));

        viewer.openInventory(gui);

    }

    public void eventHandler(InventoryClickEvent e){
        e.setCancelled(true);

        if (e.getCurrentItem() == null) return;

        if (e.getSlot() == 13){
            LevelsGUI levelsGUI = new LevelsGUI();
            levelsGUI.openLevelsGUI((Player) e.getWhoClicked(), (Player) e.getWhoClicked());
        }
    }

}
