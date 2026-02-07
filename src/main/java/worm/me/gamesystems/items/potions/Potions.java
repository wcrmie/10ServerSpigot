package worm.me.gamesystems.items.potions;

import io.papermc.paper.datacomponent.DataComponentTypes;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataType;
import worm.me.Server;
import worm.me.utils.Item;

import java.util.List;

public class Potions {

    public static ItemStack createPotion(PotionType potionType) {

        ItemStack potion = new Item.ItemBuilder(Item.create(Material.POTION, ChatColor.WHITE + potionType.getName(), ChatColor.DARK_GRAY + potionType.getLore())).addComponent(DataComponentTypes.MAX_STACK_SIZE, 4).build();

        PotionMeta meta = (PotionMeta) potion.getItemMeta();

        meta.setColor(Color.fromRGB(potionType.getR(), potionType.getG(), potionType.getB()));

        meta.customName(Component.text(potionType.getName()));

        meta.lore(List.of(Component.text(potionType.getLore())));

        meta.getPersistentDataContainer().set(new NamespacedKey(Server.getPlugin(), "potion_type"), PersistentDataType.INTEGER, potionType.ordinal());

        potion.setItemMeta(meta);

        return potion;

    }

}
