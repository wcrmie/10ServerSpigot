package worm.me.gamesystems.fishing;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.InteractionHand;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.world.Location;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerInput;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientUseItem;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDestroyEntities;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityTeleport;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnEntity;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import worm.me.Server;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

public class FishingSystem implements PacketListener {
/*
    private FakeArmorStand fakeArmorStand = null;

    @Override
    public void onPacketReceive(PacketReceiveEvent e) {
        User user = e.getUser();
        if (e.getPacketType() != PacketType.Play.Client.INTERACT_ENTITY) {
            return;
        }

        WrapperPlayClientInteractEntity packet = new WrapperPlayClientInteractEntity(e);

        int entityId = packet.getEntityId();

        if (entityId  == fakeArmorStand.entityId) {
            UUID uuid = user.getUUID();
            fakeArmorStand.clicks.put(uuid, fakeArmorStand.clicks.getOrDefault(uuid, 0) + 1);
        }

        if (fakeArmorStand.clicks.get(user.getUUID()) >= 15) {
            fakeArmorStand.clicks.remove(user.getUUID());

        }
    }

    public org.bukkit.Location generateParticleLocation(FishHook hook, Player player) {

        org.bukkit.Location hookLoc = hook.getLocation();
        org.bukkit.Location playerLoc = player.getLocation();

        Vector direction = playerLoc.toVector().subtract(hookLoc.toVector());

        double xRNG = ThreadLocalRandom.current().nextDouble(-90, 90);
        double yRNG = ThreadLocalRandom.current().nextDouble(-20, 0);

        double yaw = Math.toDegrees(Math.atan2(direction.getZ(), direction.getX()));

        yaw += xRNG;

        playerLoc.setYaw((float) yaw);
        playerLoc.setPitch((float) yRNG);

        direction = playerLoc.getDirection();
        direction.multiply(3);

        playerLoc.setPitch(0);
        playerLoc.setYaw(0);

        playerLoc.add(direction);

        return playerLoc;
    }

    public void playerFishing(PlayerFishEvent e) {

        FakeArmorStand fakeArmorStand = new FakeArmorStand(UUID.randomUUID(), SpigotReflectionUtil.generateEntityId());

        org.bukkit.Location loc = generateParticleLocation(e.getHook(), e.getPlayer());

        fakeArmorStand.spawn(e.getPlayer(), SpigotConversionUtil.fromBukkitLocation(loc));

        for (int i = 0; i < 15; i++) {

            e.getHook().setMinWaitTime(600);

            loc = generateParticleLocation(e.getHook(), e.getPlayer());

            PacketEvents.getAPI().getPlayerManager().sendPacket(e.getPlayer(), new WrapperPlayServerEntityTeleport(fakeArmorStand.entityId, SpigotConversionUtil.fromBukkitLocation(loc), false));


            int finalI = i;

            int[] loop = new int[1];

            org.bukkit.Location finalLoc = loc;
            new BukkitRunnable() {
                @Override
                public void run() {

                    if (Objects.requireNonNullElse(fakeArmorStand.clicks.get(e.getPlayer().getUniqueId()), 0) == loop[0]) {
                        finalLoc.getWorld().spawnParticle(Particle.CRIT, finalLoc, 25);
                    } else {
                        cancel();
                    }

                    e.getHook().setMinWaitTime(600);

                    loop[0]++;

                    if (loop[0] >= 60) {
                        cancel();
                    }

                }
            }.runTaskTimer(Server.getPlugin(), 1L, 1L);

            if (Objects.requireNonNullElse(fakeArmorStand.clicks.get(e.getPlayer().getUniqueId()), 0) >= 15) {

                e.getHook().setMinWaitTime(1);
                e.getHook().setMaxWaitTime(1);
                e.getHook().setLureTime(1, 1);
                break;
            }

            if (Objects.requireNonNullElse(fakeArmorStand.clicks.get(e.getPlayer().getUniqueId()), 0) == loop[0]) {
                new BukkitRunnable() {
                    @Override
                    public void run() {

                        if (Objects.requireNonNullElse(fakeArmorStand.clicks.get(e.getPlayer().getUniqueId()), 0) == finalI) {
                            Server.pluginMessage(ChatColor.RED + "It got away!", e.getPlayer());

                            int RNG = ThreadLocalRandom.current().nextInt(0, 2);

                            if (RNG == 0) {
                                e.getPlayer().playSound(e.getPlayer(), Sound.ITEM_GOAT_HORN_SOUND_3, 100, 2);
                            } else if (RNG == 1) {
                                e.getPlayer().playSound(e.getPlayer(), Sound.ITEM_GOAT_HORN_SOUND_6, 100, 2);
                            } else {
                                e.getPlayer().playSound(e.getPlayer(), Sound.ITEM_GOAT_HORN_SOUND_7, 100, 2);
                            }

                            WrapperPlayServerDestroyEntities destroyEntitiesPacket = new WrapperPlayServerDestroyEntities(fakeArmorStand.entityId);

                            PacketEvents.getAPI().getPlayerManager().sendPacket(e.getPlayer(), destroyEntitiesPacket);

                            Server.pluginMessage("bleehh", e.getPlayer());
                        }
                    }

                }.runTaskLater(Server.getPlugin(), 60L);

            }

        }
    }

    private static class FakeArmorStand {
        private final int entityId;
        private final UUID uuid;
        // Track their clicks
        private final Map<UUID, Integer> clicks = new ConcurrentHashMap<>();

        public FakeArmorStand(UUID uuid, int entityId) {
            this.uuid = uuid;
            this.entityId = entityId;
        }

        public void spawn(Player p, Location location) {
            WrapperPlayServerSpawnEntity packet = new WrapperPlayServerSpawnEntity(
                    entityId,
                    uuid,
                    EntityTypes.ARMOR_STAND,
                    location,
                    location.getYaw(), // Head yaw
                    0, // No additional data
                    null // We won't specify any initial velocity
            );
            PacketEvents.getAPI().getPlayerManager().sendPacket(p, packet);
        }
    }

*/
}
