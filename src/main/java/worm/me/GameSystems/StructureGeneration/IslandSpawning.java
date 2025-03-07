package worm.me.GameSystems.StructureGeneration;

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
    }

    private void updateBorderValues(int edgeBufferX, int edgeBufferY, int edgeBufferZ) {
        double borderSize = worldBorder.getSize();
        Location center = worldBorder.getCenter();
        xMoreThan = (int) (center.getX() - (borderSize / 2)+edgeBufferX);
        xLessThan = (int) (center.getX() + (borderSize / 2)-edgeBufferX);
        zMoreThan = (int) (center.getZ() - (borderSize / 2)+edgeBufferZ);
        zLessThan = (int) (center.getZ() + (borderSize / 2)-edgeBufferZ);
        yMoreThan = Math.max(world.getMinHeight(), 220+edgeBufferY);
        yLessThan = Math.min(world.getMaxHeight(), 280-edgeBufferY);
    }

    public Location worldBorderGetRandomBlock(int maxSizeNum) {
        Location spawnCenter = new Location(world, 0.5, 240, 0.5);
        int excludeRadius = maxSizeNum + 20;

        Location randomLocation;
        do {
            int xCord = ThreadLocalRandom.current().nextInt(xMoreThan, xLessThan);
            int yCord = ThreadLocalRandom.current().nextInt(yMoreThan, yLessThan);
            int zCord = ThreadLocalRandom.current().nextInt(zMoreThan, zLessThan);
            randomLocation = new Location(world, xCord, yCord, zCord);
        } while (randomLocation.distance(spawnCenter) < excludeRadius);

        return randomLocation;
    }

    private Location findValidLocation(int xSize, int ySize, int zSize, int maxAttempts, String successMessage, boolean vc, int maxSizeNum) {
        int attempts = 0;
        while (attempts < maxAttempts) {
            Location baseLocation = worldBorderGetRandomBlock(maxSizeNum);
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



    public Location validSpawnLocation(int xsize, int ysize, int zsize, int maxSizeNum) {
        Location location = null;
        location = findValidLocation(6*xsize, 6*ysize, 6*zsize, 10, "Estra Extra big", false, maxSizeNum);
        if (location != null) {
            return location;
        }
        location = findValidLocation(5*xsize, 5*ysize, 5*zsize,  20, "Extra big",false, maxSizeNum);
        if (location != null) {
            return location;
        }
        location = findValidLocation(4*xsize, 4*ysize, 4*zsize,  30, "Big",false, maxSizeNum);
        if (location != null) {
            return location;
        }
        location = findValidLocation(3*xsize, 3*ysize, 3*zsize,  100, "Medium",false, maxSizeNum);
        if (location != null) {
            return location;
        }
        location = findValidLocation(2*xsize, 2*ysize, 2*zsize,  500, "Small",false, maxSizeNum);
        if (location != null) {
            return location;
        }
        location = findValidLocation((int) (1.5*xsize), (int) (1.5*ysize), (int) (1.5*zsize),   2000, "enxtra Small",true, maxSizeNum);
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
        int randomRotation = ThreadLocalRandom.current().nextInt(4);
        String nameAdd = "";
        switch (randomRotation){
            case 0:
                nameAdd = "";
                break;
            case 1:
                nameAdd = "_90";
                break;
            case 2:
                nameAdd = "_180";
                break;
            case 3:
                nameAdd = "_270";
                break;
        }
        //File schematicFile = new File(plugin.getDataFolder(), "schematics/" + schematicName + nameAdd + ".schem");
        File schematicFile = new File(plugin.getDataFolder(), "schematics/" + schematicName + ".schem");
        if (!schematicFile.exists()) {
            return false;
        }
        try {
            ClipboardFormat format = ClipboardFormats.findByFile(schematicFile);
            if (format == null) {
                Bukkit.broadcastMessage("null format");
                return false;
            }
            Bukkit.broadcastMessage("not null format");
            try (ClipboardReader reader = format.getReader(new FileInputStream(schematicFile));
                 EditSession editSession = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(world))) {
                Clipboard clipboard = reader.read();
                BlockVector3 pasteLocation3 = BlockVector3.at(
                        pasteLocation.getBlockX(),
                        pasteLocation.getBlockY(),
                        pasteLocation.getBlockZ()
                );

                ClipboardHolder clipboardHolder = new ClipboardHolder(clipboard);

                Operation operation = clipboardHolder
                        .createPaste(editSession)
                        .to(pasteLocation3)
                        .ignoreAirBlocks(true)
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



        int maxSizeNum;
        if (selectedSchematic.xSize >= selectedSchematic.ySize && selectedSchematic.xSize >= selectedSchematic.zSize){
            maxSizeNum = selectedSchematic.xSize/2;
        } else if (selectedSchematic.ySize >= selectedSchematic.xSize && selectedSchematic.ySize >= selectedSchematic.zSize) {
            maxSizeNum = selectedSchematic.ySize/2;
        }else {
            maxSizeNum = selectedSchematic.zSize/2;
        }

        updateBorderValues((selectedSchematic.xSize/2), (selectedSchematic.ySize/2), (selectedSchematic.zSize/2));

        Location location = validSpawnLocation(selectedSchematic.xSize, selectedSchematic.ySize, selectedSchematic.zSize, maxSizeNum);

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
