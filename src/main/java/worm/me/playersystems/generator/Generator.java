package worm.me.PlayerSystems.Generator;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import worm.me.Server;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class Generator {

    private final Server plugin;

    public Generator(Server plugin) {
        this.plugin = plugin;
    }


    public void generateItem(Player player){
        new BukkitRunnable() {
            @Override
            public void run() {
                if (player.isOnline()) {
                    if (hasEmptySlot(player)) player.getInventory().addItem(randomItem(player));
                } else cancel();
            }
        }.runTaskTimer(plugin, 200, 200);
    }

    // Check if the player has an empty slot
    public boolean hasEmptySlot(Player player) {
        Inventory inventory = player.getInventory();
        for (ItemStack item : inventory.getContents()) {
            if (item == null || item.getType() == Material.AIR) {
                return true;
            }
        }
        return false;
    }

    // Declares randomItem variable
    private Material randomItem;

    // Allows the setting of a random item.
    private void setRandomItem(Material m){
        randomItem = m;
    }

    // Generates the random item the player is given
    public ItemStack randomItem(Player p){
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
            setRandomItem(Material.valueOf(Objects.requireNonNullElse(container.get(genEdit.miscItem, PersistentDataType.STRING), Material.COAL.toString())));
        } else {
            setRandomItem(Material.IRON_ORE);
        }
        return new ItemStack(randomItem, 1);
    }

}
