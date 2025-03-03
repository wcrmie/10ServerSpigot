package worm.me.Commands;

import com.google.common.base.Joiner;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import worm.me.server;

import java.util.Arrays;

public class shoutCommand implements CommandExecutor {

    private NamespacedKey shoutCd = new NamespacedKey("10server", "shoutcooldown");

    private final server plugin;

    public shoutCommand(server plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player){
            Player p = (Player) sender;
            if (args != null){
                PersistentDataContainer container = p.getPersistentDataContainer();
                if (container.get(shoutCd, PersistentDataType.LONG) != null && container.get(shoutCd, PersistentDataType.LONG) > System.currentTimeMillis()/1000L && !p.isOp()) {
                    plugin.pluginMessage(String.format(ChatColor.RED + "Your /shout is on cooldown for %s seconds.", container.get(shoutCd, PersistentDataType.LONG) - System.currentTimeMillis()/1000L), p);
                    return true;
                } else {
                    container.set(shoutCd, PersistentDataType.LONG, System.currentTimeMillis()/1000L + 300);
                }
                plugin.broadcast(ChatColor.GOLD + "[SHOUT] " + ChatColor.YELLOW + p.getName() + ": " + Joiner.on(" ").join(args));
                return true;
            } else return false;
        }
        return false;
    }
}
