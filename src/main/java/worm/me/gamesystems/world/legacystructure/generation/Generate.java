package worm.me.gamesystems.world.legacystructure.generation;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.structure.Structure;
import org.bukkit.structure.StructureManager;
import worm.me.Server;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Generate {

    private final JavaPlugin plugin;
    private final StructureManager structureManager;

    public Generate(JavaPlugin plugin) {
        this.plugin = plugin;
        this.structureManager = Bukkit.getStructureManager();
    }

    public boolean saveStructure(Location corner1, Location corner2, String name) {
        try {
            World world = corner1.getWorld();

            int x1 = corner1.getBlockX();
            int y1 = corner1.getBlockY();
            int z1 = corner1.getBlockZ();

            int x2 = corner2.getBlockX();
            int y2 = corner2.getBlockY();
            int z2 = corner2.getBlockZ();

            int minX = Math.min(x1, x2);
            int minY = Math.min(y1, y2);
            int minZ = Math.min(z1, z2);

            int maxX = Math.max(x1, x2) + 1;
            int maxY = Math.max(y1, y2) + 1;
            int maxZ = Math.max(z1, z2) + 1;

            Location min = new Location(world, minX, minY, minZ);
            Location max = new Location(world, maxX, maxY, maxZ);

            Structure structure = structureManager.createStructure();
            structure.fill(min, max, true);

            File folder = new File(plugin.getDataFolder(), "structures");
            if (!folder.exists()) folder.mkdirs();

            File file = new File(folder, name + ".nbt");
            structureManager.saveStructure(file, structure);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean placeStructure(Location location, String name) {
        try {
            File folder = new File(plugin.getDataFolder(), "structures");
            File file = new File(folder, name + ".nbt");

            if (!file.exists()) return false;

            Structure structure = structureManager.loadStructure(file);
            if (structure == null) return false;

            int sizeX = structure.getSize().getBlockX();
            int sizeY = structure.getSize().getBlockY();
            int sizeZ = structure.getSize().getBlockZ();

            Location centered = location.clone().subtract(sizeX / 2.0, sizeY / 2.0, sizeZ / 2.0);

            structure.place(centered, true,
                    StructureRotation.NONE, Mirror.NONE, 0,
                    1.0f, new Random());

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<String> listStructures() {
        File folder = new File(plugin.getDataFolder(), "structures");
        List<String> names = new ArrayList<>();
        if (!folder.exists()) return names;

        File[] files = folder.listFiles((dir, name) -> name.endsWith(".nbt"));
        if (files != null) {
            for (File f : files) {
                names.add(f.getName().replace(".nbt", ""));
            }
        }
        return names;
    }


    public static boolean placeStructureStatic(Location location, String name) {
        try {
            File folder = new File(Server.getPlugin().getDataFolder(), "structures");
            File file = new File(folder, name + ".nbt");

            if (!file.exists()) return false;

            Structure structure = Bukkit.getStructureManager().loadStructure(file);
            if (structure == null) return false;


            int sizeX = structure.getSize().getBlockX();
            int sizeY = structure.getSize().getBlockY();
            int sizeZ = structure.getSize().getBlockZ();

            Location centered = location.clone().subtract(sizeX / 2.0, sizeY / 2.0, sizeZ / 2.0);

            StructureRotation rotation = StructureRotation.values()[ThreadLocalRandom.current().nextInt(StructureRotation.values().length)];
            Mirror mirror = Mirror.values()[ThreadLocalRandom.current().nextInt(Mirror.values().length)];

            structure.place(centered, true, rotation, mirror, 0, 1.0f, ThreadLocalRandom.current());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



    public void handleMakeStructure(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("only players can use this");
            return;
        }

        if (args.length != 7) {
            sender.sendMessage("Usage: /makestructure <x1> <y1> <z1> <x2> <y2> <z2> <name>");
            return;
        }

        try {
            int x1 = Integer.parseInt(args[0]);
            int y1 = Integer.parseInt(args[1]);
            int z1 = Integer.parseInt(args[2]);
            int x2 = Integer.parseInt(args[3]);
            int y2 = Integer.parseInt(args[4]);
            int z2 = Integer.parseInt(args[5]);
            String name = args[6];

            Location loc1 = new Location(player.getWorld(), x1, y1, z1);
            Location loc2 = new Location(player.getWorld(), x2, y2, z2);

            if (saveStructure(loc1, loc2, name)) {
                player.sendMessage("saved structure: " + name);
            } else {
                player.sendMessage("failed to save structure");
            }
        } catch (NumberFormatException e) {
            sender.sendMessage("invalid coordinates");
        }
    }

    public void handlePlaceStructure(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("only players can use this");
            return;
        }

        if (args.length != 4) {
            sender.sendMessage("Usage: /placestructure <name> <x> <y> <z>");
            return;
        }

        try {
            String name = args[0];
            int x = Integer.parseInt(args[1]);
            int y = Integer.parseInt(args[2]);
            int z = Integer.parseInt(args[3]);

            Location loc = new Location(player.getWorld(), x, y, z);

            if (placeStructure(loc, name)) {
                player.sendMessage("Placed structure: " + name);
            } else {
                player.sendMessage("Failed to place structure");
            }
        } catch (NumberFormatException e) {
            sender.sendMessage("Invalid coordinates");
        }
    }

    public void handleListStructures(CommandSender sender) {
        List<String> structures = listStructures();
        if (structures.isEmpty()) {
            sender.sendMessage("no structures found.");
        } else {
            sender.sendMessage("Saved structures: " + String.join(", ", structures));
        }
    }

    public void handleRemoveStructure(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return;
        }

        if (args.length != 1) {
            player.sendMessage("Usage: /structuresremove <name>");
            return;
        }

        String name = args[0];
        File folder = new File(plugin.getDataFolder(), "structures");
        File file = new File(folder, name + ".nbt");

        if (!file.exists()) {
            player.sendMessage("No structure found with that name.");
            return;
        }

        if (file.delete()) {
            player.sendMessage("Removed structure: " + name);
        } else {
            player.sendMessage("Failed to delete structure file.");
        }
    }
}
