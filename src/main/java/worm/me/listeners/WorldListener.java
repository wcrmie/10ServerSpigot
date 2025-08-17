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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import worm.me.Server;
import worm.me.gamesystems.SnowballKnockback;
import worm.me.gamesystems.mobs.traders.Trader;
import worm.me.gamesystems.mobs.traders.VillagerProfession;
import worm.me.gamesystems.mobs.traders.Wandering;
import worm.me.gamesystems.worldreset.WorldReset;
import worm.me.playersystems.data.VoidPlayer;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class WorldListener implements Listener {

    private static final Logger log = LoggerFactory.getLogger(WorldListener.class);
    private final Server plugin;

    private static List<LivingEntity> tickList = new ArrayList<>();

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

        if(!event.isCancelled()){
            plugin.getWorldBorderHandler().incrementBlockCount(event);
        }

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
            event.getBlock().getLocation().getWorld().dropItem(event.getBlock().getLocation(), new ItemStack(event.getBlock().getType()));

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

    // --------------------IRON GOLEM BOSS-----------------------

    @EventHandler
    public void onIronGolemSpawn(EntitySpawnEvent event) {

        if (event.getEntity() instanceof IronGolem golem && event.getEntity().getEntitySpawnReason().equals(CreatureSpawnEvent.SpawnReason.BUILD_IRONGOLEM)) {

            golem.setPlayerCreated(false);

            golem.getWorld().playSound(golem.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 1F, 0.1F);

            golem.getAttribute(Attribute.SCALE).setBaseValue(1.5);
            golem.getAttribute(Attribute.MAX_HEALTH).setBaseValue(500);
            golem.getAttribute(Attribute.ATTACK_DAMAGE).setBaseValue(10);

            golem.setHealth(500);

            createIronGolemBossBar(golem);

        }
    }

    public void createIronGolemBossBar(IronGolem golem) {

        BossBar bossBar = Bukkit.createBossBar(ChatColor.RED + "Iron Golem - 100% Health", BarColor.RED, BarStyle.SEGMENTED_12);
        bossBar.setProgress(1);
        ironGolemBossBars.put(golem, bossBar);

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getWorld().equals(golem.getWorld())) {
                bossBar.addPlayer(p);
            }
        }
    }

    public void manageIronGolemBossBar(IronGolem golem) {

        BossBar bossBar = ironGolemBossBars.get(golem);
        if (bossBar != null) {
            double healthPercentage = golem.getHealth() / golem.getAttribute(Attribute.MAX_HEALTH).getValue();
            bossBar.setProgress(healthPercentage);
            bossBar.setTitle(ChatColor.RED + "Iron Golem - " + (int)(healthPercentage * 100) + "% Health");
        }
    }

    public void removeIronGolemBossBar(IronGolem golem) {

        BossBar bossBar = ironGolemBossBars.get(golem);
        if (bossBar != null) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                bossBar.removePlayer(p);
            }
            bossBar.removeAll();
        }

        ironGolemBossBars.remove(golem);

    }

    public List<ItemStack> generateIronGolemDrops() {

        List<ItemStack> drops = new ArrayList<>();

        int RNG = ThreadLocalRandom.current().nextInt(0, 100);

        if (RNG >= 50) {

            drops.add(generateKnowledgeTome());

        }

        drops.add(new ItemStack(Material.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE));


        return drops;
    }

    public ItemStack generateKnowledgeTome() {

        VillagerProfession profession = determineProfession();

        int[] genetics = new Trader.GeneticsBuilder().addGenetic(profession).build();

        ItemStack tome = new ItemStack(Material.KNOWLEDGE_BOOK);

        ItemMeta meta = tome.getItemMeta();

        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        pdc.set(new NamespacedKey(Server.getPlugin(), "genetics"), PersistentDataType.INTEGER_ARRAY, genetics);

        meta.setDisplayName(ChatColor.GOLD + "Villager Knowledge Tome");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Right click this on a villager to train them how to use a workstation.");
        lore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "PROFESSION: " + ChatColor.RESET + profession.getName());
        meta.setLore(lore);
        tome.setItemMeta(meta);

        return tome;

    }

    public VillagerProfession determineProfession() {

        int RNG = ThreadLocalRandom.current().nextInt(0, VillagerProfession.values().length - 1);
        return VillagerProfession.values()[RNG];

    }

    @EventHandler
    public void onIronGolemDamage(EntityDamageEvent event) {

        if (event.getEntity() instanceof IronGolem golem && event.getEntity().getEntitySpawnReason().equals(CreatureSpawnEvent.SpawnReason.BUILD_IRONGOLEM)) {

            if (event.getCause().equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) || event.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE) || event.getCause().equals(EntityDamageEvent.DamageCause.SUFFOCATION) || event.getCause().equals(EntityDamageEvent.DamageCause.FIRE) || event.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK) || event.getCause().equals(EntityDamageEvent.DamageCause.CAMPFIRE) || event.getCause().equals(EntityDamageEvent.DamageCause.LAVA)) {
                event.setCancelled(true);
                event.getEntity().getWorld().playSound(event.getEntity().getLocation(), Sound.BLOCK_ANVIL_PLACE, 1F, 2F);
                return; // Ignore explosion damage
            }

            manageIronGolemBossBar(golem);

            if (event.getDamageSource().getCausingEntity() instanceof Player p && p.getLocation().distance(golem.getLocation()) >= 2) {

                ProtectedRegion region = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(p.getWorld())).getRegion("spawn");
                Location loc = p.getLocation();

                // If the player is in spawn, don't damage them
                if (region.contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())) {
                    event.setCancelled(true);
                    return;
                }

                int RNG = ThreadLocalRandom.current().nextInt(1, 10);

                if (RNG == 1) {

                    golem.getWorld().spawn(golem.getLocation().add(0, 1, 0), Creeper.class, spawnedCreeper -> {
                        spawnedCreeper.setPowered(true);
                        spawnedCreeper.setTarget(p);
                        spawnedCreeper.setIgnited(true);

                        Vector dir = p.getLocation().toVector().subtract(golem.getLocation().toVector().multiply(0.5));

                        spawnedCreeper.setVelocity(dir.normalize());

                    });

                }

            }

        }



    }

    @EventHandler
    public void onIronGolemDeath(EntityDeathEvent event) {

        if (event.getEntity() instanceof IronGolem golem && event.getEntity().getEntitySpawnReason().equals(CreatureSpawnEvent.SpawnReason.BUILD_IRONGOLEM)) {

            removeIronGolemBossBar(golem);

            event.getDrops().clear();

            // Generate drops

            List<ItemStack> drops = generateIronGolemDrops();

            for (ItemStack drop : drops) {
                event.getDrops().add(drop);
            }

            // Remove from tick list
            tickList.remove(golem);
        }
    }

    public void setGolemFalling(IronGolem golem, boolean bool) {

        PersistentDataContainer pdc = golem.getPersistentDataContainer();

        pdc.set(new NamespacedKey(Server.getPlugin(), "golem_falling"), PersistentDataType.BOOLEAN, bool);

    }

    public boolean isGolemFalling(IronGolem golem) {

        PersistentDataContainer pdc = golem.getPersistentDataContainer();

        return pdc.getOrDefault(new NamespacedKey(Server.getPlugin(), "golem_falling"), PersistentDataType.BOOLEAN, false);

    }

    @EventHandler
    public void onIronGolemMove(EntityMoveEvent event) {

        if (event.getEntity() instanceof IronGolem golem && event.getEntity().getEntitySpawnReason().equals(CreatureSpawnEvent.SpawnReason.BUILD_IRONGOLEM)) {

            // Prevent golem from falling in void and dying
            if (golem.getLocation().getBlockY() <= 200) {
                golem.teleport(golem.getLocation().add(0, 100, 0));
                setGolemFalling(golem, true);
            }

            if (isGolemFalling(golem) && golem.getLocation().subtract(0, 1, 0).getBlock().getType() != Material.AIR) {

                golem.getWorld().createExplosion(golem, 2F);
                golem.getWorld().playSound(golem.getLocation(), Sound.BLOCK_ANVIL_LAND, 1F, 0.5F);
                setGolemFalling(golem, false);

            }

        }
    }

    // --------------------END IRON GOLEM BOSS-----------------------



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
