package worm.me.gamesystems.world.mob.boss;

import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class Boss {

    private static final ArrayList<BossBar> bossBars = new ArrayList<>();
    private static final ArrayList<Entity> bossEntities = new ArrayList<>();

    private final Entity boss;
    private final double maxHealth;
    private final double attackDamage;
    private final float size;
    private final Location spawnLocation;
    private BossBar bossBar;

    protected Boss(Entity boss, double maxHealth, double attackDamage, float size, Location spawnLocation) {
        this.boss = boss;
        this.maxHealth = maxHealth;
        this.attackDamage = attackDamage;
        this.size = size;
        this.spawnLocation = spawnLocation;
        bossBar = initializeBossBar();
    }

    public abstract void spawn(Location location);

    public abstract BossBar initializeBossBar();

    public abstract void updateBossBar();

    public void destroyBossBar() {

        if (bossBar != null) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                bossBar.removeViewer(p);
            }
        }

        bossBars.remove(bossBar);

    }

    public static List<BossBar> getBossBars() {
        return bossBars;
    }

    public static List<Entity> getBossEntities() {
        return bossEntities;
    }

}
