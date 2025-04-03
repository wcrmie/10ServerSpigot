package worm.me.playersystems.abilities.active;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.world.Location;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientAnimation;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEntityAction;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerPosition;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDestroyEntities;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityTeleport;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnEntity;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import worm.me.Server;
import worm.me.playersystems.abilities.Abilities;
import worm.me.playersystems.data.levels.Levels;

import java.util.Optional;
import java.util.UUID;


public class AgilityDash {

    //private FakeBoat fakeBoat = null;

    public void dash(Player p){
        Vector direction = p.getLocation().getDirection();
/*
        final int[] ticks = {0};

        UUID uuid = UUID.randomUUID();

        int entityId = SpigotReflectionUtil.generateEntityId();

        FakeBoat fakeBoat = new FakeBoat(entityId, uuid);

        Location spawnLocation = SpigotConversionUtil.fromBukkitLocation(p.getLocation().add(0, 1.3, 0));
        fakeBoat.spawn(p, spawnLocation);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (ticks[0] < 21) {
                    ticks[0]++;

                    Location currentLoc = SpigotConversionUtil.fromBukkitLocation(p.getLocation().add(0, 1.3, 0));

                    WrapperPlayServerEntityTeleport teleportPacket = new WrapperPlayServerEntityTeleport(entityId, currentLoc, false);

                    PacketEvents.getAPI().getPlayerManager().sendPacket(p, teleportPacket);


                } else {

                    WrapperPlayServerDestroyEntities destroyEntitiesPacket = new WrapperPlayServerDestroyEntities(entityId);

                    PacketEvents.getAPI().getPlayerManager().sendPacket(p, destroyEntitiesPacket);

                    cancel();
                }
            }
        }.runTaskTimer(Server.getPlugin(), 0, 1);
*/
        direction.multiply(1.5);
        direction.setY(0.5);
        p.setVelocity(direction);
        Levels.addExp(5L, Levels.skillTranslation("Agility"), p);
    }
/*
    private static class FakeBoat {
        private final int entityId;
        private final UUID uuid;

        private FakeBoat(int entityId, UUID uuid) {
            this.entityId = entityId;
            this.uuid = uuid;
        }

        public void spawn(Player player, Location location){
            WrapperPlayServerSpawnEntity packet = new WrapperPlayServerSpawnEntity(
                    entityId,
                    uuid,
                    EntityTypes.SHULKER,
                    location,
                    location.getYaw(),
                    0,
                    new Vector3d(player.getVelocity().toVector3d().x, player.getVelocity().toVector3d().y, player.getVelocity().toVector3d().z)
            );
            PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
        }
    }

 */
}
