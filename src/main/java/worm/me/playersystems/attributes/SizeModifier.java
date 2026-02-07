package worm.me.playersystems.attributes;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

public class SizeModifier {

    public static void setPlayerSize(Player player, double size) {
        player.getAttribute(Attribute.SCALE).setBaseValue(size);
    }

    public static double calculateDamage(Player player, double baseDamage) {
        double size = getSize(player);
        return baseDamage * size;
    }

    public static double getSize(Player player) {
        return player.getAttribute(Attribute.SCALE).getValue();
    }

}
