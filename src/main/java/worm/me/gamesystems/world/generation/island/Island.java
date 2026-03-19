package worm.me.gamesystems.world.generation.island;

import worm.me.gamesystems.world.generation.island.biome.Biome;
import worm.me.gamesystems.world.generation.island.biome.SubBiome;

import java.util.concurrent.ThreadLocalRandom;

import static worm.me.gamesystems.world.generation.island.Noise.fillTaperedPerlinArray;

public class Island {

    private final Biome m_biome;
    private final SubBiome m_subBiome;
    private final int m_size;
    private final float[][] m_terrain;
    private final IslandProperties.IslandShape m_shape;
    private final IslandProperties.IslandBottom m_bottom;
    private final IslandProperties.IslandTopper m_topper;

    public Island(Biome biome, SubBiome subBiome, IslandProperties.IslandBottom bottom, IslandProperties.IslandTopper topper, IslandProperties.IslandShape shape, int size) {
        this.m_biome = biome;
        this.m_subBiome = subBiome;
        this.m_shape = shape;
        this.m_size = size;
        this.m_bottom = bottom;
        this.m_topper = topper;

        this.m_terrain = fillTaperedPerlinArray(m_size, m_shape);

    }

    public float[][] getTerrain() {
        return m_terrain;
    }

    public Biome getBiome() {
        return m_biome;
    }

    public SubBiome getSubBiome() {
        return m_subBiome;
    }

    public IslandProperties.IslandShape getShape() {
        return m_shape;
    }

    public int getSize() {
        return m_size;
    }

    public IslandProperties.IslandBottom getBottom() {
        return m_bottom;
    }

    public IslandProperties.IslandTopper getTopper() {
        return m_topper;
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

}
