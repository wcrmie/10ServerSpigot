package worm.me.listeners.items;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;
import worm.me.Server;
import worm.me.gamesystems.items.potions.Potions;
import worm.me.playersystems.attributes.SizeModifier;
import worm.me.utils.Item;
import worm.me.utils.chat.Message;
import worm.me.utils.chat.MessageType;

import java.util.Objects;

public class PotionListener implements Listener {

    @EventHandler
    public void onDrink(org.bukkit.event.player.PlayerItemConsumeEvent event) {
        if (event.getItem().getType().toString().contains("POTION")) {

            ItemStack item = event.getItem();

            Integer potionType = item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Server.getPlugin(), "potion_type"), org.bukkit.persistence.PersistentDataType.INTEGER);

            if (potionType == null) return;

            int type = potionType;

            Player player = event.getPlayer();

            switch (type) {

                case 0: {

                    double playerSize = SizeModifier.getSize(player);

                    if (playerSize <= 0.3) {

                        player.sendMessage(Message.create(MessageType.FAIL, "You cannot grow any smaller..."));
                        event.setCancelled(true);
                        return;

                    }

                    SizeModifier.setPlayerSize(player, playerSize - 0.2 * playerSize);
                    Objects.requireNonNull(player.getAttribute(Attribute.MAX_HEALTH)).setBaseValue(20*(playerSize - 0.2 * playerSize));
                    Objects.requireNonNull(player.getAttribute(Attribute.MOVEMENT_SPEED)).setBaseValue(0.1*((playerSize+0.1)/2));
                    Objects.requireNonNull(player.getAttribute(Attribute.BLOCK_INTERACTION_RANGE)).setBaseValue(4.5+playerSize);
                    Objects.requireNonNull(player.getAttribute(Attribute.ENTITY_INTERACTION_RANGE)).setBaseValue(3+playerSize);

                    break;
                }
                case 1: {

                    double playerSize = SizeModifier.getSize(player);

                    if (playerSize >= 2.25) {

                        player.sendMessage(Message.create(MessageType.FAIL, "You cannot grow any bigger..."));
                        event.setCancelled(true);
                        return;

                    }

                    SizeModifier.setPlayerSize(player, playerSize + 0.2 * playerSize);
                    Objects.requireNonNull(player.getAttribute(Attribute.MAX_HEALTH)).setBaseValue(20*(playerSize + 0.2 * playerSize));
                    Objects.requireNonNull(player.getAttribute(Attribute.MOVEMENT_SPEED)).setBaseValue(0.1*((playerSize+0.1)/2));
                    Objects.requireNonNull(player.getAttribute(Attribute.BLOCK_INTERACTION_RANGE)).setBaseValue(4.5-playerSize);
                    Objects.requireNonNull(player.getAttribute(Attribute.ENTITY_INTERACTION_RANGE)).setBaseValue(3-playerSize);


                    break;
                }
            }
        }
    }

    @EventHandler
    public void onDeath(org.bukkit.event.entity.PlayerDeathEvent event) {

        Player player = event.getEntity();
        SizeModifier.setPlayerSize(player, 1);
        Objects.requireNonNull(player.getAttribute(Attribute.MAX_HEALTH)).setBaseValue(20);
        Objects.requireNonNull(player.getAttribute(Attribute.MOVEMENT_SPEED)).setBaseValue(0.1);
        Objects.requireNonNull(player.getAttribute(Attribute.BLOCK_INTERACTION_RANGE)).setBaseValue(4.5);
        Objects.requireNonNull(player.getAttribute(Attribute.ENTITY_INTERACTION_RANGE)).setBaseValue(3);

    }

    @EventHandler
    public void onCreate(InventoryClickEvent event) {

        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null) return;

        ItemStack cursorItem = event.getCursor();

        if (event.getCurrentItem().getType() == Material.POTION) {

            PotionMeta meta = (PotionMeta) clickedItem.getItemMeta();

            if (meta.getBasePotionType().equals(PotionType.WATER)) {

                for (worm.me.gamesystems.items.potions.PotionType customPotion : worm.me.gamesystems.items.potions.PotionType.values()) {

                    if (cursorItem.getType() == customPotion.getMaterial()) {

                        cursorItem.setAmount(cursorItem.getAmount() - 1);

                        clickedItem.setAmount(clickedItem.getAmount() - 1);

                        Item.giveOrDrop(Potions.createPotion(customPotion), (Player) event.getWhoClicked());

                    }
                }

            }
        }


    }
}
