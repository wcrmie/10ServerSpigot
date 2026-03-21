package worm.me.gamesystems.world.generation.island.biome;

import worm.me.gamesystems.world.generation.island.IslandProperties;
import worm.me.utils.RNG;

import java.util.List;
import java.util.Map;

public enum Biome {
    PLAINS(
            List.of(IslandProperties.IslandTopper.GRASSY),
            List.of(IslandProperties.IslandBottom.STONE, IslandProperties.IslandBottom.GRANITE, IslandProperties.IslandBottom.ANDESITE, IslandProperties.IslandBottom.DIORITE),
            Map.of(

                    SubBiome.PLAINS, 60,
                    SubBiome.SAVANNA, 20,
                    SubBiome.MEADOW, 20

            )
    ),

    FOREST(
            List.of(IslandProperties.IslandTopper.GRASSY),
            List.of(IslandProperties.IslandBottom.STONE, IslandProperties.IslandBottom.GRANITE, IslandProperties.IslandBottom.ANDESITE, IslandProperties.IslandBottom.DIORITE),
            Map.of(

                    SubBiome.OAK_FOREST, 25,
                    SubBiome.MIXED_FOREST, 15,
                    SubBiome.BIRCH_FOREST, 15,
                    SubBiome.FLOWER_FOREST, 10,
                    SubBiome.DARK_FOREST, 15

            )
    ),

    TAIGA(

            List.of(IslandProperties.IslandTopper.GRASSY, IslandProperties.IslandTopper.PODZOL),
            List.of(IslandProperties.IslandBottom.STONE, IslandProperties.IslandBottom.GRANITE, IslandProperties.IslandBottom.ANDESITE, IslandProperties.IslandBottom.DIORITE),
            Map.of(

                    SubBiome.TAIGA, 50,
                    SubBiome.SNOWY_TAIGA, 25

            )

    ),

    DESERT(
            List.of(IslandProperties.IslandTopper.SANDY),
            List.of(IslandProperties.IslandBottom.STONE, IslandProperties.IslandBottom.GRANITE, IslandProperties.IslandBottom.ANDESITE),
            Map.of(

                SubBiome.DESERT, 100

            )
    ),

    JUNGLE(
                    List.of(IslandProperties.IslandTopper.GRASSY),
                    List.of(IslandProperties.IslandBottom.STONE, IslandProperties.IslandBottom.GRANITE, IslandProperties.IslandBottom.ANDESITE, IslandProperties.IslandBottom.DIORITE),
                    Map.of(

                        SubBiome.JUNGLE, 100

                    )
    ),
//
//    MUSHROOM(
//            List.of(IslandProperties.IslandTopper.GRASSY),
//            List.of(IslandProperties.IslandBottom.STONE, IslandProperties.IslandBottom.GRANITE, IslandProperties.IslandBottom.ANDESITE, IslandProperties.IslandBottom.DIORITE),
//            Map.of(
//
//
//
//            )
//    ),
//
//    SNOWY(
//            List.of(IslandProperties.IslandTopper.GRASSY, IslandProperties.IslandTopper.PODZOL, IslandProperties.IslandTopper.SNOWY),
//            List.of(IslandProperties.IslandBottom.STONE, IslandProperties.IslandBottom.GRANITE, IslandProperties.IslandBottom.ANDESITE, IslandProperties.IslandBottom.DIORITE),
//            Map.of(
//
//
//
//            )
//    )
    ;


    private final List<IslandProperties.IslandTopper> toppers;
    private final List<IslandProperties.IslandBottom> bottoms;
    private final Map<SubBiome, Integer> subBiomes;

    Biome(List<IslandProperties.IslandTopper> toppers, List<IslandProperties.IslandBottom> bottoms, Map<SubBiome, Integer> subBiomes) {

        this.toppers = toppers;
        this.bottoms = bottoms;
        this.subBiomes = subBiomes;
    }

    public List<IslandProperties.IslandTopper> getToppers() {
        return toppers;
    }
    public List<IslandProperties.IslandBottom> getBottoms() {
        return bottoms;
    }
    public Map<SubBiome, Integer> getSubBiomes() {
        return subBiomes;
    }

    public SubBiome getRandomSubBiome() {

        return RNG.chooseRandom(getSubBiomes());

    }

}
