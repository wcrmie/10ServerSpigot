package worm.me.gamesystems.fishing;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.player.PlayerFishEvent;
import worm.me.playersystems.playerdata.levels.Levels;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


public class FishingSystem {

    private final Map<Class<? extends Entity>, List<Integer>> lootTable1 = new HashMap<>();
    private final Map<Material, List<Integer>> lootTable2 = new HashMap<>();
    private final Map<Class<? extends Entity>, List<Integer>> seaCreatureTable = new HashMap<>();
    private final Map<String, FishingConfiguration> fishingConfigurations = new HashMap<>();

    public FishingSystem() {
        lootTable1.put(Cod.class, List.of(60, 2));
        lootTable1.put(Salmon.class, List.of(25, 2));
        lootTable1.put(TropicalFish.class, List.of(2, 10));
        lootTable1.put(PufferFish.class, List.of(13, 5));


        lootTable2.put(Material.NAME_TAG, List.of(167, 5));
        lootTable2.put(Material.SADDLE, List.of(167, 10));

        seaCreatureTable.put(ElderGuardian.class, List.of(1, 25));
        seaCreatureTable.put(Guardian.class, List.of(9, 15));
        seaCreatureTable.put(Zombie.class, List.of(25, 5));

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

                    spawnEntityOnHook(event, lootTable1);
                    item.setItemStack(new ItemStack(Material.AIR));

                } else if (rng < config.getLootTable1Weight() + config.getLootTable2Weight()) {

                    ItemStack itemStack = generateFishingItem(lootTable2);

                    item.setItemStack(itemStack);

                    Levels.addExp(lootTable2.get(itemStack.getType()).get(1), 3, event.getPlayer());

                } else if (rng < config.getLootTable1Weight() + config.getLootTable2Weight() + config.getSeaCreatureWeight() ) {

                    spawnEntityOnHook(event, seaCreatureTable);

                    item.setItemStack(new ItemStack(Material.AIR));

                }
                else {
                    item.setItemStack(new ItemStack(Material.AIR));
                }
            }
        }
    }

    public void spawnEntityOnHook(PlayerFishEvent event, Map<Class<? extends Entity>, List<Integer>> table) {
        Location hookLocation = event.getHook().getLocation();
        double strength = ThreadLocalRandom.current().nextDouble(0.1, 0.3);

        Class<? extends Entity> entity = generateSeaCreature(table);

        if (entity != null) {

            Levels.addExp(table.get(entity).get(1), 3, event.getPlayer());

            Entity creature = hookLocation.getWorld().spawn(hookLocation, entity);

            Location playerLocation = event.getPlayer().getLocation().clone();
            org.bukkit.util.Vector direction = playerLocation.subtract(hookLocation).toVector().normalize();

            creature.setVelocity(direction.multiply(strength));
        }
    }

    public ItemStack generateFishingItem(Map<Material, List<Integer>> lootTable) {


        int totalWeight = 0;

        for (int i = 0; i < lootTable.size(); i++){

            totalWeight += lootTable.get(lootTable.keySet().toArray(new Material[2])[i]).getFirst();

        }
        int randomWeight = ThreadLocalRandom.current().nextInt(totalWeight);

        int currentWeight = 0;
        for (Map.Entry<Material, List<Integer>> entry : lootTable.entrySet()) {
            currentWeight += entry.getValue().getFirst();
            if (randomWeight < currentWeight) {
                return new ItemStack(entry.getKey());
            }
        }
        return new ItemStack(Material.AIR);
    }

    public Class<? extends Entity> generateSeaCreature(Map<Class<? extends Entity>, List<Integer>> table) {

        int totalWeight = 0;

        for (int i = 0; i < table.size(); i++){

            totalWeight += table.get(table.keySet().toArray(new Class[2])[i]).getFirst();

        }

        int randomWeight = ThreadLocalRandom.current().nextInt(totalWeight);

        int currentWeight = 0;
        for (Map.Entry<Class<? extends Entity>, List<Integer>> entry : table.entrySet()) {
            currentWeight += entry.getValue().getFirst();
            if (randomWeight < currentWeight) {
                return entry.getKey();
            }
        }

        return Cod.class;
    }
}