package worm.me.PlayerSystems.PlayerData.Levels;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import worm.me.GuiConstructor;
import worm.me.Server;

import java.util.List;

public class LevelsGUI {

    public void openLevelsGUI(Player p, Player viewer) {

        String whose;

        if (p == viewer) {

            whose = "Your ";

        } else {

            whose = p.getName() + "'s ";

        }

        Inventory gui = GuiConstructor.chestInventory(ChatColor.DARK_GRAY + whose + "Skill Levels", 36);

        gui.setItem(0, GuiConstructor.createGuiItem(Material.KNOWLEDGE_BOOK, ChatColor.BLUE + "\uD83D\uDEC8", " ", ChatColor.GRAY + "Your skill levels are a representation of the activities",  ChatColor.GRAY + "you partake in when you play on the server.", " " , ChatColor.GRAY + "As you play, you will level up and unlock abilities,", ChatColor.GRAY + "recipes, and more!", " ", ChatColor.GRAY + "Keep an eye on chat and your recipe book to see", ChatColor.GRAY + "what you have unlocked. "));

        gui.setItem(11, GuiConstructor.createGuiItem(Material.FISHING_ROD, ChatColor.DARK_AQUA + "\uD83C\uDFA3 Fishing Level " + Levels.getLevel(3, p), " ", ChatColor.GRAY + "EXP: " + Levels.getExp("Fishing", p) + " / " + Levels.expRequired(Levels.getLevel(3, p) + 1)));

        gui.setItem(12, GuiConstructor.createGuiItem(Material.WHEAT, ChatColor.DARK_AQUA + "\uD83C\uDF3E Farming Level " + Levels.getLevel(1, p), " ", ChatColor.GRAY + "EXP: " + Levels.getExp("Farming", p) + " / " + Levels.expRequired(Levels.getLevel(1, p) + 1)));

        gui.setItem(13, GuiConstructor.createGuiItem(Material.COBBLESTONE, ChatColor.DARK_AQUA + "⛏ Mining Level " + Levels.getLevel(5, p), " ", ChatColor.GRAY + "EXP: " + Levels.getExp("Mining", p) + " / " + Levels.expRequired(Levels.getLevel(5, p) + 1)));

        gui.setItem(14, GuiConstructor.createGuiItem(Material.JUNGLE_SAPLING, ChatColor.DARK_AQUA + "\uD83E\uDE93 Foraging Level " + Levels.getLevel(7, p), " ", ChatColor.GRAY + "EXP: " + Levels.getExp("Foraging", p) + " / " + Levels.expRequired(Levels.getLevel(7, p) + 1)));

        gui.setItem(15, GuiConstructor.createGuiItem(Material.STONE_SWORD, ChatColor.DARK_AQUA + "⚔ Combat Level " + Levels.getLevel(0, p), " ", ChatColor.GRAY + "EXP: " + Levels.getExp("Combat", p) + " / " + Levels.expRequired(Levels.getLevel(0, p) + 1)));

        gui.setItem(21, GuiConstructor.createGuiItem(Material.CRAFTING_TABLE, ChatColor.DARK_AQUA + "♲ Crafting Level " + Levels.getLevel(2, p), " ", ChatColor.GRAY + "EXP: " + Levels.getExp("Crafting", p) + " / " + Levels.expRequired(Levels.getLevel(2, p) + 1)));

        gui.setItem(22, GuiConstructor.createGuiItem(Material.ANVIL, ChatColor.DARK_AQUA + "⚒ Smithing Level " + Levels.getLevel(4, p), " ", ChatColor.GRAY + "EXP: " + Levels.getExp("Smithing", p) + " / " + Levels.expRequired(Levels.getLevel(4, p) + 1)));

        gui.setItem(23, GuiConstructor.createGuiItem(Material.FEATHER, ChatColor.DARK_AQUA + "⚡ Agility Level " + Levels.getLevel(6, p), " ", ChatColor.GRAY + "EXP: " + Levels.getExp("Agility", p) + " / " + Levels.expRequired(Levels.getLevel(6, p) + 1)));

        p.openInventory(gui);

    }

    public void eventHandler(InventoryClickEvent e){
        e.setCancelled(true);

    }
}
