package worm.me.PlayerSystems.Abilities;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;


public class AgilityDoubleJump {

    private final AgilityDash dash = new AgilityDash();

    public void doubleJump(Player p){
        PersistentDataContainer container = p.getPersistentDataContainer();
        Vector direction = p.getLocation().getDirection();
        direction.multiply(0.5);
        direction.setY(0.5);
        p.setVelocity(direction);
        p.setFlying(false);
        p.setAllowFlight(false);
        container.set(dash.dashCooldown, PersistentDataType.LONG, System.currentTimeMillis()/1000L + 15);
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy(String.format(ChatColor.RED + "Your Double Jump is now on cooldown for 15 seconds")));
    }
}
