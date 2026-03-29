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
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import worm.me.utils.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static worm.me.listeners.world.mobs.CustomMobsListener.generateKnowledgeTome;

public class IronGolemBoss extends Boss {

    private static final NamespacedKey BOSS_TYPE_KEY =
            new NamespacedKey(JavaPlugin.getProvidingPlugin(IronGolemBoss.class), "iron_golem_boss");

    public IronGolemBoss(Entity boss, Location spawnLocation) {
        super(boss, 500.0, 10.0, 1.5F, spawnLocation);
        health = maxHealth;
    }

    private IronGolemBoss(IronGolem golem) {
        super(golem, 500.0, 10.0, 1.5F, golem.getLocation());
        this.health = golem.getHealth();
    }

    @Override
    public void spawn(Location location) {
        IronGolem golem = (IronGolem) m_boss;
        golem.setPlayerCreated(false);
        golem.getWorld().playSound(golem.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 1F, 0.1F);
        golem.getAttribute(Attribute.SCALE).setBaseValue(size);
        golem.getAttribute(Attribute.MAX_HEALTH).setBaseValue(maxHealth);
        golem.getAttribute(Attribute.ATTACK_DAMAGE).setBaseValue(attackDamage);
        golem.setWaypointColor(Color.RED);
        golem.getAttribute(Attribute.WAYPOINT_TRANSMIT_RANGE).setBaseValue(1250);
        golem.setHealth(maxHealth);
        golem.getPersistentDataContainer().set(BOSS_TYPE_KEY, PersistentDataType.STRING, "iron_golem");
    }

    @EventHandler
    public void onTakeDamage(EntityDamageEvent event) {
        if (!event.getEntity().equals(m_boss)) return;

        LivingEntity entity = (LivingEntity) event.getEntity();
        health = entity.getHealth();

        switch (event.getCause()) {
            case BLOCK_EXPLOSION, PROJECTILE, SUFFOCATION, FIRE, FIRE_TICK, CAMPFIRE, LAVA -> {
                event.setCancelled(true);
                entity.getWorld().playSound(entity.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1F, 2F);
                return;
            }
        }

        updateBossBar();

        if (!(event.getDamageSource().getCausingEntity() instanceof Player p)) return;
        if (p.getLocation().distance(m_boss.getLocation()) < 2.5) return;

        ProtectedRegion region = WorldGuard.getInstance().getPlatform()
                .getRegionContainer()
                .get(BukkitAdapter.adapt(p.getWorld()))
                .getRegion("spawn");

        Location loc = p.getLocation();
        if (region != null && region.contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())) {
            event.setCancelled(true);
            return;
        }

        if (ThreadLocalRandom.current().nextInt(1, 10) == 1) {
            m_boss.getWorld().spawn(m_boss.getLocation().add(0, 1, 0), Creeper.class, creeper -> {
                creeper.setPowered(true);
                creeper.setTarget(p);
                creeper.setIgnited(true);
                Vector dir = p.getLocation().toVector()
                        .subtract(m_boss.getLocation().toVector().multiply(0.5));
                creeper.setVelocity(dir.normalize());
            });
        }
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        if (!event.getEntity().equals(m_boss)) return;

        event.getDrops().clear();
        event.getDrops().addAll(generateDrops());

        disable();
    }

    @Override
    public BossBar initializeBossBar() {
        BossBar bar = BossBar.bossBar(
                MiniMessage.miniMessage().deserialize("<red>Iron Golem - 100% Health"),
                1.0f,
                BossBar.Color.RED,
                BossBar.Overlay.NOTCHED_12
        );
        Bukkit.getOnlinePlayers().forEach(bar::addViewer);
        return bar;
    }

    @Override
    public void updateBossBar() {
        double pct = health / maxHealth;
        bossBar.progress((float) pct);
        bossBar.name(MiniMessage.miniMessage().deserialize(
                String.format("<red>Iron Golem - %d%% Health", (int)(pct * 100))
        ));
    }

    @Override
    public List<ItemStack> generateDrops() {
        List<ItemStack> drops = new ArrayList<>();
        drops.add(generateKnowledgeTome());
        drops.add(new Item.ItemBuilder(new ItemStack(Material.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE))
                .addComponent(DataComponentTypes.DAMAGE_RESISTANT,
                        DamageResistant.damageResistant(
                                TagKey.create(RegistryKey.DAMAGE_TYPE, DamageType.EXPLOSION.getKey())))
                .build());
        return drops;
    }

    public static void restoreBosses() {
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (!(entity instanceof IronGolem golem)) continue;
                if (!golem.getPersistentDataContainer().has(BOSS_TYPE_KEY, PersistentDataType.STRING)) continue;
                if (Boss.isBoss(golem)) continue;

                IronGolemBoss boss = new IronGolemBoss(golem);
                boss.enable();
                boss.updateBossBar();
            }
        }
    }
}