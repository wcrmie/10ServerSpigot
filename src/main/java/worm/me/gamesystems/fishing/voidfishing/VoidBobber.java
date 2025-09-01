package worm.me.gamesystems.fishing.voidfishing;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import worm.me.Server;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
@Getter
public class VoidBobber {

    private final Player angler;
    private final FishHook bobber;
    private final Integer lureLevel;
    private final Integer luckLevel;
    private static final List<VoidBobber> activeBobbers = new ArrayList<>();

    @Setter
    private VoidFishState state;
    private final long beganFishingTime = System.currentTimeMillis();

    public enum VoidFishState {

        FISHING,
        FISH_COMING,
        FISH_BITE,
        FISH_CAUGHT,
        FAILED_CATCH;

    }

    public void tick() {

        if (getFishingDuration() >= 5) {

            int RNG = ThreadLocalRandom.current().nextInt(0,127);

            if (RNG >= considerLure()) {
                takeBait();
            }

        }

    }

    public void mainLoop() {

        new BukkitRunnable() {
            @Override
            public void run() {

                if (getState() != VoidFishState.FISHING) {
                    cancel();
                }

                tick();

            }
        }.runTaskTimer(Server.getPlugin(), 0L, 5L);

    }

    public long getFishingDuration() {
        return (System.currentTimeMillis() - beganFishingTime) * 1000;
    }

    public void takeBait() {
        setState(VoidFishState.FISH_COMING);
        Bukkit.getServer().getPluginManager().callEvent(new VoidFishEvent(getAngler(), this, getState()));

        BukkitTask particles = new BukkitRunnable() {

            final LinkedList<Location> path = new VoidFishPath(bobber.getLocation()).createCurve();

            @Override
            public void run() {

                if (getState() != VoidFishState.FISH_COMING) {
                    cancel();
                    return;
                }

                if (path.isEmpty()) {
                    setState(VoidFishState.FISH_BITE);

                    new BukkitRunnable() {

                        @Override
                        public void run() {
                            if (getState() == VoidFishState.FISH_BITE) {
                                setState(VoidFishState.FAILED_CATCH);
                                Bukkit.getServer().getPluginManager().callEvent(new VoidFishEvent(getAngler(), VoidBobber.this, getState()));
                            }
                        }
                    }.runTaskLater(Server.getPlugin(), 30L);

                    Bukkit.getServer().getPluginManager().callEvent(new VoidFishEvent(getAngler(), VoidBobber.this, getState()));
                    return;
                }

                Location loc = path.poll();

            }


        }.runTaskTimer(Server.getPlugin(), 0L, 2L);

    }

    public void startFishing() {
        setState(VoidFishState.FISHING);
        activeBobbers.add(this);
        getBobber().setGravity(false);
        Bukkit.getServer().getPluginManager().callEvent(new VoidFishEvent(getAngler(), this, getState()));
        mainLoop();
    }

    public int considerLure() {

        if (getLureLevel() == null) {
            return 1;
        }

        return getLureLevel() + 1;
    }

    public static VoidBobber findBobber(FishHook bobber) {
        for (VoidBobber voidBobber : activeBobbers) {
            if (voidBobber.getBobber().getEntityId() == bobber.getEntityId()) {
                return voidBobber;
            }
        }
        return null;
    }
    
    public static boolean canVoidFish(FishHook bobber) {

        PersistentDataContainer data = bobber.getPersistentDataContainer();
        return data.getOrDefault(new NamespacedKey(Server.getPlugin(), "voidfish"), PersistentDataType.BOOLEAN, false);

    }

    public void remove() {
        activeBobbers.remove(this);
    }

    @Getter
    public static class VoidFishPath implements CurveFactory{

        private final Location end;
        private Location start;

        public VoidFishPath(Location end) {

            this.end = end;
            this.start = generateStartLocation();

        }

        private Location generateStartLocation() {

            int distance = ThreadLocalRandom.current().nextInt(4,6);
            int angle = ThreadLocalRandom.current().nextInt(0,360);

            double xCoord = (float) (end.getX() + distance * Math.cos(Math.toRadians(angle)));
            double zCoord = (float) (end.getZ() + angle * Math.sin(Math.toRadians(angle)));

            return new Location(Bukkit.getWorld("world"), xCoord, end.getY(), zCoord);

        }

        private Location generateRandomPointStart() {
            int xOffset = ThreadLocalRandom.current().nextInt(-2,3);
            int yOffset = ThreadLocalRandom.current().nextInt(-1,2);
            int zOffset = ThreadLocalRandom.current().nextInt(-2,3);
            return new Location(Bukkit.getWorld("world"), start.getX() + xOffset, start.getY() + yOffset, start.getZ() + zOffset);
        }

        private Location generateRandomPointEnd() {
            int xOffset = ThreadLocalRandom.current().nextInt(-1,2);
            int yOffset = ThreadLocalRandom.current().nextInt(-1,2);
            int zOffset = ThreadLocalRandom.current().nextInt(-1,2);
            return new Location(Bukkit.getWorld("world"), end.getX() + xOffset, end.getY() + yOffset, end.getZ() + zOffset);
        }

        private Location getStart() {
            return start;
        }

        private Location getEnd() {
            return end;
        }



        @Override
        public LinkedList<Location> createCurve() {

            LinkedList<Location> locations = new LinkedList<>();

            for (int i = 0; i <= 20; i++) {

                float t = i / 20f;

                Location p0 = getStart();
                Location p1 = generateRandomPointStart();
                Location p2 = getEnd();
                Location p3 = generateRandomPointEnd();

                double x = Math.pow(1 - t, 3) * p0.getX() +
                        3 * Math.pow(1 - t, 2) * t * p1.getX() +
                        3 * (1 - t) * Math.pow(t, 2) * p2.getX() +
                        Math.pow(t, 3) * p3.getX();

                double z = Math.pow(1 - t, 3) * p0.getZ() +
                        3 * Math.pow(1 - t, 2) * t * p1.getZ() +
                        3 * (1 - t) * Math.pow(t, 2) * p2.getZ() +
                        Math.pow(t, 3) * p3.getZ();

                locations.add(new Location(Bukkit.getWorld("world"), x, getEnd().getY(), z));

            }


            return locations;
        }






    }

}
