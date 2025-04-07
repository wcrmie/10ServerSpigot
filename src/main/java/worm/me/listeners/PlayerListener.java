package worm.me.listeners;

import net.coreprotect.CoreProtectAPI;
import net.coreprotect.api.result.ParseResult;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import worm.me.GuiConstructor;
import worm.me.Server;
import worm.me.gamesystems.fishing.FishingTables;
import worm.me.playersystems.PlayerSpawning;
import worm.me.playersystems.abilities.active.FeatherDash;
import worm.me.playersystems.data.VoidPlayer;
import worm.me.playersystems.data.levels.Levels;
import worm.me.playersystems.generator.Generator;
import worm.me.playersystems.saddle.SaddleSystem;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class PlayerListener implements Listener {

    private final Server plugin;

    public PlayerListener(Server plugin) {
        this.plugin = plugin;
    }

    // ----------------------------Player Gameplay Actions-----------------------------

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {

        FishingTables handler = new FishingTables();
        handler.handleFishing(event);

        Levels.expHandler(event.getPlayer(), event);

    }

    @EventHandler
    public void onArrowShot(EntityShootBowEvent e){
        if (e.getEntity() instanceof Player shooter) {
            e.getProjectile().getPersistentDataContainer().set(new NamespacedKey(plugin, "arrow"), PersistentDataType.STRING, "arrow");
            VoidPlayer.registerStats(shooter, 3);
        }
    }

    @EventHandler
    public void onArrowHit(ProjectileHitEvent e){
        if (e.getEntity().getShooter() instanceof Player shooter && e.getHitEntity() != null && e.getEntity().getPersistentDataContainer().get(new NamespacedKey(plugin, "arrow"), PersistentDataType.STRING) != null){
            VoidPlayer.registerStats(shooter, 2);
        }
    }

    @EventHandler
    public void onPlayerLeftClick(PlayerAnimationEvent event){

        if (event.getAnimationType() == PlayerAnimationType.ARM_SWING) {

            SaddleSystem ss = new SaddleSystem(plugin);
            ss.leftClick(event);

        }
    }

    @EventHandler
    public void onPlayerRightClick(PlayerInteractEntityEvent event){
        SaddleSystem handler = new SaddleSystem(plugin);
        handler.rightClick(event);
    }


    // ----------------------------Experience and Leveling-----------------------------

    @EventHandler
    public void onPlayerBreak(BlockBreakEvent e){
        Player player = e.getPlayer();

        CoreProtectAPI api = worm.me.Server.getPlugin().getCoreProtect();


        if (e.getBlock().getType() == Material.PUMPKIN) {
            e.setDropItems(false);
            e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.CARVED_PUMPKIN));
            e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.PUMPKIN_SEEDS));
        }

        // If the block was placed by the player in the last 10 seconds, don't give exp

        if (api != null) {

            List<String[]> lookup = api.performLookup(10, null, null, null, null, null, 1, e.getBlock().getLocation());


            boolean exp = true;

            for (String[] value : lookup) {
                ParseResult result = api.parseResult(value);

                if (e.getBlock().getType() == Material.IRON_ORE) break;

                if (result.getType().equals(e.getBlock().getType()) && result.getActionString().equals("place") && result.getX() == e.getBlock().getX() && result.getY() == e.getBlock().getY() && result.getZ() == e.getBlock().getZ()) {
                    exp = false;
                    break;
                }

            }


            if (exp) Levels.expHandler(player, e);
        } else player.sendMessage(ChatColor.RED + "CoreProtect is not enabled on this server.");
    }

    @EventHandler
    public void onEntityKilled(EntityDeathEvent e){
        if (e.getEntity().getKiller() != null && e.getEntity().getKiller() instanceof Player p){
            Levels.expHandler(p, e);

            if (e.getEntity().getType() == EntityType.PLAYER) {

                PersistentDataContainer c = p.getPersistentDataContainer();

                long[] repsess = c.get(new NamespacedKey(worm.me.Server.getPlugin(), "repsess"), PersistentDataType.LONG_ARRAY);

                repsess[1]++;

                c.set(new NamespacedKey(worm.me.Server.getPlugin(), "repsess"), PersistentDataType.LONG_ARRAY, repsess);

            }

        }

        // Change drop system in future
        if (e.getEntity().getType() == EntityType.ENDERMAN) {
            int emeraldCount = ThreadLocalRandom.current().nextInt(0, 3);
            e.getDrops().add(new ItemStack(Material.EMERALD, emeraldCount));
        }
        if (e.getEntity().getType() == EntityType.ENDERMITE) {
            int emeraldCount = ThreadLocalRandom.current().nextInt(0, 2);
            e.getDrops().add(new ItemStack(Material.SMALL_AMETHYST_BUD, emeraldCount));
        }
        if (e.getEntity().getType() == EntityType.SILVERFISH) {
            int emeraldCount = ThreadLocalRandom.current().nextInt(3, 6);
            e.getDrops().add(new ItemStack(Material.IRON_NUGGET, emeraldCount));
        }
        if (e.getEntity().getType() == EntityType.BAT) {
            int emeraldCount = ThreadLocalRandom.current().nextInt(0, 2);
            e.getDrops().add(new ItemStack(Material.IRON_NUGGET, emeraldCount));
        }

        if (e.getEntity().getType() == EntityType.PLAYER) e.getDrops().add(GuiConstructor.createGuiItem(Material.ROTTEN_FLESH, ChatColor.WHITE + "Player Flesh", ChatColor.GRAY + "A Player's flesh."));

        e.getDrops().removeIf(item -> item.getType().toString().contains("sword") || item.getType().toString().contains("axe") ||
                item.getType().toString().contains("shovel") || item.getType().toString().contains("pickaxe"));

    }

    @EventHandler
    public void onFurnaceCollect(FurnaceExtractEvent e){

        Levels.expHandler(e.getPlayer(), e);

    }

    @EventHandler
    public void onHarvest(PlayerHarvestBlockEvent e){

        Levels.expHandler(e.getPlayer(), e);

    }
    // ----------------------------END Experience and Levelling-----------------------------

    // ----------------------------END Player Gameplay Actions-----------------------------



    // ----------------------------Player Initializations----------------------------------

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){

        // Set player variable
        Player player = event.getPlayer();

        // Give the player a random item every 10 seconds
        worm.me.Server.getPlugin().generating.add(event.getPlayer());
        Generator generator = new Generator(plugin);
        generator.generateItem(player);

        // Initialize player data
        VoidPlayer.initializeData(player);

        NamespacedKey customSpawnKey = new NamespacedKey(plugin, "custom_spawn");
        if (!player.getPersistentDataContainer().has(customSpawnKey, PersistentDataType.BOOLEAN)) {
            player.getPersistentDataContainer().set(customSpawnKey, PersistentDataType.BOOLEAN, true);
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e){
        Player p = e.getPlayer();
        worm.me.Server.getPlugin().generating.remove(e.getPlayer());

        PersistentDataContainer container = p.getPersistentDataContainer();

        FeatherDash featherDash = new FeatherDash();
        featherDash.cancelDash(e.getPlayer().getUniqueId());

        container.set(new NamespacedKey(plugin, "repsess"), PersistentDataType.LONG_ARRAY, new long[]{System.currentTimeMillis()/1000L, container.get(new NamespacedKey(plugin, "repsess"), PersistentDataType.LONG_ARRAY)[1]});
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event){
        if (!event.isBedSpawn()) {
            NamespacedKey customSpawnKey = new NamespacedKey(plugin, "custom_spawn");
            if (event.getPlayer().getPersistentDataContainer().has(customSpawnKey, PersistentDataType.BOOLEAN)) {
                if (event.getPlayer().getPersistentDataContainer().get(customSpawnKey, PersistentDataType.BOOLEAN) == true) {
                    PlayerSpawning handler = new PlayerSpawning(plugin);
                    handler.playerSpawn(event.getPlayer());
                }
            }
        }
    }

}
