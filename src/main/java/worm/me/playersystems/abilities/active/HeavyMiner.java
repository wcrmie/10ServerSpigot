package worm.me.playersystems.abilities.active;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import worm.me.Server;
import worm.me.playersystems.data.levels.Levels;

public class HeavyMiner {

    public void heavyMiner(Player p){

        p.getWorld().playSound(p.getLocation(), Sound.BLOCK_CONDUIT_AMBIENT, .8f, 1.5f);
        p.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, 20 * 10, 2, false, false));
        p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 10, 1, false, true));
        p.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 20 * 10, 1, false, true));
        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 20 * 10, 2, false, true));

        new BukkitRunnable() {
            int ticks = 0;
            @Override
            public void run() {
                if (ticks++ > 20*10) {
                    cancel();
                }
                if (ticks == 10*20){
                    p.getWorld().playSound(p.getLocation(), Sound.BLOCK_CONDUIT_AMBIENT, .8f, 1.5f);
                }

                Vector vel = p.getVelocity();

                Vector resistance = vel.clone().multiply(-1);
                Vector gravity = new Vector(0, -0.6, 0);

                p.setVelocity(vel.add(resistance).add(gravity));

                p.getWorld().spawnParticle(Particle.SQUID_INK, p.getLocation(), 2, 0.2, 0.05, 0.2, 0.01);
            }
        }.runTaskTimer(Server.getPlugin(), 0L, 1L);

        Levels.addExp(5L, Levels.skillTranslation("Mining"), p);

    }

}
