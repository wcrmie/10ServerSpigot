package worm.me.gamesystems.world.generation.island;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.world.biome.BiomeType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import worm.me.Server;
import worm.me.gamesystems.world.PlayArea;
import worm.me.gamesystems.world.generation.island.biome.Biome;
import worm.me.gamesystems.world.generation.island.biome.SubBiome;
import worm.me.gamesystems.world.generation.poisson.PoissonDisc;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class IslandPlacement {

    private final Biome m_biome;
    private final SubBiome m_subBiome;

    private final IslandProperties.IslandBottom m_bottom;
    private final IslandProperties.IslandTopper m_topper;

    private final ArrayList<Island> m_islands;
    private final Location m_center;

    public IslandPlacement(Biome mBiome, SubBiome mSubBiome, IslandProperties.IslandBottom mBottom, IslandProperties.IslandTopper mTopper, Location center, ArrayList<Island> islands) {

        m_biome = mBiome;
        m_subBiome = mSubBiome;
        m_bottom = mBottom;
        m_topper = mTopper;

        this.m_center = center;
        this.m_islands = islands;

    }

    public void placeIslands() {

        ArrayList<Location> islandLocations = generateIslandLocations();

        Bukkit.getAsyncScheduler().runNow(Server.getPlugin(), $ -> {

                    CuboidRegion globalRegion = new CuboidRegion(BlockVector3.at(80 + m_center.getBlockX(), 180, 80 + m_center.getBlockZ()), BlockVector3.at(-80 + m_center.getBlockX(), 300, -80 + m_center.getBlockZ()));

                    World world = PlayArea.getPlayWorld();

                    try (EditSession editSession = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(world))) {

                        for (BlockVector3 block : globalRegion) {

                            editSession.setBiome(block, BiomeType.REGISTRY.get("minecraft:" + m_subBiome.getM_vanillaBiome().getKey().getKey()));

                        }

                        editSession.commit();

                    }


                }
        );

        Bukkit.getScheduler().runTaskTimer(Server.getPlugin(), gamer -> {

            if (islandLocations.isEmpty() || m_islands.isEmpty()) {
                Server.getPlugin().getLogger().warning("No islands found. Attempting cancel (placeIslands)");
                gamer.cancel();
                return;
            }

            Island island = m_islands.removeFirst();
            Location location = islandLocations.removeFirst();

            IslandGenerator.generate(island, location.add(0, m_center.y() + ThreadLocalRandom.current().nextInt(0, 8), 0));

        }, 1L, 1L);
    }

    private int largestIslandSize() {
        int largest = 0;
        for (Island island : m_islands) {
            if (largest < island.getSize()) {
                largest = island.getSize();
            }
        }
        return largest;
    }

    public ArrayList<Location> generateIslandLocations() {

        World world = m_center.getWorld();

        return PoissonDisc.generatePoints(world, new Location(world, 70, m_center.y(), 70).add(m_center), new Location(world, -70, m_center.y(), -70).add(m_center), largestIslandSize() - 20, largestIslandSize(), 20, null, m_islands.size(), m_center);
    }
}
