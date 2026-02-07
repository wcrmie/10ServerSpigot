package worm.me.gamesystems.halloween;

import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Witch;
import org.bukkit.persistence.PersistentDataContainer;
import worm.me.Server;

public class FriendlyWitch {

    public static void tameWitch(Witch witch, Player tamer) {

        PersistentDataContainer data = witch.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(Server.getPlugin(), "tamed_witch");
        data.set(key, org.bukkit.persistence.PersistentDataType.STRING, tamer.getUniqueId().toString());
        witch.setTarget(null);
        witch.getLocation().getWorld().spawnParticle(Particle.HEART, witch.getLocation().getX(), witch.getLocation().getY() + 1, witch.getLocation().getZ(), 20, 0.5, 0.5, 0.5, 0.1);
        tamer.playSound(tamer, Sound.ENTITY_WITCH_CELEBRATE, 1.0f, 1.0f);

    }

    public boolean isTamed(Witch witch) {
        return witch.getPersistentDataContainer().has(new NamespacedKey(Server.getPlugin(), "tamed_witch"));
    }

    public boolean isOwner(Player player, Witch witch) {
        NamespacedKey key = new NamespacedKey(Server.getPlugin(), "tamed_witch");
        String ownerUUID = witch.getPersistentDataContainer().get(key, org.bukkit.persistence.PersistentDataType.STRING);
        return ownerUUID != null && ownerUUID.equals(player.getUniqueId().toString());
    }

    public static Player getOwner(Witch witch) {
        NamespacedKey key = new NamespacedKey(Server.getPlugin(), "tamed_witch");
        String ownerUUID = witch.getPersistentDataContainer().get(key, org.bukkit.persistence.PersistentDataType.STRING);
        if (ownerUUID != null) {
            return Server.getPlugin().getServer().getPlayer(java.util.UUID.fromString(ownerUUID));
        }
        return null;
    }

    public static void preventAggroOnOwner(Witch witch) {
        if (witch.getTarget() == getOwner(witch)) {
            witch.setTarget(null);
        }
    }
}
