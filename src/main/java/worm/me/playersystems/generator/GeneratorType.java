package worm.me.playersystems.generator;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import worm.me.Server;

import java.util.Map;
import java.util.Set;

public enum GeneratorType {
    STONE("Rocky", Server.stones, new NamespacedKey(Server.getPlugin(), "stoneblock")),
    LOOSE("Loose", Server.dirts, new NamespacedKey(Server.getPlugin(), "dirtblock")),
    WOOD("Wooden", Server.planks, new NamespacedKey(Server.getPlugin(), "woodblock")),
    MISC("Miscellaneous", Server.miscs, new NamespacedKey(Server.getPlugin(), "miscitem"));

    private final String name;
    private final Map<Material, int[]> generators;
    private final NamespacedKey key;

    GeneratorType(String name, Map<Material,int[]> generators, NamespacedKey key) {
        this.name = name;
        this.generators = generators;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public Map<Material, int[]> getGenerators(){
        return generators;
    }

    public Set<Material> getMaterials(){
        return generators.keySet();
    }

    public NamespacedKey getKey(){
        return key;
    }
}
