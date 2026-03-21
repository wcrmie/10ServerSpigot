package worm.me.gamesystems.world.generation.island;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import worm.me.Server;
import worm.me.gamesystems.world.generation.poisson.PoissonDisc;
import worm.me.gamesystems.world.generation.structure.StructureSupplier;
import worm.me.gamesystems.world.generation.structure.TreeStructure;
import worm.me.utils.FastNoiseLite;
import worm.me.utils.RNG;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class IslandGenerator {

    /** Generates an island at the specified location
     * @param location The bottom middle point to generate the island at
     */
    public static void generate(Island island, Location location) {

        int size = island.getSize();


        Location spawnLoc = location.clone().add(-(size / 2), Island.getMaxUndersideDepth(island), -(size / 2));

        float[][] terrain = island.getTerrain();

        generateUnderside(spawnLoc, terrain, island);
        generateTop(spawnLoc, terrain, island);

        placeTrees(spawnLoc, island);

        placeFoliage(spawnLoc, island);

    }

    /** Determines a random size for the island
     * @return An integer representing the diameter of the island
     */
    public static int determineSize() {
        return ThreadLocalRandom.current().nextInt(25,35);
    }

    public static Material determineBlockMaterial(List<Material> palette, float densityValue) {

        int size = palette.size();

        densityValue = Math.abs(densityValue);

        densityValue = Math.round(densityValue * size - (float) 1 /size);

        int index = Math.min((int) Math.max(densityValue, 0), size - 1);



        return palette.get(index);
    }

    private static List<Location> findValidGroundLocations(Location location, Island island) {

        ArrayList<Location> validLocations = new ArrayList<>();

        for (int x = 0; x < island.getSize(); x++) {
            for (int z = 0; z < island.getSize(); z++) {

                Location currentLoc = location.clone().add(x, 0, z);

                Block ground;

                try {

                    ground = Objects.requireNonNull(location.getWorld()).getHighestBlockAt(currentLoc);

                } catch (Exception e) {

                    break;

                }

                if (ground.getLocation().getBlockY() <= -60) continue;

                Location groundLocation = ground.getLocation();

                boolean isValid = true;

                for (int localX = -1; localX <= 1; localX++) {

                    if (!isValid) break;

                    for (int localZ = -1; localZ <= 1; localZ++) {

                        if (!isValid) break;

                        // if the island topper materials does not contain a block that is touching the block we want
                        if (!island.getTopper().getTopMaterials().contains(
                                groundLocation.clone().add(localX, 0, localZ).getBlock().getType())
                        ) isValid = false;

                    }
                }

                if (!isValid) continue;

                validLocations.add(groundLocation);

            }
        }

        return validLocations;

    }

    private static void placeTrees(Location location, Island island) {


            List<Location> validLocations = findValidGroundLocations(location, island);

            Location corner1 = location.clone();
            Location corner2 = location.clone().add(island.getSize(), 0, island.getSize());

            int minTreeDistance = island.getSubBiome().getTreeSeparation()[0];
            int maxTreeDistance = island.getSubBiome().getTreeSeparation()[1];

            ArrayList<Location> points = PoissonDisc.generatePoints(location.getWorld(), corner1, corner2, minTreeDistance, maxTreeDistance, 100, validLocations, null, null);
            Iterator<Location> iterator = points.iterator();

            HashMap<Location, String> treeLocations = new HashMap<>();

            while (iterator.hasNext()) {
                Location point = iterator.next();

                TreeStructure structure = RNG.chooseRandom(island.getSubBiome().getTreesMap());
                String structureName = structure.getStructureNames().get(ThreadLocalRandom.current().nextInt(0, structure.getStructureNames().size()));

                Block ground;

                try {

                    ground = Objects.requireNonNull(location.getWorld()).getHighestBlockAt(point);

                } catch (Exception e) {
                    iterator.remove();

                    Server.getPlugin().getLogger().info("Can't find ground at " + point);

                    break;

                }

                if (ground.getLocation().getBlockY() <= -60) continue;

                Location groundLocation = ground.getLocation();

                groundLocation.add(0, 1, 0);

                treeLocations.put(groundLocation, structureName);

            }

            for (Map.Entry<Location, String> entry : treeLocations.entrySet()) {

                    StructureSupplier.placeStructure(
                            entry.getValue(),
                            entry.getKey(),
                            false
                    );

            }

    }

    private static void placeFoliage(Location location, Island island) {

        for (int x = 0; x < island.getSize(); x++) {
            for (int z = 0; z < island.getSize(); z++) {

                int foliageChance = ThreadLocalRandom.current().nextInt(0, 100);

                if (foliageChance >= island.getSubBiome().getFoliageDensity()) continue;

                Block ground;

                try {

                    ground = Objects.requireNonNull(location.getWorld()).getHighestBlockAt(location.clone().add(x, 0, z));

                } catch (Exception e) {

                    Server.getPlugin().getLogger().info("could not place foliage");
                    continue;

                }

                if (ground.getLocation().getBlockY() <= -60) continue;

                if (!island.getTopper().getTopMaterials().contains(ground.getType())) continue;

                Location groundLocation = ground.getLocation();

                groundLocation.add(0, 1, 0);

                try {
                    groundLocation.getBlock().setType(Objects.requireNonNull(RNG.chooseRandom(island.getSubBiome().getFoliageMap())));
                } catch (Exception e) {
                    Server.getPlugin().getLogger().info("foliage type was null " + ground.getLocation());
                }
            }
        }

    }


    /** Generates the underside of the island
     * @param loc The Southwest top corner of the underside
     * @param terrain A 2D array of floats representing noise values
     * @param island The island object containing properties for generation
     */
    private static void generateUnderside(Location loc, float[][] terrain, Island island) {

        World world = loc.getWorld();

        int size = island.getSize();
        IslandProperties.IslandShape shape = island.getShape();

        IslandProperties.IslandBottom palette = island.getBottom();
        IslandProperties.IslandTopper topper = island.getTopper();

        // 120 is the tallest an island bottom can be
        float[][][] density = determineDensity(size, 120);

        int baseMultiplier = 3;

        float frequency = shape.getFrequency();

        float radius = (float) size/2;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {

                // making the terrain value at least 1, multiplying it by the baseMultiplier, and then raising it to a power to get a larger height value
                float heightValue = (float) Math.pow(baseMultiplier * (1 + terrain[i][j]), 2);

                // calculating distance from center
                float distance = radius - (radius - (float) Math.sqrt(Math.pow(i - radius, 2) + Math.pow(j - radius, 2)));

                // linear interpolation so we dont have ugly edges
                float elevation = heightValue + (frequency * shape.getLerpMix() * (radius - distance/2)) - (size + 2);

                // do stuff for flat island here





                for (int h = 0; h < elevation; h++) {

                    Location currentBlock = new Location(world, loc.getBlockX() + i, loc.getBlockY() - h, loc.getBlockZ() + j);

                    Material blockMaterial = determineBlockMaterial(palette.getGradientMaterials(), density[i][h][j]);

                    // Adding some variation to the transition materials
                    if ((h + 2 <= 5) && Math.round(density[i][h][j] * 100) % 3 != 0) {
                        blockMaterial = determineBlockMaterial(topper.getTransitionMaterials(), density[i][h][j]);
                    }

                    if (h == 1) blockMaterial = determineBlockMaterial(topper.getTransitionMaterials(), density[i][h][j]);

                    if (h == 0) blockMaterial = determineBlockMaterial(topper.getTopMaterials(), density[i][h][j]);

                    currentBlock.getBlock().setType(blockMaterial);

                }
            }
        }

    }

    private static float[][][] determineDensity(int diameter, int height) {

        FastNoiseLite noise = new FastNoiseLite();
        noise.SetNoiseType(FastNoiseLite.NoiseType.Perlin);
        noise.SetSeed(ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE));
        noise.SetFrequency(9.0f);

        float[][][] density = new float[diameter][height][diameter];


        for (int x = 0; x < diameter; x++) {
            for (int y = 0; y < height; y++) {
                for (int z = 0; z < diameter; z++) {

                    float nx = (float) ((double) x /diameter - 0.5);
                    float ny = (float) ((double) y / height - 0.5);
                    float nz = (float) ((double) z /diameter - 0.5);
                    density[x][y][z] = noise.GetNoise(nx, ny, nz);

                }
            }
        }

        return density;



    }


    /** Generates the top of the island
     * @param loc The Southwest bottom corner of the top
     * @param terrain A 2D array of floats representing noise values
     * @param island The island object containing properties for generation
     */
    private static void generateTop(Location loc, float[][] terrain, Island island) {

        int size = island.getSize();
        IslandProperties.IslandShape shape = island.getShape();
        IslandProperties.IslandTopper topper = island.getTopper();

        World world = loc.getWorld();

        float[][][] density = determineDensity(size, 120);

        // Because the float array is very high values, we can take the reciprocal of them
        // to get lower values, and then multiply them by some value to get a smoother topside terrain

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {

                float heightValue = (float) Math.pow(3 * (1 + terrain[i][j]), 2);

                float radius = (float) size/2;

                float distance = radius - (radius - (float) Math.sqrt(Math.pow(i - radius, 2) + Math.pow(j - radius, 2)));

                float elevation = heightValue + ((shape.getFrequency() * shape.getLerpMix() - 0.2f) * (radius - distance/2)) - size;

                elevation = Math.round(((float) Math.sqrt(elevation))); // Smoother topside terrain

                if (elevation >= 1) {

                    elevation = (float) Math.pow(elevation + 2, (double) 1 /2);

                }


                for (int h = 0; h < elevation; h++) {

                    Location currentBlock = new Location(world, loc.getBlockX() + i, loc.getBlockY() + h, loc.getBlockZ() + j);

                    Material blockMaterial;

                    blockMaterial = determineBlockMaterial(topper.getTopMaterials(), density[i][h][j]);

                    if (h < elevation - 1) {

                        blockMaterial = determineBlockMaterial(topper.getTransitionMaterials(), density[i][h][j]);

                    }

                    // or setblockdata
                    currentBlock.getBlock().setType(blockMaterial, false);

                }
            }
        }

    }

}
