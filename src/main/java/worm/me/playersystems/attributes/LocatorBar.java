package worm.me.playersystems.attributes;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class LocatorBar {

    private static final HashMap<UUID, ArmorStand> anchors = new HashMap<>();

    public static void attachVillager(Villager villager) {
        AttributeInstance range = villager.getAttribute(Attribute.WAYPOINT_TRANSMIT_RANGE);
        if (range != null) {
            range.setBaseValue(99999);
        }
        villager.setWaypointColor(Color.GREEN);
    }

    public static void attachPlayer(Player player) {
        AttributeInstance range = player.getAttribute(Attribute.WAYPOINT_TRANSMIT_RANGE);
        if (range != null) {
            range.setBaseValue(99999);
        }
        player.setWaypointColor(Color.WHITE);
    }

    public static void handlePlayerMovement(Player player) {
        ItemStack helm = player.getInventory().getHelmet();

        boolean isWearingHead = helm != null && (helm.getType().name().contains("_HEAD") || helm.getType() == Material.CARVED_PUMPKIN);

        boolean shouldHaveAnchor = isWearingHead && !player.isSneaking() && !player.isInvisible();

        if (shouldHaveAnchor) {
            updateAnchor(player);
        } else {
            removeAnchor(player);
        }
    }

    private static void updateAnchor(Player player) {
        ArmorStand stand = anchors.get(player.getUniqueId());

        if (stand == null || !stand.isValid()) {
            stand = (ArmorStand) player.getWorld().spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);

            stand.setVisible(false);
            stand.setMarker(true);
            stand.setPersistent(false);
            stand.setGravity(false);

            AttributeInstance range = stand.getAttribute(Attribute.WAYPOINT_TRANSMIT_RANGE);
            if (range != null) range.setBaseValue(99999);
            stand.setWaypointColor(Color.WHITE);

            anchors.put(player.getUniqueId(), stand);
        } else {
            stand.teleport(player.getLocation());
        }
    }

    public static void removeAnchor(Player player) {
        ArmorStand stand = anchors.remove(player.getUniqueId());
        if (stand != null) {
            stand.remove();
        }
    }

    public static void cleanup() {
        for (ArmorStand stand : anchors.values()) {
            if (stand != null) {
                stand.remove();
            }
        }
        anchors.clear();
    }
}