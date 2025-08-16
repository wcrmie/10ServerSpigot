package worm.me.gamesystems.worldreset;

import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import worm.me.Server;
import worm.me.discord.bot.BotInteractions;

public class WorldReset {
    private long scoreNeededForReset = 60*60*48;
    private static WorldReset instance;
    private final Server plugin;
    private final World world;
    private final WorldBorder worldBorder;
    private float resetScore;
    private boolean isBlockPlacingDisabled = false;
    private WorldResetBlocks resetBlocks;
    private int originalBorderSize;
    private Location originalCenter;
    private float addPerSecScore = 1;
    private boolean oneHourWarn = false;
    private boolean fiveMinWarn = false;
    private boolean oneMinWarn = false;
    private boolean resetNow = false;


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

    public float projectedTimeTillReset(){
        float projectedTimeTillReset = (scoreNeededForReset - resetScore)/addPerSecScore;
        return projectedTimeTillReset;
    }

    public float resetScore(){
        float presetScore = resetScore;
        return presetScore;
    }

    public float scoreNeededForReset(){
        float pscoreNeededForReset = scoreNeededForReset;
        return pscoreNeededForReset;
    }


    public void scoreAdder(){
        loadData();

        Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
            @Override
            public void run() {
                resetScore += addPerSecScore;

                if(resetScore >= (scoreNeededForReset-(60L *60/addPerSecScore)) && !oneHourWarn){
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        Server.pluginMessage(ChatColor.RED + "The world is projected to reset in 1 hour!", player);
                        oneHourWarn = true;
                        saveData();
                    }
                }
                if(resetScore >= (scoreNeededForReset-(60L *5/addPerSecScore)) && !fiveMinWarn){
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        Server.pluginMessage(ChatColor.RED + "The world is projected to reset in 5 minute!", player);
                        fiveMinWarn = true;
                        saveData();
                    }
                }
                if(resetScore >= (scoreNeededForReset-(60L / addPerSecScore)) && !oneMinWarn){
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        Server.pluginMessage(ChatColor.RED + "The world is projected to reset in 1 minute!", player);
                        oneMinWarn = true;
                        saveData();
                    }
                }

                if (scoreNeededForReset <= resetScore && !resetNow){
                    WorldReset.getInstance().triggerReset(null);
                    resetNow = true;
                    saveData();
                }

            }
        }, 0L, 20L);

        Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
            @Override
            public void run() {
                saveData();
            }
        }, 0L, 15*60*20L);

    }


    public void triggerReset(Player p){
        isBlockPlacingDisabled = true;
        Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "WORLD RESET STARTING!");
        this.originalBorderSize = (int) worldBorder.getSize();
        this.originalCenter = worldBorder.getCenter().clone();


        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getGameMode() != GameMode.CREATIVE) {
                player.setHealth(0);
            }
        }

        Server.getPlugin().getWorldBorderHandler().setBlockCount(0);
        worldBorder.setSize(99);
        resetBlocks.startBlockReset(originalBorderSize, originalCenter);
    }

    public boolean isBlockPlacingDisabled(){
        return (isBlockPlacingDisabled);
    }

    public void completeReset() {
        isBlockPlacingDisabled = false;
        this.resetScore = 0;

        Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "WORLD RESET COMPLETE!");

        oneHourWarn = false;
        fiveMinWarn = false;
        oneMinWarn = false;
        resetNow = false;

        saveData();
    }

    public void saveData() {
        FileConfiguration config = plugin.getConfig();
        config.set("worldReset.resetTime", resetScore);
        config.set("worldReset.oneHourWarn", oneHourWarn);
        config.set("worldReset.fiveMinWarn", fiveMinWarn);
        config.set("worldReset.oneMinWarn", oneMinWarn);
        config.set("worldReset.resetNow", resetNow);
        plugin.saveConfig();
    }

    public void loadData() {
        FileConfiguration config = plugin.getConfig();
        resetScore = config.getLong("worldReset.resetTime", 0);
        oneHourWarn = config.getBoolean("worldReset.oneHourWarn", false);
        fiveMinWarn = config.getBoolean("worldReset.fiveMinWarn", false);
        oneMinWarn = config.getBoolean("worldReset.oneMinWarn", false);
        resetNow = config.getBoolean("worldReset.resetNow", false);
    }
}