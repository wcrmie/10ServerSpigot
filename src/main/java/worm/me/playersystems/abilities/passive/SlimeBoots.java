package worm.me.playersystems.abilities.passive;

import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import worm.me.playersystems.abilities.Abilities;

public class SlimeBoots {

    private final Abilities ability = new Abilities();

    public void bounce(Player p) {

        float fallDistance = p.getFallDistance();

        if (fallDistance < 2.3f) return;

        float bouncePercentage = 0.85f;

        float desiredHeight = p.getFallDistance() * bouncePercentage;
        float bounceFactor = (float) Math.sqrt(2 * 0.08 * desiredHeight);

        Vector currentVelocity = p.getVelocity();
        Vector bounceVelocity = new Vector(
                currentVelocity.getX(),
                bounceFactor,
                currentVelocity.getZ()
        );

        p.setVelocity(bounceVelocity);
        World world = (World) p.getWorld();
        world.playSound(p.getLocation(), Sound.ENTITY_SLIME_JUMP, 0.2f, 1.0f);

    }

}