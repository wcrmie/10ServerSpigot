package worm.me.playersystems.abilities.active;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import worm.me.data.levels.Levels;
import worm.me.Server;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FeatherDash {
    private final Map<UUID, BukkitTask> activeDashes = new HashMap<>();

    private final int DASH_DURATION = 20;
    private final double DASH_SPEED = 0.5;

    public void featherDash(Player player) {
        cancelDash(player.getUniqueId());

        player.setGravity(false);

        BukkitTask dashTask = new BukkitRunnable() {
            private int ticks = 0;

            @Override
            public void run() {
                if (!player.isOnline()) {
                    cleanup(player);
                    this.cancel();
                    return;
                }

                if (ticks < DASH_DURATION) {
                    Vector direction = player.getLocation().getDirection().normalize();
                    player.setVelocity(direction.multiply(DASH_SPEED));
                    PotionEffect effect = new PotionEffect(PotionEffectType.SLOW_FALLING, 20, 1);
                    player.addPotionEffect(effect);
                    player.setFallDistance(0);
                    ticks++;
                } else {
                    cleanup(player);
                    Levels.addExp(5L, Levels.skillTranslation("Agility"), player);
                    PotionEffect effect = new PotionEffect(PotionEffectType.SLOW_FALLING, 30, 1);
                    player.addPotionEffect(effect);
                    this.cancel();
                }
                if (ticks % 4 == 0 && ticks != 20) {
                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PARROT_FLY, 1f, 1f);
                }
            }
        }.runTaskTimer(Server.getPlugin(), 0L, 1L);

        activeDashes.put(player.getUniqueId(), dashTask);
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