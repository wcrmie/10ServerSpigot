package worm.me.gamesystems.world.mob.boss;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import worm.me.Server;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ZiglinJouster extends Boss {

    private static final double STAGE_1_MAX_HEALTH                 = 300.0;
    private static final double STAGE_2_MAX_HEALTH                 = 400.0;
    private static final double BASE_ATTACK_DAMAGE                 = 15.0;
    private static final double ARENA_RADIUS                       = 64.0;
    private static final double ARENA_RADIUS_SQ                    = ARENA_RADIUS * ARENA_RADIUS;
    private static final double RETREAT_SPEED                      = 0.4;
    private static final double STAGE_1_REPOSITION_DIST_SQ        = 18.0 * 18.0;
    private static final double STAGE_2_REPOSITION_DIST_SQ        = 8.0  * 8.0;
    private static final double STAGE_1_REPOSITION_SPEED          = 0.35;
    private static final double STAGE_2_REPOSITION_SPEED          = 0.6;
    private static final int    STAGE_1_WINDUP_THRESHOLD           = 80;
    private static final int    STAGE_2_WINDUP_THRESHOLD           = 40;
    private static final int    STAGE_1_WINDUP_DURATION            = 30;
    private static final int    STAGE_2_WINDUP_DURATION            = 15;
    private static final double STAGE_1_CHARGE_SPEED               = 1.0;
    private static final double STAGE_2_CHARGE_SPEED               = 1.4;
    private static final int    CHARGE_MAX_TICKS                   = 45;
    private static final double KNOCKBACK_MULTIPLIER               = 1.2;
    private static final int    STAGE_1_COOLDOWN_DURATION          = 60;
    private static final int    STAGE_2_COOLDOWN_DURATION          = 40;
    private static final int    STUCK_THRESHOLD_TICKS              = 80;
    private static final int    EASY_PATH_AFTER_TICKS              = 5  * 20;
    private static final int    IGNORE_PATH_AFTER_TICKS            = 20 * 20;
    private static final double NORMAL_REGEN_PER_TICK              = (2.0  / 60.0) / 20.0;
    private static final double FAST_REGEN_PER_TICK                = (10.0 / 60.0) / 20.0;
    private static final double DAMAGE_DEALT_MULTIPLIER_PER_PLAYER = 1.5;
    private static final double STUCK_MOVE_THRESHOLD_SQ            = 0.04;

    private static final double SPEAR_OFFSET_X      =  0.4;
    private static final double SPEAR_OFFSET_Y      =  0.8;
    private static final double SPEAR_OFFSET_Z      =  0.3;
    private static final float  SPEAR_YAW_OFFSET    =  0.0f;

    public  static final Set<Block>      temporaryBlocks  = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private final        Map<Block, Long> blockExpirations = new ConcurrentHashMap<>();

    private static final Set<Material> UNBREAKABLE_BLOCKS = Set.of(
            Material.WHITE_BED,
            Material.LODESTONE
    );

    private static final NamespacedKey BOSS_TYPE_KEY =
            new NamespacedKey(JavaPlugin.getProvidingPlugin(ZiglinJouster.class), "ziglin_boss");
    private static final NamespacedKey STAGE_KEY =
            new NamespacedKey(JavaPlugin.getProvidingPlugin(ZiglinJouster.class), "ziglin_stage");
    private static final NamespacedKey SPAWN_X_KEY =
            new NamespacedKey(JavaPlugin.getProvidingPlugin(ZiglinJouster.class), "ziglin_spawn_x");
    private static final NamespacedKey SPAWN_Y_KEY =
            new NamespacedKey(JavaPlugin.getProvidingPlugin(ZiglinJouster.class), "ziglin_spawn_y");
    private static final NamespacedKey SPAWN_Z_KEY =
            new NamespacedKey(JavaPlugin.getProvidingPlugin(ZiglinJouster.class), "ziglin_spawn_z");
    private static final NamespacedKey HEALTH_KEY =
            new NamespacedKey(JavaPlugin.getProvidingPlugin(ZiglinJouster.class), "ziglin_health");
    private static final NamespacedKey ZOGLIN_UUID_KEY =
            new NamespacedKey(JavaPlugin.getProvidingPlugin(ZiglinJouster.class), "ziglin_zoglin_uuid");
    private static final NamespacedKey SPEAR_DISPLAY_KEY =
            new NamespacedKey(JavaPlugin.getProvidingPlugin(ZiglinJouster.class), "ziglin_spear_display");

    private enum State { REPOSITIONING, WINDUP, CHARGING, COOLDOWN, RETREATING }

    private State   state      = State.REPOSITIONING;
    private int     stateTicks = 0;
    private Zoglin  zoglin;
    private int     stage = 1;
    private boolean isCircling   = false;
    private boolean isStuck      = false;
    private boolean isRetreating = false;
    private int     pathBlockedTicks     = 0;
    private boolean usingStrictPathCheck = true;
    private Location lastPosition        = null;
    private int     stuckTicks           = 0;

    private BukkitRunnable logicLoop;
    private BukkitRunnable regenTask;
    private ItemDisplay spearDisplay;

    public static ZiglinJouster spawnAt(Location location) {
        PigZombie rider = location.getWorld().spawn(location, PigZombie.class);
        ZiglinJouster boss = new ZiglinJouster(rider, location, 1);
        boss.spawn(location);
        boss.enable();
        return boss;
    }

    private ZiglinJouster(Entity rider, Location spawnLocation, int stage) {
        super(rider, STAGE_2_MAX_HEALTH, BASE_ATTACK_DAMAGE, 1.0F, spawnLocation);
        this.stage = stage;
        regenTask = new BukkitRunnable() {
            @Override
            public void run() {
                LivingEntity primary = getPrimaryEntity();
                if (primary == null || primary.isDead()) return;
                double rate = (isInvincibleState(primary) || isRetreating) ? FAST_REGEN_PER_TICK : NORMAL_REGEN_PER_TICK;
                double cap  = primary.getAttribute(Attribute.MAX_HEALTH).getValue();
                primary.setHealth(Math.min(primary.getHealth() + rate, cap));
            }
        };
    }

    @Override
    public void enable() {
        bossMap.put(m_boss, this);
        if (zoglin != null) bossMap.put(zoglin, this);
        getBossEntities().add(m_boss);
        getBossBars().add(bossBar);
        regenTask.runTaskTimer(Server.getPlugin(), 1L, 1L);
        setupVisuals();
        startLogicLoop();
    }

    @Override
    public void disable() {
        bossMap.remove(m_boss);
        if (zoglin != null) bossMap.remove(zoglin);
        getBossEntities().remove(m_boss);
        if (logicLoop != null && !logicLoop.isCancelled()) logicLoop.cancel();
        if (regenTask  != null && !regenTask.isCancelled())  regenTask.cancel();
        removeSpearDisplay();
        cleanupBlocks();
        super.destroyBossBar();
    }

    private void disableForUnload() {
        saveToPDC();
        bossMap.remove(m_boss);
        if (zoglin != null) bossMap.remove(zoglin);
        getBossEntities().remove(m_boss);
        if (logicLoop != null && !logicLoop.isCancelled()) logicLoop.cancel();
        if (regenTask  != null && !regenTask.isCancelled())  regenTask.cancel();
        spearDisplay = null;
        cleanupBlocks();
        super.destroyBossBar();
    }

    private void saveToPDC() {
        PersistentDataContainer pdc = ((PigZombie) m_boss).getPersistentDataContainer();
        pdc.set(STAGE_KEY,   PersistentDataType.INTEGER, stage);
        pdc.set(SPAWN_X_KEY, PersistentDataType.DOUBLE,  spawnLocation.getX());
        pdc.set(SPAWN_Y_KEY, PersistentDataType.DOUBLE,  spawnLocation.getY());
        pdc.set(SPAWN_Z_KEY, PersistentDataType.DOUBLE,  spawnLocation.getZ());
        LivingEntity primary = getPrimaryEntity();
        if (primary != null && !primary.isDead()) {
            pdc.set(HEALTH_KEY, PersistentDataType.DOUBLE, primary.getHealth());
        }
        if (zoglin != null) {
            pdc.set(ZOGLIN_UUID_KEY, PersistentDataType.STRING, zoglin.getUniqueId().toString());
        }
    }

    @Override
    public void spawn(Location location) {
        zoglin = location.getWorld().spawn(location, Zoglin.class, z -> {
            z.getAttribute(Attribute.MAX_HEALTH).setBaseValue(STAGE_1_MAX_HEALTH);
            z.setHealth(STAGE_1_MAX_HEALTH);
            AttributeInstance speed = z.getAttribute(Attribute.MOVEMENT_SPEED);
            if (speed != null) speed.setBaseValue(0);
            z.setRemoveWhenFarAway(false);
            z.setPersistent(true);
            z.getPersistentDataContainer().set(BOSS_TYPE_KEY, PersistentDataType.STRING, "ziglin");
        });

        PigZombie rider = (PigZombie) m_boss;
        rider.getAttribute(Attribute.MAX_HEALTH).setBaseValue(maxHealth);
        rider.setHealth(maxHealth);
        AttributeInstance riderSpeed = rider.getAttribute(Attribute.MOVEMENT_SPEED);
        if (riderSpeed != null) riderSpeed.setBaseValue(0);
        rider.setRemoveWhenFarAway(false);
        rider.setPersistent(true);

        PersistentDataContainer pdc = rider.getPersistentDataContainer();
        pdc.set(BOSS_TYPE_KEY, PersistentDataType.STRING, "ziglin");
        pdc.set(STAGE_KEY,     PersistentDataType.INTEGER, stage);
        pdc.set(SPAWN_X_KEY,   PersistentDataType.DOUBLE,  spawnLocation.getX());
        pdc.set(SPAWN_Y_KEY,   PersistentDataType.DOUBLE,  spawnLocation.getY());
        pdc.set(SPAWN_Z_KEY,   PersistentDataType.DOUBLE,  spawnLocation.getZ());

        rider.getEquipment().setItemInMainHand(null);
        rider.getEquipment().setItemInMainHandDropChance(0f);

        zoglin.addPassenger(rider);
    }

    @Override
    public void handleTakeDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (!entity.equals(m_boss) && !entity.equals(zoglin)) return;

        if (isInvincibleState(getPrimaryEntity())) {
            event.setCancelled(true);
            return;
        }

        int playerCount = Math.max(1, getPlayersInArena().size());

        if (stage == 1) {
            if (zoglin == null || zoglin.isDead() || !zoglin.isValid()) {
                transitionToStage2();
                return;
            }
            if (entity.equals(m_boss)) {
                event.setCancelled(true);
                zoglin.damage(event.getFinalDamage() / playerCount);
            }
        } else if (stage == 2 && entity.equals(m_boss)) {
            event.setDamage(event.getDamage() / playerCount);
        }
    }

    @Override
    public void onOtherEntityDeath(EntityDeathEvent event) {
        if (zoglin == null || !event.getEntity().equals(zoglin)) return;
        if (stage == 1) transitionToStage2();
    }

    @Override
    public void handleDeath(EntityDeathEvent event) {
        if (!event.getEntity().equals(m_boss)) return;
        event.getDrops().clear();
        removeSpearDisplay();
        disable();
    }

    @Override
    public void onVehicleExit(VehicleExitEvent event) {
        if (stage != 1 || zoglin == null || zoglin.isDead()) return;
        if (!event.getExited().equals(m_boss) && !event.getVehicle().equals(zoglin)) return;
        Bukkit.getScheduler().runTaskLater(Server.getPlugin(), () -> {
            if (zoglin != null && !zoglin.isDead() && m_boss.isValid() && !m_boss.isDead()) {
                zoglin.addPassenger(m_boss);
            }
        }, 1L);
    }

    @Override
    public void onChunkUnload(ChunkUnloadEvent event) {
        LivingEntity primary = getPrimaryEntity();
        if (primary == null) return;
        Location loc = primary.getLocation();
        if (!event.getChunk().getWorld().equals(loc.getWorld())) return;
        if (event.getChunk().getX() != loc.getBlockX() >> 4) return;
        if (event.getChunk().getZ() != loc.getBlockZ() >> 4) return;
        disableForUnload();
    }

    @Override
    public BossBar initializeBossBar() {
        BossBar bar = BossBar.bossBar(
                MiniMessage.miniMessage().deserialize("<gold>Ziglin Jouster - 100% Health"),
                1.0f,
                BossBar.Color.YELLOW,
                BossBar.Overlay.NOTCHED_10
        );
        Bukkit.getOnlinePlayers().forEach(bar::addViewer);
        return bar;
    }

    @Override
    public void updateBossBar() {
        double current, max;
        if (stage == 1 && zoglin != null && zoglin.isValid()) {
            current = zoglin.getHealth();
            max     = STAGE_1_MAX_HEALTH;
        } else if (m_boss instanceof LivingEntity living) {
            current = living.getHealth();
            max     = STAGE_2_MAX_HEALTH;
        } else {
            current = max = STAGE_2_MAX_HEALTH;
        }
        float pct = (float) Math.max(0.0, Math.min(1.0, current / max));
        bossBar.progress(pct);
        bossBar.name(MiniMessage.miniMessage().deserialize(
                String.format("<gold>Ziglin Jouster - %.0f%% Health", pct * 100)
        ));
    }

    @Override
    public List<ItemStack> generateDrops() { return new ArrayList<>(); }

    @Override
    public void handleDealDamage(EntityDamageEvent event) {}

    private void setupVisuals() {
        removeSpearDisplay();
        purgeOrphanedDisplays();
        if (m_boss instanceof PigZombie rider) {
            rider.getEquipment().setItemInMainHand(null);
            rider.getEquipment().setItemInMainHandDropChance(0f);
        }
        Location startLoc = m_boss.getLocation();
        spearDisplay = startLoc.getWorld().spawn(startLoc, ItemDisplay.class, d -> {
            d.setItemStack(new ItemStack(Material.GOLDEN_SPEAR));
            d.setTeleportDuration(1);
            d.setInterpolationDelay(0);
            d.setBillboard(Display.Billboard.FIXED);
            d.setTransformation(buildSpearTransformation());
            d.setPersistent(false);
            d.getPersistentDataContainer().set(SPEAR_DISPLAY_KEY, PersistentDataType.STRING,
                    m_boss.getUniqueId().toString());
        });
    }

    private void purgeOrphanedDisplays() {
        String riderUUID = m_boss.getUniqueId().toString();
        m_boss.getWorld()
                .getNearbyEntities(m_boss.getLocation(), ARENA_RADIUS, 32, ARENA_RADIUS)
                .forEach(e -> {
                    if (!(e instanceof ItemDisplay d)) return;
                    PersistentDataContainer pdc = d.getPersistentDataContainer();
                    if (!pdc.has(SPEAR_DISPLAY_KEY, PersistentDataType.STRING)) return;
                    if (riderUUID.equals(pdc.get(SPEAR_DISPLAY_KEY, PersistentDataType.STRING))) {
                        d.remove();
                    }
                });
    }

    private Transformation buildSpearTransformation() {
        Quaternionf leftRotation = new Quaternionf()
                .rotateX((float) Math.toRadians(90f))
                .rotateZ((float) Math.toRadians(40f));
        return new Transformation(
                new Vector3f(0f, 0.44f, 0f),
                leftRotation,
                new Vector3f(3f, 3f, 3f),
                new Quaternionf()
        );
    }

    private void updateSpearDisplay() {
        if (spearDisplay == null || !spearDisplay.isValid()) {
            setupVisuals();
            return;
        }
        Location riderLoc = m_boss.getLocation();
        float    yaw      = riderLoc.getYaw();
        double   yawRad   = Math.toRadians(yaw);
        double   sinYaw   = Math.sin(yawRad);
        double   cosYaw   = Math.cos(yawRad);
        double worldX = riderLoc.getX() + SPEAR_OFFSET_X * cosYaw - SPEAR_OFFSET_Z * sinYaw;
        double worldY = riderLoc.getY() + SPEAR_OFFSET_Y;
        double worldZ = riderLoc.getZ() + SPEAR_OFFSET_X * sinYaw + SPEAR_OFFSET_Z * cosYaw;
        spearDisplay.teleport(new Location(
                riderLoc.getWorld(),
                worldX, worldY, worldZ,
                yaw + SPEAR_YAW_OFFSET,
                0f
        ));
    }

    private void removeSpearDisplay() {
        if (spearDisplay != null) {
            if (!spearDisplay.isDead()) spearDisplay.remove();
            spearDisplay = null;
        }
    }

    private void startLogicLoop() {
        logicLoop = new BukkitRunnable() {
            @Override
            public void run() {
                if (m_boss.isDead() || !m_boss.isValid()) {
                    cancel();
                    return;
                }
                LivingEntity primary = getPrimaryEntity();
                if (primary == null || !primary.isValid() || primary.isDead()) {
                    if (stage == 1) transitionToStage2();
                    updateBossBar();
                    return;
                }
                enforceArenaBoundary(primary);
                boolean playersInside = !getPlayersInArena().isEmpty();
                if (!playersInside) {
                    if (!isRetreating) {
                        isRetreating = true;
                        isCircling   = false;
                        setState(State.RETREATING);
                    }
                } else if (isRetreating) {
                    isRetreating = false;
                    resetCombatState();
                }
                Player target = playersInside ? getTarget(primary) : null;
                if (!isRetreating && target != null
                        && (state == State.REPOSITIONING || state == State.CHARGING)) {
                    handleBridge(primary);
                }
                processBlockCleanup();
                updateStuckStatus(primary);
                boolean pathClear = true;
                if (!isRetreating && target != null) {
                    pathClear = checkPathClear(primary, target);
                    if (!pathClear) {
                        pathBlockedTicks++;
                        if (pathBlockedTicks > EASY_PATH_AFTER_TICKS) usingStrictPathCheck = false;
                    } else {
                        pathBlockedTicks     = 0;
                        usingStrictPathCheck = true;
                    }
                }
                if (state == State.RETREATING) {
                    doRetreat(primary);
                } else if (target != null) {
                    switch (state) {
                        case REPOSITIONING -> doReposition(primary, target, pathClear);
                        case WINDUP        -> doWindup(primary, target);
                        case CHARGING      -> doCharge(primary, target);
                        case COOLDOWN      -> doCooldown(primary, target);
                        default            -> {}
                    }
                } else {
                    primary.setVelocity(new Vector(0, -0.1, 0));
                }
                if (target != null) lookAt(m_boss, target.getLocation());
                updateSpearDisplay();
                updateBossBar();
                stateTicks++;
            }
        };
        logicLoop.runTaskTimer(Server.getPlugin(), 1L, 1L);
    }

    private void setState(State newState) {
        state      = newState;
        stateTicks = 0;
        if (newState != State.REPOSITIONING) isCircling = false;
    }

    private void resetCombatState() {
        state                = State.REPOSITIONING;
        stateTicks           = 0;
        isCircling           = false;
        isStuck              = false;
        stuckTicks           = 0;
        lastPosition         = null;
        pathBlockedTicks     = 0;
        usingStrictPathCheck = true;
    }

    private void enforceArenaBoundary(LivingEntity entity) {
        Location loc = entity.getLocation();
        double dx = loc.getX() - spawnLocation.getX();
        double dz = loc.getZ() - spawnLocation.getZ();
        if (dx * dx + dz * dz <= ARENA_RADIUS_SQ) return;
        double angle = Math.atan2(dz, dx);
        entity.teleport(new Location(loc.getWorld(),
                spawnLocation.getX() + ARENA_RADIUS * Math.cos(angle),
                loc.getY(),
                spawnLocation.getZ() + ARENA_RADIUS * Math.sin(angle),
                loc.getYaw(), loc.getPitch()));
        entity.setVelocity(new Vector(0, 0, 0));
    }

    private void doRetreat(LivingEntity primary) {
        if (isStuck) {
            breakBlocksUnderAndAround(primary);
            isStuck = false;
            stuckTicks = 0;
            lastPosition = null;
        }
        Vector toSpawn = spawnLocation.toVector().subtract(primary.getLocation().toVector());
        if (toSpawn.lengthSquared() < 1.0) {
            primary.setVelocity(new Vector(0, -0.1, 0));
        } else {
            Vector move = toSpawn.normalize().multiply(RETREAT_SPEED);
            move.setY(clampY(spawnLocation.getY() - primary.getLocation().getY()));
            primary.setVelocity(move);
        }
        lookAt(primary, spawnLocation);
    }

    private void doReposition(LivingEntity primary, Player target, boolean pathClear) {
        double distSq = primary.getLocation().distanceSquared(target.getLocation());
        if (isStuck) {
            breakBlocksUnderAndAround(primary);
            isStuck    = false;
            stuckTicks = 0;
        }
        if (!pathClear && pathBlockedTicks <= IGNORE_PATH_AFTER_TICKS) {
            isCircling = true;
            double currentDist  = Math.sqrt(distSq);
            double desiredDist  = 10.0;
            double radiusFactor = currentDist > desiredDist + 2.0 ? 0.8
                    : currentDist < desiredDist - 2.0 ? 1.2 : 1.0;
            Vector toPlayer = target.getLocation().toVector().subtract(primary.getLocation().toVector());
            Vector tangent  = new Vector(-toPlayer.getZ(), 0, toPlayer.getX()).normalize();
            Vector move     = tangent.multiply(radiusFactor * 0.5);
            move.setY(clampY(target.getLocation().getY() - primary.getLocation().getY()));
            primary.setVelocity(move);
            lookAt(primary, target.getLocation());
            return;
        }
        isCircling = false;
        double repoDist = stage == 1 ? STAGE_1_REPOSITION_DIST_SQ : STAGE_2_REPOSITION_DIST_SQ;
        double speed    = stage == 1 ? STAGE_1_REPOSITION_SPEED   : STAGE_2_REPOSITION_SPEED;
        Vector dir      = target.getLocation().toVector().subtract(primary.getLocation().toVector()).normalize();
        Vector move     = distSq < repoDist ? dir.multiply(-speed) : dir.multiply(speed * 0.5);
        move.setY(clampY(target.getLocation().getY() - primary.getLocation().getY()));
        primary.setVelocity(move);
        lookAt(primary, target.getLocation());
        int windupThreshold = stage == 1 ? STAGE_1_WINDUP_THRESHOLD : STAGE_2_WINDUP_THRESHOLD;
        if (stateTicks > windupThreshold && pathClear) setState(State.WINDUP);
    }

    private void doWindup(LivingEntity primary, Player target) {
        primary.setVelocity(new Vector(0, -0.1, 0));
        lookAt(primary, target.getLocation());
        int duration = stage == 1 ? STAGE_1_WINDUP_DURATION : STAGE_2_WINDUP_DURATION;
        if (stateTicks > duration) {
            setState(State.CHARGING);
        }
    }

    private void doCharge(LivingEntity primary, Player target) {
        double chargeSpeed = stage == 1 ? STAGE_1_CHARGE_SPEED : STAGE_2_CHARGE_SPEED;
        double yDiff       = target.getLocation().getY() - primary.getLocation().getY();
        double yBoost      = yDiff > 0.8 ? 0.2 : yDiff < -0.5 ? -0.1 : 0.0;
        Vector dir         = target.getLocation().toVector().subtract(primary.getLocation().toVector()).normalize();
        primary.setVelocity(dir.multiply(chargeSpeed).setY(yBoost));
        lookAt(primary, target.getLocation());
        breakBlocksInWay(primary);
        if (primary.getLocation().distance(target.getLocation()) < 2.0) {
            int    playerCount = Math.max(1, getPlayersInArena().size());
            double damage      = attackDamage * DAMAGE_DEALT_MULTIPLIER_PER_PLAYER * playerCount;
            target.damage(damage, primary);
            target.setVelocity(primary.getVelocity().multiply(KNOCKBACK_MULTIPLIER).setY(0.5));
            setState(State.COOLDOWN);
        } else if (stateTicks > CHARGE_MAX_TICKS) {
            setState(State.COOLDOWN);
        }
    }

    private void doCooldown(LivingEntity primary, Player target) {
        primary.setVelocity(new Vector(0, -0.1, 0));
        if (target != null) lookAt(primary, target.getLocation());
        int duration = stage == 1 ? STAGE_1_COOLDOWN_DURATION : STAGE_2_COOLDOWN_DURATION;
        if (stateTicks > duration) setState(State.REPOSITIONING);
    }

    private void handleBridge(LivingEntity primary) {
        Location loc    = primary.getLocation();
        long     expiry = System.currentTimeMillis() + 3000;
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                Block b = loc.clone().add(x, -1, z).getBlock();
                if (primary.getBoundingBox().contains(b.getX() + 0.5, b.getY() + 0.5, b.getZ() + 0.5)) continue;
                if (b.getType().isAir() || b.isLiquid()) {
                    b.setType(Material.NETHERRACK);
                    temporaryBlocks.add(b);
                    blockExpirations.put(b, expiry);
                }
            }
        }
    }

    private void transitionToStage2() {
        stage = 2;
        if (zoglin != null) {
            bossMap.remove(zoglin);
            zoglin.remove();
            zoglin = null;
        }
        setState(State.REPOSITIONING);
        updateBossBar();
    }

    private boolean checkPathClear(LivingEntity boss, Player target) {
        if (!usingStrictPathCheck && pathBlockedTicks > IGNORE_PATH_AFTER_TICKS) return true;
        return usingStrictPathCheck ? pathClearStrict(boss, target) : pathClearRayTrace(boss, target);
    }

    private boolean pathClearRayTrace(LivingEntity boss, Player target) {
        Location eye    = boss.getEyeLocation();
        Vector   dir    = target.getEyeLocation().toVector().subtract(eye.toVector());
        RayTraceResult result = boss.getWorld().rayTraceBlocks(eye, dir, dir.length());
        return result == null || result.getHitBlock() == null || temporaryBlocks.contains(result.getHitBlock());
    }

    private boolean pathClearStrict(LivingEntity boss, Player target) {
        Location start     = boss.getEyeLocation();
        Vector   direction = target.getEyeLocation().toVector().subtract(start.toVector());
        double   distance  = direction.length();
        direction.normalize();
        for (double d = 0.5; d <= distance; d += 0.5) {
            Vector point = start.toVector().add(direction.clone().multiply(d));
            Block  block = start.getWorld().getBlockAt(
                    (int) Math.floor(point.getX()),
                    (int) Math.floor(point.getY()),
                    (int) Math.floor(point.getZ()));
            if (!block.getType().isAir() && !temporaryBlocks.contains(block)) return false;
        }
        return true;
    }

    private void updateStuckStatus(LivingEntity primary) {
        if (state != State.REPOSITIONING && state != State.CHARGING) {
            lastPosition = primary.getLocation().clone();
            stuckTicks   = 0;
            isStuck      = false;
            return;
        }
        Location now = primary.getLocation().clone();
        if (lastPosition == null) { lastPosition = now; return; }
        if (now.distanceSquared(lastPosition) < STUCK_MOVE_THRESHOLD_SQ) {
            if (++stuckTicks > STUCK_THRESHOLD_TICKS) isStuck = true;
        } else {
            stuckTicks = 0;
            isStuck    = false;
        }
        lastPosition = now;
    }

    private boolean isInvincibleState(LivingEntity primary) {
        if (isRetreating) return true;
        if (isCircling && stateTicks < 200) return true;
        return false;
    }

    private void breakBlocksUnderAndAround(LivingEntity entity) {
        Block feet = entity.getLocation().getBlock();
        for (int x = -1; x <= 1; x++)
            for (int z = -1; z <= 1; z++)
                breakSafely(entity.getWorld().getBlockAt(feet.getX() + x, feet.getY(), feet.getZ() + z));
        breakSafely(feet.getRelative(0, 1, 0));
    }

    private void breakBlocksInWay(LivingEntity boss) {
        Vector vel = boss.getVelocity();
        if (vel.lengthSquared() < 0.001) return;
        Location center = boss.getLocation().add(vel.normalize().multiply(1.2));
        for (int x = -1; x <= 1; x++) for (int y = 0; y <= 2; y++) for (int z = -1; z <= 1; z++) {
            Block b = center.clone().add(x, y, z).getBlock();
            if (b.getType().isAir() || UNBREAKABLE_BLOCKS.contains(b.getType())) continue;
            if (b.getType() == Material.NETHERRACK) b.setType(Material.AIR); else b.breakNaturally();
            temporaryBlocks.remove(b);
            blockExpirations.remove(b);
        }
    }

    private void breakSafely(Block block) {
        if (block.getType().isAir() || UNBREAKABLE_BLOCKS.contains(block.getType())) return;
        if (block.getType() == Material.NETHERRACK) block.setType(Material.AIR); else block.breakNaturally();
        temporaryBlocks.remove(block);
        blockExpirations.remove(block);
    }

    private void processBlockCleanup() {
        long now = System.currentTimeMillis();
        blockExpirations.entrySet().removeIf(e -> {
            if (now > e.getValue()) {
                e.getKey().setType(Material.AIR);
                temporaryBlocks.remove(e.getKey());
                return true;
            }
            return false;
        });
    }

    private void cleanupBlocks() {
        temporaryBlocks.forEach(b -> b.setType(Material.AIR));
        temporaryBlocks.clear();
        blockExpirations.clear();
    }

    private List<Player> getPlayersInArena() {
        return spawnLocation.getWorld().getPlayers().stream()
                .filter(p -> p.getGameMode() == GameMode.SURVIVAL && !p.isDead())
                .filter(p -> {
                    double dx = p.getLocation().getX() - spawnLocation.getX();
                    double dz = p.getLocation().getZ() - spawnLocation.getZ();
                    return dx * dx + dz * dz <= ARENA_RADIUS_SQ;
                }).toList();
    }

    private Player getTarget(LivingEntity boss) {
        return getPlayersInArena().stream()
                .min(Comparator.comparingDouble(p -> p.getLocation().distanceSquared(boss.getLocation())))
                .orElse(null);
    }

    private void lookAt(Entity entity, Location target) {
        Vector dir   = target.toVector().subtract(entity.getLocation().toVector());
        float  yaw   = (float) Math.toDegrees(Math.atan2(-dir.getX(), dir.getZ()));
        float  pitch = (float) Math.toDegrees(Math.atan2(-dir.getY(),
                Math.sqrt(dir.getX() * dir.getX() + dir.getZ() * dir.getZ())));
        entity.setRotation(yaw, pitch);
    }

    private double clampY(double yDiff) {
        return Math.max(-0.5, Math.min(0.5, yDiff * 0.3));
    }

    private LivingEntity getPrimaryEntity() {
        return (LivingEntity) (stage == 1 && zoglin != null ? zoglin : m_boss);
    }

    public static void restoreBosses() {
        for (World world : Bukkit.getWorlds()) {
            for (Chunk chunk : world.getLoadedChunks()) {
                restoreBossesInChunk(chunk);
            }
        }
    }

    public static void restoreBossesInChunk(Chunk chunk) {
        for (Entity entity : chunk.getEntities()) {
            restoreIfBoss(entity);
        }
    }

    private static void restoreIfBoss(Entity entity) {
        if (!(entity instanceof PigZombie rider)) return;
        PersistentDataContainer pdc = rider.getPersistentDataContainer();
        if (!pdc.has(BOSS_TYPE_KEY, PersistentDataType.STRING)) return;
        if (Boss.isBoss(rider)) return;

        int    stage = pdc.getOrDefault(STAGE_KEY, PersistentDataType.INTEGER, 1);
        Double sx    = pdc.get(SPAWN_X_KEY, PersistentDataType.DOUBLE);
        Double sy    = pdc.get(SPAWN_Y_KEY, PersistentDataType.DOUBLE);
        Double sz    = pdc.get(SPAWN_Z_KEY, PersistentDataType.DOUBLE);
        Location spawnLoc = (sx != null && sy != null && sz != null)
                ? new Location(rider.getWorld(), sx, sy, sz)
                : rider.getLocation().clone();

        ZiglinJouster boss = new ZiglinJouster(rider, spawnLoc, stage);

        if (rider.getVehicle() instanceof Zoglin z) {
            boss.zoglin = z;
        } else if (pdc.has(ZOGLIN_UUID_KEY, PersistentDataType.STRING)) {
            UUID zoglinUUID = UUID.fromString(pdc.get(ZOGLIN_UUID_KEY, PersistentDataType.STRING));
            Entity found = Bukkit.getEntity(zoglinUUID);
            if (found instanceof Zoglin z) boss.zoglin = z;
        }

        Double savedHealth = pdc.get(HEALTH_KEY, PersistentDataType.DOUBLE);
        if (savedHealth != null) {
            LivingEntity primary = boss.getPrimaryEntity();
            if (primary != null) {
                double cap = primary.getAttribute(Attribute.MAX_HEALTH).getValue();
                primary.setHealth(Math.min(savedHealth, cap));
            }
        }
        boss.enable();
        boss.updateBossBar();
    }
}