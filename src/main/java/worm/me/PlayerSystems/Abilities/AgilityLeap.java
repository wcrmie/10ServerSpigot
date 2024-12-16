package worm.me.PlayerSystems.Abilities;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import java.util.Objects;


public class AgilityLeap {

    public final AgilityDash dash = new AgilityDash();

    public void leap(Player p){
        PersistentDataContainer container = p.getPersistentDataContainer();
        long cd = Objects.requireNonNullElse(container.get(dash.dashCooldown, PersistentDataType.LONG), 0L);
        if (cd > System.currentTimeMillis()/1000L){
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy(String.format(ChatColor.RED + "Your Leap is on cooldown for %s seconds", cd - System.currentTimeMillis()/1000L)));
            return;
        }
        Vector direction = p.getLocation().getDirection();
        direction.multiply(0.1);
        direction.setY(2.5);
        p.setVelocity(direction);
        container.set(dash.dashCooldown, PersistentDataType.LONG, System.currentTimeMillis()/1000L + 10);
    }
}
