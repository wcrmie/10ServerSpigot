package worm.me.commands.admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import worm.me.gamesystems.world.generation.structure.StructureSupplier;
import worm.me.gamesystems.world.generation.structure.TreeStructure;

public class PlaceTreeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {

        if (sender instanceof Player player && player.isOp()) {

            StructureSupplier.placeStructure(TreeStructure.OAK.getStructureNames().getFirst(), player.getLocation(), true);
            return true;

        }

        return false;
    }
}
