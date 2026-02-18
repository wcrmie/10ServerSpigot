package worm.me.gamesystems.world.reset;

import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import worm.me.Server;
import worm.me.gamesystems.world.border.WorldBorderSizeHandler;
import worm.me.gamesystems.world.structure.TreasureChests;
import worm.me.utils.chat.Message;
import worm.me.utils.chat.MessageType;

import java.util.concurrent.ThreadLocalRandom;

public class WorldReset {
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

    public int worldStartingSize = 150;
    public long scoreNeededForReset = 60*60*48;


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
                    Message.sendAll(Message.create(MessageType.GENERIC, "The world is projected to reset in 1 hour!"));
                    oneHourWarn = true;
                    saveData();
                }
                if(resetScore >= (scoreNeededForReset-(60L *5/addPerSecScore)) && !fiveMinWarn){
                    Message.sendAll(Message.create(MessageType.GENERIC, "The world is projected to reset in 5 minutes!"));
                    fiveMinWarn = true;
                    saveData();
                }
                if(resetScore >= (scoreNeededForReset-(60L / addPerSecScore)) && !oneMinWarn){
                    Message.sendAll(Message.create(MessageType.GENERIC, "The world is projected to reset in 1 minute!"));
                    oneMinWarn = true;
                    saveData();
                }

                if (scoreNeededForReset <= resetScore){
                    resetScore = 0;
                    oneHourWarn = false;
                    fiveMinWarn = false;
                    oneMinWarn = false;
                    //WorldReset.getInstance().triggerReset(null);
                    resetNow = true;
                }
                saveData();

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
        Server.getPlugin().getTreasureChests().removeAllDisplays();
        worldStartingSize = ThreadLocalRandom.current().nextInt(150, 200);
        this.originalBorderSize = (int) worldBorder.getSize();
        this.originalCenter = worldBorder.getCenter().clone();


        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getGameMode() != GameMode.CREATIVE) {
                player.setHealth(0);
            }
        }

        Server.getPlugin().getWorldBorderHandler().setBlockCount(0);
        worldBorder.setSize(worldStartingSize);
        resetBlocks.startBlockReset(originalBorderSize, originalCenter);
    }

    public boolean isBlockPlacingDisabled(){
        return (isBlockPlacingDisabled);
    }

    public void completeReset() {
        isBlockPlacingDisabled = false;
        this.resetScore = 0;

        scoreNeededForReset = 60L*60*ThreadLocalRandom.current().nextInt(60, 120);
        double setExpansionPercentage = ThreadLocalRandom.current().nextInt(40, 120)/100D;
        Server.getPlugin().getWorldBorderHandler().setExpansionPercentage(setExpansionPercentage);

        Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "WORLD RESET COMPLETE!");
        Bukkit.broadcastMessage(ChatColor.RED + "Reset will be in " + (scoreNeededForReset/60/60) + " hours");
        Bukkit.broadcastMessage(ChatColor.RED + "world Starting Size is "  + worldStartingSize);
        Bukkit.broadcastMessage(ChatColor.RED + "Expansion Percentage is " + setExpansionPercentage + "%");

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