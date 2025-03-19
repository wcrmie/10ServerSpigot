package worm.me.playersystems.abilities.active;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import worm.me.playersystems.data.levels.Levels;


public class AgilityLeap {

    public void leap(Player p){
        Vector direction = p.getLocation().getDirection();
        direction.multiply(0.1);
        direction.setY(1.25);
        p.setVelocity(direction);
        Levels.addExp(5L, Levels.skillTranslation("Agility"), p);
    }
}
