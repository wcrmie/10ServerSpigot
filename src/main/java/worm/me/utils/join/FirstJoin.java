package worm.me.utils.join;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public class FirstJoin {

    private static List<String> gameInfo = List.of(ChatColor.GRAY + "Welcome to 10!", ChatColor.DARK_GRAY + ">---===========---<", ChatColor.GRAY + "You can " + ChatColor.GOLD + "upgrade " + ChatColor.GRAY + "your tools in your inventory! Hover over them to see how.", ChatColor.GRAY + "This gamemode has some custom recipes, viewable with" + ChatColor.YELLOW + " /recipes.", ChatColor.GRAY + "There are also skill levels, which can be " + ChatColor.GOLD + "upgraded" + ChatColor.GRAY + " by performing certain actions.", ChatColor.GRAY + "As you level your skills, different abilities and item generators will be unlocked. These and more can be accessed through " + ChatColor.GOLD + "/menu.", ChatColor.GRAY + "For more information about the gamemode, and to connect with the community, use " + ChatColor.BLUE + "/discord.", ChatColor.DARK_GRAY + ">---===========---<");

    public static void firstJoin(Player p) {

        sendInfo(p);

    }

    public static void sendInfo(Player p) {

        for (String s : gameInfo) {
            p.sendMessage(s);
        }

    }

}
