package worm.me.PlayerSystems;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import worm.me.Server;

public class SaddleRide {

    private final JavaPlugin plugin;
    public SaddleRide(Server plugin) {
        this.plugin = plugin;
    }

    public void rightClick(PlayerInteractEntityEvent event){
        if (event.getRightClicked() instanceof Player clickedPlayer) {
            Player rider = event.getPlayer();
            ItemStack helmet = clickedPlayer.getInventory().getHelmet();

            if (helmet != null && helmet.getType() == Material.SADDLE) {
                getOnPlayer(rider, clickedPlayer);
            }

        }
    }

    public void leftClick(PlayerAnimationEvent event){

        Player p = event.getPlayer();

        if (!p.getPassengers().isEmpty()){
            Entity rider = p.getPassengers().getFirst();
            if (p.isSneaking()) {
                getThrown(rider, p);
            }
        }
    }

    public void getOnPlayer(Player rider, Player clickedPlayer) {
        Player topPlayer = clickedPlayer;
        Player secondToTopPlayer = null;

        while (!topPlayer.getPassengers().isEmpty() && topPlayer.getPassengers().getFirst() instanceof Player) {
            secondToTopPlayer = topPlayer;
            topPlayer = (Player) topPlayer.getPassengers().getFirst();
        }

        ItemStack helmet = topPlayer.getInventory().getHelmet();
        if (helmet == null || helmet.getType() != Material.SADDLE) {
            if (secondToTopPlayer != null) {
                secondToTopPlayer.removePassenger(topPlayer);
                secondToTopPlayer.addPassenger(rider);
            } else {
                clickedPlayer.removePassenger(topPlayer);
                clickedPlayer.addPassenger(rider);
            }
        } else {
            topPlayer.addPassenger(rider);
        }
    }

    public void getThrown(Entity rider, Player thrower){
        thrower.removePassenger(rider);
        new BukkitRunnable() {
            @Override
            public void run() {
                Location playerLocation = thrower.getLocation();
                org.bukkit.util.Vector direction = thrower.getLocation().getDirection().multiply(1.5);
                rider.setVelocity(direction);
            }
        }.runTaskLater(plugin, 2L);
    }

}
