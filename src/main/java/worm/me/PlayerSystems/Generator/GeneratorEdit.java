package worm.me.PlayerSystems.Generator;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import worm.me.PlayerSystems.PlayerData.Unlocks;
import worm.me.PlayerSystems.PlayerData.VoidPlayer;
import worm.me.Server;

import java.util.Arrays;

public class GeneratorEdit {

    public NamespacedKey stoneBlock = new NamespacedKey("10server", "stoneblock");
    public NamespacedKey dirtBlock = new NamespacedKey("10server", "dirtblock");
    public NamespacedKey woodBlock = new NamespacedKey("10server", "woodblock");
    public NamespacedKey miscItem = new NamespacedKey("10server", "miscitem");

    public Inventory i;

    public static void genSet(Player p, NamespacedKey type, Block block){
        PersistentDataContainer container = p.getPersistentDataContainer();
        container.set(type, PersistentDataType.STRING, block.getTranslationKey());
    }

    public void genOptions(Player p){
        i = Bukkit.createInventory(null, 27, ChatColor.DARK_GRAY + "Select Generated Blocks");

        // Add items to inventory
        for (int j = 0; j < 27; j++){
            i.setItem(j, createGuiItem(Material.GRAY_STAINED_GLASS_PANE, " ", ""));
        }
        i.setItem(11, createGuiItem(Material.COBBLESTONE, ChatColor.GRAY + "Stones", " ", ChatColor.DARK_GRAY + "Choose your generated stone block."));
        i.setItem(12, createGuiItem(Material.DIRT, ChatColor.GRAY + "Loose Blocks", " ", ChatColor.DARK_GRAY + "Choose your generated loose block."));
        i.setItem(14, createGuiItem(Material.OAK_PLANKS, ChatColor.GRAY + "Woods", " ", ChatColor.DARK_GRAY + "Choose your generated wood block."));
        i.setItem(15, createGuiItem(Material.COAL, ChatColor.GRAY + "Misc. Items", " ", ChatColor.DARK_GRAY + "Choose your generated miscellaneous item."));

        // Open inventory to player
        p.openInventory(i);
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
        i = Bukkit.createInventory(null, 36, ChatColor.DARK_GRAY + "Select Stone Block");

        // Add items to inventory
        for (int j = 0; j < 36; j++){
            i.setItem(j, createGuiItem(Material.GRAY_STAINED_GLASS_PANE, " ", ""));
        }

        Server pl = Server.getPlugin();

        i.setItem(10, createGuiItem(Material.COBBLESTONE, "", ""));
        i.setItem(11, createGuiItem(Material.TUFF, "", ""));
        i.setItem(12, createGuiItem(Material.ANDESITE, "", ""));
        i.setItem(13, createGuiItem(Material.DIORITE, "", ""));
        i.setItem(14, createGuiItem(Material.GRANITE, "", ""));
        i.setItem(15, createGuiItem(Material.DRIPSTONE_BLOCK, "", ""));
        i.setItem(16, createGuiItem(Material.PACKED_MUD, "", ""));
        i.setItem(20, createGuiItem(Material.COBBLED_DEEPSLATE, "", ""));
        i.setItem(24, createGuiItem(Material.SMOOTH_BASALT, "", ""));

        for (int j = 10; j < 25; j++){
            if (Server.getPlugin().stones.containsKey(i.getItem(j).getType()) && !VoidPlayer.getUnlockedGens(p).contains(i.getItem(j))){
                i.setItem(j, createGuiItem(Material.RED_STAINED_GLASS_PANE, i.getItem(j).getType().getItemTranslationKey(), ChatColor.RED + "LOCKED", Unlocks.requirementsTranslation(pl.stones.get(i.getItem(j).getType()))));
            }
        }

        i.setItem(31, createGuiItem(Material.ARROW, ChatColor.GRAY + "Go Back"));

        p.openInventory(i);
    }

    public void woodItem(Player p){
        i = Bukkit.createInventory(null, 27, ChatColor.DARK_GRAY + "Select Wood Block");

        // Add items to inventory
        for (int j = 0; j < 27; j++){
            i.setItem(j, createGuiItem(Material.GRAY_STAINED_GLASS_PANE, " ", ""));
        }

        Server pl = Server.getPlugin();

        i.setItem(10, createGuiItem(Material.OAK_PLANKS, "", ""));
        i.setItem(11, createGuiItem(Material.SPRUCE_PLANKS, "", ""));
        i.setItem(12, createGuiItem(Material.DARK_OAK_PLANKS, "", ""));
        i.setItem(13, createGuiItem(Material.MANGROVE_PLANKS, "", ""));
        i.setItem(14, createGuiItem(Material.JUNGLE_PLANKS, "", ""));
        i.setItem(15, createGuiItem(Material.BIRCH_PLANKS, "", ""));
        i.setItem(16, createGuiItem(Material.ACACIA_PLANKS, "", ""));

        for (int j = 10; j < 17; j++){
            if (Server.getPlugin().planks.containsKey(i.getItem(j).getType()) && !VoidPlayer.getUnlockedGens(p).contains(i.getItem(j))){
                i.setItem(j, createGuiItem(Material.RED_STAINED_GLASS_PANE, i.getItem(j).getTranslationKey(), ChatColor.RED + "LOCKED", Unlocks.requirementsTranslation(pl.planks.get(i.getItem(j).getType()))));
            }
        }

        i.setItem(22, createGuiItem(Material.ARROW, ChatColor.GRAY + "Go Back"));

        p.openInventory(i);
    }

    public void dirtItem(Player p){
        i = Bukkit.createInventory(null, 27, ChatColor.DARK_GRAY + "Select Loose Block");

        // Add items to inventory
        for (int j = 0; j < 27; j++){
            i.setItem(j, createGuiItem(Material.GRAY_STAINED_GLASS_PANE, " ", ""));
        }

        Server pl = Server.getPlugin();

        i.setItem(10, createGuiItem(Material.DIRT, "", ""));
        i.setItem(11, createGuiItem(Material.COARSE_DIRT, "", ""));
        i.setItem(12, createGuiItem(Material.SAND, "", ""));
        i.setItem(13, createGuiItem(Material.GRAVEL, "", ""));
        i.setItem(14, createGuiItem(Material.CLAY, "", ""));
        i.setItem(15, createGuiItem(Material.MUD, "", ""));
        i.setItem(16, createGuiItem(Material.RED_SAND, "", ""));

        for (int j = 10; j < 17; j++){
            if (Server.getPlugin().dirts.containsKey(i.getItem(j).getType()) && !VoidPlayer.getUnlockedGens(p).contains(i.getItem(j))){
                i.setItem(j, createGuiItem(Material.RED_STAINED_GLASS_PANE, i.getItem(j).getTranslationKey(), ChatColor.RED + "LOCKED", Unlocks.requirementsTranslation(pl.dirts.get(i.getItem(j).getType()))));
            }
        }

        i.setItem(22, createGuiItem(Material.ARROW, ChatColor.GRAY + "Go Back"));

        p.openInventory(i);
    }

    public void miscItem(Player p){
        i = Bukkit.createInventory(null, 27, ChatColor.DARK_GRAY + "Select Miscellaneous Item");
        for (int j = 0; j < 27; j++){
            i.setItem(j, createGuiItem(Material.GRAY_STAINED_GLASS_PANE, " ", ""));
        }

        Server pl = Server.getPlugin();

        i.setItem(11, createGuiItem(Material.COAL, "", ""));
        i.setItem(12, createGuiItem(Material.QUARTZ, "", ""));
        i.setItem(14, createGuiItem(Material.GLOWSTONE_DUST, "", ""));
        i.setItem(15, createGuiItem(Material.REDSTONE, "", ""));

        for (int j = 10; j < 16; j++){
            if (Server.getPlugin().miscs.containsKey(i.getItem(j).getType()) && !VoidPlayer.getUnlockedGens(p).contains(i.getItem(j))){
                i.setItem(j, createGuiItem(Material.RED_STAINED_GLASS_PANE, i.getItem(j).getTranslationKey(), ChatColor.RED + "LOCKED", Unlocks.requirementsTranslation(pl.miscs.get(i.getItem(j).getType()))));
            }
        }

        i.setItem(22, createGuiItem(Material.ARROW, ChatColor.GRAY + "Go Back"));

        p.openInventory(i);
    }
}
