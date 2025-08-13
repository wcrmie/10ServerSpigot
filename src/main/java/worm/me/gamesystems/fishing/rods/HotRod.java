package worm.me.gamesystems.fishing.rods;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import worm.me.Server;

import java.util.List;

public class HotRod {

    public static ItemStack createHotRod() {
        ItemStack hotRod = new ItemStack(Material.FISHING_ROD);
        ItemMeta meta = hotRod.getItemMeta();

        meta.setDisplayName(ChatColor.AQUA + "Hot Rod");

        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        pdc.set(new NamespacedKey(Server.getPlugin(), "rod"), PersistentDataType.STRING, "hot_rod");
        pdc.set(new NamespacedKey(Server.getPlugin(), "boss_spawned"), PersistentDataType.BOOLEAN, Boolean.FALSE);

        hotRod.setItemMeta(meta);

        return hotRod;
    }

}
