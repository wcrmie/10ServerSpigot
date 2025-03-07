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

    private final Map<Material, Integer> lootTable1 = new HashMap<>();
    private final Map<Material, Integer> lootTable2 = new HashMap<>();
    private final Map<Class<? extends Entity>, Integer> seaCreatureTable = new HashMap<>();
    private final Map<String, FishingConfiguration> fishingConfigurations = new HashMap<>();

    public FishingSystem(Server plugin) {
        lootTable1.put(Material.COD, 1);
        lootTable1.put(Material.SALMON, 1);

        lootTable2.put(Material.PUFFERFISH, 5);
        lootTable2.put(Material.SADDLE, 5);

        seaCreatureTable.put(Cod.class, 5);
        seaCreatureTable.put(Salmon.class, 4);
        seaCreatureTable.put(Zombie.class, 1);

        fishingConfigurations.put("0", new FishingConfiguration(40,40,20));
        fishingConfigurations.put("1", new FishingConfiguration(0,0,0));
        fishingConfigurations.put("2", new FishingConfiguration(0,0,0));
    }

    private static class FishingConfiguration {
        private final int lootTable1Weight;
        private final int lootTable2Weight;
        private final int seaCreatureWeight;

        public FishingConfiguration(int lootTable1Weight, int lootTable2Weight, int seaCreatureWeight) {
            this.lootTable1Weight = lootTable1Weight;
            this.lootTable2Weight = lootTable2Weight;
            this.seaCreatureWeight = seaCreatureWeight;
        }

        public int getLootTable1Weight() {
            return lootTable1Weight;
        }

        public int getLootTable2Weight() {
            return lootTable2Weight;
        }

        public int getSeaCreatureWeight() {
            return seaCreatureWeight;
        }

        public int getTotalWeight() {
            return lootTable1Weight + lootTable2Weight + seaCreatureWeight;
        }
    }

    public void handleFishing(PlayerFishEvent event) {
        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {

            String configName = "0";

            FishingConfiguration config = fishingConfigurations.get(configName);
            int totalWeight = config.getTotalWeight();

            double rng = ThreadLocalRandom.current().nextDouble(0.0, totalWeight);
            Item item = (Item) event.getCaught();

            if (item != null) {
                if (rng < config.getLootTable1Weight()) {
                    item.setItemStack(generateFishingItem(lootTable1));
                } else if (rng < config.getLootTable1Weight() + config.getLootTable2Weight()) {
                    item.setItemStack(generateFishingItem(lootTable2));
                } else if (rng < config.getLootTable1Weight() + config.getLootTable2Weight() + config.getSeaCreatureWeight() ) {
                    spawnEntityOnHook(event);
                    item.setItemStack(new ItemStack(Material.AIR));
                }
                else {
                    item.setItemStack(new ItemStack(Material.AIR));
                }
            }
        }
    }

    public void spawnEntityOnHook(PlayerFishEvent event) {
        Location hookLocation = event.getHook().getLocation();
        double strength = ThreadLocalRandom.current().nextDouble(0.1, 0.3);

        Entity entity = generateSeaCreature(hookLocation);

        if (entity != null && event.getPlayer() != null) {
            Location playerLocation = event.getPlayer().getLocation().clone();
            org.bukkit.util.Vector direction = playerLocation.subtract(hookLocation).toVector().normalize();

            entity.setVelocity(direction.multiply(strength));
        }
    }

    public ItemStack generateFishingItem(Map<Material, Integer> lootTable) {
        int totalWeight = sumInt(lootTable.values());
        int randomWeight = ThreadLocalRandom.current().nextInt(totalWeight);

        int currentWeight = 0;
        for (Map.Entry<Material, Integer> entry : lootTable.entrySet()) {
            currentWeight += entry.getValue();
            if (randomWeight < currentWeight) {
                return new ItemStack(entry.getKey());
            }
        }
        return new ItemStack(Material.AIR);
    }

    public Entity generateSeaCreature(Location loc) {
        int totalWeight = sumInt(seaCreatureTable.values());
        int randomWeight = ThreadLocalRandom.current().nextInt(totalWeight);

        int currentWeight = 0;
        for (Map.Entry<Class<? extends Entity>, Integer> entry : seaCreatureTable.entrySet()) {
            currentWeight += entry.getValue();
            if (randomWeight < currentWeight) {
                return loc.getWorld().spawn(loc, entry.getKey());
            }
        }

        return loc.getWorld().spawn(loc, Cod.class);
    }

    public int sumInt(Collection<Integer> list) {
        int sum = 0;
        for (int i : list) {
            sum += i;
        }
        return sum;
    }
}