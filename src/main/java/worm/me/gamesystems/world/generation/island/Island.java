package worm.me.gamesystems.world.generation.island;

import org.bukkit.entity.Player;
import worm.me.gamesystems.world.generation.island.biome.Biome;
import worm.me.gamesystems.world.generation.island.biome.SubBiome;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class Island {

    private final Biome m_biome;
    private final SubBiome m_subBiome;
    private final int m_size;
    private final IslandProperties.IslandShape m_shape;
    private final IslandProperties.IslandBottom m_bottom;
    private final IslandProperties.IslandTopper m_topper;

    public Island(Biome biome, SubBiome subBiome, IslandProperties.IslandShape shape, int size) {
        this.m_biome = biome;
        this.m_subBiome = subBiome;
        this.m_shape = shape;
        this.m_size = size;
        this.m_bottom = biome.getBottoms().get(ThreadLocalRandom.current().nextInt(0, biome.getBottoms().size()));
        this.m_topper = biome.getToppers().get(ThreadLocalRandom.current().nextInt(0, biome.getToppers().size()));

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

}
