package worm.me.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import worm.me.Server;
import worm.me.gamesystems.worldreset.WorldReset;
import worm.me.playersystems.data.VoidPlayer;

public class WorldListener implements Listener {

    private final Server plugin;


    public WorldListener(Server plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (WorldReset.getInstance().isBlockPlacingDisabled()) {
            event.setCancelled(true);
            return;
        }

        plugin.getWorldBorderHandler().incrementBlockCount(event);
        if (event.getPlayer() != null) {
            VoidPlayer.registerStats(event.getPlayer(), 0);
        }
    }

    @EventHandler
    public void onFlow(BlockFromToEvent event) {

        if (event.getToBlock().getLocation().getBlockY() > 210) return;

        if (event.getBlock().getType() == Material.WATER || event.getBlock().getType() == Material.LAVA) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        plugin.getWorldBorderHandler().decrementBlockCount(event);
        if (event.getPlayer() != null) {
            VoidPlayer.registerStats(event.getPlayer(), 1);
        }
    }


}
