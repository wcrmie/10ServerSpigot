package worm.me.PlayerSystems.Social;

import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import worm.me.server;

public class PlayerReputation {

    private final server plugin;

    public PlayerReputation(server plugin) {
        this.plugin = plugin;
    }

    public String calcRep(Player p) {
        // Calculate player reputation based a ratio of kills to time played
        int GBVal = Math.round(255F - ((float) (1000 * p.getStatistic(Statistic.PLAYER_KILLS)) /((float) p.getStatistic(Statistic.PLAY_ONE_MINUTE) /20)));

        // Convert from RGB to hex and return

        return String.format("#%02x%02x%02x", 255, GBVal, GBVal);
    }

}
