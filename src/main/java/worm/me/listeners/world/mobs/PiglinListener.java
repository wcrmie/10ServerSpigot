package worm.me.listeners.world.mobs;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Piglin;
import org.bukkit.entity.Player;
import org.bukkit.entity.PigZombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PiglinBarterEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import worm.me.gamesystems.world.mob.PiglinTraders.PiglinCure;
import worm.me.gamesystems.world.mob.PiglinTraders.PiglinTradeManager;
import worm.me.gamesystems.world.mob.PiglinTraders.PiglinTrades;

public class PiglinListener implements Listener {

    @EventHandler
    public void onPiglinBarter(PiglinBarterEvent event) {
        if (!PiglinCure.isCuredPiglin(event.getEntity())) return;

        Piglin piglin = event.getEntity();
        ItemStack reward = PiglinTradeManager.rollBarter(piglin);

        event.getOutcome().clear();
        event.getOutcome().add(reward);
    }


    @EventHandler
    public void onPlayerRightClickPiglin(PlayerInteractEntityEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;

        Entity    clicked = event.getRightClicked();
        Player    player  = event.getPlayer();
        ItemStack hand    = player.getInventory().getItemInMainHand();

        if (clicked instanceof PigZombie pigZombie && PiglinCure.isCureCarrot(hand)) {
            event.setCancelled(true);
            if (PiglinCure.isCuring(pigZombie.getUniqueId())) {
                player.sendMessage(ChatColor.YELLOW + "This piglin is already being cured!");
                return;
            }
            PiglinCure.startCuring(player, pigZombie);
            return;
        }

        if (clicked instanceof Piglin piglin && PiglinCure.isCuredPiglin(piglin)
                && PiglinTradeManager.isTome(hand)) {
            event.setCancelled(true);

            PiglinTrades tomeType = PiglinTradeManager.getTomeType(hand);
            if (tomeType == null) {
                player.sendMessage(ChatColor.RED + "This tome seems corrupted.");
                return;
            }
            if (PiglinTradeManager.hasType(piglin)) {
                PiglinTrades existing = PiglinTradeManager.getPiglinType(piglin);
                player.sendMessage(ChatColor.YELLOW + "This piglin is already a " + ChatColor.GOLD + existing.getName() + ChatColor.YELLOW + "!");
                return;
            }

            PiglinTradeManager.applyTome(piglin, tomeType);
            consumeOneFromHand(player, hand);
            player.sendMessage(ChatColor.GOLD + "The piglin has learned the ways of the " + ChatColor.YELLOW + tomeType.getName() + ChatColor.GOLD + "!");
        }
    }

    private void consumeOneFromHand(Player player, ItemStack hand) {
        if (hand.getAmount() > 1) {
            hand.setAmount(hand.getAmount() - 1);
        } else {
            player.getInventory().setItemInMainHand(null);
        }
    }
}