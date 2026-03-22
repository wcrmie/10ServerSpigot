package worm.me.gamesystems.items;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import worm.me.Server;
import worm.me.gamesystems.world.mob.traders.VillagerProfession;

public class KnowledgeTome {

    static NamespacedKey progressKey = new NamespacedKey(Server.getPlugin(), "progress");
    static NamespacedKey focusKey = new NamespacedKey(Server.getPlugin(), "focus");
    static NamespacedKey isCompleteKey = new NamespacedKey(Server.getPlugin(), "isComplete");

    public static boolean isTome(ItemStack tome) {

        ItemMeta meta = tome.getItemMeta();

        PersistentDataContainer container = meta.getPersistentDataContainer();

        return container.has(progressKey, PersistentDataType.INTEGER);

    }

    public static int getProgress(ItemStack tome, VillagerProfession focus) {

        ItemMeta meta = tome.getItemMeta();

        PersistentDataContainer persistentDataContainer = meta.getPersistentDataContainer();

        return persistentDataContainer.getOrDefault(progressKey, PersistentDataType.INTEGER_ARRAY, new int[13])[focus.ordinal()];

    }

    public static void setProgress(ItemStack tome, VillagerProfession focus, int progress) {

        ItemMeta meta = tome.getItemMeta();

        PersistentDataContainer persistentDataContainer = meta.getPersistentDataContainer();

        if (isComplete(tome)) return;

        int[] currentProgress = persistentDataContainer.getOrDefault(progressKey, PersistentDataType.INTEGER_ARRAY, new int[13]);

        currentProgress[focus.ordinal()] = progress;

        persistentDataContainer.set(focusKey, PersistentDataType.INTEGER_ARRAY, currentProgress);

        tome.setItemMeta(meta);

    }

    public static void addProgress(ItemStack tome, VillagerProfession focus, int progress) {

        setProgress(tome, focus, progress + getProgress(tome, focus));

    }

    public static void setComplete(ItemStack tome, VillagerProfession focus) {

        ItemMeta meta = tome.getItemMeta();

        PersistentDataContainer persistentDataContainer = meta.getPersistentDataContainer();

        persistentDataContainer.set(isCompleteKey, PersistentDataType.BOOLEAN, true);

        tome.setItemMeta(meta);

    }

    public static boolean isComplete(ItemStack tome) {

        ItemMeta meta = tome.getItemMeta();

        PersistentDataContainer persistentDataContainer = meta.getPersistentDataContainer();

        return persistentDataContainer.getOrDefault(isCompleteKey, PersistentDataType.BOOLEAN, false);

    }

    public static VillagerProfession getFocus(ItemStack tome) {

        ItemMeta meta = tome.getItemMeta();

        PersistentDataContainer persistentDataContainer = meta.getPersistentDataContainer();

        int professionOrdinal = persistentDataContainer.getOrDefault(focusKey, PersistentDataType.INTEGER, 0);

        return VillagerProfession.values()[professionOrdinal];

    }

    public static void setFocus(ItemStack tome, VillagerProfession focus) {

        ItemMeta meta = tome.getItemMeta();

        PersistentDataContainer persistentDataContainer = meta.getPersistentDataContainer();

        persistentDataContainer.set(focusKey, PersistentDataType.INTEGER, focus.ordinal());

        tome.setItemMeta(meta);

    }

}
