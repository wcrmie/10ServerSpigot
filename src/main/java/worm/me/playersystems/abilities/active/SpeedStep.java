package worm.me.playersystems.abilities.active;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import worm.me.Server;
import worm.me.playersystems.data.levels.Levels;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SpeedStep {
    private final Map<UUID, BukkitTask> activeDashes = new HashMap<>();

    private final int DASH_DURATION = 10;
    private final double DASH_SPEED = 1.07;

    public void speedStep(Player p) {
        cancelDash(p.getUniqueId());

        p.setGravity(false);
        Vector direction = p.getLocation().getDirection().normalize();
        direction.setY(0).normalize();
        p.getWorld().playSound(p.getLocation(), Sound.ITEM_FIRECHARGE_USE, 1.5f, 1.5f);
        p.teleport(p.getLocation().add(0,0.75, 0));


        BukkitTask dashTask = new BukkitRunnable() {
            private int ticks = 0;

            @Override
            public void run() {
                if (!p.isOnline()) {
                    cleanup(p);
                    this.cancel();
                    return;
                }

                if (ticks < DASH_DURATION) {
                    p.setVelocity(direction.multiply(DASH_SPEED));
                    ticks++;
                } else {
                    cleanup(p);
                    Levels.addExp(5L, Levels.skillTranslation("Agility"), p);
                    this.cancel();
                }
            }
        }.runTaskTimer(Server.getPlugin(), 0L, 1L);

        activeDashes.put(p.getUniqueId(), dashTask);
    }

    public void cleanup(Player p) {
        if (p != null && p.isOnline()) {
            p.setGravity(true);
            p.setVelocity(new Vector(0, 0, 0));
        }
        activeDashes.remove(p.getUniqueId());
    }

    public void cancelDash(UUID playerId) {
        BukkitTask task = activeDashes.remove(playerId);
        if (task != null) {
            task.cancel();
            Player p = Server.getPlugin().getServer().getPlayer(playerId);
            if (p != null) {
                cleanup(p);
            }
        }
    }

    public void cleanupAll() {
        for (UUID playerId : activeDashes.keySet()) {
            cancelDash(playerId);
        }
        activeDashes.clear();
    }
}

