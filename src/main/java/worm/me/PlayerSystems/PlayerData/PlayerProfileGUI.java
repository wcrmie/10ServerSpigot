package worm.me.PlayerSystems.PlayerData;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import worm.me.GuiConstructor;

public class PlayerProfileGUI {

    public static void openProfile(Player p, Player viewer){
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

        gui.setItem(11, GuiConstructor.createGuiItem(Material.SKELETON_SKULL, ChatColor.DARK_AQUA + "Deaths : ", " ", ChatColor.GRAY + "" + p.getStatistic(Statistic.DEATHS)));
        gui.setItem(12, GuiConstructor.createGuiItem(Material.DIAMOND_SWORD, ChatColor.DARK_AQUA + "Players Killed : ", " ", ChatColor.GRAY + "" + p.getStatistic(Statistic.PLAYER_KILLS)));

        gui.setItem(13, GuiConstructor.createGuiItem(Material.TOTEM_OF_UNDYING, ChatColor.AQUA + "Skill Levels", " ", ChatColor.GRAY + "Click here to view " + whose + "skill levels."));

        gui.setItem(14, GuiConstructor.createGuiItem(Material.IRON_PICKAXE, ChatColor.DARK_AQUA + "Blocks Broken", " ", ChatColor.GRAY + "" + p.getStatistic(Statistic.MINE_BLOCK)));
        gui.setItem(15, GuiConstructor.createGuiItem(Material.COBBLESTONE, ChatColor.DARK_AQUA + "Cobblestone Placed", " ", ChatColor.GRAY + "" + p.getStatistic(Statistic.USE_ITEM, Material.COBBLESTONE)));

        gui.setItem(21, GuiConstructor.createGuiItem(Material.BOW, ChatColor.DARK_AQUA + "Bow Accuracy : ", " ", ChatColor.GRAY + "" + p.getStatistic(Statistic.USE_ITEM, Material.BOW)));

        gui.setItem(22, GuiConstructor.createGuiItem(Material.CLOCK, ChatColor.DARK_AQUA + "Time Played : ", " ", ChatColor.GRAY + "" + p.getStatistic(Statistic.PLAY_ONE_MINUTE) / 20 + " "));

        gui.setItem(23, GuiConstructor.createGuiItem(Material.FISHING_ROD, ChatColor.DARK_AQUA + "Caught Fish : ", " ", ChatColor.GRAY + "" + p.getStatistic(Statistic.FISH_CAUGHT)));

        viewer.openInventory(gui);

    }

}
