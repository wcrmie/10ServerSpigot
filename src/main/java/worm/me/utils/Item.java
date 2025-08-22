package worm.me.utils;

import io.papermc.paper.datacomponent.DataComponentType;
import io.papermc.paper.datacomponent.DataComponentType;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;

import java.util.Arrays;

public class Item {

    public static ItemStack create(Material material, String name, String... lore){
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();

        if (meta.getLore() != null) meta.getLore().clear();


        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ADDITIONAL_TOOLTIP);

        meta.setDisplayName(name);

        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);

        return item;
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

        public ItemBuilder setName(String name) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.setDisplayName(name);
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