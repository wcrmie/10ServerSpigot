package worm.me.playersystems.abilities.active;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import worm.me.Server;

public class CrescentRight {

    public void crescentRight(Player p) {
        Location start = p.getLocation().clone();
        double radius = 5.0;

        Vector forward = start.getDirection().clone();
        forward.setY(0);
        forward.normalize();

        Vector right = new Vector(-forward.getZ(), 0, forward.getX()).normalize();
        Location center = start.clone().add(forward.clone().multiply(-radius));
        World world = p.getWorld();

        p.getWorld().playSound(p.getLocation(),
                Sound.ENTITY_BREEZE_SHOOT,
                0.7f,
                1.0f);

        new BukkitRunnable() {
            int ticks = 0;
            final int duration = 8;

            @Override
            public void run() {
                if (ticks >= duration || !p.isOnline()) {
                    cancel();
                    return;
                }

                world.spawnParticle(Particle.CURRENT_DOWN, p.getLocation(), 20, 0.6, 1, 0.6, 0.05);

                double progress = (double) ticks / (duration - 1);
                double theta = Math.PI * progress;

                Vector offset = forward.clone().multiply(Math.cos(theta) * radius)
                        .add(right.clone().multiply(Math.sin(theta) * radius));

                Location newLoc = center.clone().add(offset);
                newLoc.setY(start.getY());

                if (!isSafeLocation(newLoc)) {
                    cancel();
                    return;
                }

                Vector outward = newLoc.toVector().subtract(center.toVector()).normalize();
                float yaw = (float) Math.toDegrees(Math.atan2(-outward.getX(), outward.getZ()));
                newLoc.setYaw(yaw);
                newLoc.setPitch(0);

                p.teleport(newLoc);
                ticks++;
            }
        }.runTaskTimer(Server.getPlugin(), 0L, 1L);

        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 1, 1, false, false));
    }


    private boolean isSafeLocation(Location loc) {
        Block feetBlock = loc.getBlock();
        Block headBlock = loc.clone().add(0, 1, 0).getBlock();

        return feetBlock.isPassable() && headBlock.isPassable();
    }
}