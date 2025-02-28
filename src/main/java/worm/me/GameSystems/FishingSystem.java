package worm.me.GameSystems;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.player.PlayerFishEvent;
import worm.me.server;
import org.bukkit.inventory.ItemStack;
import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;
import java.util.List;


public class FishingSystem {

    private final List<ItemStack> LootTable1 = new ArrayList<>();
    private final List<ItemStack> LootTable2 = new ArrayList<>();

    public FishingSystem(server plugin) {
        LootTable1.add(new ItemStack(Material.COD, 1));
        LootTable1.add(new ItemStack(Material.SALMON, 1));

        LootTable2.add(new ItemStack(Material.PUFFERFISH, 5));
    }

    public void handleFishing(PlayerFishEvent event) {

        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            Double RNG = ThreadLocalRandom.current().nextDouble(0.0, 100.0);
            Item item = (Item) event.getCaught();

            if (RNG < 30) {
                item.setItemStack(LootTable1.get(ThreadLocalRandom.current().nextInt(LootTable1.size())));
            } else if (RNG < 50) {
                item.setItemStack(LootTable2.get(ThreadLocalRandom.current().nextInt(LootTable2.size())));
            } else if (RNG < 90) {
                spawnEntityOnHook(event);
                item.setItemStack(new ItemStack(Material.AIR));
            } else {
                item.setItemStack(new ItemStack(Material.AIR));
            }
        }
    }


    private void spawnEntityOnHook(PlayerFishEvent event) {
        Location hookLocation = event.getHook().getLocation();
        Double RNG = ThreadLocalRandom.current().nextDouble(0.0, 100.0);
        Double RNG2 = ThreadLocalRandom.current().nextDouble(3.0, 5.0);
        Entity entity = null;

        if (RNG < 50) {
            entity = hookLocation.getWorld().spawn(hookLocation, Cod.class);
        } else if (RNG < 90) {
            entity = hookLocation.getWorld().spawn(hookLocation, Salmon.class);
        } else if (RNG < 95) {
            entity = hookLocation.getWorld().spawn(hookLocation, Zombie.class);
        } else {
            entity = hookLocation.getWorld().spawn(hookLocation, Zombie.class);
        }

        if (entity != null && event.getPlayer() != null) {
            Location playerLocation = event.getPlayer().getLocation();
            org.bukkit.util.Vector direction = playerLocation.subtract(hookLocation).toVector().normalize();

            entity.setVelocity(direction.multiply(RNG2));
        }

    }
}