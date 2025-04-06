package worm.me.playersystems.abilities.passive;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import worm.me.playersystems.abilities.Abilities;

public class SlimeBoots {

    public void bounce(Player p) {

        float fallDistance = p.getFallDistance();

        if (fallDistance < 2.3f) return;

        float bouncePercentage = 0.85f;
        float desiredHeight = fallDistance * bouncePercentage;

        // Calculate vertical bounce velocity using physics
        float bounceFactor = (float) Math.sqrt(2 * 0.08 * desiredHeight);

        Vector currentVelocity = p.getVelocity();
        Vector bounceVelocity = new Vector(
                currentVelocity.getX() * 1.2f,
                bounceFactor,
                currentVelocity.getZ() * 1.2f
        );

        p.setVelocity(bounceVelocity);
        World world = p.getWorld();
        world.playSound(p.getLocation(), Sound.ENTITY_SLIME_JUMP, 0.2f, 1.0f);
        world.spawnParticle(Particle.ITEM_SLIME, p.getLocation(), 3, 3, 0.00001, 0.3, 0.05);

    }

}