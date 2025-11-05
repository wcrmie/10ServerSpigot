package worm.me.playersystems.abilities.active;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import worm.me.Server;
import worm.me.playersystems.data.levels.Levels;

public class Slide {

    public void slide (Player p){

        Location loc = p.getLocation().clone();
        World world = loc.getWorld();

        double step = 0.1;
        double maxDrop = 1.2;
        double dropped = 0.0;

        while (dropped < maxDrop) {
            Block blockBelow = world.getBlockAt(loc.clone().subtract(0, 0.2, 0));
            if (blockBelow.getType() != Material.AIR) {
                break;
            }
            loc.subtract(0, step, 0);
            dropped += step;
        }

        p.teleport(loc);

        new BukkitRunnable() {
            double speed = 0.2;
            int ticks = 0;
            @Override
            public void run() {
                if (ticks++ > 25) {
                    cancel();
                }
                Vector direction = p.getLocation().getDirection();

                if (ticks < 6){
                    speed += 0.175;
                }

                if (ticks > 16){
                    speed -= 0.1;
                }

                direction.multiply(speed);
                direction.setY(speed * -0.5);
                p.setVelocity(direction);

                Block blockBelow = p.getLocation().subtract(0, 1, 0).getBlock();
                p.getWorld().spawnParticle(Particle.SNOWFLAKE, p.getLocation(), 2, 0.2, 0.05, 0.2, 0.01);
                if (blockBelow.getType() == Material.AIR) cancel();
            }
        }.runTaskTimer(Server.getPlugin(), 0L, 1L);

        Levels.addExp(1L, Levels.skillTranslation("Agility"), p);

        p.getWorld().playSound(p.getLocation(), Sound.BLOCK_GRINDSTONE_USE, .8f, 1.5f);
    }
}
