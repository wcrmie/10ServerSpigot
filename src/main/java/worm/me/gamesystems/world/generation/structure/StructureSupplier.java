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

        // we are not centering the tree on the y-axis that is stupid I got stuck in the center of a tree

        // .subtract() works because if the location is negative, then subtracting a negative number adds it (duh)
        Location centered = location.clone().subtract(sizeX / 2.0, 0, sizeZ / 2.0);

        Vector vector = new Vector(0, 0, 0);

        if (rotation == StructureRotation.CLOCKWISE_90) {
            vector = new Vector(sizeX, 0, 0);
        }

        if (rotation == StructureRotation.CLOCKWISE_180) {
            vector = new Vector(sizeX, 0, sizeZ);
        }

        if (rotation == StructureRotation.COUNTERCLOCKWISE_90) {
            vector = new Vector(0, 0, sizeZ);
        }

        centered.add(vector);


        if (!canFloat) {

            Palette palette = structure.getPalettes().get(structure.getPaletteCount() - 1);

            int lowest = palette.getBlocks().stream().mapToInt(BlockState::getY).min().orElseThrow();

            for (BlockState state : palette.getBlocks()) {

                if (state.getY() != lowest) continue;
                if (!state.getBlockData().getMaterial().isSolid()) continue;

                Location checkLoc = location.clone().add(state.getX(), -1, state.getZ()).add(vector);

                if (checkLoc.getBlock().getType().equals(Material.AIR)) return;
            }
        }

        // Place the structure centered at the location, with no random rotation or mirroring
        // palette 0 and integrity 1 (no weathering or anything)
        // new Random() for the random seed is irrelevant but required i think
        structure.place(centered, true, rotation, Mirror.NONE, 0, 1.0f, new Random());


    }

}
