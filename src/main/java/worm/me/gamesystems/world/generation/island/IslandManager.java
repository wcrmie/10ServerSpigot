package worm.me.gamesystems.world.generation.island;

import org.bukkit.World;
import org.bukkit.entity.Player;
import worm.me.gamesystems.world.generation.island.biome.Biome;
import worm.me.gamesystems.world.generation.island.biome.SubBiome;
import worm.me.utils.RNG;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static worm.me.gamesystems.world.generation.island.IslandGenerator.determineSize;

public class IslandManager {

    public IslandManager() {
    }

    /** Creates a new island object with random properties
     * @return The newly created island
     */
    public static Island createIsland(Integer diameter) {

        IslandProperties.IslandShape shape = IslandProperties.IslandShape.values()[ThreadLocalRandom.current().nextInt(0, IslandProperties.IslandShape.values().length)];

        Biome biome = Biome.values()[ThreadLocalRandom.current().nextInt(0, Biome.values().length)];

        SubBiome subBiome = biome.getRandomSubBiome();

        int size = diameter == null ? determineSize() : diameter;

        return new Island(biome, subBiome, shape, size);
    }

}
