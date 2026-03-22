package worm.me.commands.admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import worm.me.gamesystems.world.cleanup.SpawnReset;

public class ResetSpawnCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {

        if (sender.isOp()) {

            SpawnReset.replaceSpawn();
            return true;

        }

        return false;
    }
}
