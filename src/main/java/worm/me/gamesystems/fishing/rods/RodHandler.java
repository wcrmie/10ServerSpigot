package worm.me.gamesystems.fishing.rods;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import worm.me.Server;

public class RodHandler {

    public static String getRodType(ItemStack rod) {

        ItemMeta meta = rod.getItemMeta();

        return meta.getPersistentDataContainer().getOrDefault(new NamespacedKey(Server.getPlugin(), "rod"), PersistentDataType.STRING, "default");
    }

    public static String catchFish(ItemStack rod) {

        String rodType = getRodType(rod);

        switch (rodType)
        {
            case "breeze_rod":

                BreezeRod breezeRod = new BreezeRod();
                breezeRod.caught(rod);
                return "DEFAULT";

            case "hot_rod":
                return "HOT_ROD";

            default:
                return "DEFAULT";
        }
    }
}
