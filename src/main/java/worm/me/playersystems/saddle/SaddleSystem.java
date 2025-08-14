package worm.me.playersystems.saddle;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.units.qual.N;
import worm.me.Server;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class SaddleSystem {
    private final JavaPlugin plugin;
    private final List<EntityType> pickUpAbleEntities;
    private final List<EntityType> specialEffectEntities;

    public SaddleSystem(JavaPlugin plugin) {
        this.plugin = plugin;
        this.pickUpAbleEntities = Arrays.asList(
                EntityType.BAT,
                EntityType.CHICKEN,
                EntityType.PIG,
                EntityType.CREEPER,
                EntityType.PARROT,
                EntityType.FROG,
                EntityType.SQUID,
                EntityType.COD,
                EntityType.SALMON,
                EntityType.PUFFERFISH,
                EntityType.TROPICAL_FISH,
                EntityType.GLOW_SQUID,
                EntityType.DOLPHIN,
                EntityType.AXOLOTL,
                EntityType.CAT,
                EntityType.CAVE_SPIDER,
                EntityType.SPIDER,
                EntityType.WOLF,
                EntityType.FOX,
                EntityType.OCELOT,
                EntityType.RABBIT,
                EntityType.ARMADILLO,
                EntityType.TADPOLE,
                EntityType.TURTLE,
                EntityType.ENDERMITE,
                EntityType.SILVERFISH,
                EntityType.BEE,
                EntityType.RABBIT,
                EntityType.ALLAY,
                EntityType.VEX,
                EntityType.PHANTOM,
                EntityType.SNOW_GOLEM,
                EntityType.GIANT
        );
        this.specialEffectEntities = Arrays.asList(
                EntityType.BAT,
                EntityType.CHICKEN,
                EntityType.CREEPER,
                EntityType.RABBIT,
                EntityType.TURTLE
        );
    }

    public void rightClick(PlayerInteractEntityEvent event) {

            Entity clickedEntity = event.getRightClicked();
            Player player = event.getPlayer();

            if (clickedEntity instanceof Player clickedPlayer) {
                ItemStack helmet = clickedPlayer.getInventory().getHelmet();
                if (helmet != null && helmet.getType() == Material.SADDLE) {
                    getOnPlayer(player, clickedPlayer);
                    return;
                }
            }

            EntityType entityType = clickedEntity.getType();
            if (pickUpAbleEntities.contains(entityType) && clickedEntity.getPassengers().isEmpty()) {
                ItemStack helmet = player.getInventory().getHelmet();
                if (helmet != null
                        && helmet.getType() == Material.SADDLE
                        && player.getInventory().getItemInMainHand().getType() == Material.AIR
                        && player.getInventory().getItemInOffHand().getType() == Material.AIR) {

                    if (clickedEntity instanceof Creeper creeper) {
                        if (creeper.isIgnited()) creeper.setIgnited(false);
                        creeper.setTarget(null);
                    }

                    player.addPassenger(clickedEntity);
                    if (specialEffectEntities.contains(entityType)) {
                        applySpecialEffects(clickedEntity, player, true);
                    }
                }
            }

            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!player.getPassengers().isEmpty() && specialEffectEntities.contains(player.getPassengers().getFirst().getType())) {
                        applySpecialEffects(player.getPassengers().getFirst(), player, true);
                    } else cancel();
                }
            }.runTaskTimer(plugin, 1L, 1L);
    }

    public void leftClick(Player thrower, Player rider) {
        throwEntity(thrower, rider);
    }

    private void applySpecialEffects(Entity entity, Player player, boolean apply) {
        switch (entity.getType()) {
            case BAT:
                if (apply) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 2, 1 , false, false, true));
                } else {
                    player.removePotionEffect(PotionEffectType.LEVITATION);
                }
                break;

            case CHICKEN:
                if (apply) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 2, 1, false, false, true));
                } else {
                    player.removePotionEffect(PotionEffectType.SLOW_FALLING);
                }
                break;

            case RABBIT:
                if (apply) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, 2, 1, false, false, true));
                } else {
                    player.removePotionEffect(PotionEffectType.JUMP_BOOST);
                }
                break;

            case TURTLE:
                if (apply) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 2, 0, false, false, true));
                } else {
                    player.removePotionEffect(PotionEffectType.WATER_BREATHING);
                }
                break;
        }
    }

    private void getOnPlayer(Player rider, Player clickedPlayer) {

        PersistentDataContainer container = clickedPlayer.getPersistentDataContainer();
        if (container.has(new NamespacedKey(Server.getPlugin(), "saddleThrown"), PersistentDataType.LONG)) {
            long thrownTime = container.getOrDefault(new NamespacedKey(Server.getPlugin(), "saddleThrown"), PersistentDataType.LONG, 0L);
            if (thrownTime >= System.currentTimeMillis()) {
                return;
            }
        }

        PersistentDataContainer riderContainer = rider.getPersistentDataContainer();
        if (riderContainer.getOrDefault(new NamespacedKey(Server.getPlugin(), "recentDismount"), PersistentDataType.LONG, 0L) >= System.currentTimeMillis()) {
            return;
        }

        Player topPlayer = clickedPlayer;
        Player secondToTopPlayer = null;

        while (!topPlayer.getPassengers().isEmpty() && topPlayer.getPassengers().getFirst() instanceof Player) {
            if (topPlayer.getPassengers().contains(rider)) break;
            secondToTopPlayer = topPlayer;
            topPlayer = (Player) topPlayer.getPassengers().getFirst();
        }

        ItemStack helmet = topPlayer.getInventory().getHelmet();
        if (helmet == null || helmet.getType() != Material.SADDLE && !clickedPlayer.getPassengers().isEmpty()) {
            if (secondToTopPlayer != null) {
                if (!clickedPlayer.getPassengers().isEmpty()) {
                    clickedPlayer.removePassenger(clickedPlayer.getPassengers().getFirst());
                }
                secondToTopPlayer.addPassenger(rider);
            } else {
                if (!clickedPlayer.getPassengers().isEmpty()) {
                    clickedPlayer.removePassenger(clickedPlayer.getPassengers().getFirst());
                }
                clickedPlayer.addPassenger(rider);
            }
        } else {
            if (!clickedPlayer.getPassengers().isEmpty()) {
                clickedPlayer.removePassenger(clickedPlayer.getPassengers().getFirst());
            }
            topPlayer.addPassenger(rider);
        }
    }

    private void throwEntity(Entity passenger, Player thrower) {
        thrower.removePassenger(passenger);

        PersistentDataContainer container = passenger.getPersistentDataContainer();
        container.set(new NamespacedKey(Server.getPlugin(), "saddleThrown"), PersistentDataType.LONG, System.currentTimeMillis()+1000L);

        new BukkitRunnable() {
            @Override
            public void run() {
                double rng = ThreadLocalRandom.current().nextDouble(1.6, 1.8);
                org.bukkit.util.Vector direction = thrower.getLocation().getDirection().multiply(rng);
                passenger.setVelocity(direction);
                if (specialEffectEntities.contains(passenger.getType())) {
                    applySpecialEffects(passenger, thrower, false);
                }
            }
        }.runTaskLater(plugin, 2L);
    }
}