package worm.me.gamesystems.worldreset;

import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import worm.me.Server;
import worm.me.discord.bot.BotInteractions;

public class WorldReset {
    private static WorldReset instance;
    private final Server plugin;
    private final World world;
    private final WorldBorder worldBorder;
    private long blocksUntilReset;
    private long resetTime;
    private boolean resetInProgress = false;
    private boolean blockPlacingDisabled = false;
    private WorldResetBlocks resetBlocks;
    private int originalBorderSize;
    private Location originalCenter;

    public static WorldReset getInstance() {
        if (instance == null) {
            instance = new WorldReset(Server.getPlugin());
        }
        return instance;
    }

    private WorldReset(Server plugin) {
        this.plugin = plugin;
        this.world = Bukkit.getWorld("ul_Void");
        this.worldBorder = world.getWorldBorder();
        this.resetBlocks = new WorldResetBlocks();

        loadData();
    }

    public void startResetTimer() {
        if (resetInProgress) return;

        for (Player player : Bukkit.getOnlinePlayers()) {

            Server.pluginMessage(ChatColor.RED + "The world will reset in 1 hour!", player);

        }

        resetInProgress = true;
        resetTime = System.currentTimeMillis() / 1000L + 3600;

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            triggerReset(null);
        }, 3600 * 20L);

        saveData();
    }

    public void triggerReset(Player initiator) {
        if (resetInProgress) return;

        resetInProgress = true;
        blockPlacingDisabled = true;

        this.resetTime = 0;
        this.blocksUntilReset = 0;

        this.originalBorderSize = (int) worldBorder.getSize();
        this.originalCenter = worldBorder.getCenter().clone();

        Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "WORLD RESET STARTING!");

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.getPersistentDataContainer().set(
                    new NamespacedKey(plugin, "custom_spawn"),
                    PersistentDataType.BOOLEAN,
                    false
            );
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getGameMode() != GameMode.CREATIVE) {
                player.setHealth(0);
            }
        }

        worldBorder.setSize(99);
        Server.getPlugin().getWorldBorderHandler().setBlockCount(0);
        resetBlocks.startBlockReset(originalBorderSize, originalCenter);

        BotInteractions botInteractions = new BotInteractions();
        botInteractions.announceMapReset();

        saveData();
    }

    public void completeReset() {
        blockPlacingDisabled = false;

        this.resetTime = 0;
        this.blocksUntilReset = 0;

        Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "WORLD RESET COMPLETE!");

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.getPersistentDataContainer().set(
                    new NamespacedKey(plugin, "custom_spawn"),
                    PersistentDataType.BOOLEAN,
                    true
            );
        }

        resetInProgress = false;
        saveData();
    }

    public boolean isBlockPlacingDisabled() {
        return blockPlacingDisabled;
    }

    public long getBlocksUntilReset() {
        return blocksUntilReset;
    }

    public void setBlocksUntilReset(long blocks) {
        this.blocksUntilReset = blocks;
        saveData();
    }

    public long getTimeUntilReset() {
        if (resetTime == 0) return -1;
        return resetTime - (System.currentTimeMillis() / 1000L);
    }

    public boolean isResetting() {
        return resetInProgress;
    }

    private void saveData() {
        FileConfiguration config = plugin.getConfig();
        config.set("worldReset.blocksUntilReset", blocksUntilReset);
        config.set("worldReset.resetTime", resetTime);
        config.set("worldReset.resetInProgress", resetInProgress);
        config.set("worldReset.blockPlacingDisabled", blockPlacingDisabled);
        config.set("worldReset.originalBorderSize", originalBorderSize);
        config.set("worldReset.originalCenter", originalCenter);
        plugin.saveConfig();
    }

    private void loadData() {
        FileConfiguration config = plugin.getConfig();
        blocksUntilReset = config.getLong("worldReset.blocksUntilReset", 0);
        resetTime = config.getLong("worldReset.resetTime", 0);
        resetInProgress = config.getBoolean("worldReset.resetInProgress", false);
        blockPlacingDisabled = config.getBoolean("worldReset.blockPlacingDisabled", false);
        originalBorderSize = config.getInt("worldReset.originalBorderSize", 0);
        originalCenter = config.getLocation("worldReset.originalCenter");

        if (resetInProgress) {
            completeReset();
        }
    }
}