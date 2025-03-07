package worm.me.PlayerSystems.PlayerData.Social.Skin;

import net.skinsrestorer.api.storage.SkinStorage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.ItemMeta;
import worm.me.GuiConstructor;
import net.skinsrestorer.api.exception.DataRequestException;
import net.skinsrestorer.api.exception.MineSkinException;
import net.skinsrestorer.api.property.InputDataResult;
import net.skinsrestorer.api.storage.PlayerStorage;
import worm.me.Server;

import java.util.Optional;

public class SkinGui {


    public static void skinGuiOpen(Player p){

        Inventory GUI = GuiConstructor.chestInventory(ChatColor.DARK_GRAY + "Skins", 9*3);

        GUI.setItem(0, GuiConstructor.createGuiItem(Material.KNOWLEDGE_BOOK, ChatColor.BLUE + "\uD83D\uDEC8", " ", ChatColor.GRAY + "You can set your skin in this menu.", ChatColor.GRAY + "More skins may come in the future!"));

        ItemStack snowman = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta skullMeta = (SkullMeta) snowman.getItemMeta();
        skullMeta.setOwner("snowmaam");
        skullMeta.setDisplayName(ChatColor.BLUE + "Snowman");
        snowman.setItemMeta(skullMeta);
        GUI.setItem(11, snowman);

        ItemStack steve = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta skullMeta2 = (SkullMeta) steve.getItemMeta();
        skullMeta2.setOwner("worrm");
        skullMeta2.setDisplayName(ChatColor.BLUE + "Alex Dino Onesie");
        steve.setItemMeta(skullMeta2);
        GUI.setItem(13, steve);

        ItemStack alex = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta skullMeta3 = (SkullMeta) alex.getItemMeta();
        skullMeta3.setOwner("goobier");
        skullMeta3.setDisplayName(ChatColor.BLUE + "Farmer Steve");
        alex.setItemMeta(skullMeta3);
        GUI.setItem(15, alex);

        ItemStack back =  new ItemStack(Material.ARROW, 1);
        ItemMeta meta = back.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY + "Go Back");
        back.setItemMeta(meta);
        GUI.setItem(22, back);

        p.openInventory(GUI);
    }

    public static void skinGuiDo(InventoryClickEvent e, Server plugin){

        int slot = e.getRawSlot();
        Player p = (Player) e.getWhoClicked();

        if (slot == 11){
            setSkin(p, "snowmaam", plugin);
        }
        else if (slot == 13){
            setSkin(p, "worrm", plugin);
        }
        else if (slot == 15){
            setSkin(p, "goobier", plugin);
        }
        else if (slot == 22){
            // menu back
        }

    }

    public static void setSkin(Player player, String skinName, Server plugin) {
        SkinStorage storage = plugin.skinsRestorer.getSkinStorage();

        try {
            Optional<InputDataResult> result = storage.findOrCreateSkinData(skinName);
            if (result.isEmpty()){
                player.sendMessage("Skin could not be loaded.");
                return;
            }
            PlayerStorage playerStorage = plugin.skinsRestorer.getPlayerStorage();
            playerStorage.setSkinIdOfPlayer(player.getUniqueId(), result.get().getIdentifier());
            plugin.skinsRestorer.getSkinApplier(Player.class).applySkin(player);

        } catch (DataRequestException | MineSkinException e) {
            throw new RuntimeException(e);
        }
    }
}