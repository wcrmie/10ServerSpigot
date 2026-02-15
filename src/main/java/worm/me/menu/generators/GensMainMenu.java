package worm.me.menu.generators;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import worm.me.menu.MainSubMenu;
import worm.me.menu.Menu;
import worm.me.playersystems.generator.GeneratorType;
import worm.me.utils.GuiConstructor;
import worm.me.data.Unlocks;
import worm.me.Server;
import worm.me.utils.Item;

public class GensMainMenu extends Menu implements InventoryHolder, MainSubMenu {

    private final Inventory inventory;

    public GensMainMenu(Player viewer) {
        super(viewer);
        inventory = initialize();
    }

    @Override
    public Inventory initialize() {

        Inventory inventory = GuiConstructor.chestInventory(ChatColor.DARK_GRAY + "Select Generated Blocks", 27, this);

        // Add items to inventory
        for (int j = 0; j < 27; j++){
            inventory.setItem(j, Item.create(Material.GRAY_STAINED_GLASS_PANE, " ", ""));
        }
        inventory.setItem(11, Item.create(Material.COBBLESTONE, ChatColor.GRAY + "Stones", " ", ChatColor.DARK_GRAY + "Choose your generated stone block."));
        inventory.setItem(12, Item.create(Material.DIRT, ChatColor.GRAY + "Loose Blocks", " ", ChatColor.DARK_GRAY + "Choose your generated loose block."));
        inventory.setItem(14, Item.create(Material.OAK_PLANKS, ChatColor.GRAY + "Woods", " ", ChatColor.DARK_GRAY + "Choose your generated wood block."));
        inventory.setItem(15, Item.create(Material.COAL, ChatColor.GRAY + "Misc. Items", " ", ChatColor.DARK_GRAY + "Choose your generated miscellaneous item."));

        inventory.setItem(22, Item.create(Material.SPECTRAL_ARROW, ChatColor.GRAY + "Go Back"));


        return inventory;
    }

    @Override
    public void open(){
        
        getViewer().openInventory(inventory);
        
    }

    @Override
    public void handleClick(InventoryClickEvent event) {

        if (event.getCurrentItem() == null) return;

        Player player = (Player) event.getWhoClicked();

        GeneratorType generatorType;

        switch (event.getCurrentItem().getType()) {

            case Material.DIRT -> generatorType = GeneratorType.LOOSE;
            case Material.OAK_PLANKS -> generatorType = GeneratorType.WOOD;
            case Material.COAL -> generatorType = GeneratorType.MISC;
            default -> generatorType = GeneratorType.STONE;

        }

        GensSubMenu subMenu = new GensSubMenu(player, generatorType);
        subMenu.open();

    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}
