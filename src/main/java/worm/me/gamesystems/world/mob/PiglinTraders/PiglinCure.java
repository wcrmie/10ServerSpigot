package worm.me.gamesystems.world.mob.PiglinTraders;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Piglin;
import org.bukkit.entity.Player;
import org.bukkit.entity.PigZombie;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import worm.me.Server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PiglinCure {

    private static final int CURE_DURATION_SECONDS = 10;
    private static final Map<UUID, BukkitTask> curingPiglins = new HashMap<>();

    private static NamespacedKey cureKey;
    private static NamespacedKey curedPiglinKey;

    private static NamespacedKey getCureKey() {
        if (cureKey == null) {
            cureKey = new NamespacedKey(Server.getPlugin(), "piglin_cure_carrot");
        }
        return cureKey;
    }

    private static NamespacedKey getCuredPiglinKey() {
        if (curedPiglinKey == null) {
            curedPiglinKey = new NamespacedKey(Server.getPlugin(), "cured_piglin");
        }
        return curedPiglinKey;
    }

    public static boolean isCuredPiglin(Entity entity) {
        if (!(entity instanceof Piglin)) return false;
        return ((Piglin) entity).getPersistentDataContainer()
                .has(getCuredPiglinKey(), PersistentDataType.BYTE);
    }

    public static ItemStack createCureCarrot() {
        ItemStack item = new ItemStack(Material.GOLDEN_CARROT);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + "Enchanted Golden Carrot");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "A carrot soaked in golden magic.");
        lore.add("");
        lore.add(ChatColor.GRAY + "Right-click a " + ChatColor.GOLD + "Zombified Piglin" + ChatColor.GRAY + " to begin curing.");
        meta.setLore(lore);
        meta.getPersistentDataContainer().set(getCureKey(), PersistentDataType.BYTE, (byte) 1);

        meta.addEnchant(Enchantment.LUCK_OF_THE_SEA, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        item.setItemMeta(meta);
        return item;
    }

    public static boolean isCureCarrot(ItemStack item) {
        if (item == null || item.getType() != Material.GOLDEN_CARROT || !item.hasItemMeta()) {
            return false;
        }
        return item.getItemMeta()
                .getPersistentDataContainer()
                .has(getCureKey(), PersistentDataType.BYTE);
    }

    public static boolean isCuring(UUID piglinId) {
        return curingPiglins.containsKey(piglinId);
    }

    public static void startCuring(Player player, PigZombie piglin) {
        final UUID piglinId = piglin.getUniqueId();

        ItemStack hand = player.getInventory().getItemInMainHand();
        if (hand.getAmount() > 1) {
            hand.setAmount(hand.getAmount() - 1);
        } else {
            player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
        }

        piglin.setAI(false);

        Location loc = piglin.getLocation();
        loc.getWorld().playSound(loc, Sound.BLOCK_BEACON_ACTIVATE, 0.3f, 1.4f);

        BukkitRunnable task = new BukkitRunnable() {
            int elapsed = 0;

            @Override
            public void run() {
                if (!piglin.isValid() || piglin.isDead()) {
                    curingPiglins.remove(piglinId);
                    cancel();
                    return;
                }

                elapsed++;

                Location center = piglin.getLocation().add(0, 1, 0);
                World world = center.getWorld();

                world.spawnParticle(Particle.END_ROD, center, 4, 0.2, 0.5, 0.2, 0.02);


                if (elapsed >= CURE_DURATION_SECONDS) {
                    finishCure(piglin, piglinId);
                    cancel();
                }
            }
        };

        BukkitTask handle = task.runTaskTimer(Server.getPlugin(), 20L, 20L);
        curingPiglins.put(piglinId, handle);
    }

    private static void finishCure(PigZombie piglin, UUID piglinId) {
        Location loc   = piglin.getLocation();
        World    world = piglin.getWorld();

        piglin.remove();
        Piglin cured = (Piglin) world.spawnEntity(loc, EntityType.PIGLIN);

        cured.setImmuneToZombification(true);
        cured.setRemoveWhenFarAway(false);

        cured.setAdult();

        cured.getPersistentDataContainer()
                .set(getCuredPiglinKey(), PersistentDataType.BYTE, (byte) 1);

        world.playSound(loc, Sound.ENTITY_ZOMBIE_VILLAGER_CONVERTED, 0.6f, 1f);
        world.spawnParticle(Particle.END_ROD, loc.clone().add(0, 1, 0), 100, 1, 2, 1, 0.1);

        curingPiglins.remove(piglinId);
    }

    public static void cancelAll() {
        for (BukkitTask t : curingPiglins.values()) {
            t.cancel();
        }
        curingPiglins.clear();
    }
}