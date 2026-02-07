package worm.me.playersystems.abilities.active;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;
import worm.me.Server;
import worm.me.data.levels.Levels;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

public class SpeedStep {
    private final Map<UUID, DashInfo> activeDashes = new ConcurrentHashMap<>();
    private final int DASH_DURATION = 11;
    private final int POST_DASH_DURATION = 5;
    private final double DASH_SPEED = 1.07;
    private final float INITIAL_SCALE = 0.27f;
    private final float SCALE_DECREMENT = 0.09f;
    private final int FILLER_COUNT = 30;
    private final float LOCATION_RANDOMNESS = 0.3f;
    private final float VERTICAL_RANDOMNESS = 0.65f;
    private final float SCALE_RANDOMNESS = 0.18f;
    private final float HEIGHT_OFFSET = 0.5f;
    private final float POST_DASH_SCALE_FACTOR = 0.7f;

    private final Material[] COLOR_PROGRESSION = {
            Material.RED_STAINED_GLASS,
            Material.ORANGE_STAINED_GLASS,
            Material.YELLOW_STAINED_GLASS,
            Material.LIGHT_GRAY_STAINED_GLASS
    };

    public void SpeedStep(Player p) {
        cancelDash(p.getUniqueId());
        p.setGravity(false);
        Vector direction = p.getLocation().getDirection().normalize();
        direction.setY(0).normalize();
        p.getWorld().playSound(p.getLocation(), Sound.ITEM_FIRECHARGE_USE, 1.5f, 1.5f);
        p.teleport(p.getLocation().add(0, 0.75, 0));

        List<BlockDisplay> fireDisplays = Collections.synchronizedList(new ArrayList<>());
        LinkedList<Location> locationHistory = new LinkedList<>();

        BukkitTask dashTask = new BukkitRunnable() {
            private int ticks = 0;
            private boolean isDashing = true;
            private int postDashTicks = 0;

            @Override
            public void run() {
                if (!p.isOnline()) {
                    forceCleanup(p);
                    this.cancel();
                    return;
                }

                Location currentLocation = p.getLocation().clone().add(0, HEIGHT_OFFSET, 0);
                locationHistory.addFirst(currentLocation.clone());

                if (isDashing) {
                    p.setVelocity(direction.multiply(DASH_SPEED));

                    if (ticks > 0 && locationHistory.size() > 2) {
                        Location lastTick = locationHistory.get(1);
                        Location twoTicksAgo = locationHistory.get(2);

                        for (int i = 0; i < FILLER_COUNT; i++) {
                            createRandomizedDisplay(twoTicksAgo, lastTick, INITIAL_SCALE);
                        }
                    }

                    ticks++;

                    if (ticks >= DASH_DURATION) {
                        isDashing = false;
                        p.setGravity(true);
                        Vector v = p.getVelocity();
                        v.multiply(0.3);
                        p.setVelocity(v);
                        Levels.addExp(5L, Levels.skillTranslation("Agility"), p);
                    }
                }
                else if (postDashTicks < POST_DASH_DURATION) {
                    int displaysToCreate = (int)((POST_DASH_DURATION - postDashTicks) * (FILLER_COUNT/6f));
                    displaysToCreate = Math.max(1, displaysToCreate);

                    if (locationHistory.size() > 2) {
                        Location lastTick = locationHistory.get(1);
                        Location twoTicksAgo = locationHistory.get(2);

                        for (int i = 0; i < displaysToCreate; i++) {
                            createRandomizedDisplay(twoTicksAgo, lastTick, INITIAL_SCALE * POST_DASH_SCALE_FACTOR);
                        }
                    }
                    postDashTicks++;
                }

                if (locationHistory.size() > 3) {
                    locationHistory.removeLast();
                }

                synchronized(fireDisplays) {
                    Iterator<BlockDisplay> iterator = fireDisplays.iterator();
                    while (iterator.hasNext()) {
                        BlockDisplay display = iterator.next();
                        if (display.isDead()) {
                            iterator.remove();
                            continue;
                        }

                        if (ThreadLocalRandom.current().nextFloat() < 0.50f) {
                            progressDisplayColor(display);
                        }

                        float displayScale = getCurrentScale(display);
                        float newScale = displayScale - SCALE_DECREMENT;
                        if (newScale <= 0) {
                            display.remove();
                            iterator.remove();
                        } else {
                            updateDisplayScale(display, newScale);
                        }
                    }

                    if (!isDashing && postDashTicks >= POST_DASH_DURATION && fireDisplays.isEmpty()) {
                        activeDashes.remove(p.getUniqueId());
                        this.cancel();
                    }
                }
            }

            private void createRandomizedDisplay(Location start, Location end, float baseScale) {
                float progress = ThreadLocalRandom.current().nextFloat();
                Location spawnLoc = interpolateLocation(start, end, progress);
                spawnLoc = getRandomizedLocation(spawnLoc);
                float scale = getRandomizedScale(baseScale);
                BlockDisplay display = createFireDisplay(spawnLoc, scale);
                fireDisplays.add(display);
            }
        }.runTaskTimer(Server.getPlugin(), 0L, 1L);

        activeDashes.put(p.getUniqueId(), new DashInfo(dashTask, fireDisplays));
    }

    private void progressDisplayColor(BlockDisplay display) {
        Material current = display.getBlock().getMaterial();
        for (int i = 0; i < COLOR_PROGRESSION.length - 1; i++) {
            if (current == COLOR_PROGRESSION[i]) {
                display.setBlock(COLOR_PROGRESSION[i+1].createBlockData());
                break;
            }
        }
    }

    private Location getRandomizedLocation(Location location) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return location.clone().add(
                (random.nextDouble() * 2 - 1) * LOCATION_RANDOMNESS,
                (random.nextDouble() * 2 - 1) * VERTICAL_RANDOMNESS,
                (random.nextDouble() * 2 - 1) * LOCATION_RANDOMNESS
        );
    }

    private float getRandomizedScale(float baseScale) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        float scaleVariation = (random.nextFloat() * 2 - 1) * SCALE_RANDOMNESS;
        return Math.max(0.1f, baseScale + scaleVariation);
    }

    private BlockDisplay createFireDisplay(Location location, float scale) {
        BlockDisplay display = (BlockDisplay) location.getWorld().spawnEntity(
                location,
                EntityType.BLOCK_DISPLAY
        );
        display.setBlock(Material.RED_STAINED_GLASS.createBlockData());
        display.setBrightness(new Display.Brightness(15, 15));
        Transformation transformation = new Transformation(
                new Vector3f(-0.5f * scale, -0.5f * scale, -0.5f * scale),
                new AxisAngle4f(0, 0, 0, 0),
                new Vector3f(scale, scale, scale),
                new AxisAngle4f(0, 0, 0, 0)
        );
        display.setTransformation(transformation);
        return display;
    }

    private void updateDisplayScale(BlockDisplay display, float scale) {
        Transformation transformation = new Transformation(
                new Vector3f(-0.5f * scale, -0.5f * scale, -0.5f * scale),
                new AxisAngle4f(0, 0, 0, 0),
                new Vector3f(scale, scale, scale),
                new AxisAngle4f(0, 0, 0, 0)
        );
        display.setTransformation(transformation);
    }

    private Location interpolateLocation(Location start, Location end, float progress) {
        return new Location(
                start.getWorld(),
                start.getX() + (end.getX() - start.getX()) * progress,
                start.getY() + (end.getY() - start.getY()) * progress,
                start.getZ() + (end.getZ() - start.getZ()) * progress,
                start.getYaw(),
                start.getPitch()
        );
    }

    private float getCurrentScale(BlockDisplay display) {
        return display.getTransformation().getScale().x();
    }

    public void cleanup(Player p) {
        if (p != null && p.isOnline()) {
            p.setGravity(true);
            p.setVelocity(new Vector(0, 0, 0));
        }
        DashInfo info = activeDashes.remove(p.getUniqueId());
        if (info != null) {
            info.task.cancel();
            removeFireDisplays(info.fireDisplays);
        }
    }

    private void forceCleanup(Player p) {
        cleanup(p);
    }

    public void cancelDash(UUID playerId) {
        DashInfo info = activeDashes.remove(playerId);
        if (info != null) {
            info.task.cancel();
            Player p = Server.getPlugin().getServer().getPlayer(playerId);
            if (p != null) {
                p.setGravity(true);
                p.setVelocity(new Vector(0, 0, 0));
            }
            removeFireDisplays(info.fireDisplays);
        }
    }

    public void cleanupAll() {
        Set<UUID> playerIds = new HashSet<>(activeDashes.keySet());

        for (UUID playerId : playerIds) {
            DashInfo info = activeDashes.get(playerId);
            if (info != null) {
                info.task.cancel();
                Player p = Server.getPlugin().getServer().getPlayer(playerId);
                if (p != null && p.isOnline()) {
                    p.setGravity(true);
                    p.setVelocity(new Vector(0, 0, 0));
                }
                removeFireDisplays(info.fireDisplays);
            }
        }
        activeDashes.clear();
    }

    private void removeFireDisplays(List<BlockDisplay> displays) {
        if (displays != null) {
            synchronized(displays) {
                for (BlockDisplay display : displays) {
                    if (display != null && !display.isDead()) {
                        display.remove();
                    }
                }
                displays.clear();
            }
        }
    }

    private static class DashInfo {
        private final BukkitTask task;
        private final List<BlockDisplay> fireDisplays;
        public DashInfo(BukkitTask task, List<BlockDisplay> fireDisplays) {
            this.task = task;
            this.fireDisplays = fireDisplays;
        }
    }
}