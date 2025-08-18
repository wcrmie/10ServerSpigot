package worm.me.utils.staff;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class Spectate {

    public static Map<Player, Location> spectators = new HashMap<>();

    public static boolean stop(Player player) {

        if (!spectators.containsKey(player)) {
            return false; // Player is not spectating
        }

        player.teleport(spectators.get(player));
        player.setGameMode(GameMode.SURVIVAL);
        spectators.remove(player);
        return true;
    }

    public static void start(Player moderator, Player suspect) {

        if (!moderator.getPassengers().isEmpty()) {
            moderator.getPassengers().forEach(Entity::leaveVehicle);
        }

        if (!spectators.containsKey(moderator)) {
            spectators.put(moderator, moderator.getLocation());
        }
        moderator.setGameMode(GameMode.SPECTATOR);
        moderator.teleport(suspect.getLocation());
    }

}
