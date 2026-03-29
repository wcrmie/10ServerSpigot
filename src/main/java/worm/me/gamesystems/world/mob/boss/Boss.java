package worm.me.gamesystems.world.mob.boss;

import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import worm.me.Server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Boss implements Listener {

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
        this.isFalling = false;
        this.bossBar = initializeBossBar();
    }

    public void enable() {
        bossMap.put(m_boss, this);
        bossEntities.add(m_boss);
        bossBars.add(bossBar);
        Bukkit.getPluginManager().registerEvents(this, Server.getPlugin());
    }

    public void disable() {
        HandlerList.unregisterAll(this);
        bossMap.remove(m_boss);
        bossEntities.remove(m_boss);
        destroyBossBar();
    }

    public abstract void spawn(Location location);
    public abstract BossBar initializeBossBar();
    public abstract void updateBossBar();
    public abstract List<ItemStack> generateDrops();

    public void destroyBossBar() {
        if (bossBar != null) {
            Bukkit.getOnlinePlayers().forEach(bossBar::removeViewer);
        }
        bossBars.remove(bossBar);
    }

    public void setFalling(boolean falling) { isFalling = falling; }
    public boolean isFalling()              { return isFalling; }

    public static List<BossBar> getBossBars()       { return bossBars; }
    public static List<Entity>  getBossEntities()   { return bossEntities; }
    public static Boss           getBoss(Entity e)  { return bossMap.get(e); }
    public static boolean        isBoss(Entity e)   { return bossMap.containsKey(e); }

    public static void disableAll() {
        new ArrayList<>(bossMap.values()).forEach(Boss::disable);
    }
}