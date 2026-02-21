package worm.me.gamesystems.world.generation.structure;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.structure.Palette;
import org.bukkit.structure.Structure;
import org.bukkit.structure.StructureManager;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;
import worm.me.Server;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class StructureSupplier {

    /**
     * Creates a structure based on the provided structure name.
     * @param structureName The name of the structure to create.
     * @return The created Structure object, or null if creation failed.
     */
    private static Structure createStructure(String structureName) {

        try {

            // This folder should be inside the plugin's data folder
            File folder = new File(Server.getPlugin().getDataFolder(), "structures");
            File structureFile = new File(folder, structureName + ".nbt");

            if (!structureFile.exists()) return null;

            return Bukkit.getStructureManager().loadStructure(structureFile);

        } catch (Exception e) {

            Server.getPlugin().getLogger().warning(e.getMessage());
            return null;

        }
    }

    /**
     * Places a structure at the specified location.
     * @param structureName The name of the structure to place.
     * @param location The location to place the structure at.
     */
    public static void placeStructure(String structureName, Location location) {
        Server plugin = Server.getPlugin();
        StructureManager manager = Bukkit.getStructureManager();

        File folder = new File(plugin.getDataFolder(), "structures");
        if (!folder.exists()) {
            folder.mkdirs();
        }

        File structureFile = new File(folder, structureName + ".nbt");

        if (!structureFile.exists()) {
            plugin.getLogger().warning("Structure file missing: " + structureFile.getName());
            return;
        }

        try {
            Structure structure = manager.loadStructure(structureFile);

            if (structure == null) {
                plugin.getLogger().warning("Could not load NBT data for: " + structureName);
                return;
            }

            BlockVector size = structure.getSize();


            int xOffset = size.getBlockX() / 2;
            int zOffset = size.getBlockZ() / 2;

            Location origin = location.clone().subtract(xOffset, 0, zOffset);

            structure.place(origin, true, StructureRotation.NONE, Mirror.NONE, 0, 1.0f, new Random());

        } catch (IOException e) {
            plugin.getLogger().severe("IO Error loading structure: " + e.getMessage());
        } catch (Exception e) {
            plugin.getLogger().severe("Unexpected error during structure placement: " + e.getMessage());
        }
    }

    public static boolean isEdge(Block block) {

        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {

                Location location = block.getLocation().clone().add(x, 0, z);
                if (!location.getBlock().getType().isSolid()) return true;

            }
        }
        return false;
    }

}
