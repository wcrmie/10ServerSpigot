package worm.me.gamesystems.world.generation.island;

import org.bukkit.World;
import org.bukkit.entity.Player;
import worm.me.Server;
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

        Island island = new Island(biome, subBiome, shape, size);

        return island;
    }

    public static int getMaxUndersideDepth(Island island) {
        int size = island.getSize();
        float radius = (float) size / 2;
        float frequency = island.getShape().getFrequency();
        float lerpMix = island.getShape().getLerpMix();

        float maxHeightValue = (float) Math.pow(3 * (1 + 1.0f), 2);
        float maxUnderside = maxHeightValue + (frequency * lerpMix * radius) - (size + 2);
        return (int) Math.ceil(Math.max(maxUnderside, 0));
    }

    public static int getMaxPossibleHeight(Island island) {
        int size = island.getSize();
        float radius = (float) size / 2;
        float frequency = island.getShape().getFrequency();
        float lerpMix = island.getShape().getLerpMix();

        float maxHeightValue = (float) Math.pow(3 * (1 + 1.0f), 2);

        float maxUnderside = maxHeightValue + (frequency * lerpMix * radius) - (size + 2);
        maxUnderside = Math.max(maxUnderside, 0);

        float maxTopRaw = maxHeightValue + ((frequency * lerpMix - 0.2f) * radius) - size;
        maxTopRaw = Math.max(maxTopRaw, 0);
        float maxTop = Math.round((float) Math.sqrt(maxTopRaw));
        if (maxTop >= 1) maxTop = (float) Math.pow(maxTop + 2, 0.5);
        maxTop = Math.max(maxTop, 0);

        return (int) Math.ceil(maxUnderside + maxTop);
    }

}
