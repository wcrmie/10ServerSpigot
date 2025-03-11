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


public class AgilityLeap {

    public void leap(Player p){
        Vector direction = p.getLocation().getDirection();
        direction.multiply(0.1);
        direction.setY(1.25);
        p.setVelocity(direction);
    }
}
