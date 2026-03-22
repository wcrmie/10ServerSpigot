package worm.me.gamesystems.world.mob.boss;

import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Boss {

    private static final ArrayList<BossBar> bossBars = new ArrayList<>();
    private static final ArrayList<Entity> bossEntities = new ArrayList<>();
    protected static final Map<Entity, Boss> bossMap = new HashMap<>();

    protected final Entity m_boss;

    protected final double maxHealth;
    protected double health;
    protected BossBar bossBar;

    protected final double attackDamage;

    protected final float size;

    protected final Location spawnLocation;

    protected boolean isFalling;

    protected Boss(Entity boss, double maxHealth, double attackDamage, float size, Location spawnLocation) {
        this.m_boss = boss;
        this.maxHealth = maxHealth;
        this.attackDamage = attackDamage;
        this.size = size;
        this.spawnLocation = spawnLocation;

        bossBar = initializeBossBar();

        isFalling = false;
    }

    public abstract void spawn(Location location);

    public abstract BossBar initializeBossBar();

    public abstract void updateBossBar();

    public abstract void handleDealDamage(EntityDamageEvent event);

    public abstract void handleTakeDamage(EntityDamageEvent event);

    public abstract List<ItemStack> generateDrops();

    public abstract void handleDeath(EntityDeathEvent event);

    public void destroyBossBar() {

        if (bossBar != null) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                bossBar.removeViewer(p);
            }
        }

        bossBars.remove(bossBar);

    }

    public void setFalling(boolean falling) {
        isFalling = falling;
    }

    public boolean isFalling() {
        return isFalling;
    }

    public static List<BossBar> getBossBars() {
        return bossBars;
    }

    public static List<Entity> getBossEntities() {
        return bossEntities;
    }

    public static Boss getBoss(Entity entity) {
        return bossMap.get(entity);
    }

    public static boolean isBoss(Entity entity) {
        return bossMap.containsKey(entity);
    }

}
