package worm.me.PlayerSystems.Social.Hats;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import worm.me.GuiConstructor;

import java.util.ArrayList;
import java.util.List;

public class Hats {

    public void equipDefaultHat(Player p){
        if (p.getInventory().getHelmet().getType().isAir()){
            p.getInventory().setHelmet(HatsList.hats.getFirst());
        }
    }

    public void openHatsGUI(Player p){
        List<ItemStack> hats = HatsList.hats;

        GuiConstructor guiConstructor = new GuiConstructor();

        Inventory hatsGUI = guiConstructor.chestInventory(ChatColor.DARK_GRAY + "Hats Page 1", 54);

        for (int i = 0; i < hats.size(); i++){

            int slot = i + 10;

            if (slot % 9 == 0) slot++;

            if ((slot + 1) % 9 == 0) slot += 2;

            if (slot >= 44) break;

            hatsGUI.setItem(slot, hats.get(i));

        }


    }


}
