package worm.me.listeners.items;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import worm.me.gamesystems.items.gear.tools.LodestoneTeleporter;

public class ToolListener implements Listener {

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {

        Player player = event.getPlayer();

        if (player.getInventory().getItemInMainHand().getType() == Material.COMPASS) {

            LodestoneTeleporter.handleClick(event);

        }

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {

            EquipmentSlot hand;

            if (player.getInventory().getItemInOffHand().getType().toString().contains("SHOVEL")) hand = EquipmentSlot.OFF_HAND;

            else if ((player.getInventory().getItemInMainHand().getType().toString().contains("SHOVEL"))) hand =  EquipmentSlot.HAND;

            else return;

            if (event.getClickedBlock() != null && (event.getClickedBlock().getType() == Material.DIRT_PATH)) {

                player.swingHand(hand);

                event.getClickedBlock().getWorld().playSound(event.getClickedBlock().getLocation(), Sound.ITEM_HOE_TILL, 1, 2);

                event.getClickedBlock().setType(Material.GRASS_BLOCK);
            }

        }

    }

}
