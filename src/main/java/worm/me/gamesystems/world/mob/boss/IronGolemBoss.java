package worm.me.gamesystems.world.mob.boss;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.DamageResistant;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import worm.me.utils.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static worm.me.listeners.world.mobs.CustomMobsListener.generateKnowledgeTome;

public class IronGolemBoss extends Boss {

    public IronGolemBoss(Entity boss, Location spawnLocation) {
        super(boss, 500.0, 10.0, 1.5F, spawnLocation);
        health = maxHealth;
        bossMap.put(boss, this);
    }

    @Override
    public void spawn(Location location) {

        bossMap.put(m_boss, this);

        IronGolem golem = (IronGolem) m_boss;

        golem.setPlayerCreated(false);

        golem.getWorld().playSound(golem.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 1F, 0.1F);

        golem.getAttribute(Attribute.SCALE).setBaseValue(size);
        golem.getAttribute(Attribute.MAX_HEALTH).setBaseValue(maxHealth);
        golem.getAttribute(Attribute.ATTACK_DAMAGE).setBaseValue(attackDamage);

        golem.setWaypointColor(Color.RED);
        golem.getAttribute(Attribute.WAYPOINT_TRANSMIT_RANGE).setBaseValue(1250);

        golem.setHealth(maxHealth);

    }

    @Override
    public BossBar initializeBossBar() {

        BossBar bossBar = BossBar.bossBar(
                MiniMessage.miniMessage().deserialize("<red>Iron Golem - 100% Health"),
                1.0f,
                BossBar.Color.RED,
                BossBar.Overlay.NOTCHED_12
        );

        for (Player player : Bukkit.getOnlinePlayers()) {

            bossBar.addViewer(player);

        }

        return bossBar;
    }

    @Override
    public void updateBossBar() {

        double healthPercentage = health / maxHealth;
        bossBar.progress((float) healthPercentage);
        bossBar.name(MiniMessage.miniMessage().deserialize(

                String.format(

                        "<red>Iron Golem - %s%% Health",
                        (int)(healthPercentage * 100)

                )

        ));

    }

    @Override
    public void handleDealDamage(EntityDamageEvent event) {

        // nothing here; no custom damage dealt

    }

    @Override
    public void handleTakeDamage(EntityDamageEvent event) {

        LivingEntity entity = (LivingEntity) event.getEntity();

        health = entity.getHealth();

        if (event.getCause().equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) || event.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE) || event.getCause().equals(EntityDamageEvent.DamageCause.SUFFOCATION) || event.getCause().equals(EntityDamageEvent.DamageCause.FIRE) || event.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK) || event.getCause().equals(EntityDamageEvent.DamageCause.CAMPFIRE) || event.getCause().equals(EntityDamageEvent.DamageCause.LAVA)) {
            event.setCancelled(true);
            event.getEntity().getWorld().playSound(event.getEntity().getLocation(), Sound.BLOCK_ANVIL_PLACE, 1F, 2F);
            return;
        }

        updateBossBar();

        if (event.getDamageSource().getCausingEntity() instanceof Player p && p.getLocation().distance(m_boss.getLocation()) >= 2.5) {

            ProtectedRegion region = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(p.getWorld())).getRegion("spawn");
            Location loc = p.getLocation();

            if (region.contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())) {
                event.setCancelled(true);
                return;
            }

            int RNG = ThreadLocalRandom.current().nextInt(1, 10);

            if (RNG == 1) {

                m_boss.getWorld().spawn(m_boss.getLocation().add(0, 1, 0), Creeper.class, spawnedCreeper -> {
                    spawnedCreeper.setPowered(true);
                    spawnedCreeper.setTarget(p);
                    spawnedCreeper.setIgnited(true);

                    Vector dir = p.getLocation().toVector().subtract(m_boss.getLocation().toVector().multiply(0.5));

                    spawnedCreeper.setVelocity(dir.normalize());

                });

            }

        }


    }

    @Override
    public List<ItemStack> generateDrops() {


        List<ItemStack> drops = new ArrayList<>();

        drops.add(generateKnowledgeTome());

        drops.add(
                new Item.ItemBuilder(
                        new ItemStack(Material.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE))
                        .addComponent(DataComponentTypes.DAMAGE_RESISTANT, DamageResistant.damageResistant(TagKey.create(RegistryKey.DAMAGE_TYPE, DamageType.EXPLOSION.getKey()))
                        ).build()
        );

        return drops;
    }

    @Override
    public void handleDeath(EntityDeathEvent event) {
        event.getDrops().clear();
        List<ItemStack> drops = generateDrops();
        for (ItemStack drop : drops) {
            event.getDrops().add(drop);
        }
        disable();
    }

    @Override
    public void enable() {
        bossMap.put(m_boss, this);
        getBossEntities().add(m_boss);
        getBossBars().add(bossBar);
    }

    @Override
    public void disable() {
        destroyBossBar();
        bossMap.remove(m_boss);
        getBossEntities().remove(m_boss);
    }
}