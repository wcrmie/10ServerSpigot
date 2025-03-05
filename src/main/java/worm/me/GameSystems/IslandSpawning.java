package worm.me.GameSystems;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.transform.AffineTransform;
import com.sk89q.worldedit.session.ClipboardHolder;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;
import worm.me.Server;
import worm.me.WorldBorder.WorldBorderSizeHandler;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;



public class IslandSpawning {

    public static class SchematicInfo {
        String name;
        int xSize;
        int ySize;
        int zSize;
        int blockCount;

        public SchematicInfo(String name, int xSize, int ySize, int zSize, int blockCount) {
            this.name = name;
            this.xSize = xSize;
            this.ySize = ySize;
            this.zSize = zSize;
            this.blockCount = blockCount;
        }
    }

    private final JavaPlugin plugin;
    private final World world;
    private final WorldBorder worldBorder;
    private int xMoreThan, xLessThan, yMoreThan, yLessThan, zMoreThan, zLessThan;
    private static final Map<Location, Boolean> blocksToRemove = new HashMap<>();
    private final WorldBorderSizeHandler worldBorderSizeHandler;

    public IslandSpawning(Server plugin) {
        this.plugin = plugin;
        this.world = Bukkit.getWorld("ul_Void");
        this.worldBorder = world.getWorldBorder();
        this.worldBorderSizeHandler = new WorldBorderSizeHandler(plugin);
        updateBorderValues();
    }

    private void updateBorderValues() {
        double borderSize = worldBorder.getSize();
        Location center = worldBorder.getCenter();
        int edgeBuffer = 10;
        int upDownBuffer = 8;
        xMoreThan = (int) (center.getX() - (borderSize / 2)+edgeBuffer);
        xLessThan = (int) (center.getX() + (borderSize / 2)-edgeBuffer);
        zMoreThan = (int) (center.getZ() - (borderSize / 2)+edgeBuffer);
        zLessThan = (int) (center.getZ() + (borderSize / 2)-edgeBuffer);
        yMoreThan = Math.max(world.getMinHeight(), 220+upDownBuffer);
        yLessThan = Math.min(world.getMaxHeight(), 280-upDownBuffer);
    }

    public Location worldBorderGetRandomBlock() {
        Location spawnCenter = new Location(world, 0.5, 240, 0.5);
        int excludeRadius = 20;

        Location randomLocation;
        do {
            int xCord = ThreadLocalRandom.current().nextInt(xMoreThan, xLessThan);
            int yCord = ThreadLocalRandom.current().nextInt(yMoreThan, yLessThan);
            int zCord = ThreadLocalRandom.current().nextInt(zMoreThan, zLessThan);
            randomLocation = new Location(world, xCord, yCord, zCord);
        } while (randomLocation.distance(spawnCenter) < excludeRadius);

        return randomLocation;
    }

    private Location findValidLocation(int xSize, int ySize, int zSize, int maxAttempts, String successMessage, boolean vc) {
        int attempts = 0;
        while (attempts < maxAttempts) {
            Location baseLocation = worldBorderGetRandomBlock();
            int baseX = baseLocation.getBlockX();
            int baseY = baseLocation.getBlockY();
            int baseZ = baseLocation.getBlockZ();
            boolean isValid = true;
            for (int x = baseX - xSize; x <= baseX + xSize && isValid; x++) {
                for (int y = baseY - ySize; y <= baseY + ySize && isValid; y++) {
                    for (int z = baseZ - zSize; z <= baseZ + zSize && isValid; z++) {
                        if (y < world.getMinHeight() || y > world.getMaxHeight()) {
                            continue;
                        }
                        if (!isAir(x, y, z)) {
                            isValid = false;
                            break;
                        }
                    }
                }
            }
            if (!isValid) {
                attempts++;
                continue;
            }
            if (!vc) {
                for (int y = baseY - 1; y >= Math.max(baseY - 100, world.getMinHeight()) && isValid; y--) {
                    if (!isAir(baseX, y, baseZ)) {
                        isValid = false;
                        break;
                    }
                }
                for (int y = baseY + 1; y <= Math.min(baseY + 100, world.getMaxHeight()) && isValid; y++) {
                    if (!isAir(baseX, y, baseZ)) {
                        isValid = false;
                        break;
                    }
                }
                if (!isValid) {
                    attempts++;
                    continue;
                }
            }
            return new Location(world, baseX, baseY, baseZ);
        }
        return null;
    }

    public Location validSpawnLocation(int xsize, int ysize, int zsize) {
        Location location = null;
        location = findValidLocation(6*xsize, 6*ysize, 6*zsize, 10, "Estra Extra big", false);
        if (location != null) {
            return location;
        }
        location = findValidLocation(5*xsize, 5*ysize, 5*zsize,  20, "Extra big",false);
        if (location != null) {
            return location;
        }
        location = findValidLocation(4*xsize, 4*ysize, 4*zsize,  30, "Big",false);
        if (location != null) {
            return location;
        }
        location = findValidLocation(3*xsize, 3*ysize, 3*zsize,  100, "Medium",false);
        if (location != null) {
            return location;
        }
        location = findValidLocation(2*xsize, 2*ysize, 2*zsize,  500, "Small",false);
        if (location != null) {
            return location;
        }
        location = findValidLocation((int) (1.5*xsize), (int) (1.5*ysize), (int) (1.5*zsize),   2000, "enxtra Small",true);
        if (location != null) {
            return location;
        }
        return null;
    }

    private boolean isAir(int x, int y, int z) {
        if (y < world.getMinHeight() || y > world.getMaxHeight()) {
            return true;
        }
        Block block = world.getBlockAt(x, y, z);
        Material type = block.getType();
        return type == Material.AIR ||
                type == Material.CAVE_AIR ||
                type == Material.VOID_AIR;
    }

    public boolean pasteSchematic(String schematicName, Location pasteLocation) {
        File schematicFile = new File(plugin.getDataFolder(), "schematics/" + schematicName + ".schem");
        if (!schematicFile.exists()) {
            return false;
        }
        try {
            ClipboardFormat format = ClipboardFormats.findByFile(schematicFile);
            if (format == null) {
                return false;
            }
            try (ClipboardReader reader = format.getReader(new FileInputStream(schematicFile));
                 EditSession editSession = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(world))) {
                Clipboard clipboard = reader.read();
                BlockVector3 pasteLocation3 = BlockVector3.at(
                        pasteLocation.getBlockX(),
                        pasteLocation.getBlockY(),
                        pasteLocation.getBlockZ()
                );

                int randomRotation = ThreadLocalRandom.current().nextInt(4) * 90;

                ClipboardHolder clipboardHolder = new ClipboardHolder(clipboard);
                clipboardHolder.setTransform(new AffineTransform().rotateY(randomRotation));

                Operation operation = clipboardHolder
                        .createPaste(editSession)
                        .to(pasteLocation3)
                        .ignoreAirBlocks(false)
                        .build();

                Operations.complete(operation);
                editSession.close();
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public void IslandSpawn(String islandName) {
        updateBorderValues();

        SchematicInfo selectedSchematic = null;

        if (islandName != null) {
            for (SchematicInfo schematic : SCHEMATICS) {
                if (schematic.name.equalsIgnoreCase(islandName)) {
                    selectedSchematic = schematic;
                    break;
                }
            }
        }

        if (selectedSchematic == null) {
            selectedSchematic = SCHEMATICS.get(
                    ThreadLocalRandom.current().nextInt(SCHEMATICS.size())
            );
        }

        Location location = validSpawnLocation(
                selectedSchematic.xSize,
                selectedSchematic.ySize,
                selectedSchematic.zSize
        );

        if (location != null) {
            pasteSchematic(selectedSchematic.name, location);
            worldBorderSizeHandler.addBlockCount(selectedSchematic.blockCount);
        }
    }

    private static final List<SchematicInfo> SCHEMATICS = new ArrayList<>() {{
        add(new SchematicInfo("Island1", 5, 6, 5,56));
        add(new SchematicInfo("Island2", 9, 12, 9,206));
        add(new SchematicInfo("Island3", 18, 14, 18,632));
    }};


}
