package worm.me.gamesystems.world.mob.boss;

import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class IronGolemBoss extends Boss {

    protected IronGolemBoss(Entity boss, double maxHealth, double attackDamage, float size, Location spawnLocation) {
        super(boss, maxHealth, attackDamage, size, spawnLocation);
    }

    @Override
    public void spawn(Location location) {

    }

    @Override
    public BossBar initializeBossBar() {
        return null;
    }

    @Override
    public void updateBossBar() {

    }
}
