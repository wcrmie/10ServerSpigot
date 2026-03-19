package worm.me.gamesystems.items.gear.tools;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitTask;
import worm.me.Server;
import worm.me.gamesystems.world.PlayArea;
import worm.me.utils.Item;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class LodestoneTeleporter {

    private static HashMap<Player, BukkitTask> teleporting = new HashMap<>();
    private static final World world = PlayArea.getPlayWorld();

    public static boolean isLodestoneCompass(ItemStack item) {

        ItemMeta meta = item.getItemMeta();
        if (!meta.getPersistentDataContainer().has(new NamespacedKey(Server.getPlugin(), "waypoint"), PersistentDataType.INTEGER_ARRAY)) return false;

        return true;

    }

    public static ItemStack makeLodestoneCompass(ItemStack item, Location location) {
        Server.getPlugin().getLogger().info("Making Lodestone Compass");
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();

        ItemStack compass = new ItemStack(Material.COMPASS);

        container.set(new NamespacedKey(Server.getPlugin(), "waypoint"), PersistentDataType.INTEGER_ARRAY,  new int[]{location.getBlockX(), location.getBlockY(), location.getBlockZ()});
        container.set(new NamespacedKey(Server.getPlugin(), "waypointuses"), PersistentDataType.INTEGER, 5);
        meta.displayName(MiniMessage.miniMessage().deserialize(

                "<yellow><!i>Waypoint Compass</yellow>"

        ));
        meta.lore(List.of(MiniMessage.miniMessage().deserialize(""), MiniMessage.miniMessage().deserialize(
                "<gray><!i>Teleportation uses left: <red>5</red></gray>"
        ), MiniMessage.miniMessage().deserialize(

                String.format(

                        "<gray><!i>Waypoint Location: <red>%d, %d, %d</red></gray>",
                        location.getBlockX(), location.getBlockY(), location.getBlockZ()

                )

        ), MiniMessage.miniMessage().deserialize("")));
        compass.setItemMeta(meta);

        item.setAmount(item.getAmount() - 1);

        return compass;
    }

    public static void updateCompass(Player player, ItemStack item, int newUses) {
        ItemMeta meta = item.getItemMeta();

        if (newUses == 0) {

            item.setAmount(0);
            player.playSound(player, Sound.BLOCK_BEACON_DEACTIVATE, 1f, 0.5f);
        }

        Location location = getWaypointLocation(item);

        meta.getPersistentDataContainer().set(new NamespacedKey(Server.getPlugin(), "waypointuses"), PersistentDataType.INTEGER, newUses);
        meta.lore(List.of(MiniMessage.miniMessage().deserialize(""), MiniMessage.miniMessage().deserialize(
                "<gray><!i>Teleportation uses left: <red>5</red></gray>"
        ), MiniMessage.miniMessage().deserialize(

                String.format(

                        "<gray><!i>Waypoint Location: <red>%d, %d, %d</red></gray>",
                        location.getBlockX(), location.getBlockY(), location.getBlockZ()

                )

        ), MiniMessage.miniMessage().deserialize("")));

        item.setItemMeta(meta);

    }

    public static boolean isTeleporting(Player player) {
        return teleporting.containsKey(player);
    }

    public static boolean isValidTeleport(Location location) {

        return (location.getBlock().getType().equals(Material.LODESTONE) && !location.clone().add(0,1,0).getBlock().isSolid() && !location.clone().add(0,2,0).getBlock().isSolid());

    }

    public static Location getWaypointLocation(ItemStack item) {

        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();

        int[] locationArray = container.getOrDefault(new NamespacedKey(Server.getPlugin(), "waypoint"), PersistentDataType.INTEGER_ARRAY, new int[]{0, 220, 0});

        return new Location(world, locationArray[0], locationArray[1], locationArray[2]);
    }

    public static int getUsesLeft(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();

        return container.getOrDefault(new NamespacedKey(Server.getPlugin(), "waypointuses"), PersistentDataType.INTEGER, 0);

    }

    public static void teleport(Player player, ItemStack compass, Location location) {

        AtomicInteger loops = new AtomicInteger();

        BukkitTask task = Bukkit.getScheduler().runTaskTimer(Server.getPlugin(), () -> {

            if (loops.get() <= 9) {

                player.sendMessage(MiniMessage.miniMessage().deserialize(

                        String.format(

                                "<yellow>You will be teleported in %d seconds! Do not move or your teleportation will be cancelled.",
                                10 - loops.get()

                        )

                ));

                player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BANJO, 1.0F, 2.0F);

            }

            if (loops.get() >= 10) {

                if (isValidTeleport(location)) {
                    updateCompass(player, compass, getUsesLeft(compass) - 1);
                    player.teleport(location.add(0.5, 1, 0.5));
                    player.playSound(player, Sound.ENTITY_FOX_TELEPORT, 1.0F, 2.0F);
                } else {

                    player.sendMessage(

                            MiniMessage.miniMessage().deserialize(

                                    "<red>This waypoint location is invalid. Your lodestone has either been broken or obstructed."

                            )

                    );

                    player.playSound(player, Sound.BLOCK_BEACON_DEACTIVATE, 1f, 0.5f);

                }

                teleporting.get(player).cancel();
                teleporting.remove(player);

            }

            loops.getAndIncrement();

        }, 0L, 20L);

        teleporting.put(player, task);


    }

    public static void cancelTeleport(Player player) {
        teleporting.get(player).cancel();
        teleporting.remove(player);
        player.sendMessage(

                MiniMessage.miniMessage().deserialize(

                        "<red>Your teleportation has been cancelled."

                )

        );
    }

    public static void handleClick(PlayerInteractEvent event) {

        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (LodestoneTeleporter.isLodestoneCompass(item) && getUsesLeft(item) > 0) {

            if (isTeleporting(player)) {
                player.sendMessage(MiniMessage.miniMessage().deserialize(

                        "<red>You are already teleporting!"

                ));
                event.setCancelled(true);
                return;
            }

            teleport(player, item, getWaypointLocation(item));

        } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getType().equals(Material.LODESTONE)) {

            event.setCancelled(true);

            if (!isLodestoneCompass(item)) Item.giveOrDrop(makeLodestoneCompass(item, event.getClickedBlock().getLocation()), player);

        }



    }

}
