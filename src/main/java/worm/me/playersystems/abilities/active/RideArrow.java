package worm.me.playersystems.abilities.active;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import worm.me.data.levels.Levels;

public class RideArrow {

    public void shootRiddenArrow(Player p) {

        Vector direction = p.getLocation().getDirection();
        Arrow arrow = p.launchProjectile(Arrow.class, direction.multiply(0.8));
        arrow.addPassenger(p);
        Levels.addExp(5L, Levels.skillTranslation("Agility"), p);

        World world = p.getWorld();
        world.spawnParticle(Particle.SWEEP_ATTACK, p.getLocation(), 5, 0.3, 0.00001, 0.3, 0.05);
        world.spawnParticle(Particle.EXPLOSION_EMITTER, p.getLocation(), 1, 0, 0.00001, 0.3, 0.05);
        world.playSound(p.getLocation(), Sound.ITEM_BRUSH_BRUSHING_SAND_COMPLETE, 0.5f,0.1f);


    }
}
