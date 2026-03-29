package worm.me.commands.admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import worm.me.gamesystems.world.mob.boss.ZiglinJouster;

public class SpawnPiglinJousterBossCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can run this command.");
            return true;
        }

        ZiglinJouster.spawnAt(player.getLocation());
        player.sendMessage("Ziglin Jouster spawned.");
        return true;
    }
}