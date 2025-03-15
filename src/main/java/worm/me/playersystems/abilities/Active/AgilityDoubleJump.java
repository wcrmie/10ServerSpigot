package worm.me.playersystems.abilities.Active;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import worm.me.playersystems.playerdata.levels.Levels;


public class AgilityDoubleJump {

    public void doubleJump(Player p) {
        p.setAllowFlight(false);
        p.setFlying(false);
        Vector direction = p.getLocation().getDirection();
        World world = p.getWorld();
        direction.multiply(0.5);
        direction.setY(0.5);
        p.setVelocity(direction);
        world.spawnParticle(Particle.CLOUD, p.getLocation(), 10, 0.00001, 0.00001, 0.00001);
        world.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, .8f, 1.5f);
        Levels.addExp(5L, Levels.skillTranslation("Agility"), p);
    }
}
