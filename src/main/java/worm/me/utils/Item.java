package worm.me.utils;

import io.papermc.paper.datacomponent.DataComponentType;
import io.papermc.paper.datacomponent.DataComponentType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.units.qual.N;
import worm.me.Server;

import javax.lang.model.type.PrimitiveType;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class Item {

    public static ItemStack create(Material material, String name, String... lore){
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();

        if (meta.lore() != null) Objects.requireNonNull(meta.lore()).clear();


        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ATTRIBUTES);

        meta.displayName(Component.text(name));

        List<String> loreStrings = Arrays.asList(lore);

        List<TextComponent> loreComponents = loreStrings.stream().flatMap(line -> Stream.of(Component.text(line))).toList();

        meta.lore(loreComponents);

        item.setItemMeta(meta);

        return item;
    }

    public static <P, T> T getPersistent(ItemStack item, PersistentDataType<P, T> type, String key) throws Exception {

        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();

        NamespacedKey namespace = new NamespacedKey(Server.getPlugin(), key);

        if (!container.has(namespace, type)) throw new NoSuchFieldException("The key or type was not found in this item.");

        return container.get(namespace, type);
    }

    public static void giveOrDrop(ItemStack item, org.bukkit.entity.Player player) {
        if (player.getInventory().firstEmpty() != -1) {
            player.getInventory().addItem(item);
        } else {
            player.getWorld().dropItemNaturally(player.getLocation(), item);
        }
    }

    public static class ItemBuilder {

        private final ItemStack item;

        public ItemBuilder(ItemStack item) {
            this.item = item;
        }

        public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
            item.addUnsafeEnchantment(enchantment, level);
            return this;
        }

        public <T> ItemBuilder addComponent(DataComponentType.Valued<T> type, T value) {

            item.setData(type, value);
            return this;

        }

        public <T, P> ItemBuilder addPersistent(PersistentDataType<P, T> type, T data, String key) {

            try {

                ItemMeta meta = item.getItemMeta();
                PersistentDataContainer container = meta.getPersistentDataContainer();

                NamespacedKey namespace = new NamespacedKey(Server.getPlugin(), key);

                if (!container.has(namespace, type)) throw new NoSuchFieldException("The key or type was not found in this item.");

                container.set(namespace, type, data);


            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            return this;
        }

        public ItemBuilder setName(String name) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.displayName(Component.text(name));
                item.setItemMeta(meta);
            }
            return this;
        }

        public ItemBuilder setAmount(int amount) {
            item.setAmount(amount);
            return this;
        }

        public ItemStack build() {
            return item;
        }


    }

    public static class PotionBuilder extends ItemBuilder {

        ItemStack item;

        public PotionBuilder(ItemStack item) {
            super(item);
            this.item = item;
        }

        public PotionBuilder addEffect(org.bukkit.potion.PotionEffect effect) {
            PotionMeta potionMeta = (PotionMeta) item.getItemMeta();
            if (potionMeta != null) {
                potionMeta.addCustomEffect(effect, true);
                item.setItemMeta(potionMeta);
            }
            return this;
        }

    }
}