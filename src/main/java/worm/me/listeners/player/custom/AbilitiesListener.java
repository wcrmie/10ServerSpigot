package worm.me.listeners.player.custom;

import net.kyori.adventure.util.TriState;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.EquipmentSlot;
import worm.me.playersystems.abilities.Abilities;


public class AbilitiesListener implements Listener {

    private final Abilities abilities = new Abilities();

    @EventHandler
    public void onPlayerFlightEvent(PlayerToggleFlightEvent e) {
        Player player = e.getPlayer();
        if (player.getGameMode() == GameMode.CREATIVE) return;
        e.setCancelled(true);
        player.setAllowFlight(false);
        player.setFlyingFallDamage(TriState.TRUE);
        Abilities.validateAllAbilitySlots(player);
        abilities.triggerSlot1(player);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (e.getHand() != EquipmentSlot.HAND) return;

        Material item = player.getInventory().getItemInMainHand().getType();

        if (player.isSneaking() && (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) {

            if (item.toString().toLowerCase().contains("sword")) {
                abilities.triggerSlot2(player);
            } else if (item.toString().toLowerCase().contains("_axe")) {
                abilities.triggerSlot3(player);
            }
        }
    }
}
