package worm.me.gamesystems.items;

import io.papermc.paper.datacomponent.DataComponentTypes;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import worm.me.utils.Item;

public class ExperienceBottle {

    public static final ItemStack fullBottle = new Item.ItemBuilder(Item.create(Material.EXPERIENCE_BOTTLE, "", ChatColor.GRAY + "Contains 10 Experience.")).addComponent(DataComponentTypes.MAX_STACK_SIZE, 99).build();
    public static final ItemStack emptyBottle = new Item.ItemBuilder(Item.create(Material.GLASS_BOTTLE, "", ChatColor.GRAY + "[SNEAK] + [RIGHT-CLICK] to fill with 10 experience!")).addComponent(DataComponentTypes.MAX_STACK_SIZE, 99).build();

    public static boolean hasEnoughExperience(Player player) {
        return player.getTotalExperience() >= 10;
    }

    public static void fillBottle(Player player) {

        if (hasEnoughExperience(player)) {
            player.setExperienceLevelAndProgress(player.getTotalExperience() - 10);

            int slot = player.getInventory().first(Material.GLASS_BOTTLE);

            player.getInventory().setItem(slot, new Item.ItemBuilder(player.getInventory().getItem(slot)).setAmount(player.getInventory().getItem(slot).getAmount() - 1).build());

            Item.giveOrDrop(fullBottle, player);

            player.playSound(player, Sound.ITEM_BOTTLE_FILL, 1, 0.6F);

        }
    }
}
