package worm.me.menu;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import worm.me.data.VoidPlayer;
import worm.me.utils.menu.GuiConstructor;
import worm.me.utils.Item;

public class PlayerProfileMenu extends Menu implements InventoryHolder, MainSubMenu {

    private final Inventory inventory;
    private final Player owner;

    public PlayerProfileMenu(Player viewer, Player owner) {
        super(viewer);
        this.owner = owner;
        this.inventory = initialize();
    }

    @Override
    public Inventory initialize() {

        String whose;

        if (owner == getViewer()) {

            whose = "Your ";

        } else {

            whose = owner.getName() + "'s ";

        }

        Inventory inventory = GuiConstructor.chestInventory(whose + " Profile", 36, this);

        inventory.setItem(0, Item.create(Material.KNOWLEDGE_BOOK, "<blue>\uD83D\uDEC8", " ", "<gray>You are currently viewing " + whose + "profile."));

        ItemStack skull = Item.create(Material.PLAYER_HEAD, "<aqua>" + whose + "Profile");


        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        skullMeta.setOwningPlayer(owner);
        skull.setItemMeta(skullMeta);

        inventory.setItem(4, skull);

        inventory.setItem(11, Item.create(Material.SKELETON_SKULL, "<dark_aqua>☠ Deaths: ", " ", "<gray>" + owner.getStatistic(Statistic.DEATHS)));
        inventory.setItem(12, Item.create(Material.DIAMOND_SWORD, "<dark_aqua>⚔ Players Killed: ", " ", "<gray>" + owner.getStatistic(Statistic.PLAYER_KILLS)));

        inventory.setItem(13, Item.create(Material.TOTEM_OF_UNDYING, "<aqua>Skill Levels", " ", "<gray>Click here to view " + whose + "skill levels."));

        inventory.setItem(14, Item.create(Material.IRON_PICKAXE, "<dark_aqua>⛏ Blocks Broken: ", " ", "<gray>" + VoidPlayer.getStat(owner, 1)));
        inventory.setItem(15, Item.create(Material.COBBLESTONE, "<dark_aqua>☐ Blocks Placed: ", " ", "<gray>" + VoidPlayer.getStat(owner, 0)));

        try {

            inventory.setItem(21, Item.create(Material.BOW, "<dark_aqua>\uD83C\uDFF9 Bow Accuracy: ", " ", "<gray>" + (VoidPlayer.getStat(owner, 2) * 100 / VoidPlayer.getStat(owner, 3)) + "%"));
        } catch (Exception e){
            inventory.setItem(21, Item.create(Material.BOW, "<dark_aqua>\uD83C\uDFF9 Bow Accuracy: ", " ", "<gray>0%"));
        }
        int totalSecs = owner.getStatistic(Statistic.PLAY_ONE_MINUTE) / 20;

        int hours = totalSecs / 3600;
        int minutes = (totalSecs % 3600) / 60;
        int seconds = totalSecs % 60;

        String timeString = String.format("%02d hours, %02d minutes, and %02d seconds", hours, minutes, seconds);

        inventory.setItem(22, Item.create(Material.CLOCK, "<dark_aqua>⌛ Time Played: ", " ", "<gray>" + timeString, "<gray>First Join: " + new java.util.Date(owner.getFirstPlayed())));

        inventory.setItem(23, Item.create(Material.FISHING_ROD, "<dark_aqua>\uD83C\uDFA3 Caught Fish: ", " ", "<gray>" + owner.getStatistic(Statistic.FISH_CAUGHT)));

        return inventory;
    }

    @Override
    public void open(){

        getViewer().openInventory(inventory);

    }

    @Override
    public void handleClick(InventoryClickEvent e){
        e.setCancelled(true);

        if (e.getSlot() == 13){
            SkillsMenu skillsMenu = new SkillsMenu(getViewer(), getViewer());
            skillsMenu.open();
        }
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}
