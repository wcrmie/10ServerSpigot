package worm.me.listeners.player;

import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Silverfish;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.event.player.PlayerHarvestBlockEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import worm.me.playersystems.abilities.Abilities;
import worm.me.playersystems.data.UnlockEvent;
import worm.me.playersystems.data.levels.GainExpEvent;
import worm.me.playersystems.data.levels.LevelUpEvent;
import worm.me.playersystems.data.levels.Levels;
import worm.me.utils.Item;
import worm.me.utils.chat.Message;
import worm.me.utils.chat.MessageType;

import java.util.concurrent.ThreadLocalRandom;

public class LevellingListener implements Listener {

    // ----------------------------Experience and Leveling-----------------------------

    @EventHandler
    public void onExpGain(GainExpEvent e){
        if (Levels.getExp(Levels.skills.get(e.getSkillType()), e.getPlayer()) + e.getExpValue() > Levels.expRequired(Levels.getLevel(e.getSkillType(), e.getPlayer()) + 1)){
            Levels.levelUp(e.getSkillType(), e.getPlayer());
        }
    }

    @EventHandler
    public void onLevelUp(LevelUpEvent e){

    }

    @EventHandler
    public void onUnlock(UnlockEvent e){
        Player p = e.getPlayer();

        if (e.getUnlockType().equals("Ability")) {
            p.sendMessage(Message.create(MessageType.GENERIC, "You have unlocked the " + Abilities.abilityGuiComponents.get(Integer.parseInt(e.getUnlock())).getFirst() + " " + e.getUnlockType()));
        } else {
            p.sendMessage(Message.create(MessageType.GENERIC, "You have unlocked the " + e.getUnlock() + " " + e.getUnlockType()));
        }
    }

    @EventHandler
    public void onPlayerBreak(BlockBreakEvent e){
        Player player = e.getPlayer();

        if (e.getBlock().getType() == Material.PUMPKIN) {
            e.setDropItems(false);
            e.getBlock().getLocation().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.CARVED_PUMPKIN));
            e.getBlock().getLocation().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.PUMPKIN_SEEDS, 2));
        }

        if (Levels.getLevel(6, player) >= 12) {
            if (e.getBlock().getType() == Material.IRON_ORE || e.getBlock().getType() == Material.COAL_ORE) {
                int chance = ThreadLocalRandom.current().nextInt(0, 100);
                if (chance < 10) {
                    e.getBlock().getLocation().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.RAW_GOLD));
                    player.playSound(player, Sound.BLOCK_AMETHYST_BLOCK_BREAK, 1, 0.1F);
                    player.sendMessage(Message.create(MessageType.GENERIC, "Your Midas' Touch allowed you to uncover a chunk of raw gold!"));
                }
            }
        }

        Levels.expHandler(player, e);

        if (e.getBlock().getType().equals(Material.COBBLESTONE)) {

            int rng = ThreadLocalRandom.current().nextInt(0, 100);
            if (rng <= 1) {

                e.getBlock().getLocation().getWorld().spawn(e.getBlock().getLocation(), Silverfish.class);
                e.getBlock().getLocation().getWorld().spawnParticle(Particle.CLOUD, e.getBlock().getLocation().getX(), e.getBlock().getLocation().getY(), e.getBlock().getLocation().getZ(), 25, 0.5, 0.5, 0.5, 0.1);

            }
        }
    }

    @EventHandler
    public void onEntityKilled(EntityDeathEvent e){

        if (e.getEntity().getKiller() != null && e.getEntity().getKiller() instanceof Player p){
            Levels.expHandler(p, e);
            Player player = e.getEntity().getKiller();


            if (e.getEntity().getType() == EntityType.PLAYER) {

                PersistentDataContainer c = p.getPersistentDataContainer();

                long[] repsess = c.get(new NamespacedKey(worm.me.Server.getPlugin(), "repsess"), PersistentDataType.LONG_ARRAY);

                repsess[1]++;

                c.set(new NamespacedKey(worm.me.Server.getPlugin(), "repsess"), PersistentDataType.LONG_ARRAY, repsess);

            }
        }

        if (e.getEntity().getType() == EntityType.PLAYER) {
            e.getDrops().add(Item.create(Material.ROTTEN_FLESH, ChatColor.WHITE + "Player Flesh", ChatColor.GRAY + "A Player's flesh."));
        }
    }

    @EventHandler
    public void DeathMessage(PlayerDeathEvent e){

        Player player = e.getPlayer();

        for (Player p : Bukkit.getOnlinePlayers()){

            double distance = p.getLocation().distance(e.getEntity().getLocation());

            int color = distance == 0 ? 255 : (int) Math.round(255 - 255 * (distance / (player.getWorld().getWorldBorder().getSize() + 50)));
            if (color < 50) color = 50;
            String hexColor = String.format("#%02x%02x%02x", color, color, color);
            p.sendMessage(net.md_5.bungee.api.ChatColor.of(hexColor) + "[" + ChatColor.RED + "☠" + net.md_5.bungee.api.ChatColor.of(hexColor) + "] " + e.getDeathMessage());

        }
    }

    @EventHandler
    public void onFurnaceCollect(FurnaceExtractEvent e){

        Levels.expHandler(e.getPlayer(), e);

    }

    @EventHandler
    public void onHarvest(PlayerHarvestBlockEvent e){

        Levels.expHandler(e.getPlayer(), e);

    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        if (!event.isCancelled()) Levels.addExp(1, Levels.skillTranslation("Crafting"), (Player) event.getWhoClicked());

    }

    // ----------------------------END Experience and Levelling-----------------------------

}
