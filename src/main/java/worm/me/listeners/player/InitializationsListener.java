package worm.me.listeners.player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.util.TriState;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import worm.me.Server;
import worm.me.playersystems.abilities.Abilities;
import worm.me.playersystems.abilities.active.FeatherDash;
import worm.me.data.VoidPlayer;
import worm.me.playersystems.generator.Generator;
import worm.me.utils.info.Tips;
import worm.me.utils.join.FirstJoin;

public class InitializationsListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){

        // Set player variable
        Player player = event.getPlayer();

        player.setGravity(true);

        // Give the player a random item every 10 seconds
        worm.me.Server.getPlugin().generating.add(event.getPlayer());
        Generator.generateItem(player);

        Abilities abilities = new Abilities();
        abilities.setAllCooldowns(player);

        Tips tips = new Tips();
        tips.beginTipSending(player);

        // Initialize player data
        VoidPlayer.initializeData(player);

        NamespacedKey customSpawnKey = new NamespacedKey(Server.getPlugin(), "custom_spawn");
        if (!player.getPersistentDataContainer().has(customSpawnKey, PersistentDataType.BOOLEAN)) {
            player.getPersistentDataContainer().set(customSpawnKey, PersistentDataType.BOOLEAN, true);
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player.getUniqueId());

        if (!offlinePlayer.hasPlayedBefore()) {
            Component joinMessage = event.joinMessage();

            joinMessage = joinMessage.append(Component.text(ChatColor.GRAY + " (#" + Bukkit.getServer().getOfflinePlayers().length + ")"));

            event.joinMessage(joinMessage);

            player.teleport(
                    new Location(
                            Bukkit.getWorld("ul_Void"),
                            0.5, 220, 0.5,
                            0, 0
                    ));

            FirstJoin firstJoin = new FirstJoin();
            firstJoin.firstJoin(player);

        }

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "waypoint modify " + player.getName() + " color " + "white");

    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e){

        Player p = e.getPlayer();

        e.quitMessage(null);

        worm.me.Server.getPlugin().generating.remove(e.getPlayer());

        PersistentDataContainer container = p.getPersistentDataContainer();

        FeatherDash featherDash = new FeatherDash();
        featherDash.cancelDash(e.getPlayer().getUniqueId());

        container.set(new NamespacedKey(Server.getPlugin(), "repsess"), PersistentDataType.LONG_ARRAY, new long[]{System.currentTimeMillis()/1000L, container.get(new NamespacedKey(Server.getPlugin(), "repsess"), PersistentDataType.LONG_ARRAY)[1]});

        if (p.getVehicle() != null) {
            for (Entity passenger : p.getVehicle().getPassengers()) {
                if (passenger instanceof Player) {
                    passenger.leaveVehicle();
                }
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event){

        if (!event.isBedSpawn()) {

            event.setRespawnLocation(new Location(
                    Bukkit.getWorld("ul_Void"),
                    0.5, 220, 0.5,
                    0, 0
            ));

        }
    }

}
