package worm.me.listeners.world.mobs;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.DamageResistant;
import io.papermc.paper.event.entity.EntityMoveEvent;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.tag.TagKey;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import worm.me.Server;
import worm.me.gamesystems.world.mob.traders.Trader;
import worm.me.gamesystems.world.mob.traders.VillagerProfession;
import worm.me.utils.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static worm.me.listeners.world.WorldListener.ironGolemBossBars;
import static worm.me.listeners.world.WorldListener.tickList;

public class CustomMobsListener implements Listener {

    // --------------------IRON GOLEM BOSS-----------------------

    @EventHandler
    public void onIronGolemSpawn(EntitySpawnEvent event) {

        if (event.getEntity() instanceof IronGolem golem && event.getEntity().getEntitySpawnReason().equals(CreatureSpawnEvent.SpawnReason.BUILD_IRONGOLEM)) {

            golem.setPlayerCreated(false);

            golem.getWorld().playSound(golem.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 1F, 0.1F);

            golem.getAttribute(Attribute.SCALE).setBaseValue(1.5);
            golem.getAttribute(Attribute.MAX_HEALTH).setBaseValue(500);
            golem.getAttribute(Attribute.ATTACK_DAMAGE).setBaseValue(10);

            golem.setHealth(500);

            createIronGolemBossBar(golem);

        }
    }

    public void createIronGolemBossBar(IronGolem golem) {

        BossBar bossBar = Bukkit.createBossBar(ChatColor.RED + "Iron Golem - 100% Health", BarColor.RED, BarStyle.SEGMENTED_12);
        bossBar.setProgress(1);
        ironGolemBossBars.put(golem, bossBar);

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getWorld().equals(golem.getWorld())) {
                bossBar.addPlayer(p);
            }
        }
    }

    public void manageIronGolemBossBar(IronGolem golem) {

        BossBar bossBar = ironGolemBossBars.get(golem);
        if (bossBar != null) {
            double healthPercentage = golem.getHealth() / golem.getAttribute(Attribute.MAX_HEALTH).getValue();
            bossBar.setProgress(healthPercentage);
            bossBar.setTitle(ChatColor.RED + "Iron Golem - " + (int)(healthPercentage * 100) + "% Health");
        }
    }

    public void removeIronGolemBossBar(IronGolem golem) {

        BossBar bossBar = ironGolemBossBars.get(golem);
        if (bossBar != null) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                bossBar.removePlayer(p);
            }
            bossBar.removeAll();
        }

        ironGolemBossBars.remove(golem);

    }

    public List<ItemStack> generateIronGolemDrops() {

        List<ItemStack> drops = new ArrayList<>();

        int RNG = ThreadLocalRandom.current().nextInt(0, 100);

        if (RNG >= 50) {

            drops.add(generateKnowledgeTome());

        }

        drops.add(new Item.ItemBuilder(new ItemStack(Material.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE)).addComponent(DataComponentTypes.DAMAGE_RESISTANT, DamageResistant.damageResistant(TagKey.create(RegistryKey.DAMAGE_TYPE, DamageType.EXPLOSION.getKey()))).build());


        return drops;
    }

    public static ItemStack generateKnowledgeTome() {

        VillagerProfession profession = determineProfession();

        int[] genetics = new Trader.GeneticsBuilder().addGenetic(profession).build();

        ItemStack tome = new ItemStack(Material.KNOWLEDGE_BOOK);

        ItemMeta meta = tome.getItemMeta();

        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        pdc.set(new NamespacedKey(worm.me.Server.getPlugin(), "genetics"), PersistentDataType.INTEGER_ARRAY, genetics);

        meta.setDisplayName(ChatColor.GOLD + "Villager Knowledge Tome");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Right click this on a villager to train them how to use a workstation.");
        lore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "PROFESSION: " + ChatColor.RESET + profession.getName());
        meta.setLore(lore);
        tome.setItemMeta(meta);

        return new Item.ItemBuilder(tome).addComponent(DataComponentTypes.DAMAGE_RESISTANT, DamageResistant.damageResistant(TagKey.create(RegistryKey.DAMAGE_TYPE, DamageType.EXPLOSION.getKey()))).build();

    }

    public static VillagerProfession determineProfession() {

        int RNG = ThreadLocalRandom.current().nextInt(0, VillagerProfession.values().length);
        return VillagerProfession.values()[RNG];

    }
        
    @EventHandler
    public void onIronGolemDamage(EntityDamageEvent event) {

        if (event.getEntity() instanceof IronGolem golem && event.getEntity().getEntitySpawnReason().equals(CreatureSpawnEvent.SpawnReason.BUILD_IRONGOLEM)) {

            if (event.getCause().equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) || event.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE) || event.getCause().equals(EntityDamageEvent.DamageCause.SUFFOCATION) || event.getCause().equals(EntityDamageEvent.DamageCause.FIRE) || event.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK) || event.getCause().equals(EntityDamageEvent.DamageCause.CAMPFIRE) || event.getCause().equals(EntityDamageEvent.DamageCause.LAVA)) {
                event.setCancelled(true);
                event.getEntity().getWorld().playSound(event.getEntity().getLocation(), Sound.BLOCK_ANVIL_PLACE, 1F, 2F);
                return; // Ignore explosion damage
            }

            manageIronGolemBossBar(golem);

            if (event.getDamageSource().getCausingEntity() instanceof Player p && p.getLocation().distance(golem.getLocation()) >= 2) {

                ProtectedRegion region = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(p.getWorld())).getRegion("spawn");
                Location loc = p.getLocation();

                // If the player is in spawn, don't damage them
                if (region.contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())) {
                    event.setCancelled(true);
                    return;
                }

                int RNG = ThreadLocalRandom.current().nextInt(1, 10);

                if (RNG == 1) {

                    golem.getWorld().spawn(golem.getLocation().add(0, 1, 0), Creeper.class, spawnedCreeper -> {
                        spawnedCreeper.setPowered(true);
                        spawnedCreeper.setTarget(p);
                        spawnedCreeper.setIgnited(true);

                        Vector dir = p.getLocation().toVector().subtract(golem.getLocation().toVector().multiply(0.5));

                        spawnedCreeper.setVelocity(dir.normalize());

                    });

                }

            }

        }



    }

    @EventHandler
    public void onIronGolemDeath(EntityDeathEvent event) {

        if (event.getEntity() instanceof IronGolem golem && event.getEntity().getEntitySpawnReason().equals(CreatureSpawnEvent.SpawnReason.BUILD_IRONGOLEM)) {

            removeIronGolemBossBar(golem);

            event.getDrops().clear();

            // Generate drops

            List<ItemStack> drops = generateIronGolemDrops();

            for (ItemStack drop : drops) {
                event.getDrops().add(drop);
            }

            // Remove from tick list
            tickList.remove(golem);
        }
    }

    public void setGolemFalling(IronGolem golem, boolean bool) {

        PersistentDataContainer pdc = golem.getPersistentDataContainer();

        pdc.set(new NamespacedKey(worm.me.Server.getPlugin(), "golem_falling"), PersistentDataType.BOOLEAN, bool);

    }

    public boolean isGolemFalling(IronGolem golem) {

        PersistentDataContainer pdc = golem.getPersistentDataContainer();

        return pdc.getOrDefault(new NamespacedKey(Server.getPlugin(), "golem_falling"), PersistentDataType.BOOLEAN, false);

    }

    @EventHandler
    public void onIronGolemMove(EntityMoveEvent event) {

        if (event.getEntity() instanceof IronGolem golem && event.getEntity().getEntitySpawnReason().equals(CreatureSpawnEvent.SpawnReason.BUILD_IRONGOLEM)) {

            // Prevent golem from falling in void and dying
            if (golem.getLocation().getBlockY() <= 200) {
                golem.teleport(golem.getLocation().add(0, 100, 0));
                setGolemFalling(golem, true);
            }

            if (isGolemFalling(golem) && golem.getLocation().subtract(0, 1, 0).getBlock().getType() != Material.AIR) {

                golem.getWorld().createExplosion(golem, 2F);
                golem.getWorld().playSound(golem.getLocation(), Sound.BLOCK_ANVIL_LAND, 1F, 0.5F);
                setGolemFalling(golem, false);

            }

        }
    }

    // --------------------END IRON GOLEM BOSS-----------------------

}
