package worm.me.listeners;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.event.entity.EntityMoveEvent;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import worm.me.Server;
import worm.me.gamesystems.items.ExperienceBottle;
import worm.me.gamesystems.items.SnowballKnockback;
import worm.me.gamesystems.mobs.traders.Trader;
import worm.me.gamesystems.mobs.traders.VillagerProfession;
import worm.me.gamesystems.mobs.traders.Wandering;
import worm.me.gamesystems.worldreset.WorldReset;
import worm.me.playersystems.data.VoidPlayer;

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

        if (!event.isCancelled()) plugin.getWorldBorderHandler().incrementBlockCount(event);
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

    // --------------------END Block Events--------------------


    // --------------------Entity Events-----------------------

    @EventHandler
    public void onExplosion(EntityExplodeEvent event) {
        Entity entity = event.getEntity();
        if (entity.getType().equals(EntityType.TNT) || entity.getType().equals(EntityType.TNT_MINECART)) {
            event.setCancelled(true);
            World world = entity.getWorld();
            world.createExplosion(entity.getLocation(), 6);
        }
    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent e) {
        if (e.getEntity().getVehicle() instanceof Player) e.setCancelled(true);
    }

    @EventHandler
    public void onVillagerBreed(EntityBreedEvent event){

        if (event.getEntity() instanceof Villager child){

            Villager mother = (Villager) event.getMother();
            Villager father = (Villager) event.getFather();

            int[] motherGenetics = Trader.getGenetics(mother);
            int[] fatherGenetics = Trader.getGenetics(father);

            Trader.addGenetics(child, motherGenetics);
            Trader.addGenetics(child, fatherGenetics);

        }
    }

    @EventHandler
    public void onVillagerEmploy(VillagerCareerChangeEvent event) {

        Villager villager = event.getEntity();

        if (!villager.getProfession().equals(Villager.Profession.NONE)) event.setCancelled(true);

        if (Trader.getGenetics(villager)[VillagerProfession.valueOf(event.getProfession().toString()).ordinal()] != 1) event.setCancelled(true);

    }

    @EventHandler
    public void onVillagerLevel(VillagerAcquireTradeEvent event) {

        Villager villager = (Villager) event.getEntity();

        Villager.Profession profession = villager.getProfession();

        VillagerProfession villagerProfession = VillagerProfession.valueOf(profession.toString());

        int level = villager.getVillagerLevel();




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
            } else if (item.getItemStack().getType().equals(Material.GLASS_BOTTLE)) {
                item.setItemStack(new worm.me.utils.Item.ItemBuilder(ExperienceBottle.emptyBottle).setAmount(item.getItemStack().getAmount()).build());
            }
        }

        if (event.getEntity() instanceof LivingEntity entity) tickList.add(entity);

        if (event.getEntity() instanceof org.bukkit.entity.WanderingTrader) {
            Wandering trader = new Wandering();
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
                    spawnedSpider.getAttribute(Attribute.SCALE).setBaseValue(0.5);
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

        if (entity instanceof Zombie zombie && entity.getEntitySpawnReason().equals(CreatureSpawnEvent.SpawnReason.NATURAL)) {

            int RNG = ThreadLocalRandom.current().nextInt(0, 10);

            if (zombie.getWorld().isThundering() && RNG == 0) {
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
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e){
        SnowballKnockback sbk = new SnowballKnockback(plugin);
        sbk.onSnowballHit(e);
    }

    // --------------------END Entity Events-----------------------


}
