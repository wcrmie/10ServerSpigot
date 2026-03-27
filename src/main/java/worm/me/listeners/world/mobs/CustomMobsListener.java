package worm.me.listeners.world.mobs;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.DamageResistant;
import io.papermc.paper.event.entity.EntityMoveEvent;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.tag.TagKey;
import org.bukkit.*;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import worm.me.Server;
import worm.me.gamesystems.world.PlayArea;
import worm.me.gamesystems.world.mob.boss.Boss;
import worm.me.gamesystems.world.mob.boss.IronGolemBoss;
import worm.me.gamesystems.world.mob.traders.Trader;
import worm.me.gamesystems.world.mob.traders.VillagerProfession;
import worm.me.utils.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static worm.me.listeners.world.WorldListener.tickList;

public class CustomMobsListener implements Listener {

    // --------------------IRON GOLEM BOSS-----------------------

    @EventHandler
    public void onBossSpawn(EntitySpawnEvent event) {

        if (event.getEntity() instanceof IronGolem golem && golem.getEntitySpawnReason().equals(CreatureSpawnEvent.SpawnReason.BUILD_IRONGOLEM)) {

            IronGolemBoss golemBoss = new IronGolemBoss(golem, golem.getLocation());
            golemBoss.spawn(event.getEntity().getLocation());

        }
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
    public void onBossDamage(EntityDamageEvent event) {

        if (!Boss.isBoss(event.getEntity())) return;

        Boss boss = Boss.getBoss(event.getEntity());

        boss.handleTakeDamage(event);

    }

    @EventHandler
    public void onBossDeath(EntityDeathEvent event) {

        if (!Boss.isBoss(event.getEntity())) return;

        Boss boss = Boss.getBoss(event.getEntity());

        boss.handleDeath(event);

    }

    @EventHandler
    public void onBossMove(EntityMoveEvent event) {

        if (!Boss.isBoss(event.getEntity())) return;

        Entity entity = event.getEntity();

        Boss boss = Boss.getBoss(entity);

        if (entity.getLocation().getBlockY() <= PlayArea.getMinHeight() - 32) {

            entity.teleport(entity.getLocation().add(0, PlayArea.getMaxHeight() + 64, 0));
            boss.setFalling(true);

        }

        if (boss.isFalling() && entity.getLocation().subtract(0, 1, 0).getBlock().getType() != Material.AIR) {

            entity.getWorld().createExplosion(entity, 2F);
            entity.getWorld().playSound(entity.getLocation(), Sound.BLOCK_ANVIL_LAND, 1F, 0.5F);
            boss.setFalling(false);

        }


    }

}
