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

    private static IslandManager instance;

    public IslandManager() {
        instance = this;
    }

    /** Creates a new island object with random properties
     * @return The newly created island
     */
    public Island createIsland() {

        IslandProperties.IslandShape shape = IslandProperties.IslandShape.values()[ThreadLocalRandom.current().nextInt(0, IslandProperties.IslandShape.values().length)];

        Biome biome = Biome.values()[ThreadLocalRandom.current().nextInt(0, Biome.values().length - 1)];

        SubBiome subBiome = biome.getRandomSubBiome();

        int size = determineSize();

        return new Island(biome, subBiome, shape, size);
    }

    public static IslandManager getInstance() {
        return instance;
    }

}
