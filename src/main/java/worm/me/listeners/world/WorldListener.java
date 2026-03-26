package worm.me.listeners.world;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import io.papermc.paper.datacomponent.DataComponentTypes;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import worm.me.Server;
import worm.me.gamesystems.halloween.PumpkinPatch;
import worm.me.gamesystems.items.ExperienceBottle;
import worm.me.gamesystems.items.SnowballKnockback;
import worm.me.gamesystems.world.mob.traders.Wandering;
import worm.me.gamesystems.world.cleanup.WorldReset;
import worm.me.gamesystems.world.legacystructure.TreasureChests;
import worm.me.data.VoidPlayer;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class WorldListener implements Listener {

    private final Server plugin;

    public static List<LivingEntity> tickList = new ArrayList<>();

    public static Map<IronGolem, BossBar> ironGolemBossBars = new HashMap<>();

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
        if(!Server.zoneGridManager.isInAllowedGrid(event.getBlock().getLocation())) {
            event.setCancelled(true);
            return;
        }
        if (event.getPlayer() != null) {
            VoidPlayer.registerStats(event.getPlayer(), 0);
        }
    }

    @EventHandler
    public void onPistonExtend(BlockPistonExtendEvent event) {
        BlockFace direction = event.getDirection();

        for (Block pushed : event.getBlocks()) {
            Block destination = pushed.getRelative(direction);
            if (!Server.zoneGridManager.isInAllowedGrid(destination.getLocation())) {
                event.setCancelled(true);
                return;
            }
            if (destination.getY() < 193) {
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onFlow(BlockFromToEvent event) {
        Block from = event.getBlock();
        Block to   = event.getToBlock();

        ProtectedRegion region = WorldGuard.getInstance()
                .getPlatform().getRegionContainer()
                .get(BukkitAdapter.adapt(to.getWorld()))
                .getRegion("spawn");
        if (region != null && region.contains(to.getX(), to.getY(), to.getZ())) {
            event.setCancelled(true);
            return;
        }

        if (!Server.zoneGridManager.isInAllowedGrid(to.getLocation())) {
            event.setCancelled(true);
            return;
        }

        if (to.getLocation().getBlockY() <= 210) {
            if (from.getType() == Material.WATER || from.getType() == Material.LAVA) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onVineGrowth(BlockSpreadEvent event) {
        Material type = event.getSource().getType();
        if (type != Material.VINE && type != Material.WEEPING_VINES && type != Material.TWISTING_VINES) {
            return;
        }

        if (event.getBlock().getY() < 195) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        if (event.getPlayer() != null) {
            VoidPlayer.registerStats(event.getPlayer(), 1);
        }

        if (event.getBlock().getType().equals(Material.ICE) || event.getBlock().getType().equals(Material.PACKED_ICE) || event.getBlock().getType().equals(Material.BLUE_ICE)) {
            event.setCancelled(true);
            event.getBlock().setType(Material.AIR);
            event.getBlock().getLocation().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(event.getBlock().getType()));
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
        } else if (e.getSpecies() == TreeType.RED_MUSHROOM) {

            e.getLocation().getNearbyEntities(5, 5, 5).stream()
                    .filter(entity -> entity instanceof Cow)
                    .forEach(entity -> {

                        int RNG = ThreadLocalRandom.current().nextInt(0, 10);
                        if (RNG == 0) {
                            Cow cow = (Cow) entity;
                            cow.getWorld().spawn(cow.getLocation(), MushroomCow.class);
                            cow.getWorld().playSound(e.getLocation(), Sound.ENTITY_EVOKER_CAST_SPELL, 1F, 0.5F);
                            cow.remove();
                        }

                    });


            Location loc = e.getLocation();
            for (int x = loc.getBlockX() - 2; x < loc.getBlockX() + 2; x++){
                for (int z = loc.getBlockZ() - 2; z < loc.getBlockZ() + 2; z++){
                    for (int y = loc.getBlockY() - 2; y < loc.getBlockY(); y++){
                        Block block = loc.getWorld().getBlockAt(x, y, z);
                        if (block.getType() == Material.DIRT && block.getRelative(0, 1, 0).getType() == Material.AIR){
                            int RNG = ThreadLocalRandom.current().nextInt(0, 25);
                            if (RNG == 0) block.setType(Material.MYCELIUM);
                        }
                    }

                }
            }

        } else if (e.getSpecies() == TreeType.BROWN_MUSHROOM) {

            e.getLocation().getNearbyEntities(5, 5, 5).stream()
                    .filter(entity -> entity instanceof Cow)
                    .forEach(entity -> {

                        int RNG = ThreadLocalRandom.current().nextInt(0, 10);
                        if (RNG == 0) {
                            Cow cow = (Cow) entity;
                            cow.getWorld().spawn(cow.getLocation(), MushroomCow.class, mushroomCow -> {
                                mushroomCow.setVariant(MushroomCow.Variant.BROWN);
                            });
                            cow.getWorld().playSound(e.getLocation(), Sound.ENTITY_EVOKER_CAST_SPELL, 1F, 0.5F);
                            cow.remove();
                        }

                    });

            Location loc = e.getLocation();
            for (int x = loc.getBlockX() - 2; x < loc.getBlockX() + 2; x++){
                for (int z = loc.getBlockZ() - 2; z < loc.getBlockZ() + 2; z++){
                    for (int y = loc.getBlockY() - 2; y < loc.getBlockY(); y++){
                        Block block = loc.getWorld().getBlockAt(x, y, z);
                        if (block.getType() == Material.DIRT && block.getRelative(0, 1, 0).getType() == Material.AIR){
                            int RNG = ThreadLocalRandom.current().nextInt(0, 25);
                            if (RNG == 0) block.setType(Material.MYCELIUM);
                        }
                    }

                }
            }

        }
    }

    @EventHandler
    public void onPumpkinGrow(BlockGrowEvent event) {

        if (event.getNewState().getType().equals(Material.PUMPKIN)) {

            event.getBlock().setType(Material.AIR);

            Server.getPlugin().getLogger().info("pumkin");

            if (PumpkinPatch.tryGrowUndead(event.getBlock().getLocation())){
                event.getNewState().setType(Material.AIR);
            };

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
            world.createExplosion(entity.getLocation(), 4);
        }
    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent e) {
        if (e.getEntity().getVehicle() instanceof Player) e.setCancelled(true);
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {

        if (event.getEntity() instanceof Player) return;

        if (event.getEntity() instanceof Witch) {

            int RNG = ThreadLocalRandom.current().nextInt(5, 10);

            for (int i = 0; i < RNG; i++) {

                event.getEntity().getWorld().spawn(event.getEntity().getLocation(), Bat.class);

            }

            if (ThreadLocalRandom.current().nextInt(0, 2) == 0) {

                event.getEntity().getWorld().spawn(event.getEntity().getLocation(), Cat.class ,cat -> {
                    cat.setCatType(Cat.Type.ALL_BLACK);
                });

            }

        }

        if (event.getEntity() instanceof Item) {
            Item item = (Item) event.getEntity();
            if (item.getItemStack().getType().toString().toLowerCase().contains("_disc")) {
                item.getItemStack().setData(DataComponentTypes.MAX_STACK_SIZE, 20);
            } else if (item.getItemStack().getType().equals(Material.DIRT)) {
                item.getItemStack().setData(DataComponentTypes.MAX_STACK_SIZE, 99);
            } else if (item.getItemStack().getType().equals(Material.GLASS_BOTTLE)) {
                item.setItemStack(new worm.me.utils.Item.ItemBuilder(ExperienceBottle.emptyBottle).setAmount(item.getItemStack().getAmount()).build());
            } else if (item.getItemStack().getType().equals(Material.GOAT_HORN)) {
                item.getItemStack().setData(DataComponentTypes.MAX_STACK_SIZE, 4);
            } else if (item.getItemStack().getType().toString().toLowerCase().contains("potion") && !item.getItemStack().getType().toString().toLowerCase().contains("arrow")) {
                item.getItemStack().setData(DataComponentTypes.MAX_STACK_SIZE, 6);
            } else if (item.getItemStack().getType().toString().toLowerCase().contains("_CHESTPLATE")) {
                item.getItemStack().setData(DataComponentTypes.MAX_STACK_SIZE, 4);
            } else if (item.getItemStack().getType().toString().toLowerCase().contains("_LEGGINGS")) {
                item.getItemStack().setData(DataComponentTypes.MAX_STACK_SIZE, 4);
            } else if (item.getItemStack().getType().toString().toLowerCase().contains("_BOOTS")) {
                item.getItemStack().setData(DataComponentTypes.MAX_STACK_SIZE, 4);
            }
        }

        if (event.getEntity() instanceof LivingEntity entity) tickList.add(entity);

        if (event.getEntity() instanceof org.bukkit.entity.WanderingTrader) {
            Wandering trader = new Wandering();
            trader.newTrades((org.bukkit.entity.WanderingTrader) event.getEntity());
        }

        if (event.getEntity() instanceof Spider spider && event.getEntity().getEntitySpawnReason().equals(CreatureSpawnEvent.SpawnReason.NATURAL)) {

            spider.getAttribute(Attribute.SCALE).setBaseValue(0.3);
            spider.getAttribute(Attribute.ATTACK_DAMAGE).setBaseValue(2);
            spider.getAttribute(Attribute.MAX_HEALTH).setBaseValue(8);

            int RNG = ThreadLocalRandom.current().nextInt(1,3);

            for (int i = 0; i < RNG; i++) {

                spider.getWorld().spawn(spider.getLocation(), Spider.class, spawnedSpider -> {
                    spawnedSpider.getAttribute(Attribute.SCALE).setBaseValue(0.5);
                    spawnedSpider.getAttribute(Attribute.ATTACK_DAMAGE).setBaseValue(2);
                    spawnedSpider.getAttribute(Attribute.MAX_HEALTH).setBaseValue(8);
                });


            }

        }

        Entity entity = event.getEntity();

        if (entity instanceof Zombie || entity instanceof Skeleton || entity instanceof Stray || entity instanceof Cow || entity instanceof Pig || entity instanceof Sheep || entity instanceof Chicken || entity instanceof Villager || entity instanceof Parrot || entity instanceof Bat) {


            LivingEntity e = (LivingEntity) entity;

            double rng = ThreadLocalRandom.current().nextDouble(0.85, 1.15);

            e.getAttribute(Attribute.SCALE).setBaseValue(rng);

            if (e.getType().equals(EntityType.CHICKEN) && ThreadLocalRandom.current().nextDouble(0, 50) == 50) {

                e.getAttribute(Attribute.SCALE).setBaseValue(2.5);
            }

        }

        if (entity instanceof Zombie zombie && entity.getEntitySpawnReason().equals(CreatureSpawnEvent.SpawnReason.NATURAL)) {

            int RNG = ThreadLocalRandom.current().nextInt(0, 1000);

            if (zombie.getWorld().isThundering() && RNG <= 200) {
                zombie.getWorld().playSound(zombie.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1F, 0.5F);
                zombie.getWorld().spawnEntity(zombie.getLocation(), EntityType.ZOMBIFIED_PIGLIN);
                zombie.remove();
            }
            else if (zombie.getWorld().isThundering() && RNG <= 20) {
                zombie.getWorld().playSound(zombie.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1F, 0.5F);
                zombie.getWorld().spawnEntity(zombie.getLocation(), EntityType.ZOMBIFIED_PIGLIN);
                zombie.remove();
            }
            else if (RNG <= 2) {
                zombie.getWorld().playSound(zombie.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1F, 0.5F);
                zombie.getWorld().spawnEntity(zombie.getLocation(), EntityType.ZOMBIFIED_PIGLIN);
                zombie.remove();
            }

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
    public void onEndermanTeleport(EntityTeleportEvent event) {

        if (event.getEntity() instanceof Enderman enderman) {

            int RNG = ThreadLocalRandom.current().nextInt(0, 5);

            if (RNG == 0) {
                enderman.getWorld().spawn(event.getFrom(), Endermite.class);
            }

        }


    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {

        if (event.getEntity() instanceof Player) return;

        LivingEntity entity = event.getEntity();

        tickList.remove(entity);

        if (event.getEntity() instanceof Bat) {
            event.getDrops().add(new ItemStack(Material.IRON_NUGGET, 1));
        }

        // Change drop system in future
        if (event.getEntity().getType() == EntityType.ENDERMAN) {
            int emeraldCount = ThreadLocalRandom.current().nextInt(1, 3);
            event.getDrops().add(new ItemStack(Material.EMERALD, emeraldCount));
        }
        if (event.getEntity().getType() == EntityType.ENDERMITE) {
            int emeraldCount = ThreadLocalRandom.current().nextInt(2, 4);
            event.getDrops().add(new ItemStack(Material.SMALL_AMETHYST_BUD, emeraldCount));
            event.getDrops().add(new ItemStack(Material.AMETHYST_SHARD, Math.round(emeraldCount/2F)));
        }
        if (event.getEntity().getType() == EntityType.SILVERFISH) {
            Silverfish sf = (Silverfish) event.getEntity();
            CreatureSpawnEvent.SpawnReason reason = sf.getEntitySpawnReason();
            if (reason == CreatureSpawnEvent.SpawnReason.POTION_EFFECT) {
                if (ThreadLocalRandom.current().nextInt(100) <6) {
                    int nuggetCount = ThreadLocalRandom.current().nextInt(1, 2);
                    event.getDrops().add(new ItemStack(Material.IRON_NUGGET, nuggetCount));
                }
            } else {
                int nuggetCount = ThreadLocalRandom.current().nextInt(3, 6);
                event.getDrops().add(new ItemStack(Material.IRON_NUGGET, nuggetCount));
            }
        }
        if (event.getEntity().getType() == EntityType.BAT) {
            int emeraldCount = ThreadLocalRandom.current().nextInt(0, 2);
            event.getDrops().add(new ItemStack(Material.IRON_NUGGET, emeraldCount));
        }
        if (event.getEntity().getType() == EntityType.DROWNED) {
            int emeraldCount = ThreadLocalRandom.current().nextInt(4, 6);
            event.getDrops().add(new ItemStack(Material.RAW_COPPER, emeraldCount));
        }
        if (event.getEntity().getType() == EntityType.IRON_GOLEM && !event.getEntity().getEntitySpawnReason().equals(CreatureSpawnEvent.SpawnReason.BUILD_IRONGOLEM)) {
            event.getDrops().removeIf(drop -> drop != null && drop.getType() == Material.IRON_INGOT);
            int nuggetCount = ThreadLocalRandom.current().nextInt(2, 5);
            event.getDrops().add(new ItemStack(Material.IRON_NUGGET, nuggetCount));
        }
        if (event.getEntity().getType() == EntityType.VINDICATOR) {
            event.getDrops().removeIf(drop -> drop != null && drop.getType() == Material.EMERALD);
            if (ThreadLocalRandom.current().nextInt(100) < 10) {
                int nuggetCount = ThreadLocalRandom.current().nextInt(1, 2);
                event.getDrops().add(new ItemStack(Material.EMERALD, nuggetCount));
            }
        }

        for (ItemStack itemStack : event.getDrops()) {

            if (itemStack.getType().toString().contains("HELMET")){

                ItemMeta meta = itemStack.getItemMeta();

                String material = itemStack.getType().toString();

                material = material.replace("HELMET", "BOOTS");

                ItemStack newItem = new ItemStack(Material.valueOf(material));

                newItem.setItemMeta(meta);

                event.getDrops().remove(itemStack);
                event.getDrops().add(newItem);

            }
        }

        if (event.getEntity() instanceof Monster monster && PumpkinPatch.isHalloweenUndead(monster)) {

            event.getDrops().clear();
            event.getDrops().addAll(PumpkinPatch.ScaryMob.getDrops());

        }

    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e){
        SnowballKnockback sbk = new SnowballKnockback(plugin);
        sbk.onSnowballHit(e);
    }

    @EventHandler
    public void onChestOpen(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Block block = event.getClickedBlock();
        if (block == null) return;

        if (block.getType() == Material.CHEST || block.getType() == Material.TRAPPED_CHEST) {
            Location chestLocation = block.getLocation();

            if (plugin.getPlugin().getTreasureChests() != null) {
                TreasureChests treasureChests = plugin.getPlugin().getTreasureChests();

                if (treasureChests.getDisplayAt(chestLocation) != null) {
                    treasureChests.chestOpened(chestLocation, event.getPlayer());
                }
            }
        }
    }

    // --------------------END Entity Events-----------------------


}