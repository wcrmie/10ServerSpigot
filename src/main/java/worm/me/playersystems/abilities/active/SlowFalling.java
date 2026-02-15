package worm.me.playersystems.abilities.active;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import worm.me.Server;


public class SlowFalling {

    public void slowFalling(Player p){

        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 60, 1, false, false));

        p.getWorld().playSound(
                p.getLocation(),
                Sound.ENTITY_PHANTOM_FLAP,
                0.3f,
                0.6f
        );

        new BukkitRunnable() {
            int ticks = 0;

            @Override
            public void run() {

                if (ticks >= 20) {
                    cancel();
                    return;
                }

                Vector velocity = p.getVelocity();

                velocity.setY(0.18);
                p.setVelocity(velocity);

                ticks++;
            }

        }.runTaskTimer(Server.getPlugin(), 0L, 1L);
    }



}

