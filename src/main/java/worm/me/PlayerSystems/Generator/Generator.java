package worm.me.PlayerSystems.Generator;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import worm.me.server;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class Generator {

    private final server plugin;

    public Generator(server plugin) {
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
        if (1 <= RNG && RNG <= 45) {
            setRandomItem(plugin.stones.get(Objects.requireNonNullElse(container.get(genEdit.stoneBlock, PersistentDataType.INTEGER), 0)));
        } else if (45 < RNG && RNG <= 60) {
            setRandomItem(plugin.dirts.get(Objects.requireNonNullElse(container.get(genEdit.dirtBlock, PersistentDataType.INTEGER), 0)));
        } else if (60 < RNG && RNG <= 75) {
            setRandomItem(plugin.planks.get(Objects.requireNonNullElse(container.get(genEdit.woodBlock, PersistentDataType.INTEGER), 0)));
        } else if (75 < RNG && RNG <= 90) {
            setRandomItem(plugin.miscs.get(Objects.requireNonNullElse(container.get(genEdit.miscItem, PersistentDataType.INTEGER), 0)));
        } else {
            setRandomItem(Material.IRON_ORE);
        }
        return new ItemStack(randomItem, 1);
    }

}
