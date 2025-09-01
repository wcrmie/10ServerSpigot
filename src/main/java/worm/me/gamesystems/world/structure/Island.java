package worm.me.gamesystems.world.structure;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.structure.Structure;
import org.bukkit.structure.StructureManager;
import org.bukkit.util.Vector;
import worm.me.gamesystems.world.structure.generation.Generate;
import worm.me.gamesystems.world.PlayArea;

import java.io.File;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


public class Island {

    private static JavaPlugin plugin;

    public static void init(JavaPlugin pluginInstance) {
        plugin = pluginInstance;
    }

    private static StructureManager structureManager = Bukkit.getStructureManager();

    public static void spawnIslandType(String typeName) {
        IslandType type;
        try {
            type = IslandType.valueOf(typeName.toUpperCase()); // Convert string to enum
        } catch (IllegalArgumentException e) {
            Bukkit.getLogger().warning("[Island] Invalid island type: " + typeName);
            return;
        }

        File folder = new File(plugin.getDataFolder(), "structures");
        if (!folder.exists()) {
            Bukkit.getLogger().warning("[Island] No structures folder found");
            return;
        }

        File[] files = folder.listFiles((dir, name) ->
                name.startsWith(type.name() + "_") && name.endsWith(".nbt"));

        if (files == null || files.length == 0) {
            Bukkit.getLogger().warning("[Island] No structures found for type: " + type);
            return;
        }

        IslandSize size;
        List<File> candidates = new ArrayList<>();

        int attempts = 0;
        int maxAttempts = 20;

        while (attempts < maxAttempts) {
            size = pickWeightedSize();

            candidates.clear();
            for (File f : files) {
                if (f.getName().contains("_" + size.name())) {
                    candidates.add(f);
                }
            }

            if (!candidates.isEmpty()) break;
            attempts++;
        }

        if (candidates.isEmpty()) {
            Bukkit.getLogger().warning("[Island] No structures found for type: " + type + " in any size");
            return;
        }

        File chosen = candidates.get(ThreadLocalRandom.current().nextInt(candidates.size()));
        String structureName = chosen.getName().replace(".nbt", "");

        spawnIsland(structureName);
    }


    public static void spawnIsland(String name) {
        try {
            File file = new File(plugin.getDataFolder(), "structures/" + name + ".nbt");
            if (!file.exists()) {
                Bukkit.getLogger().warning("[Island] Structure not found: " + name);
                return;
            }

            Structure structure = structureManager.loadStructure(file);
            if (structure == null) {
                Bukkit.getLogger().warning("[Island] Failed to load: " + name);
                return;
            }

            Vector size = structure.getSize();
            int xSize = size.getBlockX();
            int ySize = size.getBlockY();
            int zSize = size.getBlockZ();
            int maxSize = Math.max(xSize, Math.max(ySize, zSize));

            Location loc = PlayArea.validSpawnLocation(xSize, ySize, zSize, maxSize);
            if (loc == null) {
                Bukkit.getLogger().warning("[Island] Could not find valid location for: " + name);
                return;
            }

            if (Generate.placeStructureStatic(loc, name)) {
                Bukkit.getLogger().info("[Island] Spawned island: " + name + " at " + loc);
            } else {
                Bukkit.getLogger().warning("[Island] Failed to place island: " + name);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static IslandSize pickWeightedSize() {
        Map<IslandSize, Integer> weights = new LinkedHashMap<>();
        weights.put(IslandSize.SMALL, 6);
        weights.put(IslandSize.MEDIUM, 8);
        weights.put(IslandSize.LARGE, 10);
        weights.put(IslandSize.EXTRA_LARGE, 1);

        int total = weights.values().stream().mapToInt(Integer::intValue).sum();
        int roll = ThreadLocalRandom.current().nextInt(total);

        int cumulative = 0;
        for (Map.Entry<IslandSize, Integer> entry : weights.entrySet()) {
            cumulative += entry.getValue();
            if (roll < cumulative) {
                return entry.getKey();
            }
        }
        return IslandSize.SMALL;
    }
}
