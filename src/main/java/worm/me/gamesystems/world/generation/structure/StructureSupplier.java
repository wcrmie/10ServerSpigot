package worm.me.gamesystems.world.generation.structure;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.structure.Palette;
import org.bukkit.structure.Structure;
import org.bukkit.util.Vector;
import worm.me.Server;

import java.io.File;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

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

    public static int[] placementOffset(StructureRotation rotation, int structWidth, int structDepth) {
        return switch (rotation) {
            case NONE   -> new int[]{ 0,      0      };
            case CLOCKWISE_90  -> new int[]{ structDepth - 1, 0      };
            case CLOCKWISE_180 -> new int[]{ structWidth - 1, structDepth - 1 };
            case COUNTERCLOCKWISE_90 -> new int[]{ 0,      structWidth - 1 };
        };
    }

    /**
     * Places a structure at the specified location.
     * @param structureName The name of the structure to place.
     * @param location The location to place the structure at.
     */
    public static void placeStructure(String structureName, Location location, boolean canFloat) {

        Structure structure = createStructure(structureName);

        if (structure == null) {
            Server.getPlugin().getLogger().warning("Failed to create structure: " + structureName);
            return;
        }

        // Thank you clem
        int sizeX = structure.getSize().getBlockX();
        int sizeZ = structure.getSize().getBlockZ();

        StructureRotation rotation = StructureRotation.values()[ThreadLocalRandom.current().nextInt(0, StructureRotation.values().length)];

        int[] offset = placementOffset(rotation, sizeX, sizeZ);

        Location centered = location.clone().add(offset[0] - sizeX/2, 0, offset[1] - sizeZ/2);

        if (!canFloat) {

            Palette palette = structure.getPalettes().get(structure.getPaletteCount() - 1);

            int lowest = palette.getBlocks().stream().mapToInt(BlockState::getY).min().orElseThrow();

            for (BlockState state : palette.getBlocks()) {

                if (state.getY() != lowest) continue;
                if (!state.getBlockData().getMaterial().isSolid()) continue;

                Location checkLoc = centered.clone().add(state.getX(), -1, state.getZ());

                if (checkLoc.getBlock().getType().equals(Material.AIR)) {
                    Server.getPlugin().getLogger().warning("Placement at " + centered + " canceled due to air block at " + checkLoc);
                    return;
                }
            }
        }

        // Place the structure centered at the location, with no random rotation or mirroring
        // palette 0 and integrity 1 (no weathering or anything)
        // new Random() for the random seed is irrelevant but required i think
        structure.place(centered, true, rotation, Mirror.NONE, 0, 1.0f, new Random());


    }

}
