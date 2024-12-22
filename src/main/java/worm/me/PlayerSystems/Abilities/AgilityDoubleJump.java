package worm.me.PlayerSystems.Abilities;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;


public class AgilityDoubleJump {

    private final AbilitiesMain ability = new AbilitiesMain();

    public void doubleJump(Player p){
        PersistentDataContainer container = p.getPersistentDataContainer();
        Vector direction = p.getLocation().getDirection();
        World world = p.getWorld();
        direction.multiply(0.5);
        direction.setY(0.5);
        p.setVelocity(direction);
        world.spawnParticle(Particle.CLOUD, p.getLocation(), 10, 0.00001, 0.00001, 0.00001);
        world.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, .8f, 1.5f);
        container.set(ability.moveCooldown, PersistentDataType.LONG, System.currentTimeMillis()/1000L + 15);
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy(String.format(ChatColor.RED + "Your Double Jump is now on cooldown for 15 seconds")));
    }
}
