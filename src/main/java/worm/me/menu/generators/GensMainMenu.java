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

    private Inventory inventory;

    public GensMainMenu(Player viewer) {
        super(viewer);
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

    public void stoneItem(Player p){
        inventory = Bukkit.createInventory(null, 36, ChatColor.DARK_GRAY + "Select Stone Block");

        // Add items to inventory
        for (int j = 0; j < 36; j++){
            inventory.setItem(j, Item.create(Material.GRAY_STAINED_GLASS_PANE, " ", ""));
        }

        inventory.setItem(10, Item.create(Material.COBBLESTONE, "", ""));
        inventory.setItem(11, Item.create(Material.TUFF, "", ""));
        inventory.setItem(12, Item.create(Material.ANDESITE, "", ""));
        inventory.setItem(13, Item.create(Material.DIORITE, "", ""));
        inventory.setItem(14, Item.create(Material.GRANITE, "", ""));
        inventory.setItem(15, Item.create(Material.DRIPSTONE_BLOCK, "", ""));
        inventory.setItem(16, Item.create(Material.PACKED_MUD, "", ""));
        inventory.setItem(20, Item.create(Material.COBBLED_DEEPSLATE, "", ""));
        inventory.setItem(24, Item.create(Material.SMOOTH_BASALT, "", ""));

        for (int j = 10; j < 25; j++){

            ItemStack item = inventory.getItem(j);

            if (Server.stones.containsKey(inventory.getItem(j).getType())){

                if (!Unlocks.hasGenUnlocked(p, item.getType())) inventory.setItem(j, Item.create(Material.RED_STAINED_GLASS_PANE, ChatColor.RED + item.getType().toString() + " (LOCKED)", Unlocks.requirementsTranslation(Server.stones.get(inventory.getItem(j).getType()))));
            }
        }

        inventory.setItem(31, Item.create(Material.ARROW, ChatColor.GRAY + "Go Back"));

        p.openInventory(inventory);
    }

    public void woodItem(Player p){
        inventory = Bukkit.createInventory(null, 27, ChatColor.DARK_GRAY + "Select Wood Block");

        // Add items to inventory
        for (int j = 0; j < 27; j++){
            inventory.setItem(j, Item.create(Material.GRAY_STAINED_GLASS_PANE, " ", ""));
        }

        inventory.setItem(10, Item.create(Material.OAK_PLANKS, "", ""));
        inventory.setItem(11, Item.create(Material.SPRUCE_PLANKS, "", ""));
        inventory.setItem(12, Item.create(Material.DARK_OAK_PLANKS, "", ""));
        inventory.setItem(13, Item.create(Material.MANGROVE_PLANKS, "", ""));
        inventory.setItem(14, Item.create(Material.JUNGLE_PLANKS, "", ""));
        inventory.setItem(15, Item.create(Material.BIRCH_PLANKS, "", ""));
        inventory.setItem(16, Item.create(Material.ACACIA_PLANKS, "", ""));

        for (int j = 10; j < 17; j++){

            ItemStack item = inventory.getItem(j);

            if (Server.planks.containsKey(item.getType())){


                if (!Unlocks.hasGenUnlocked(p, item.getType())) inventory.setItem(j, Item.create(Material.RED_STAINED_GLASS_PANE, ChatColor.RED + item.getType().toString() + " (LOCKED)", Unlocks.requirementsTranslation(Server.planks.get(inventory.getItem(j).getType()))));
            }
        }

        inventory.setItem(22, Item.create(Material.ARROW, ChatColor.GRAY + "Go Back"));

        p.openInventory(inventory);
    }

    public void dirtItem(Player p){
        inventory = Bukkit.createInventory(null, 27, ChatColor.DARK_GRAY + "Select Loose Block");

        // Add items to inventory
        for (int j = 0; j < 27; j++){
            inventory.setItem(j, Item.create(Material.GRAY_STAINED_GLASS_PANE, " ", ""));
        }

        Server pl = Server.getPlugin();

        inventory.setItem(10, Item.create(Material.DIRT, "", ""));
        inventory.setItem(11, Item.create(Material.COARSE_DIRT, "", ""));
        inventory.setItem(12, Item.create(Material.SAND, "", ""));
        inventory.setItem(13, Item.create(Material.GRAVEL, "", ""));
        inventory.setItem(14, Item.create(Material.CLAY, "", ""));
        inventory.setItem(15, Item.create(Material.MUD, "", ""));
        inventory.setItem(16, Item.create(Material.RED_SAND, "", ""));

        for (int j = 10; j < 17; j++){

            ItemStack item = inventory.getItem(j);

            if (Server.dirts.containsKey(inventory.getItem(j).getType())){

                if (!Unlocks.hasGenUnlocked(p, item.getType())) inventory.setItem(j, Item.create(Material.RED_STAINED_GLASS_PANE, ChatColor.RED + item.getType().toString() + " (LOCKED)", Unlocks.requirementsTranslation(Server.dirts.get(inventory.getItem(j).getType()))));
            }
        }

        inventory.setItem(22, Item.create(Material.ARROW, ChatColor.GRAY + "Go Back"));

        p.openInventory(inventory);
    }

    public void miscItem(Player p){
        inventory = Bukkit.createInventory(null, 27, ChatColor.DARK_GRAY + "Select Miscellaneous Item");
        for (int j = 0; j < 27; j++){
            inventory.setItem(j, Item.create(Material.GRAY_STAINED_GLASS_PANE, " ", ""));
        }

        inventory.setItem(11, Item.create(Material.COAL_ORE, "", ""));
        inventory.setItem(12, Item.create(Material.QUARTZ, "", ""));
        inventory.setItem(14, Item.create(Material.GLOWSTONE_DUST, "", ""));
        inventory.setItem(15, Item.create(Material.REDSTONE, "", ""));

        for (int j = 10; j < 16; j++){

            ItemStack item = inventory.getItem(j);

            if (Server.miscs.containsKey(inventory.getItem(j).getType())){

                if (!Unlocks.hasGenUnlocked(p, item.getType())) inventory.setItem(j, Item.create(Material.RED_STAINED_GLASS_PANE, ChatColor.RED + item.getType().toString() + " (LOCKED)", Unlocks.requirementsTranslation(Server.miscs.get(inventory.getItem(j).getType()))));
            }
        }

        inventory.setItem(22, Item.create(Material.ARROW, ChatColor.GRAY + "Go Back"));

        p.openInventory(inventory);
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
