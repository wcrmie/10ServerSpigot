package worm.me.listeners.world.mobs;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.PigZombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTransformEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import worm.me.gamesystems.world.mob.PiglinTraders.PiglinCure;

public class PiglinListener implements Listener {

    @EventHandler
    public void onPlayerRightClickPiglin(PlayerInteractEntityEvent event) {

        if (event.getHand() != EquipmentSlot.HAND) return;

        Entity clicked = event.getRightClicked();
        if (!(clicked instanceof PigZombie)) return;
        PigZombie piglin = (PigZombie) clicked;

        Player    player = event.getPlayer();
        ItemStack hand   = player.getInventory().getItemInMainHand();

        if (!PiglinCure.isCureCarrot(hand)) return;

        event.setCancelled(true);

        if (PiglinCure.isCuring(piglin.getUniqueId())) {
            player.sendMessage(ChatColor.YELLOW + "This piglin is already being cured!");
            return;
        }

        PiglinCure.startCuring(player, piglin);
    }
}