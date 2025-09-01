package worm.me.gamesystems.world.blocks;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;
import worm.me.Server;

public class BlockManipulation {

    public void decayBlock(Block block) {

        new BukkitRunnable() {
            @Override
            public void run() {

                if (!block.getType().isAir()) {
                    block.setType(Material.AIR);
                    block.getWorld().playSound(block.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1f, 0.1f);
                    block.getWorld().spawnParticle(Particle.CLOUD, block.getLocation(), 10, 0.3, 0.00001, 0.3, 0.05);
                }

            }
        }.runTaskLater(Server.getPlugin(), 600L); // 30 second block decay
    }
}
