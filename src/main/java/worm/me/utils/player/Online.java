package worm.me.utils.player;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Online {

    public static Player find(String name) {

        for (Player player : org.bukkit.Bukkit.getOnlinePlayers()) {
            if (player.getName().equalsIgnoreCase(name)) {
                return player;
            }
        }
        return null;
    }

    // Check if the player has an empty slot
    public static boolean hasEmptySlot(Player player) {
        Inventory inventory = player.getInventory();
        for (ItemStack item : inventory.getContents()) {
            if (item == null || item.getType() == Material.AIR) {
                return true;
            }
        }
        return false;
    }
}
