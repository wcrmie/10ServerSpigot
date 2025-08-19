package worm.me.playersystems.generator;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import io.papermc.paper.datacomponent.DataComponentTypes;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import worm.me.Server;
import worm.me.gamesystems.gear.tools.Tools;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

import static worm.me.utils.player.Online.hasEmptySlot;

public class Generator {


    public static void generateItem(Player player){
        new BukkitRunnable() {
            @Override
            public void run() {

                PersistentDataContainer container = player.getPersistentDataContainer();
                if (player.isOnline()){
                    if (hasEmptySlot(player) && container.getOrDefault(new NamespacedKey(Server.getPlugin(), "togglegens"), PersistentDataType.INTEGER, 0) != 1) {

                        ProtectedRegion region = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(player.getWorld())).getRegion("spawn");
                        Location loc = player.getLocation();

                        ItemStack item = randomItem(player);

                        // If the player is in spawn, don't damage them
                        if (region.contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()) && item.getType().equals(Material.IRON_ORE)) {
                            
                        } else {
                            player.getInventory().addItem(item);
                        }
                    }
                } else cancel();
            }
        }.runTaskTimer(Server.getPlugin(), 200, 200);
    }

    // Declares randomItem variable
    private static Material randomItem;

    // Allows the setting of a random item.
    private static void setRandomItem(Material m){
        randomItem = m;
    }

    // Generates the random item the player is given
    public static ItemStack randomItem(Player p){
        PersistentDataContainer container = p.getPersistentDataContainer();
        GeneratorEdit genEdit = new GeneratorEdit();
        int RNG = ThreadLocalRandom.current().nextInt(0,100);
        if (1 <= RNG && RNG <= 55) {
            setRandomItem(Material.valueOf(Objects.requireNonNullElse(container.get(genEdit.stoneBlock, PersistentDataType.STRING), Material.COBBLESTONE.toString())));
        } else if (55 < RNG && RNG <= 80) {
            setRandomItem(Material.valueOf(Objects.requireNonNullElse(container.get(genEdit.dirtBlock, PersistentDataType.STRING), Material.DIRT.toString())));
        } else if (80 < RNG && RNG <= 90) {
            setRandomItem(Material.valueOf(Objects.requireNonNullElse(container.get(genEdit.woodBlock, PersistentDataType.STRING), Material.OAK_PLANKS.toString())));
        } else if (90 < RNG && RNG <= 95) {
            setRandomItem(Material.valueOf(Objects.requireNonNullElse(container.get(genEdit.miscItem, PersistentDataType.STRING), Material.COAL_ORE.toString())));
            if (randomItem == Material.COAL) setRandomItem(Material.COAL_ORE);
        } else {
            setRandomItem(Material.IRON_ORE);
        }

        if (randomItem == Material.DIRT) {
            ItemStack dirt = new ItemStack(Material.DIRT, 1);
            dirt.setData(DataComponentTypes.MAX_STACK_SIZE, 99);
            return dirt;
        }

        return new ItemStack(randomItem, 1);
    }

}
