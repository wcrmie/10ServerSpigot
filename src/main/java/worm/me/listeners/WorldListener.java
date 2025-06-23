package worm.me.listeners;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import io.papermc.paper.datacomponent.DataComponentTypes;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.units.qual.N;
import worm.me.Server;
import worm.me.gamesystems.blocks.BlockManipulation;
import worm.me.gamesystems.mobs.traders.WanderingTrader;
import worm.me.gamesystems.worldreset.WorldReset;
import worm.me.playersystems.data.VoidPlayer;
import worm.me.playersystems.data.tools.Tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    public void onEntityTarget(EntityTargetEvent e) {
        if (e.getEntity().getVehicle() instanceof Player) e.setCancelled(true);
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {

        if (event.getEntity() instanceof Player) return;

        if (event.getEntity() instanceof Item) {
            Item item = (Item) event.getEntity();
            if (item.getItemStack().getType().toString().toLowerCase().contains("_disc")) {
                item.getItemStack().setData(DataComponentTypes.MAX_STACK_SIZE, 20);
            } else if (item.getItemStack().getType().equals(Material.DIRT)) {
                item.getItemStack().setData(DataComponentTypes.MAX_STACK_SIZE, 99);
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

        if (event.getEntity() instanceof Spider spider && event.getEntity().getEntitySpawnReason().equals(CreatureSpawnEvent.SpawnReason.NATURAL)) {

            spider.getAttribute(Attribute.SCALE).setBaseValue(0.3);
            spider.getAttribute(Attribute.ATTACK_DAMAGE).setBaseValue(2);
            spider.getAttribute(Attribute.MAX_HEALTH).setBaseValue(8);

            int RNG = ThreadLocalRandom.current().nextInt(1,3);

            for (int i = 0; i < RNG; i++) {

                spider.getWorld().spawn(spider.getLocation(), Spider.class, spawnedSpider -> {
                    spawnedSpider.getAttribute(Attribute.SCALE).setBaseValue(0.3);
                    spawnedSpider.getAttribute(Attribute.ATTACK_DAMAGE).setBaseValue(2);
                    spawnedSpider.getAttribute(Attribute.MAX_HEALTH).setBaseValue(8);
                });


            }

        }

        Entity entity = event.getEntity();

        if (entity instanceof Zombie || entity instanceof Skeleton || entity instanceof Stray || entity instanceof Cow || entity instanceof Pig || entity instanceof Sheep || entity instanceof Chicken || entity instanceof Villager || entity instanceof Parrot) {


            LivingEntity e = (LivingEntity) entity;

            double rng = ThreadLocalRandom.current().nextDouble(0.85, 1.15);

            e.getAttribute(Attribute.SCALE).setBaseValue(rng);

        }

        if (entity.getEntitySpawnReason().equals(CreatureSpawnEvent.SpawnReason.BREEDING)) {

            int RNG = ThreadLocalRandom.current().nextInt(1, 10);

            if (entity instanceof Rabbit) {
                RNG = ThreadLocalRandom.current().nextInt(4, 20);
            }

            World world = entity.getWorld();
            if (RNG >= 15){

                Entity twin = world.spawn(entity.getLocation(), entity.getType().getEntityClass());
                Breedable livingTwin = (Breedable) twin;
                livingTwin.setBaby();

                Entity twin2 = world.spawn(entity.getLocation(), entity.getType().getEntityClass());
                Breedable livingTwin2 = (Breedable) twin2;
                livingTwin2.setBaby();

                Entity twin3 = world.spawn(entity.getLocation(), entity.getType().getEntityClass());
                Breedable livingTwin3 = (Breedable) twin3;
                livingTwin3.setBaby();


            } else if (RNG >= 9) {

                Entity twin = world.spawn(entity.getLocation(), entity.getType().getEntityClass());
                Breedable livingTwin = (Breedable) twin;
                livingTwin.setBaby();

                Entity twin2 = world.spawn(entity.getLocation(), entity.getType().getEntityClass());
                Breedable livingTwin2 = (Breedable) twin2;
                livingTwin2.setBaby();



            } else if (RNG >= 5) {
                Entity twin = world.spawn(entity.getLocation(), entity.getType().getEntityClass());
                Breedable livingTwin = (Breedable) twin;

                livingTwin.setBaby();
            }

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

        // Change drop system in future
        if (event.getEntity().getType() == EntityType.ENDERMAN) {
            int emeraldCount = ThreadLocalRandom.current().nextInt(0, 3);
            event.getDrops().add(new ItemStack(Material.EMERALD, emeraldCount));
        }
        if (event.getEntity().getType() == EntityType.ENDERMITE) {
            int emeraldCount = ThreadLocalRandom.current().nextInt(0, 2);
            event.getDrops().add(new ItemStack(Material.SMALL_AMETHYST_BUD, emeraldCount));
        }
        if (event.getEntity().getType() == EntityType.SILVERFISH) {
            int emeraldCount = ThreadLocalRandom.current().nextInt(3, 6);
            event.getDrops().add(new ItemStack(Material.IRON_NUGGET, emeraldCount));
        }
        if (event.getEntity().getType() == EntityType.BAT) {
            int emeraldCount = ThreadLocalRandom.current().nextInt(0, 2);
            event.getDrops().add(new ItemStack(Material.IRON_NUGGET, emeraldCount));
        }
        if (event.getEntity().getType() == EntityType.DROWNED) {
            int emeraldCount = ThreadLocalRandom.current().nextInt(4, 6);
            event.getDrops().add(new ItemStack(Material.RAW_COPPER, emeraldCount));
        }

    }

    // --------------------END Entity Events-----------------------


}
