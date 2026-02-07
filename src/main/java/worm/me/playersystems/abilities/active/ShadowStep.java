package worm.me.playersystems.abilities.active;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;
import worm.me.playersystems.abilities.Abilities;
import worm.me.data.levels.Levels;
import worm.me.Server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ShadowStep {
    private final Abilities ability = new Abilities();
    private static final Map<UUID, GhostInfo> activeGhosts = new HashMap<>();

    public void shadowStep(Player p) {
        cancelGhosts(p.getUniqueId());
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ALLAY_AMBIENT_WITH_ITEM, 0.5f, -5);
        p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 30, 1, false, false));

        Vector direction = p.getLocation().getDirection().normalize();

        double maxAngle = Math.toRadians(45);
        double pitch = Math.toRadians(-p.getLocation().getPitch());

        if (pitch > maxAngle) pitch = maxAngle;
        if (pitch < -maxAngle) pitch = -maxAngle;

        double xzLen = Math.cos(pitch);
        double y = Math.sin(pitch);
        Vector horizontal = new Vector(direction.getX(), 0, direction.getZ()).normalize();
        Vector limitedDir = horizontal.multiply(xzLen);
        limitedDir.setY(y);
        limitedDir.normalize();

        final Vector initialDirection = limitedDir;

        new BukkitRunnable() {
            private int ticks = 0;
            private final List<Location> pathHistory = new ArrayList<>();
            private final Vector stepDirection = initialDirection.clone();

            @Override
            public void run() {
                Location currentLocation = p.getLocation();
                pathHistory.add(currentLocation.clone());

                if (ticks >= 15 || !p.isOnline()) {
                    this.cancel();
                    createTrailingGhosts(p, pathHistory);
                    Levels.addExp(10L, Levels.skillTranslation("Agility"), p);
                    return;
                }

                if (ticks == 5) {
                    createTrailingGhosts(p, pathHistory);
                }

                Location newLocation = currentLocation.clone().add(stepDirection);
                newLocation.setPitch(currentLocation.getPitch());
                newLocation.setYaw(currentLocation.getYaw());
                p.teleport(newLocation);

                ticks++;
            }
        }.runTaskTimer(Server.getPlugin(), 0L, 1L);
    }

    private void createTrailingGhosts(Player player, List<Location> pathHistory) {
        if (pathHistory.size() < 2) return;

        BlockDisplay ghost1 = createGhostBlock(pathHistory.get(pathHistory.size() - 2));
        BlockDisplay ghost2 = null;
        if (pathHistory.size() >= 10) {
            ghost2 = createGhostBlock(pathHistory.get(pathHistory.size() - 10));
        }

        final BlockDisplay finalGhost1 = ghost1;
        final BlockDisplay finalGhost2 = ghost2;
        List<BlockDisplay> ghostList = new ArrayList<>();
        ghostList.add(ghost1);
        if (ghost2 != null) ghostList.add(ghost2);

        BukkitTask task = new BukkitRunnable() {
            private int index = 0;

            @Override
            public void run() {
                if (index >= pathHistory.size() - 1 || !player.isOnline()) {
                    if (finalGhost1 != null) finalGhost1.remove();
                    if (finalGhost2 != null) finalGhost2.remove();
                    activeGhosts.remove(player.getUniqueId());
                    this.cancel();
                    return;
                }

                if (finalGhost1 != null && !finalGhost1.isDead() && index + 2 < pathHistory.size()) {
                    Location ghostLoc = adjustGhostLocation(pathHistory.get(index + 2).clone());
                    finalGhost1.teleport(ghostLoc);
                }

                if (finalGhost2 != null && !finalGhost2.isDead() && index < pathHistory.size()) {
                    Location ghostLoc = adjustGhostLocation(pathHistory.get(index).clone());
                    finalGhost2.teleport(ghostLoc);
                }

                index++;
            }
        }.runTaskTimer(Server.getPlugin(), 0L, 1L);

        activeGhosts.put(player.getUniqueId(), new GhostInfo(ghostList, task));
    }

    private Location adjustGhostLocation(Location playerLoc) {
        Location adjustedLoc = playerLoc.clone();
        adjustedLoc.setPitch(0);
        adjustedLoc.add(0, 0.7, 0);
        return adjustedLoc;
    }

    private BlockDisplay createGhostBlock(Location location) {
        Location ghostLocation = adjustGhostLocation(location);

        BlockDisplay ghost = (BlockDisplay) ghostLocation.getWorld().spawnEntity(
                ghostLocation,
                EntityType.BLOCK_DISPLAY);

        ghost.setBlock(Material.GRAY_STAINED_GLASS.createBlockData());

        Transformation transformation = new Transformation(
                new Vector3f(-0.3f, -0.7f, -0.3f),
                new AxisAngle4f(0, 0, 0, 0),
                new Vector3f(0.6f, 1.4f, 0.6f),
                new AxisAngle4f(0, 0, 0, 0)
        );

        ghost.setTransformation(transformation);
        ghost.setBrightness(new Display.Brightness(15, 15));

        return ghost;
    }

    private static void cancelGhosts(UUID playerUuid) {
        GhostInfo ghostInfo = activeGhosts.get(playerUuid);
        if (ghostInfo != null) {
            ghostInfo.task.cancel();
            for (BlockDisplay ghost : ghostInfo.ghosts) {
                if (ghost != null && !ghost.isDead()) {
                    ghost.remove();
                }
            }
            activeGhosts.remove(playerUuid);
        }
    }

    public static void cancelAllGhosts() {
        for (UUID playerUuid : activeGhosts.keySet()) {
            GhostInfo ghostInfo = activeGhosts.get(playerUuid);
            if (ghostInfo != null) {
                ghostInfo.task.cancel();
                for (BlockDisplay ghost : ghostInfo.ghosts) {
                    if (ghost != null && !ghost.isDead()) {
                        ghost.remove();
                    }
                }
            }
        }
        activeGhosts.clear();
    }

    private static class GhostInfo {
        private final List<BlockDisplay> ghosts;
        private final BukkitTask task;

        public GhostInfo(List<BlockDisplay> ghosts, BukkitTask task) {
            this.ghosts = ghosts;
            this.task = task;
        }
    }
}
