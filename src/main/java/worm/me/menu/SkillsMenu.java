package worm.me.menu;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import worm.me.data.levels.Levels;
import worm.me.utils.GuiConstructor;
import worm.me.utils.Item;

public class SkillsMenu extends Menu implements InventoryHolder, MainSubMenu{

    private final Inventory inventory;
    private final Player owner;

    public SkillsMenu(Player viewer, Player owner){
        super(viewer);
        this.owner = owner;
        inventory = initialize();
    }

    @Override
    public Inventory initialize() {

        String whose;

        if (owner == getViewer()) {

            whose = "Your ";

        } else {

            whose = owner.getName() + "'s ";

        }

        Inventory inventory = GuiConstructor.chestInventory(whose + " Skill Levels", 36, this);

        inventory.setItem(0, Item.create(Material.KNOWLEDGE_BOOK, ChatColor.BLUE + "\uD83D\uDEC8", " ", ChatColor.GRAY + "Your skill levels are a representation of the activities",  ChatColor.GRAY + "you partake in when you play on the server.", " " , ChatColor.GRAY + "As you play, you will level up and unlock abilities,", ChatColor.GRAY + "recipes, and more!", " ", ChatColor.GRAY + "Keep an eye on chat and your recipe book to see", ChatColor.GRAY + "what you have unlocked. "));

        inventory.setItem(11, Item.create(Material.FISHING_ROD, ChatColor.DARK_AQUA + "\uD83C\uDFA3 Fishing Level " + Levels.getLevel(3, owner), " ", ChatColor.GRAY + "EXP: " + Levels.getExp("Fishing", owner) + " / " + Levels.expRequired(Levels.getLevel(3, owner) + 1)));

        inventory.setItem(12, Item.create(Material.WHEAT, ChatColor.DARK_AQUA + "\uD83C\uDF3E Farming Level " + Levels.getLevel(1, owner), " ", ChatColor.GRAY + "EXP: " + Levels.getExp("Farming", owner) + " / " + Levels.expRequired(Levels.getLevel(1, owner) + 1)));

        inventory.setItem(13, Item.create(Material.COBBLESTONE, ChatColor.DARK_AQUA + "⛏ Mining Level " + Levels.getLevel(5, owner), " ", ChatColor.GRAY + "EXP: " + Levels.getExp("Mining", owner) + " / " + Levels.expRequired(Levels.getLevel(5, owner) + 1)));

        inventory.setItem(14, Item.create(Material.JUNGLE_SAPLING, ChatColor.DARK_AQUA + "\uD83E\uDE93 Foraging Level " + Levels.getLevel(7, owner), " ", ChatColor.GRAY + "EXP: " + Levels.getExp("Foraging", owner) + " / " + Levels.expRequired(Levels.getLevel(7, owner) + 1)));

        inventory.setItem(15, Item.create(Material.STONE_SWORD, ChatColor.DARK_AQUA + "⚔ Combat Level " + Levels.getLevel(0, owner), " ", ChatColor.GRAY + "EXP: " + Levels.getExp("Combat", owner) + " / " + Levels.expRequired(Levels.getLevel(0, owner) + 1)));

        inventory.setItem(21, Item.create(Material.CRAFTING_TABLE, ChatColor.DARK_AQUA + "♲ Crafting Level " + Levels.getLevel(2, owner), " ", ChatColor.GRAY + "EXP: " + Levels.getExp("Crafting", owner) + " / " + Levels.expRequired(Levels.getLevel(2, owner) + 1)));

        inventory.setItem(22, Item.create(Material.ANVIL, ChatColor.DARK_AQUA + "⚒ Smithing Level " + Levels.getLevel(4, owner), " ", ChatColor.GRAY + "EXP: " + Levels.getExp("Smithing", owner) + " / " + Levels.expRequired(Levels.getLevel(4, owner) + 1)));

        inventory.setItem(23, Item.create(Material.FEATHER, ChatColor.DARK_AQUA + "⚡ Agility Level " + Levels.getLevel(6, owner), " ", ChatColor.GRAY + "EXP: " + Levels.getExp("Agility", owner) + " / " + Levels.expRequired(Levels.getLevel(6, owner) + 1)));



        return inventory;
    }

    @Override
    public void open() {

        getViewer().openInventory(inventory);

    }

    @Override
    public void handleClick(InventoryClickEvent e){
        e.setCancelled(true);

    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}
