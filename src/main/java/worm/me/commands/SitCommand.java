package worm.me.commands;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import worm.me.Server;
import worm.me.utils.chat.Message;
import worm.me.utils.chat.MessageType;

public class SitCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player p){
            Location loc = p.getLocation();
            loc.setY(loc.getY() - 1);
            if (!loc.getBlock().getType().isAir()){
                loc.setY(loc.getY() + 1.25);
                ArmorStand a = loc.getWorld().spawn(loc, ArmorStand.class);
                a.setGravity(false);
                a.setInvulnerable(true);
                a.setInvisible(true);
                a.setMarker(true);
                a.addPassenger(p);
            } else {
                p.sendMessage(Message.create(MessageType.FAIL, "You can't sit on air!"));
            }
            return true;
        } else {
            sender.sendMessage("The console can't sit, dummy!");
            return false;
        }
    }
}
