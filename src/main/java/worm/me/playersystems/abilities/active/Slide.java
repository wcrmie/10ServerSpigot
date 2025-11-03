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
        p.teleport(p.getLocation().subtract(0, 0.5, 0));

        new BukkitRunnable() {
            int ticks = 0;
            @Override
            public void run() {
                if (ticks++ > 8) {
                    cancel();
                }
                Vector direction = p.getLocation().getDirection();

                direction.multiply(0.9);
                direction.setY(-0.5);
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
