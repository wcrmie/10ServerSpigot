package worm.me.gamesystems.world.structure;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.structure.Structure;
import org.bukkit.structure.StructureManager;
import org.bukkit.util.Vector;
import worm.me.Server;
import worm.me.gamesystems.world.structure.generation.Generate;
import worm.me.gamesystems.world.PlayArea;

import java.io.File;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


public class Island {

    private static final StructureManager structureManager = Bukkit.getStructureManager();

    public static void spawnIslandType(String typeName) {
        IslandType type;
        try {
            type = IslandType.valueOf(typeName.toUpperCase()); // Convert string to enum
        } catch (IllegalArgumentException e) {
            Server.getPlugin().getLogger().warning("[Island] Invalid island type: " + typeName.toUpperCase());
            return;
        }

        File folder = new File(Server.getPlugin().getDataFolder(), "structures");
        if (!folder.exists()) {
            Server.getPlugin().getLogger().warning("[Island] No structures folder found");
            return;
        }

        File[] files = folder.listFiles((dir, name) ->
                name.startsWith(type.name() + "_") && name.endsWith(".nbt"));

        if (files == null || files.length == 0) {
            Server.getPlugin().getLogger().warning("[Island] No structures found for type: " + type);
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
                if (f.getName().contains(size.name())) {
                    candidates.add(f);
                }
            }

            if (!candidates.isEmpty()) break;
            attempts++;
        }

        if (candidates.isEmpty()) {
            Server.getPlugin().getLogger().warning("[Island] No structures found for type: " + type + " in any size");
            return;
        }

        File chosen = candidates.get(ThreadLocalRandom.current().nextInt(candidates.size()));
        String structureName = chosen.getName().replace(".nbt", "");

        spawnIsland(structureName);
    }

    public static void forceSpawnIslandType(String typeName) {
        IslandType type;
        try {
            type = IslandType.valueOf(typeName.toUpperCase()); // Convert string to enum
        } catch (IllegalArgumentException e) {
            Server.getPlugin().getLogger().warning("[Island] Invalid island type: " + typeName.toUpperCase());
            return;
        }

        File folder = new File(Server.getPlugin().getDataFolder(), "structures");
        if (!folder.exists()) {
            Server.getPlugin().getLogger().warning("[Island] No structures folder found");
            return;
        }

        File[] files = folder.listFiles((dir, name) ->
                name.startsWith(type.name() + "_") && name.endsWith(".nbt"));

        if (files == null || files.length == 0) {
            Server.getPlugin().getLogger().warning("[Island] No structures found for type: " + type);
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
                if (f.getName().contains(size.name())) {
                    candidates.add(f);
                }
            }

            if (!candidates.isEmpty()) break;
            attempts++;
        }

        if (candidates.isEmpty()) {
            Server.getPlugin().getLogger().warning("[Island] No structures found for type: " + type + " in any size");
            return;
        }

        File chosen = candidates.get(ThreadLocalRandom.current().nextInt(candidates.size()));
        String structureName = chosen.getName().replace(".nbt", "");

        forceSpawnIsland(structureName);
    }


    public static void spawnIsland(String name) {
        try {
            File file = new File(Server.getPlugin().getDataFolder(), "structures/" + name + ".nbt");
            if (!file.exists()) {
                Server.getPlugin().getLogger().warning("[Island] Structure not found: " + name);
                return;
            }

            Structure structure = structureManager.loadStructure(file);
            if (structure == null) {
                Server.getPlugin().getLogger().warning("[Island] Failed to load: " + name);
                return;
            }

            Vector size = structure.getSize();
            int xSize = size.getBlockX();
            int ySize = size.getBlockY();
            int zSize = size.getBlockZ();

            Location loc = PlayArea.validSpawnLocation(xSize, ySize, zSize);
            if (loc == null) {
                Server.getPlugin().getLogger().warning("[Island] Could not find valid location for: " + name);
                return;
            }

            if (Generate.placeStructureStatic(loc, name)) {
                Server.getPlugin().getLogger().info("[Island] Spawned island: " + name + " at " + loc);
            } else {
                Server.getPlugin().getLogger().warning("[Island] Failed to place island: " + name);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void forceSpawnIsland(String name) {
        try {
            for (int i = 1; i <= 50 ; i++) {
                File file = new File(Server.getPlugin().getDataFolder(), "structures/" + name + ".nbt");
                if (!file.exists()) {
                    Server.getPlugin().getLogger().warning("[Island] Structure not found: " + name);
                    return;
                }

                Structure structure = structureManager.loadStructure(file);
                if (structure == null) {
                    Server.getPlugin().getLogger().warning("[Island] Failed to load: " + name);
                    return;
                }

                Vector size = structure.getSize();
                int xSize = size.getBlockX();
                int ySize = size.getBlockY();
                int zSize = size.getBlockZ();

                Location loc = PlayArea.validSpawnLocation(xSize, ySize, zSize);
                if (loc == null) {
                    World world = Bukkit.getWorld("ul_Void");
                    WorldBorder worldBorder = world.getWorldBorder();
                    worldBorder.setSize((worldBorder.getSize() + 2));
                    continue;
                }

                if (Generate.placeStructureStatic(loc, name)) {
                    Server.getPlugin().getLogger().info("[Island] Spawned island: " + name + " at " + loc);
                } else {
                    Server.getPlugin().getLogger().warning("[Island] Failed to place island: " + name);
                }
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
