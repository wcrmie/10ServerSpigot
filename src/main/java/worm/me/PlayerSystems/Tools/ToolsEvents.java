package worm.me.PlayerSystems.Tools;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemBreakEvent;
import worm.me.server;

public class ToolsEvents {

    public void toolBreak(Player p, PlayerItemBreakEvent e){
        Tools tools = new Tools();
        p.getInventory().setItemInMainHand(tools.toolDowngrade(e.getBrokenItem()));
    }

}
