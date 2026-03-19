package worm.me.gamesystems.world.border;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import worm.me.Server;

public class OutOfBorDer {

    private static final double MIN_PUSH = 0.2;

    public static void apply(Player player) {
        if (player.getGameMode() != GameMode.CREATIVE) {
            player.damage(1.0);

            Location playerLoc = player.getLocation();
            Location closest = Server.zoneGridManager.getClosestAllowedLocation(playerLoc);

            if (closest == null) return;

            double dx = closest.getX() - playerLoc.getX();
            double dz = closest.getZ() - playerLoc.getZ();
            double dist = Math.sqrt(dx * dx + dz * dz);

            Vector push;
            if (dist < 0.001) {
                push = new Vector(0, 0, 0);
            } else {
                push = new Vector(dx / dist, 0, dz / dist);
            }

            double strength = Math.max(MIN_PUSH, dist * 0.2);
            push.multiply(strength);

            Vector current = player.getVelocity();
            push.setY(Math.max(current.getY(), -0.5));

            player.setVelocity(push);
        }
    }
}