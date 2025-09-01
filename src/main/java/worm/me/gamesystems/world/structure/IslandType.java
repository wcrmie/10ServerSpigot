package worm.me.gamesystems.world.structure;

import lombok.Getter;

@Getter
public enum IslandType {
    DEFAULT("Plains"),
    DESERT("Desert"),
    SAVANNA("Savanna"),
    DARK_OAK("Dark Oak"),
    BIRCH("Birch"),
    MUSHROOM("Mushroom"),
    JUNGLE("Jungle"),
    TAIGA("Taiga"),
    SNOWY("Snowy Taiga"),
    DUNGEON("Dungeon"),
    TRIAL("Trial Chamber");

    private final String name;

    IslandType(String name) {
        this.name = name;
    }
}
