package worm.me.Commands;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import worm.me.PlayerSystems.Levels.Level;
import worm.me.server;

public class GetLevel implements CommandExecutor {

    private server plugin;

    public GetLevel(server plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Level level = new Level(plugin);
        for (Player player : Bukkit.getOnlinePlayers()){
            if (strings[0].equals(player.getName())){
                if (plugin.expToLevel.containsKey(NamespacedKey.fromString(strings[1]))) {
                    NamespacedKey skillExp = NamespacedKey.fromString(strings[1]);
                    NamespacedKey skillLevel = plugin.expToLevel.get(skillExp);
                    commandSender.sendMessage(String.format("%s's %s exp is %d.", strings[0], strings[1], level.getExp(skillExp, player)));
                    commandSender.sendMessage(String.format("Their %s level is %d.", strings[1], level.getLevel(skillLevel, player)));
                    return true;
                }
                else commandSender.sendMessage("You must use one of the following keywords: mining, farming, fishing, combat, crafting, survival, smithing");
            }
        }
        return false;
    }
}
