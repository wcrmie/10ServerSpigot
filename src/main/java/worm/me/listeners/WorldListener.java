package worm.me.listeners;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import worm.me.Server;
import worm.me.gamesystems.mobs.traders.WanderingTrader;
import worm.me.gamesystems.worldreset.WorldReset;
import worm.me.playersystems.data.VoidPlayer;
import worm.me.playersystems.data.tools.Tools;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class WorldListener implements Listener {

    private final Server plugin;

    private static List<LivingEntity> tickList = new ArrayList<>();


    public WorldListener(Server plugin) {
        this.plugin = plugin;
    }

    // --------------------Block Events--------------------

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (WorldReset.getInstance().isBlockPlacingDisabled()) {
            event.setCancelled(true);
            return;
        }

        plugin.getWorldBorderHandler().incrementBlockCount(event);
        if (event.getPlayer() != null) {
            VoidPlayer.registerStats(event.getPlayer(), 0);
        }
    }

    @EventHandler
    public void onFlow(BlockFromToEvent event) {

        ProtectedRegion region = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(event.getToBlock().getWorld())).getRegion("spawn");
        if (region.contains(event.getToBlock().getX(), event.getToBlock().getY(), event.getToBlock().getZ())) {
            event.setCancelled(true);
            return;
        }

        if (event.getToBlock().getLocation().getBlockY() > 210) return;

        if (event.getBlock().getType() == Material.WATER || event.getBlock().getType() == Material.LAVA) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        plugin.getWorldBorderHandler().decrementBlockCount(event);
        if (event.getPlayer() != null) {
            VoidPlayer.registerStats(event.getPlayer(), 1);
        }
    }

    @EventHandler
    public void onTreeGrow(StructureGrowEvent e){
        if (e.getSpecies() == TreeType.TREE){
            Location loc = e.getLocation();
            for (int x = loc.getBlockX() - 2; x < loc.getBlockX() + 2; x++){
                for (int z = loc.getBlockZ() - 2; z < loc.getBlockZ() + 2; z++){
                    for (int y = loc.getBlockY() - 2; y < loc.getBlockY(); y++){
                        Block block = loc.getWorld().getBlockAt(x, y, z);
                        if (block.getType() == Material.DIRT && block.getRelative(0, 1, 0).getType() == Material.AIR){
                            int RNG = ThreadLocalRandom.current().nextInt(0, 50);
                            if (RNG == 0) block.setType(Material.GRASS_BLOCK);
                        }
                    }

                }
            }
        }
    }

    // --------------------END Block Events--------------------


    // --------------------Entity Events-----------------------

    @EventHandler
    public void onExplosion(EntityExplodeEvent event) {
        Entity entity = event.getEntity();
        if (entity.getType().equals(EntityType.TNT) || entity.getType().equals(EntityType.TNT_MINECART)) {
            event.setCancelled(true);
            World world = entity.getWorld();
            world.createExplosion(entity.getLocation(), 3);
        }
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {

        if (event.getEntity() instanceof Player) return;

        if (event.getEntity() instanceof Item) {
            Item item = (Item) event.getEntity();
            if (item.getItemStack().getType().toString().toLowerCase().contains("_disc")) {
                item.getItemStack().setData(DataComponentTypes.MAX_STACK_SIZE, 20);
            }
        }

        if (event.getEntity() instanceof LivingEntity entity) tickList.add(entity);

        if (event.getEntity() instanceof org.bukkit.entity.WanderingTrader) {
            WanderingTrader trader = new WanderingTrader();
            trader.newTrades((org.bukkit.entity.WanderingTrader) event.getEntity());
        }

        if (event.getEntity() instanceof Villager villy) {
            villy.setProfession(Villager.Profession.NITWIT);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {

        if (event.getEntity() instanceof Player) return;

        for (ItemStack itemStack : event.getDrops()) {
            if (!Tools.toolType(itemStack.getType()).equals("none")) {
                event.getDrops().remove(itemStack);
            }
        }

        LivingEntity entity = event.getEntity();

        tickList.remove(entity);

        if (event.getEntity() instanceof Bat) {
            event.getDrops().add(new ItemStack(Material.IRON_NUGGET, 1));
        }

    }

    // --------------------END Entity Events-----------------------


}
