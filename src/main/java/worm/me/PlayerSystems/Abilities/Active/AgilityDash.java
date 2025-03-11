package worm.me.PlayerSystems.Abilities.Active;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import worm.me.PlayerSystems.Abilities.Abilities;

import java.util.Objects;


public class AgilityDash {

    private final Abilities ability = new Abilities();

    public void dash(Player p){
        Vector direction = p.getLocation().getDirection();
        direction.multiply(1.5);
        direction.setY(0.5);
        p.setVelocity(direction);
    }
}
