package worm.me.playersystems.abilities.historical;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import worm.me.playersystems.abilities.Abilities;
import worm.me.Server;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class FrogGrab {
    public final Abilities ability = new Abilities();
    private final int MAX_DISTANCE = 20;
    private final int MAX_GRAB_TIME_TICKS = 20 * 10;
    private final List<EntityType> pickUpAbleEntities;
    private static final Map<UUID, GrabInfo> activeGrabs = new HashMap<>();
    private static final Set<UUID> restrictedPlayers = new HashSet<>();

    public FrogGrab() {
        this.pickUpAbleEntities = Arrays.asList(
                EntityType.BAT,
                EntityType.CHICKEN,
                EntityType.PIG,
                EntityType.CREEPER,
                EntityType.PARROT,
                EntityType.FROG,
                EntityType.SQUID,
                EntityType.COD,
                EntityType.SALMON,
                EntityType.PUFFERFISH,
                EntityType.TROPICAL_FISH,
                EntityType.GLOW_SQUID,
                EntityType.DOLPHIN,
                EntityType.AXOLOTL,
                EntityType.CAT,
                EntityType.CAVE_SPIDER,
                EntityType.SPIDER,
                EntityType.WOLF,
                EntityType.FOX,
                EntityType.OCELOT,
                EntityType.RABBIT,
                EntityType.ARMADILLO,
                EntityType.TADPOLE,
                EntityType.TURTLE,
                EntityType.ENDERMITE,
                EntityType.SILVERFISH,
                EntityType.BEE,
                EntityType.RABBIT,
                EntityType.ALLAY,
                EntityType.VEX,
                EntityType.PHANTOM,
                EntityType.SNOW_GOLEM,
                EntityType.GIANT,
                EntityType.LLAMA,
                EntityType.ITEM,
                EntityType.PLAYER,
                EntityType.SHEEP,
                EntityType.COW,
                EntityType.MOOSHROOM,
                EntityType.ZOMBIE,
                EntityType.SKELETON,
                EntityType.HUSK,
                EntityType.STRAY,
                EntityType.DROWNED,
                EntityType.ZOMBIE_VILLAGER,
                EntityType.VILLAGER,
                EntityType.WITCH,
                EntityType.GUARDIAN,
                EntityType.TRADER_LLAMA,
                EntityType.WANDERING_TRADER
        );
    }

    public static void restrictPlayerMovement(UUID playerUuid) {
        restrictedPlayers.add(playerUuid);
    }

    public static void unrestrictPlayerMovement(UUID playerUuid) {
        restrictedPlayers.remove(playerUuid);
    }

    public static boolean isPlayerMovementRestricted(UUID playerUuid) {
        return restrictedPlayers.contains(playerUuid);
    }

    public static void clearAllMovementRestrictions() {
        restrictedPlayers.clear();
    }

    public void frogGrab(Player player) {
        if (activeGrabs.containsKey(player.getUniqueId())) {
            cancelGrab(player.getUniqueId());
        }

        Object target = getTargetInSight(player, MAX_DISTANCE);
        if (target instanceof Entity) {
            Entity entity = (Entity) target;
            if (pickUpAbleEntities.contains(entity.getType())) {
                restrictPlayerMovement(player.getUniqueId());
                BlockDisplay tongue = (BlockDisplay) player.getWorld().spawnEntity(
                        player.getLocation(), EntityType.BLOCK_DISPLAY);
                tongue.setBlock(Material.PINK_CONCRETE_POWDER.createBlockData());

                final int[] tickCounter = {0};

                BukkitTask task = new BukkitRunnable() {
                    @Override
                    public void run() {
                        tickCounter[0]++;

                        if (tickCounter[0] >= MAX_GRAB_TIME_TICKS) {
                            entity.setGravity(true);
                            tongue.remove();
                            unrestrictPlayerMovement(player.getUniqueId());
                            activeGrabs.remove(player.getUniqueId());
                            this.cancel();
                            return;
                        }

                        if (!player.isOnline() || !entity.isValid()) {
                            entity.setGravity(true);
                            tongue.remove();
                            unrestrictPlayerMovement(player.getUniqueId());
                            activeGrabs.remove(player.getUniqueId());
                            this.cancel();
                            return;
                        }

                        if (entity instanceof Player) {
                            Player playere = ((Player) entity).getPlayer();
                            if (playere.isSneaking()) {
                                entity.setGravity(true);
                                tongue.remove();
                                unrestrictPlayerMovement(player.getUniqueId());
                                activeGrabs.remove(player.getUniqueId());
                                this.cancel();
                                return;
                            }
                        }

                        entity.setGravity(false);
                        Location playerLoc = player.getLocation().clone().add(0, 1.3, 0);
                        Location entityLoc = entity.getLocation().add(0, entity.getHeight()/2, 0);
                        double distance = (playerLoc.distance(entityLoc))+0.25;
                        Vector direction = entityLoc.toVector().subtract(playerLoc.toVector()).normalize();
                        Vector rightVector = direction.getCrossProduct(new Vector(0, 1, 0)).normalize();
                        Vector backVector = direction.clone().multiply(-0.25);
                        Location tongueLocation = playerLoc.clone().add(rightVector.multiply(0.075)).add(backVector);
                        tongueLocation.setDirection(direction);
                        tongue.teleport(tongueLocation);

                        Transformation transformation = new Transformation(
                                new Vector3f(0, 0, 0),
                                new Quaternionf().identity(),
                                new Vector3f(0.10f, 0.001f, (float)distance),
                                new Quaternionf().identity()
                        );
                        tongue.setTransformation(transformation);

                        if (distance <= 2) {
                            entity.setVelocity(new Vector(0, 0, 0));
                            entity.setGravity(true);
                            tongue.remove();
                            unrestrictPlayerMovement(player.getUniqueId());
                            activeGrabs.remove(player.getUniqueId());
                            this.cancel();
                            return;
                        }

                        Vector pullDirection = playerLoc.toVector().subtract(entity.getLocation().toVector());
                        double pullSpeed = 0.7;
                        Vector velocity = pullDirection.normalize().multiply(pullSpeed);
                        entity.setVelocity(velocity);
                    }
                }.runTaskTimer(Server.getPlugin(), 0L, 1L);

                activeGrabs.put(player.getUniqueId(), new GrabInfo(entity, tongue, task));
            }
        }
    }

    private static void cancelGrab(UUID playerUuid) {
        GrabInfo grabInfo = activeGrabs.get(playerUuid);
        if (grabInfo != null) {
            grabInfo.entity.setGravity(true);
            grabInfo.tongue.remove();
            grabInfo.task.cancel();
            unrestrictPlayerMovement(playerUuid);
            activeGrabs.remove(playerUuid);
        }
    }

    public static void cancelAllGrabs() {
        for (UUID playerUuid : activeGrabs.keySet()) {
            unrestrictPlayerMovement(playerUuid);
        }
        for (GrabInfo grabInfo : activeGrabs.values()) {
            grabInfo.entity.setGravity(true);
            grabInfo.tongue.remove();
            grabInfo.task.cancel();
        }
        activeGrabs.clear();
    }

    private Object getTargetInSight(Player player, int maxDistance) {
        Location eyeLocation = player.getEyeLocation();
        Vector direction = player.getEyeLocation().getDirection().normalize();

        RayTraceResult rayTraceEntities = player.getWorld().rayTraceEntities(
                eyeLocation,
                direction,
                maxDistance,
                entity -> entity != player
        );

        RayTraceResult rayTraceBlocks = player.getWorld().rayTraceBlocks(
                eyeLocation,
                direction,
                maxDistance
        );

        Entity hitEntity = rayTraceEntities != null ? rayTraceEntities.getHitEntity() : null;
        Block hitBlock = rayTraceBlocks != null ? rayTraceBlocks.getHitBlock() : null;

        if (hitEntity != null && hitBlock != null) {
            double entityDistance = eyeLocation.distance(hitEntity.getLocation());
            double blockDistance = eyeLocation.distance(hitBlock.getLocation());
            return entityDistance < blockDistance ? hitEntity : hitBlock;
        } else if (hitEntity != null) {
            return hitEntity;
        } else if (hitBlock != null) {
            return hitBlock;
        } else {
            return null;
        }
    }

    private static class GrabInfo {
        private final Entity entity;
        private final BlockDisplay tongue;
        private final BukkitTask task;

        public GrabInfo(Entity entity, BlockDisplay tongue, BukkitTask task) {
            this.entity = entity;
            this.tongue = tongue;
            this.task = task;
        }
    }
}