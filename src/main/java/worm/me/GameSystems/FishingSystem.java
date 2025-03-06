package worm.me.GameSystems;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.player.PlayerFishEvent;
import worm.me.Server;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


public class FishingSystem {

    private final Map<Material, Integer> LootTable1 = new HashMap<>();
    private final Map<Material, Integer> LootTable2 = new HashMap<>();

    public FishingSystem(Server plugin) {
        LootTable1.put(Material.COD, 1);
        LootTable1.put(Material.SALMON, 1);
        LootTable2.put(Material.DIAMOND_BLOCK, 5);
        LootTable2.put(Material.EMERALD_BLOCK, 5);
    }

    public int sumInt(Collection<Integer> list) {
        int sum = 0;
        for (int i : list) {
            sum += i;
        }
        return sum;
    }

    public void handleFishing(PlayerFishEvent event) {

        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            Double RNG = ThreadLocalRandom.current().nextDouble(0.0, 100.0);
            Item item = (Item) event.getCaught();

            assert item != null;

            if (RNG <= 40) {
                item.setItemStack(generateFishingItem(LootTable1));
                event.getPlayer().sendMessage("1");

            } else if (RNG <= 80) {
                item.setItemStack(generateFishingItem(LootTable2));
                event.getPlayer().sendMessage("2");
            } else if (RNG <= 100) {
                spawnEntityOnHook(event);
                item.setItemStack(new ItemStack(Material.AIR));
                event.getPlayer().sendMessage("mob");

            } else {
                item.setItemStack(new ItemStack(Material.AIR));
                event.getPlayer().sendMessage("nothing");
            }
        }
    }


    public void spawnEntityOnHook(PlayerFishEvent event) {
        Location hookLocation = event.getHook().getLocation();
        double RNG2 = ThreadLocalRandom.current().nextDouble(0.1, 0.2);


        Entity entity = generateSeaCreature(hookLocation);

        if (entity != null && event.getPlayer() != null) {
            Location playerLocation = event.getPlayer().getLocation();
            org.bukkit.util.Vector direction = playerLocation.subtract(hookLocation).toVector();

            entity.setVelocity(direction.multiply(RNG2));
        }

    }

    public ItemStack generateFishingItem(Map<Material, Integer> map){

        int RNG = ThreadLocalRandom.current().nextInt(0, sumInt(map.values()));

        Server.getPlugin().broadcast("RNG: " + RNG + ", " + sumInt(map.values()));

        int excess = 0;

        for (Material m : map.keySet()){

            int value = map.get(m) + excess;
            if (map.get(m) >= RNG){
                return new ItemStack(m);
            }
            excess = value;
        }
        return new ItemStack(Material.AIR);
    }

    public Entity generateSeaCreature(Location loc){
        double RNG = ThreadLocalRandom.current().nextDouble(0.0, 100.0);
        Entity entity;

        // In the future, a Map could be created, linking the entity to the weight of spawning
        // In that case, the lower bound would be zero and the upper bound would be the sum of all weights.
        // The Keys would be the entities and the values would be the weights.

        if (RNG < 50) {
            entity = loc.getWorld().spawn(loc, Cod.class);
        } else if (RNG < 90) {
            entity = loc.getWorld().spawn(loc, Salmon.class);
        } else if (RNG < 95) {
            entity = loc.getWorld().spawn(loc, Zombie.class);
        } else {
            entity = loc.getWorld().spawn(loc, Zombie.class);
        }
        return entity;


    }
}