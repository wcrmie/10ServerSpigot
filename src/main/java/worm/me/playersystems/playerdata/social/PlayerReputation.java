package worm.me.PlayerSystems.PlayerData.Social;

import org.bukkit.NamespacedKey;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import worm.me.Server;

import java.util.Objects;

public class PlayerReputation {

    private final Server plugin;

    public PlayerReputation(Server plugin) {
        this.plugin = plugin;
    }

    public String calcRep(Player p) {
        // Calculate player reputation based a ratio of kills to time played during a session.

        PersistentDataContainer container = p.getPersistentDataContainer();

        int GBVal = Math.round(255F - ((float) (6000 * Objects.requireNonNull(container.get(new NamespacedKey(Server.getPlugin(), "repsess"), PersistentDataType.LONG_ARRAY))[1]) / ((float) Objects.requireNonNull(container.get(new NamespacedKey(Server.getPlugin(), "repsess"), PersistentDataType.LONG_ARRAY))[0]) /20));
        if (GBVal < 0) GBVal = 0;

        // Convert from RGB to hex and return

        return String.format("#%02x%02x%02x", 255, GBVal, GBVal);
    }

}
