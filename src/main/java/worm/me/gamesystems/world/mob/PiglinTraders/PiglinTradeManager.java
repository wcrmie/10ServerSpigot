package worm.me.gamesystems.world.mob.PiglinTraders;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Piglin;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import worm.me.Server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class PiglinTradeManager {

    private static NamespacedKey tomeTypeKey;
    private static NamespacedKey piglinTypeKey;

    private static NamespacedKey getTomeTypeKey() {
        if (tomeTypeKey == null) tomeTypeKey = new NamespacedKey(Server.getPlugin(), "piglin_tome_type");
        return tomeTypeKey;
    }

    private static NamespacedKey getPiglinTypeKey() {
        if (piglinTypeKey == null) piglinTypeKey = new NamespacedKey(Server.getPlugin(), "piglin_type");
        return piglinTypeKey;
    }

    public static ItemStack generateTome(PiglinTrades type) {
        if (!type.requiresTome()) throw new IllegalArgumentException("Cannot create a tome for DEFAULT.");

        ItemStack tome = new ItemStack(Material.KNOWLEDGE_BOOK);
        ItemMeta meta  = Bukkit.getItemFactory().getItemMeta(Material.KNOWLEDGE_BOOK);
        if (meta == null) {
            Server.getPlugin().getLogger().severe("Failed to create ItemMeta for KNOWLEDGE_BOOK.");
            return null;
        }

        meta.getPersistentDataContainer().set(getTomeTypeKey(), PersistentDataType.STRING, type.name());
        meta.setDisplayName(ChatColor.GOLD + "Piglin Knowledge Tome");

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Right-click a " + ChatColor.GOLD + "Cured Piglin" + ChatColor.GRAY + " to teach them the ways of:");
        lore.add("");
        lore.add(ChatColor.YELLOW + "" + ChatColor.BOLD + type.getName());
        lore.add("");
        lore.add(ChatColor.DARK_GRAY + type.getDescription());
        meta.setLore(lore);

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
        return item.getItemMeta().getPersistentDataContainer().has(getTomeTypeKey(), PersistentDataType.STRING);
    }

    public static PiglinTrades getTomeType(ItemStack item) {
        if (!isTome(item)) return null;
        String stored = item.getItemMeta().getPersistentDataContainer().get(getTomeTypeKey(), PersistentDataType.STRING);
        try {
            return PiglinTrades.valueOf(stored);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static PiglinTrades getPiglinType(Piglin piglin) {
        String stored = piglin.getPersistentDataContainer()
                .getOrDefault(getPiglinTypeKey(), PersistentDataType.STRING, PiglinTrades.DEFAULT.name());
        try {
            return PiglinTrades.valueOf(stored);
        } catch (IllegalArgumentException e) {
            return PiglinTrades.DEFAULT;
        }
    }

    public static void setPiglinType(Piglin piglin, PiglinTrades type) {
        piglin.getPersistentDataContainer().set(getPiglinTypeKey(), PersistentDataType.STRING, type.name());
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