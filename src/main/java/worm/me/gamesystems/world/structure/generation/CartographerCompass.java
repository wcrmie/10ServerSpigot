package worm.me.gamesystems.world.structure.generation;

import io.papermc.paper.datacomponent.DataComponentTypes;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import worm.me.Server;
import worm.me.gamesystems.world.structure.IslandType;
import worm.me.utils.Item;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class CartographerCompass {

    public ItemStack createCompass(IslandType type) {

        ItemStack compass = new ItemStack(org.bukkit.Material.COMPASS);

        compass = new Item.ItemBuilder(compass).setName(ChatColor.YELLOW + "Cartographer's Compass").addComponent(DataComponentTypes.MAX_STACK_SIZE, 1).build();

        ItemMeta meta = compass.getItemMeta();

        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(new NamespacedKey(Server.getPlugin(), "island_type"), PersistentDataType.STRING, type.toString());

        meta.lore(List.of(Component.text("[RIGHT-CLICK]").color(NamedTextColor.GRAY).append(
                Component.text("to create a new" + type.getName() + " island.").color(NamedTextColor.YELLOW)
        )));

        compass.setItemMeta(meta);

        return compass;
    }

    public String determineFileName(IslandType type) {

        String island = type.toString();

        double size = Math.round(Math.floor(Math.sqrt(ThreadLocalRandom.current().nextInt(1, 24)))); // curse of 1 million parenthesis

        switch ((int) size) {

            case 1 -> island += "_xl";
            case 2 -> island += "_l";
            case 3 -> island += "_m";
            case 4 -> island += "_s";

        }

        return island;
    }



}
