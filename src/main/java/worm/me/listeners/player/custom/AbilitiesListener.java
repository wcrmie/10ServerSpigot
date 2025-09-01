package worm.me.listeners.player.custom;

import com.sk89q.worldguard.bukkit.event.inventory.UseItemEvent;
import net.kyori.adventure.util.TriState;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import worm.me.Server;
import worm.me.gamesystems.world.blocks.ArtBlocks;
import worm.me.gamesystems.items.ExperienceBottle;
import worm.me.playersystems.abilities.Abilities;
import worm.me.playersystems.abilities.AbilityAttemptEvent;
import worm.me.playersystems.quests.QuestGui;
import worm.me.utils.chat.Message;
import worm.me.utils.chat.MessageType;

import java.util.Objects;

public class AbilitiesListener implements Listener {

    // ----------------------------Ability Events-----------------------------

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent e){

        Abilities ability = new Abilities();

        if (e.getFrom() == e.getTo()) return;

        if (!ability.isActive(e.getPlayer(), 1) && e.getPlayer().getGameMode() == GameMode.SURVIVAL) {
            e.getPlayer().setAllowFlight(false);
            e.getPlayer().setFlying(false);
        }

        if (Objects.requireNonNull(e.getTo()).getY() < 200) e.getPlayer().damage(1000000);

        if (ability.getAbilitySlot(e.getPlayer(), 1) != -1){
            if (Objects.requireNonNullElse(e.getPlayer().getPersistentDataContainer().get(Abilities.abilityCooldown, PersistentDataType.LONG_ARRAY), new long[3])[ability.getAbilitySlot(e.getPlayer(), 1)] <= System.currentTimeMillis()/1000L && ability.isActive(e.getPlayer(), 1)){
                e.getPlayer().setAllowFlight(true);
                e.getPlayer().setFlyingFallDamage(TriState.TRUE);
            }
        }
    }

    @EventHandler
    public void onAbilityAttempt(AbilityAttemptEvent e){

        Abilities ability = new Abilities();
        Player player = e.getPlayer();

        PersistentDataContainer container = player.getPersistentDataContainer();

        if (ability.isActive(player, e.getAbilityID())){
            if (Objects.requireNonNullElse(container.get(Abilities.abilityCooldown, PersistentDataType.LONG_ARRAY), new long[3])[ability.getAbilitySlot(player, e.getAbilityID())] <= System.currentTimeMillis()/1000L) {
                ability.handleAbility(player, e.getAbilityID());
            } else {
                player.sendMessage(Message.create(MessageType.FAIL, "Your " + Abilities.abilityGuiComponents.get(e.getAbilityID()).getFirst() + " is on cooldown for " + (container.get(Abilities.abilityCooldown, PersistentDataType.LONG_ARRAY)[ability.getAbilitySlot(e.getPlayer(), e.getAbilityID())] - (System.currentTimeMillis()/1000)) + " seconds."));
                e.setCancelled(true);
            }
        }
        else e.setCancelled(true);

    }


    @EventHandler
    public void onPlayerFlightEvent(PlayerToggleFlightEvent e){
        Player p = e.getPlayer();

        if (p.getGameMode().equals(GameMode.CREATIVE)) return;

        // Make the player double jump
        e.setCancelled(true);

        Bukkit.getServer().getPluginManager().callEvent(new AbilityAttemptEvent(1, p));

    }

    @EventHandler (ignoreCancelled = true)
    public void onPlayerRightClick(UseItemEvent e){
        Player p = e.getCause().getFirstPlayer();

        if (e.getOriginalEvent() instanceof PlayerInteractEvent) {

            if (((PlayerInteractEvent) e.getOriginalEvent()).getAction().equals(Action.RIGHT_CLICK_AIR) || ((PlayerInteractEvent) e.getOriginalEvent()).getAction().equals(Action.RIGHT_CLICK_BLOCK)) {

                if (e.getItemStack().getType().equals(Material.KNOWLEDGE_BOOK)) {
                    e.setCancelled(true);
                }

                if (e.getItemStack().getType().equals(Material.GLASS_BOTTLE) && p.isSneaking()) {
                    e.setCancelled(true);
                    ExperienceBottle.fillBottle(p);
                }

                if (p.getInventory().getItemInMainHand().toString().toLowerCase().contains("sword")) {
                    if (p.isSneaking()){
                        Bukkit.getServer().getPluginManager().callEvent(new AbilityAttemptEvent(2, p));
                    }
                } else if (p.getInventory().getItemInMainHand().toString().toLowerCase().contains("_axe")) {
                    if (p.isSneaking()){
                        Bukkit.getServer().getPluginManager().callEvent(new AbilityAttemptEvent(3, p));
                    }
                } else if (p.getInventory().getItemInMainHand().getType().toString().contains("HELMET")){

                    ItemStack item = p.getInventory().getItemInMainHand();
                    ItemMeta meta = item.getItemMeta();

                    String material = item.getType().toString();

                    material = material.replace("HELMET", "BOOTS");

                    ItemStack newItem = new ItemStack(Material.valueOf(material));

                    newItem.setItemMeta(meta);

                    p.getInventory().remove(item);

                    p.getInventory().setItemInMainHand(newItem);

                    p.playSound(p, Sound.ENTITY_EVOKER_PREPARE_WOLOLO, 1F, 0.5F);

                }
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e){



        if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {

            if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.NOTE_BLOCK && e.getPlayer().isSneaking()) {
                Bukkit.getServer().getPluginManager().callEvent(new AbilityAttemptEvent(5, e.getPlayer()));
            }

            if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.FEATHER && e.getPlayer().isSneaking()) {
                Bukkit.getServer().getPluginManager().callEvent(new AbilityAttemptEvent(8, e.getPlayer()));
            }

            if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.RABBIT_FOOT && e.getPlayer().isSneaking()) {
                Bukkit.getServer().getPluginManager().callEvent(new AbilityAttemptEvent(9, e.getPlayer()));
            }

            if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.BOW) {
                Bukkit.getServer().getPluginManager().callEvent(new AbilityAttemptEvent(11, e.getPlayer()));
            }

            if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.TINTED_GLASS && e.getPlayer().isSneaking()) {
                Player player = e.getPlayer();

                Location playerLoc = player.getLocation();
                Vector direction = playerLoc.getDirection();
                direction.setY(0);
                if (direction.length() > 0) {
                    direction.normalize();
                }

                Location finalLocation = playerLoc.clone();
                finalLocation.add(direction.multiply(10));

                Block finalFeetBlock = finalLocation.getBlock();
                Block finalHeadBlock = finalLocation.clone().add(0, 1, 0).getBlock();

                if (player.isOnGround() && finalFeetBlock.isPassable() && finalHeadBlock.isPassable()) {
                    Bukkit.getServer().getPluginManager().callEvent(new AbilityAttemptEvent(6, player));
                }
            }
        }

        if (e.getHand() != EquipmentSlot.HAND) {
            return;
        }

        Player p = e.getPlayer();
        if (Objects.requireNonNullElse(e.getClickedBlock(), p.getLocation().getBlock()).getType().equals(Material.END_PORTAL_FRAME)){
            QuestGui questGui = new QuestGui();
            questGui.questGuiOpen(p);
        }
        if (Objects.requireNonNullElse(e.getClickedBlock(), p.getLocation().getBlock()).getType().toString().contains("WOOL") || Objects.requireNonNullElse(e.getClickedBlock(), p.getLocation().getBlock()).getType().toString().contains("CONCRETE")){
            ArtBlocks handler = new ArtBlocks(Server.getPlugin());
            handler.rightClick(e);
        }
    }

    // ----------------------------END Ability Events-----------------------------

}
