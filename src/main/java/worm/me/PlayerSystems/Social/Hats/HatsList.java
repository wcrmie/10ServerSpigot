package worm.me.PlayerSystems.Social.Hats;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
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
        nothing.setItemMeta(nothingMeta);

        hats.add(nothing);

    }




}
