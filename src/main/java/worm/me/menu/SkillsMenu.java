package worm.me.menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import worm.me.data.levels.Levels;
import worm.me.utils.menu.GuiConstructor;
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

        inventory.setItem(0, Item.create(Material.KNOWLEDGE_BOOK, "<blue>\uD83D\uDEC8", " ", "<gray>Your skill levels are a representation of the activities",  "<gray>you partake in when you play on the server.", " " , "<gray>As you play, you will level up and unlock abilities,", "<gray>recipes, and more!", " ", "<gray>Keep an eye on chat and your recipe book to see", "<gray>what you have unlocked. "));

        inventory.setItem(11, Item.create(Material.FISHING_ROD, "<dark_aqua>\uD83C\uDFA3 Fishing Level " + Levels.getLevel(3, owner), " ", "<gray>EXP: " + Levels.getExp("Fishing", owner) + " / " + Levels.expRequired(Levels.getLevel(3, owner) + 1)));

        inventory.setItem(12, Item.create(Material.WHEAT, "<dark_aqua>\uD83C\uDF3E Farming Level " + Levels.getLevel(1, owner), " ", "<gray>EXP: " + Levels.getExp("Farming", owner) + " / " + Levels.expRequired(Levels.getLevel(1, owner) + 1)));

        inventory.setItem(13, Item.create(Material.COBBLESTONE, "<dark_aqua>⛏ Mining Level " + Levels.getLevel(5, owner), " ", "<gray>EXP: " + Levels.getExp("Mining", owner) + " / " + Levels.expRequired(Levels.getLevel(5, owner) + 1)));

        inventory.setItem(14, Item.create(Material.JUNGLE_SAPLING, "<dark_aqua>\uD83E\uDE93 Foraging Level " + Levels.getLevel(7, owner), " ", "<gray>EXP: " + Levels.getExp("Foraging", owner) + " / " + Levels.expRequired(Levels.getLevel(7, owner) + 1)));

        inventory.setItem(15, Item.create(Material.STONE_SWORD, "<dark_aqua>⚔ Combat Level " + Levels.getLevel(0, owner), " ", "<gray>EXP: " + Levels.getExp("Combat", owner) + " / " + Levels.expRequired(Levels.getLevel(0, owner) + 1)));

        inventory.setItem(21, Item.create(Material.CRAFTING_TABLE, "<dark_aqua>♲ Crafting Level " + Levels.getLevel(2, owner), " ", "<gray>EXP: " + Levels.getExp("Crafting", owner) + " / " + Levels.expRequired(Levels.getLevel(2, owner) + 1)));

        inventory.setItem(22, Item.create(Material.ANVIL, "<dark_aqua>⚒ Smithing Level " + Levels.getLevel(4, owner), " ", "<gray>EXP: " + Levels.getExp("Smithing", owner) + " / " + Levels.expRequired(Levels.getLevel(4, owner) + 1)));

        inventory.setItem(23, Item.create(Material.FEATHER, "<dark_aqua>⚡ Agility Level " + Levels.getLevel(6, owner), " ", "<gray>EXP: " + Levels.getExp("Agility", owner) + " / " + Levels.expRequired(Levels.getLevel(6, owner) + 1)));



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
