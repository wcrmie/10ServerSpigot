package worm.me.utils.world;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import org.bukkit.Bukkit;

public class WGUtil {

    public static RegionManager getRegionManager(org.bukkit.World world) {
        World wgWorld = BukkitAdapter.adapt(world);
        return WorldGuard.getInstance()
                .getPlatform()
                .getRegionContainer()
                .get(wgWorld);
    }
}
