package worm.me.gamesystems.world.structure;

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
    VILLAGE("Village"),
    TRIAL("Trial Chamber");

    private final String name;

    IslandType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
