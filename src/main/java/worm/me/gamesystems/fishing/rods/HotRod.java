package worm.me.gamesystems.fishing.rods;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import worm.me.Server;

public class HotRod {

    public static ItemStack createItem() {
        ItemStack hotRod = new ItemStack(Material.FISHING_ROD);
        ItemMeta meta = hotRod.getItemMeta();

        meta.setDisplayName(ChatColor.AQUA + "Hot Rod");

        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        pdc.set(new NamespacedKey(Server.getPlugin(), "rod"), PersistentDataType.STRING, "hot_rod");
        pdc.set(new NamespacedKey(Server.getPlugin(), "void_rod"), PersistentDataType.BOOLEAN, Boolean.TRUE);

        hotRod.setItemMeta(meta);

        return hotRod;
    }

}
