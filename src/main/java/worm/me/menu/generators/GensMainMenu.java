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
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import worm.me.menu.MainSubMenu;
import worm.me.menu.Menu;
import worm.me.utils.GuiConstructor;
import worm.me.data.Unlocks;
import worm.me.Server;

import java.util.Arrays;

public class GensMainMenu extends Menu implements InventoryHolder, MainSubMenu {

    public NamespacedKey stoneBlock = new NamespacedKey(Server.getPlugin(), "stoneblock");
    public NamespacedKey dirtBlock = new NamespacedKey(Server.getPlugin(), "dirtblock");
    public NamespacedKey woodBlock = new NamespacedKey(Server.getPlugin(), "woodblock");
    public NamespacedKey miscItem = new NamespacedKey(Server.getPlugin(), "miscitem");


    private Inventory inventory;

    public GensMainMenu(Player viewer) {
        super(viewer);
    }

    @Override
    public Inventory initialize() {
        return null;
    }

    public void genOptions(Player p){
        inventory = GuiConstructor.chestInventory(ChatColor.DARK_GRAY + "Select Generated Blocks", 27, this);

        // Add items to inventory
        for (int j = 0; j < 27; j++){
            inventory.setItem(j, createGuiItem(Material.GRAY_STAINED_GLASS_PANE, " ", ""));
        }
        inventory.setItem(11, createGuiItem(Material.COBBLESTONE, ChatColor.GRAY + "Stones", " ", ChatColor.DARK_GRAY + "Choose your generated stone block."));
        inventory.setItem(12, createGuiItem(Material.DIRT, ChatColor.GRAY + "Loose Blocks", " ", ChatColor.DARK_GRAY + "Choose your generated loose block."));
        inventory.setItem(14, createGuiItem(Material.OAK_PLANKS, ChatColor.GRAY + "Woods", " ", ChatColor.DARK_GRAY + "Choose your generated wood block."));
        inventory.setItem(15, createGuiItem(Material.COAL, ChatColor.GRAY + "Misc. Items", " ", ChatColor.DARK_GRAY + "Choose your generated miscellaneous item."));

        inventory.setItem(22, createGuiItem(Material.SPECTRAL_ARROW, ChatColor.GRAY + "Go Back"));

        // Open inventory to player
        p.openInventory(inventory);
    }


    public ItemStack createGuiItem(Material material, String name, String... lore){
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(name);

        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);

        return item;
    }

    public void stoneItem(Player p){
        inventory = Bukkit.createInventory(null, 36, ChatColor.DARK_GRAY + "Select Stone Block");

        // Add items to inventory
        for (int j = 0; j < 36; j++){
            inventory.setItem(j, createGuiItem(Material.GRAY_STAINED_GLASS_PANE, " ", ""));
        }

        inventory.setItem(10, createGuiItem(Material.COBBLESTONE, "", ""));
        inventory.setItem(11, createGuiItem(Material.TUFF, "", ""));
        inventory.setItem(12, createGuiItem(Material.ANDESITE, "", ""));
        inventory.setItem(13, createGuiItem(Material.DIORITE, "", ""));
        inventory.setItem(14, createGuiItem(Material.GRANITE, "", ""));
        inventory.setItem(15, createGuiItem(Material.DRIPSTONE_BLOCK, "", ""));
        inventory.setItem(16, createGuiItem(Material.PACKED_MUD, "", ""));
        inventory.setItem(20, createGuiItem(Material.COBBLED_DEEPSLATE, "", ""));
        inventory.setItem(24, createGuiItem(Material.SMOOTH_BASALT, "", ""));

        for (int j = 10; j < 25; j++){

            ItemStack item = inventory.getItem(j);

            if (Server.stones.containsKey(inventory.getItem(j).getType())){

                if (!Unlocks.hasGenUnlocked(p, item.getType())) inventory.setItem(j, createGuiItem(Material.RED_STAINED_GLASS_PANE, ChatColor.RED + item.getType().toString() + " (LOCKED)", Unlocks.requirementsTranslation(Server.stones.get(inventory.getItem(j).getType()))));
            }
        }

        inventory.setItem(31, createGuiItem(Material.ARROW, ChatColor.GRAY + "Go Back"));

        p.openInventory(inventory);
    }

    public void woodItem(Player p){
        inventory = Bukkit.createInventory(null, 27, ChatColor.DARK_GRAY + "Select Wood Block");

        // Add items to inventory
        for (int j = 0; j < 27; j++){
            inventory.setItem(j, createGuiItem(Material.GRAY_STAINED_GLASS_PANE, " ", ""));
        }

        Server pl = Server.getPlugin();

        inventory.setItem(10, createGuiItem(Material.OAK_PLANKS, "", ""));
        inventory.setItem(11, createGuiItem(Material.SPRUCE_PLANKS, "", ""));
        inventory.setItem(12, createGuiItem(Material.DARK_OAK_PLANKS, "", ""));
        inventory.setItem(13, createGuiItem(Material.MANGROVE_PLANKS, "", ""));
        inventory.setItem(14, createGuiItem(Material.JUNGLE_PLANKS, "", ""));
        inventory.setItem(15, createGuiItem(Material.BIRCH_PLANKS, "", ""));
        inventory.setItem(16, createGuiItem(Material.ACACIA_PLANKS, "", ""));

        for (int j = 10; j < 17; j++){

            ItemStack item = inventory.getItem(j);

            if (Server.planks.containsKey(item.getType())){


                if (!Unlocks.hasGenUnlocked(p, item.getType())) inventory.setItem(j, createGuiItem(Material.RED_STAINED_GLASS_PANE, ChatColor.RED + item.getType().toString() + " (LOCKED)", Unlocks.requirementsTranslation(Server.planks.get(inventory.getItem(j).getType()))));
            }
        }

        inventory.setItem(22, createGuiItem(Material.ARROW, ChatColor.GRAY + "Go Back"));

        p.openInventory(inventory);
    }

    public void dirtItem(Player p){
        inventory = Bukkit.createInventory(null, 27, ChatColor.DARK_GRAY + "Select Loose Block");

        // Add items to inventory
        for (int j = 0; j < 27; j++){
            inventory.setItem(j, createGuiItem(Material.GRAY_STAINED_GLASS_PANE, " ", ""));
        }

        Server pl = Server.getPlugin();

        inventory.setItem(10, createGuiItem(Material.DIRT, "", ""));
        inventory.setItem(11, createGuiItem(Material.COARSE_DIRT, "", ""));
        inventory.setItem(12, createGuiItem(Material.SAND, "", ""));
        inventory.setItem(13, createGuiItem(Material.GRAVEL, "", ""));
        inventory.setItem(14, createGuiItem(Material.CLAY, "", ""));
        inventory.setItem(15, createGuiItem(Material.MUD, "", ""));
        inventory.setItem(16, createGuiItem(Material.RED_SAND, "", ""));

        for (int j = 10; j < 17; j++){

            ItemStack item = inventory.getItem(j);

            if (Server.dirts.containsKey(inventory.getItem(j).getType())){

                if (!Unlocks.hasGenUnlocked(p, item.getType())) inventory.setItem(j, createGuiItem(Material.RED_STAINED_GLASS_PANE, ChatColor.RED + item.getType().toString() + " (LOCKED)", Unlocks.requirementsTranslation(Server.dirts.get(inventory.getItem(j).getType()))));
            }
        }

        inventory.setItem(22, createGuiItem(Material.ARROW, ChatColor.GRAY + "Go Back"));

        p.openInventory(inventory);
    }

    public void miscItem(Player p){
        inventory = Bukkit.createInventory(null, 27, ChatColor.DARK_GRAY + "Select Miscellaneous Item");
        for (int j = 0; j < 27; j++){
            inventory.setItem(j, createGuiItem(Material.GRAY_STAINED_GLASS_PANE, " ", ""));
        }

        Server pl = Server.getPlugin();

        inventory.setItem(11, createGuiItem(Material.COAL_ORE, "", ""));
        inventory.setItem(12, createGuiItem(Material.QUARTZ, "", ""));
        inventory.setItem(14, createGuiItem(Material.GLOWSTONE_DUST, "", ""));
        inventory.setItem(15, createGuiItem(Material.REDSTONE, "", ""));

        for (int j = 10; j < 16; j++){

            ItemStack item = inventory.getItem(j);

            if (Server.miscs.containsKey(inventory.getItem(j).getType())){

                if (!Unlocks.hasGenUnlocked(p, item.getType())) inventory.setItem(j, createGuiItem(Material.RED_STAINED_GLASS_PANE, ChatColor.RED + item.getType().toString() + " (LOCKED)", Unlocks.requirementsTranslation(Server.miscs.get(inventory.getItem(j).getType()))));
            }
        }

        inventory.setItem(22, createGuiItem(Material.ARROW, ChatColor.GRAY + "Go Back"));

        p.openInventory(inventory);
    }

    @Override
    public void open() {

    }

    @Override
    public void handleClick(InventoryClickEvent event) {

    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}
