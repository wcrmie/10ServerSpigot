package worm.me.playersystems.abilities.Active;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import worm.me.playersystems.abilities.Abilities;
import worm.me.playersystems.playerdata.levels.Levels;


public class AgilityDash {

    private final Abilities ability = new Abilities();

    public void dash(Player p){
        Vector direction = p.getLocation().getDirection();
        direction.multiply(1.5);
        direction.setY(0.5);
        p.setVelocity(direction);
        Levels.addExp(5L, Levels.skillTranslation("Agility"), p);
    }
}
