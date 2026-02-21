package worm.me.gamesystems.world.generation.island;

import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class Island {

    private final IslandProperties.IslandTopper topper;
    private final IslandProperties.IslandShape shape;
    private final IslandProperties.IslandBottom bottom;
    private final int size;
    private final List<Player> players;
    private final String id;



    public Island(IslandProperties.IslandTopper topper, IslandProperties.IslandShape shape, IslandProperties.IslandBottom bottom, int size, List<Player> players) {
        this.topper = topper;
        this.shape = shape;
        this.bottom = bottom;
        this.size = size;
        this.players = players;
        this.id = UUID.randomUUID().toString();
    }

    public IslandProperties.IslandTopper getTopper() {
        return topper;
    }

    public IslandProperties.IslandShape getShape() {
        return shape;
    }

    public IslandProperties.IslandBottom getBottom() {
        return bottom;
    }

    public int getSize() {
        return size;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public String getId() {
        return id;
    }
}
