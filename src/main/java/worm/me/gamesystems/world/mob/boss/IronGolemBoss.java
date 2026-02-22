package worm.me.gamesystems.world.mob.boss;

import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.Location;

public class IronGolemBoss extends Boss {
    
    protected IronGolemBoss(double maxHealth, double attackDamage, float size, Location spawnLocation) {
        super(maxHealth, attackDamage, size, spawnLocation);
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
