package worm.me.PlayerSystems;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import worm.me.server;

public class saddleRide {

    private final JavaPlugin plugin;
    public saddleRide(server plugin) {
        this.plugin = plugin;
    }

    public void rightClick(PlayerInteractEntityEvent event){
        Bukkit.getServer().broadcastMessage("right click");
        if (event.getRightClicked() instanceof Player) {
            Player clickedPlayer = (Player) event.getRightClicked();
            Player rider = event.getPlayer();
            ItemStack helmet = clickedPlayer.getInventory().getHelmet();

            if (helmet != null && helmet.getType() == Material.SADDLE) {
                getOnPlayer(rider, clickedPlayer);
            }

        }
    }

    public void leftClick(EntityDamageByEntityEvent event){
        Bukkit.getServer().broadcastMessage("left click");
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player){
            Player thrower = (Player) event.getDamager();
            Player rider = (Player) event.getEntity();
            if (thrower.getPassengers().contains(rider)) {
                if (thrower.isSneaking()) {
                    getThrown(rider, thrower);
                }
            }
        }
    }

    public void getOnPlayer(Player rider, Player clickedPlayer) {
        Bukkit.getServer().broadcastMessage("get on player");
        Player topPlayer = clickedPlayer;
        while (!topPlayer.getPassengers().isEmpty() && topPlayer.getPassengers().get(0) instanceof Player) {
            topPlayer = (Player) topPlayer.getPassengers().get(0);
        }
        topPlayer.addPassenger(rider);
    }

    public void getThrown(Player rider, Player thrower){
        Bukkit.getServer().broadcastMessage("get thrown");
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
