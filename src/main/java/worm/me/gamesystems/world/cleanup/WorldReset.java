package worm.me.gamesystems.world.cleanup;

import org.bukkit.*;
import org.bukkit.entity.Player;
import worm.me.Server;
import worm.me.gamesystems.world.WorldTimer;

public class WorldReset {

    private static WorldReset instance;
    private final Server plugin;
    private final WorldResetBlocks resetBlocks;

    private boolean isBlockPlacingDisabled = false;

    private static final int GRID_SIZE = 5;
    private static final int GRID_MID  = GRID_SIZE / 2;

    public static WorldReset getInstance() {
        if (instance == null) {
            instance = new WorldReset(Server.getPlugin());
        }
        return instance;
    }

    private WorldReset(Server plugin) {
        this.plugin = plugin;
        this.resetBlocks = new WorldResetBlocks();
    }


    public void triggerReset(Player p) {
        isBlockPlacingDisabled = true;
        Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "WORLD RESET STARTING!");

        //Server.getPlugin().getTreasureChests().removeAllDisplays();

        setAllGridCells(0);
        Server.zoneGridManager.setGridValue(GRID_MID, GRID_MID, 1);

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getGameMode() != GameMode.CREATIVE) {
                player.setHealth(0);
            }
        }


        resetBlocks.startBlockReset();
    }


    public void completeReset() {
        isBlockPlacingDisabled = false;

        setAllGridCells(0);
        Server.zoneGridManager.setGridValue(GRID_MID, GRID_MID, 1);

        WorldTimer.getInstance().resetTimer();

        Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "WORLD RESET COMPLETE!");
        Bukkit.broadcastMessage(ChatColor.RED + "The border will expand every 6 hours.");
    }

    private void setAllGridCells(int value) {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                Server.zoneGridManager.setGridValue(row, col, value);
            }
        }
    }

    public boolean isBlockPlacingDisabled() {
        return isBlockPlacingDisabled;
    }
}