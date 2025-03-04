package worm.me.PlayerSystems.PlayerData.Social.Hats;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class HatsList {

    public static ArrayList<ItemStack> hats = new ArrayList<>();


    public void createHats(){

        // Default "Nothing" hat (ID 0)
        ItemStack nothing = new ItemStack(Material.STONE_BUTTON);
        ItemMeta nothingMeta = nothing.getItemMeta();
        nothingMeta.addEnchant(Enchantment.BINDING_CURSE, 1, true);
        nothingMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        nothing.setItemMeta(nothingMeta);

        hats.add(nothing);

        // Saddle hat (ID 1)
        ItemStack saddle = new ItemStack(Material.SADDLE);
        ItemMeta saddleMeta = saddle.getItemMeta();
        saddleMeta.addEnchant(Enchantment.BINDING_CURSE, 1, true);
        saddleMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        saddle.setItemMeta(saddleMeta);

        hats.add(saddle);

    }




}
