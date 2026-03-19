package worm.me.gamesystems.world.generation.island.biome;

import org.bukkit.Material;
import org.bukkit.block.Biome;
import worm.me.gamesystems.world.generation.structure.TreeStructure;
import worm.me.utils.RNG;

import java.util.Map;

public enum SubBiome {

    // Plains biomes
    PLAINS(
            Map.of(

                    Material.SHORT_GRASS, 50,
                    Material.DANDELION, 2,
                    Material.POPPY, 5

            ),

            Map.of(

                    TreeStructure.OAK, 100

            ),

            Biome.PLAINS
    ),

    SUNFLOWER_PLAINS(
            Map.of(

                    Material.SHORT_GRASS, 50

            ),

            Map.of(

                    TreeStructure.OAK, 100

            ),

            Biome.SUNFLOWER_PLAINS
    ),

    MEADOW(
            Map.of(

                    Material.SHORT_GRASS, 65,
                    Material.CORNFLOWER, 5,
                    Material.OXEYE_DAISY, 10

            ),

            Map.of(

                    TreeStructure.OAK, 100

            ),

            Biome.MEADOW
    ),

    // Forest biomes
    OAK_FOREST(
            Map.of(

                    Material.SHORT_GRASS, 65,
                    Material.DANDELION, 5,
                    Material.POPPY, 10

            ),

            Map.of(

                    TreeStructure.OAK, 100

            ),

            Biome.FOREST
            ),

    MIXED_FOREST(
            Map.of(

                    Material.SHORT_GRASS, 65,
                    Material.DANDELION, 5,
                    Material.POPPY, 10

            ),

            Map.of(

                    TreeStructure.OAK, 60,
                    TreeStructure.BIRCH, 40

            ),

            Biome.FOREST),

    BIRCH_FOREST(
            Map.of(

                    Material.SHORT_GRASS, 65,
                    Material.DANDELION, 5,
                    Material.POPPY, 10

            ),

            Map.of(

                    TreeStructure.BIRCH, 100

            ),

            Biome.BIRCH_FOREST),

    // TODO: add more flowers
    FLOWER_FOREST(
            Map.of(

                    Material.SHORT_GRASS, 65,
                    Material.DANDELION, 5,
                    Material.POPPY, 10

            ),

            Map.of(

                    TreeStructure.OAK, 70,
                    TreeStructure.BIRCH, 30

            ),

            Biome.FLOWER_FOREST),
//    DARK_FOREST(
//            Map.of(
//
//                    Material.SHORT_GRASS, 50,
//                    Material.BUSH, 35,
//                    Material.AZURE_BLUET, 5,
//                    Material.LILY_OF_THE_VALLEY, 10
//
//            ),
//
//            Map.of(
//
//                    TreeStructure.DARK_OAK, 100
//
//            ),
//
//            Biome.DARK_FOREST),

//    // Cold biomes
//    TUNDRA(15),
//    TAIGA(45),
//    SNOWY_TAIGA(25),
//    GROVE(15),
//    //ICE_SPIKES(),
//
//    // Hot biomes
//    SAVANNA(25),
    DESERT(

            Map.of(

                    Material.SHORT_DRY_GRASS, 50,
                    Material.DEAD_BUSH, 10,
                    Material.TALL_DRY_GRASS, 35

            ),

            Map.of(

                    TreeStructure.CACTUS, 100

            ),

        Biome.DESERT

),
//    BADLANDS(15),
//    WOODED_BADLANDS(10),
//    JUNGLE(25),
//
//    // Mushroom biome
//    MUSHROOM(100)
    ;

    private final Map<Material, Integer> materials;
    private final Map<TreeStructure, Integer> trees;
    private final Biome vanillaBiome;

    SubBiome(Map<Material, Integer> materials, Map<TreeStructure, Integer> trees, Biome vanillaBiome) {
        this.materials = materials;
        this.trees = trees;
        this.vanillaBiome = vanillaBiome;
    }

    public Map<Material, Integer> getFoliageMap() {
        return materials;
    }

    public Map<TreeStructure, Integer> getTreesMap() {
        return trees;
    }

    public Biome getVanillaBiome() {
        return vanillaBiome;
    }

    /**
     *
     * @return random foliage material from map based on weight
     *
     */
    public Material chooseRandomFoliage() {

        return RNG.chooseRandom(getFoliageMap());

    }


}
