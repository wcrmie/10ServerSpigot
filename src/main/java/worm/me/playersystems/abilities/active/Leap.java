package worm.me.playersystems.abilities.active;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import worm.me.data.levels.Levels;


public class Leap {

    public void leap(Player p){
        Vector direction = p.getLocation().getDirection();
        direction.multiply(0.1);
        direction.setY(1.25);
        p.setVelocity(direction);
        Levels.addExp(5L, Levels.skillTranslation("Agility"), p);

        World world = p.getWorld();
        world.spawnParticle(Particle.GUST_EMITTER_SMALL, p.getLocation(), 1, 0.3, 0.00001, 0.3, 0.05);
        world.playSound(p.getLocation(), Sound.ITEM_TRIDENT_RIPTIDE_1, .8f, 2f);

        p.swingMainHand();

    }
}
