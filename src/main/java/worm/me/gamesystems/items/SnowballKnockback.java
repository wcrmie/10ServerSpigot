package worm.me.gamesystems.items;


import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class SnowballKnockback {

    private final JavaPlugin plugin;

    public SnowballKnockback(JavaPlugin plugin) {
        this.plugin = plugin;
    }



    public void onSnowballHit(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player player)) return;
        if (!(e.getDamager() instanceof Snowball snowball)) return;

        ProtectedRegion region = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(player.getWorld())).getRegion("spawn");
        Location loc = player.getLocation();

        // If the player is in spawn, don't kb them
        if (region.contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())) {
            e.setCancelled(true);
            return;
        }

        e.setDamage(0.0);

        Entity hit = e.getEntity();

        Vector knockback = snowball.getVelocity().clone().normalize().multiply(0.8);
        knockback.setY(0.35);

        hit.setVelocity(hit.getVelocity().add(knockback));
    }
}
