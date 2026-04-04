package worm.me.gamesystems.world.mob.PiglinTraders;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Piglin;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import worm.me.Server;
import worm.me.utils.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class PiglinTradeManager {

    private static final NamespacedKey TOME_TYPE_KEY = new NamespacedKey(Server.getPlugin(), "piglin_tome_type");
    private static final NamespacedKey PIGLIN_TYPE_KEY = new NamespacedKey(Server.getPlugin(), "piglin_type");

    public static ItemStack generateTome(PiglinTrades type) {
        if (!type.requiresTome()) throw new IllegalArgumentException("Cannot create a tome for DEFAULT.");

        ItemStack tome = Item.create(Material.KNOWLEDGE_BOOK,
                "<gold>Piglin Knowledge Tome",
                "<gray>Right-click a <gold>Cured Piglin <gray>to teach them the ways of:",
                " ",
                "<yellow>" + type.getName(),
                " ",
                "<dark_gray>" + type.getDescription()
        );

        ItemMeta meta = tome.getItemMeta();
        if (meta == null) {
            Server.getPlugin().getLogger().severe("Failed to create ItemMeta for KNOWLEDGE_BOOK.");
            return null;
        }
        meta.getPersistentDataContainer().set(TOME_TYPE_KEY, PersistentDataType.STRING, type.name());
        tome.setItemMeta(meta);
        return tome;
    }

    public static ItemStack generateRandomTome() {
        PiglinTrades[] typed = Arrays.stream(PiglinTrades.values())
                .filter(PiglinTrades::requiresTome)
                .toArray(PiglinTrades[]::new);
        return generateTome(typed[ThreadLocalRandom.current().nextInt(typed.length)]);
    }

    public static boolean isTome(ItemStack item) {
        if (item == null || item.getType() != Material.KNOWLEDGE_BOOK || !item.hasItemMeta()) return false;
        return item.getItemMeta().getPersistentDataContainer().has(TOME_TYPE_KEY, PersistentDataType.STRING);
    }

    public static PiglinTrades getTomeType(ItemStack item) {
        if (!isTome(item)) return null;
        String stored = item.getItemMeta().getPersistentDataContainer().get(TOME_TYPE_KEY, PersistentDataType.STRING);
        try {
            return PiglinTrades.valueOf(stored);
        } catch (IllegalArgumentException | NullPointerException e) {
            return null;
        }
    }

    public static PiglinTrades getPiglinType(Piglin piglin) {
        String stored = piglin.getPersistentDataContainer()
                .getOrDefault(PIGLIN_TYPE_KEY, PersistentDataType.STRING, PiglinTrades.DEFAULT.name());
        try {
            return PiglinTrades.valueOf(stored);
        } catch (IllegalArgumentException e) {
            return PiglinTrades.DEFAULT;
        }
    }

    public static void setPiglinType(Piglin piglin, PiglinTrades type) {
        piglin.getPersistentDataContainer().set(PIGLIN_TYPE_KEY, PersistentDataType.STRING, type.name());
    }

    public static boolean hasType(Piglin piglin) {
        return getPiglinType(piglin) != PiglinTrades.DEFAULT;
    }

    public static void applyTome(Piglin piglin, PiglinTrades type) {
        setPiglinType(piglin, type);
    }

    public static ItemStack rollBarter(Piglin piglin) {
        return getPiglinType(piglin).rollLoot();
    }
}