package worm.me.playersystems.data.social.hats;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.persistence.PersistentDataType;
import worm.me.GuiConstructor;
import worm.me.Server;

public class Hats {

    public static final NamespacedKey hatsUnlocked = new NamespacedKey(Server.getPlugin(), "hatsUnlocked");

    public void openHatsGUI(Player p, int page){

        Inventory hatsGUI = GuiConstructor.chestInventory(ChatColor.DARK_GRAY + "Hats Page " + page, 54);

        hatsGUI.setItem(0, GuiConstructor.createGuiItem(Material.KNOWLEDGE_BOOK, ChatColor.BLUE + "\uD83D\uDEC8", " ", ChatColor.GRAY + "You may select any of your unlocked hats here."));

        HatsStorage hatsStorage = new HatsStorage();

        int slot = 10;

        for (int i = 0; i < 28; i++){

            if (i > 0){
                slot++;
            }

            Server.pluginMessage(ChatColor.RED + "" + slot + ", " + i, p);

            if ((slot + 1) % 9 == 0) {
                slot += 2;
            }

            hatsGUI.setItem(slot, hatsStorage.getHat(i + 54 * (page - 1)));

        }

        p.openInventory(hatsGUI);


    }

    public void equipHat(Player p, int i){

        HatsStorage hatsStorage = new HatsStorage();
        if (p.getPersistentDataContainer().get(Hats.hatsUnlocked, PersistentDataType.INTEGER_ARRAY)[i] == 1 || p.getGameMode().equals(GameMode.CREATIVE)){
            p.getInventory().setHelmet(hatsStorage.getHat(i));
            Server.pluginMessage(ChatColor.GREEN + "Hat set!", p);
        } else {
            Server.pluginMessage(ChatColor.RED + "You don't have that hat unlocked!", p);
        }

    }

    public void unlockHat(Player p, int i){

        int[] hatsUnlocked = p.getPersistentDataContainer().get(Hats.hatsUnlocked, PersistentDataType.INTEGER_ARRAY);

            if (hatsUnlocked[i] == 0) {
                hatsUnlocked[i] = 1;
                p.getPersistentDataContainer().set(Hats.hatsUnlocked, PersistentDataType.INTEGER_ARRAY, hatsUnlocked);

                Server.pluginMessage(ChatColor.GREEN + "Hat unlocked!", p);
            }
    }

    public void hatGuiEventHandler(InventoryClickEvent e){

        e.setCancelled(true);

        if (e.getCurrentItem().getType().equals(Material.GRAY_STAINED_GLASS_PANE) || e.getCurrentItem().getType().equals(Material.AIR)){
            return;
        }

        if (e.getSlot() > 9 && e.getSlot() % 9 != 0 && (e.getSlot() + 1) % 9 != 0 && e.getSlot() < 45){

            Server.pluginMessage(ChatColor.RED + "it got here", (Player) e.getWhoClicked());

            int slot = e.getSlot();

            Server.pluginMessage(ChatColor.RED + "" + slot, (Player) e.getWhoClicked());

            if ((slot - 1) % 9 == 0 && slot != 10){
                slot--;
            } else if ((slot - 2) % 9 == 0 && slot != 11) slot -= 2;

            int page = Integer.parseInt(e.getView().getTitle().split("Hats Page ")[1]);

            equipHat((Player) e.getWhoClicked(), slot - 10 + 54 * (page - 1));
        }


    }


}
