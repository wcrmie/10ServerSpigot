package worm.me.gamesystems.structure.generation;

import org.bukkit.Location;

import java.io.File;

public enum IslandSize {
    SMALL(6, 8, 6),
    MEDIUM(10, 14, 10),
    LARGE(15, 15, 15),
    EXTRA_LARGE(25, 25, 25);

    private final int xSize;
    private final int ySize;
    private final int zSize;

    IslandSize(int xSize, int ySize, int zSize) {
        this.xSize = xSize;
        this.ySize = ySize;
        this.zSize = zSize;
    }

    public int getXSize() {
        return xSize;
    }

    public int getYSize() {
        return ySize;
    }

    public int getZSize() {
        return zSize;
    }
}
