package worm.me.PlayerSystems.Abilities;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import java.util.Date;
import java.util.Objects;


public class AgilityDash {

    public final NamespacedKey dashCooldown = new NamespacedKey("10server", "dashcooldown");

    public void dash(Player p){
        PersistentDataContainer container = p.getPersistentDataContainer();
        long cd = Objects.requireNonNullElse(container.get(dashCooldown, PersistentDataType.LONG), 0L);
        if (cd > System.currentTimeMillis()/1000L){
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy(String.format(ChatColor.RED + "Your Dash is on cooldown for %s seconds", cd - System.currentTimeMillis()/1000L)));
            return;
        }
        Vector direction = p.getLocation().getDirection();
        direction.multiply(1.5);
        direction.setY(0.5);
        p.setVelocity(direction);
        container.set(dashCooldown, PersistentDataType.LONG, System.currentTimeMillis()/1000L + 10);
    }
}
