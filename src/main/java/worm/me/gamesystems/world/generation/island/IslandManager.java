package worm.me.gamesystems.world.generation.island;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitTask;
import worm.me.Server;
import worm.me.gamesystems.world.PlayArea;
import worm.me.gamesystems.world.generation.island.biome.Biome;
import worm.me.gamesystems.world.generation.island.biome.SubBiome;
import worm.me.gamesystems.world.generation.poisson.PoissonDisc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static worm.me.gamesystems.world.generation.island.IslandGenerator.determineSize;

public class IslandManager {

    private final Map<IslandPlacement, Location> m_locations;

    public IslandManager(Map<IslandPlacement, Location> mLocations) {
        m_locations = mLocations;
    }

    /** Creates a new island object with random properties
     * @return The newly created island
     */
    public static IslandPlacement createIslandPlacement(Location location) {

        Biome biome = Biome.values()[ThreadLocalRandom.current().nextInt(0, Biome.values().length)];

        SubBiome subBiome = biome.getRandomSubBiome();

        int islandCount = ThreadLocalRandom.current().nextInt(7, 10);

        ArrayList<Island> islands = new ArrayList<>();

        IslandProperties.IslandBottom bottom = biome.getBottoms().get(ThreadLocalRandom.current().nextInt(0, biome.getBottoms().size()));
        IslandProperties.IslandTopper topper = biome.getToppers().get(ThreadLocalRandom.current().nextInt(0, biome.getToppers().size()));

        for (int i = 0; i < islandCount; i++) {

            islands.add(new Island(biome, subBiome, bottom, topper, IslandProperties.IslandShape.STRAWBERRY, determineSize()));


        }

        return new IslandPlacement(
                biome,
                subBiome,
                bottom,
                topper,
                location,
                islands
                );



    }

    public static void generateIslands() {

        ArrayList<Location> islands = generateIslandPlacementLocations();

        Bukkit.getScheduler().runTaskTimer(Server.getPlugin(), gamer -> {

            if (islands.isEmpty()) {
                Server.getPlugin().getLogger().warning("No islands found. Attempting cancel (generateIslands)");
                gamer.cancel();
                return;
            }

            Location location = islands.removeFirst();

            int x = location.getBlockX();
            int z = location.getBlockZ();

            if (Math.pow(x, 2) +  Math.pow(z, 2) > 10000) {

                location.setY(200 + ThreadLocalRandom.current().nextInt(0, 8));

                IslandManager.createIslandPlacement(location).placeIslands();

            }

        }, 2L, 2L);
    }

    public static ArrayList<Location> generateIslandPlacementLocations() {

        World world = PlayArea.getPlayWorld();

        return PoissonDisc.generatePoints(world, new Location(world, 1170, 200, 1170), new Location(world, -1170, 200, -1170), 150, 200, 50, null, null, null);
    }

}
