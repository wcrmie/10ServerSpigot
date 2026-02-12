package worm.me.playersystems.abilities.active;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import worm.me.data.levels.Levels;
import worm.me.Server;

import java.util.concurrent.ThreadLocalRandom;

public class ZigZag {

    public void zigzag(Player p) {
        final boolean startRight = ThreadLocalRandom.current().nextBoolean();

        BukkitTask zigzagTask = new BukkitRunnable() {
            private int step = 0;
            private int totalSteps=5;

            @Override
            public void run() {
                if (!p.isOnline() || step >= totalSteps) {
                    this.cancel();
                    return;
                }

                Vector direction = p.getLocation().getDirection().normalize();
                Vector dashVelocity;

                boolean goRight = startRight ? (step % 2 == 0) : (step % 2 == 1);

                if (goRight) {
                    dashVelocity = getDiagonalRight(direction);
                } else {
                    dashVelocity = getDiagonalLeft(direction);
                }


                dashVelocity.multiply(1.4);
                dashVelocity.setY(0.15);
                p.setVelocity(dashVelocity);

                if (step == 0) {
                    Levels.addExp(5L, Levels.skillTranslation("Agility"), p);
                }

                World world = p.getWorld();
                world.spawnParticle(Particle.ELECTRIC_SPARK, p.getLocation(), 25, 0.2, 0.2, 0.2, 0.05);

                float pitchVariation = 5.0f + (step * 0.2f);
                world.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 0.3f, pitchVariation);

                step++;
            }
        }.runTaskTimer(Server.getPlugin(), 0L, 4L);
    }

    private Vector getDiagonalRight(Vector forward) {
        Vector right = new Vector(-forward.getZ(), 0, forward.getX()).normalize();
        return forward.clone().add(right).normalize();
    }

    private Vector getDiagonalLeft(Vector forward) {
        Vector left = new Vector(forward.getZ(), 0, -forward.getX()).normalize();
        return forward.clone().add(left).normalize();
    }
}