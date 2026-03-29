package worm.me.listeners.world.mobs;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.DamageResistant;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.tag.TagKey;
import org.bukkit.*;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import worm.me.gamesystems.world.mob.boss.IronGolemBoss;
import worm.me.gamesystems.world.mob.boss.ZiglinJouster;
import worm.me.gamesystems.world.mob.traders.Trader;
import worm.me.gamesystems.world.mob.traders.VillagerProfession;
import worm.me.utils.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class CustomMobsListener implements Listener {

    @EventHandler
    public void onBossSpawn(EntitySpawnEvent event) {
        if (event.getEntity() instanceof IronGolem golem
                && golem.getEntitySpawnReason() == CreatureSpawnEvent.SpawnReason.BUILD_IRONGOLEM) {
            IronGolemBoss golemBoss = new IronGolemBoss(golem, golem.getLocation());
            golemBoss.spawn(event.getEntity().getLocation());
            golemBoss.enable();
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (ZiglinJouster.temporaryBlocks.contains(event.getBlock())) {
            event.setDropItems(false);
            ZiglinJouster.temporaryBlocks.remove(event.getBlock());
        }
    }

    public static ItemStack generateKnowledgeTome() {
        VillagerProfession profession = determineProfession();
        int[] genetics = new Trader.GeneticsBuilder().addGenetic(profession).build();

        ItemStack tome = new ItemStack(Material.KNOWLEDGE_BOOK);
        ItemMeta meta = tome.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(new NamespacedKey(worm.me.Server.getPlugin(), "genetics"),
                PersistentDataType.INTEGER_ARRAY, genetics);
        meta.setDisplayName(ChatColor.GOLD + "Villager Knowledge Tome");

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Right click this on a villager to train them how to use a workstation.");
        lore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "PROFESSION: "
                + ChatColor.RESET + profession.getName());
        meta.setLore(lore);
        tome.setItemMeta(meta);

        return new Item.ItemBuilder(tome)
                .addComponent(DataComponentTypes.DAMAGE_RESISTANT,
                        DamageResistant.damageResistant(
                                TagKey.create(RegistryKey.DAMAGE_TYPE, DamageType.EXPLOSION.getKey())))
                .build();
    }

    public static VillagerProfession determineProfession() {
        return VillagerProfession.values()[
                ThreadLocalRandom.current().nextInt(VillagerProfession.values().length)];
    }

    public static void restoreBosses() {
        IronGolemBoss.restoreBosses();
        ZiglinJouster.restoreBosses();
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        ZiglinJouster.restoreBossesInChunk(event.getChunk());
    }
}