package worm.me.playersystems.abilities.active;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;
import worm.me.playersystems.data.levels.Levels;
import worm.me.Server;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DoubleJump {
    private static final Map<UUID, JumpPlatformInfo> activePlatforms = new HashMap<>();

    public static void doubleJump(Player p) {
        cancelPlatform(p.getUniqueId());

        BlockDisplay platform = createJumpPlatform(p);

        p.setAllowFlight(false);
        p.setFlying(false);
        Vector direction = p.getLocation().getDirection();
        World world = p.getWorld();
        direction.multiply(0.5);
        direction.setY(0.5);
        p.setVelocity(direction);

        world.spawnParticle(Particle.CLOUD, p.getLocation(), 10, 0.3, 0.00001, 0.3, 0.05);
        world.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, .8f, 1.5f);

        BukkitTask removalTask = new BukkitRunnable() {
            private int ticks = 0;
            private final Vector3f initialCenter = new Vector3f(0, 0, 0);

            @Override
            public void run() {
                ticks++;

                if (ticks == 10) {
                    platform.setBlock(Material.GRAY_STAINED_GLASS.createBlockData());
                }

                if (ticks > 10) {
                    float scaleFactor = 1.0f - ((ticks - 10) / 20.0f);

                    float rotationAngle = (float) Math.toRadians((ticks - 10) * 18);

                    float originalScaleX = 1.125f;
                    float originalScaleY = 0.125f;
                    float originalScaleZ = 1.125f;

                    float newScaleX = originalScaleX * scaleFactor;
                    float newScaleY = originalScaleY * scaleFactor;
                    float newScaleZ = originalScaleZ * scaleFactor;

                    float baseTranslationX = -0.5625f;
                    float baseTranslationY = -0.05625f;
                    float baseTranslationZ = -0.5625f;

                    float riseAmount = 2f * (1.0f - scaleFactor);
                    float translationY = baseTranslationY + riseAmount;

                    float translationX = baseTranslationX - (newScaleX / 2 - originalScaleX / 2);
                    float translationZ = baseTranslationZ - (newScaleZ / 2 - originalScaleZ / 2);

                    float rotTransX = (float)(translationX * Math.cos(-rotationAngle) - translationZ * Math.sin(-rotationAngle));
                    float rotTransZ = (float)(translationX * Math.sin(-rotationAngle) + translationZ * Math.cos(-rotationAngle));

                    Transformation newTransform = new Transformation(
                            new Vector3f(rotTransX, translationY, rotTransZ),
                            new AxisAngle4f(rotationAngle, 0, 1, 0),
                            new Vector3f(newScaleX, newScaleY, newScaleZ),
                            new AxisAngle4f(0, 0, 0, 0)
                    );

                    platform.setTransformation(newTransform);
                }

                if (ticks >= 30 || !p.isOnline()) {
                    platform.remove();
                    activePlatforms.remove(p.getUniqueId());
                    this.cancel();
                }
            }
        }.runTaskTimer(Server.getPlugin(), 0L, 1L);

        activePlatforms.put(p.getUniqueId(), new JumpPlatformInfo(platform, removalTask));

        Levels.addExp(5L, Levels.skillTranslation("Agility"), p);
    }

    private static BlockDisplay createJumpPlatform(Player player) {
        Location playerLoc = player.getLocation().clone();

        Location platformLocation = new Location(
                playerLoc.getWorld(),
                playerLoc.getX(),
                playerLoc.getY() - 0.1,
                playerLoc.getZ(),
                playerLoc.getYaw(),
                0
        );

        BlockDisplay platform = (BlockDisplay) platformLocation.getWorld().spawnEntity(
                platformLocation,
                EntityType.BLOCK_DISPLAY);

        platform.setBlock(Material.LIGHT_BLUE_STAINED_GLASS.createBlockData());

        Transformation transformation = new Transformation(
                new Vector3f(-0.5625f, -0.05625f, -0.5625f),
                new AxisAngle4f(0, 0, 0, 0),
                new Vector3f(1.125f, 0.125f, 1.125f),
                new AxisAngle4f(0, 0, 0, 0)
        );

        platform.setTransformation(transformation);
        platform.setBrightness(new Display.Brightness(15, 15));

        return platform;
    }

    private static void cancelPlatform(UUID playerUuid) {
        JumpPlatformInfo platformInfo = activePlatforms.get(playerUuid);
        if (platformInfo != null) {
            platformInfo.task.cancel();
            if (platformInfo.platform != null && !platformInfo.platform.isDead()) {
                platformInfo.platform.remove();
            }
            activePlatforms.remove(playerUuid);
        }
    }

    public static void cancelAllPlatforms() {
        for (UUID playerUuid : activePlatforms.keySet()) {
            JumpPlatformInfo platformInfo = activePlatforms.get(playerUuid);
            if (platformInfo != null) {
                platformInfo.task.cancel();
                if (platformInfo.platform != null && !platformInfo.platform.isDead()) {
                    platformInfo.platform.remove();
                }
            }
        }
        activePlatforms.clear();
    }

    private static class JumpPlatformInfo {
        private final BlockDisplay platform;
        private final BukkitTask task;

        public JumpPlatformInfo(BlockDisplay platform, BukkitTask task) {
            this.platform = platform;
            this.task = task;
        }
    }
}