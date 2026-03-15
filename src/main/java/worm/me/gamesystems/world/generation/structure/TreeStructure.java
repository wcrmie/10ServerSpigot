package worm.me.gamesystems.world.generation.structure;

import java.util.List;

public enum TreeStructure {

    OAK(List.of("tiny_oak_tree_1", "oak_tree_small_1", "oak_tree_medium_1")),
    BIRCH(List.of("small_birch_tree_1", "large_birch_tree_1", "large_birch_tree_2", "birch_tree_1", "birch_tree_2")),
    SPRUCE(List.of("placeholder_tree")),
    CACTUS(List.of("placeholder_tree")),
    RED_MUSHROOM(List.of("placeholder_tree")),
    BROWN_MUSHROOM(List.of("placeholder_tree")),
    JUNGLE(List.of("placeholder_tree")),
    ACACIA(List.of("placeholder_tree")),
    DARK_OAK(List.of("placeholder_tree"));

    private final List<String> structures;

    TreeStructure(List<String> structures) {
        this.structures = structures;
    }

    /**
     * Get the names of the structures associated with this tree type.
     * @return list of structure names
     */
    public List<String> getStructureNames() {
        return structures;
    }
}
