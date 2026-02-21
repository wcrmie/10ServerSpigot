package worm.me.gamesystems.world.generation.island;

import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static worm.me.gamesystems.world.generation.island.IslandGenerator.determineSize;

public class IslandManager {

    private static IslandManager instance;

    public IslandManager() {
        instance = this;
    }

    private List<Island> islands = new java.util.ArrayList<>();

    /** Creates a new island object with random properties
     * @return The newly created island
     */
    public Island createIsland(List<Player> players) {

        IslandProperties.IslandShape shape = IslandProperties.IslandShape.values()[ThreadLocalRandom.current().nextInt(0, IslandProperties.IslandShape.values().length)];

        int size = determineSize();

        // to do with the blocks used for the island
        IslandProperties.IslandTopper topper = IslandProperties.IslandTopper.GRASSY;
        IslandProperties.IslandBottom palette = IslandProperties.IslandBottom.STONE;

        Island island = new Island(topper, shape, palette, size, players);
        islands.add(island);

        return island;
    }

    public World getIslandWorld() {
        return org.bukkit.Bukkit.getWorld("islands");
    }

    public List<Island> getIslands() {
        return islands;
    }

    public static IslandManager getInstance() {
        return instance;
    }

}
