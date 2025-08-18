package worm.me.gamesystems.fishing;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.player.PlayerFishEvent;
import org.jetbrains.annotations.NotNull;
import worm.me.gamesystems.fishing.rods.RodHandler;
import worm.me.gamesystems.items.ExperienceBottle;
import worm.me.playersystems.data.levels.Levels;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


public class FishingTables {

    private final Map<Class<? extends Entity>, List<Integer>> lootTable1 = new TableBuilder<Class<? extends Entity>>()
            .add(Cod.class, 60, 2)
            .add(Salmon.class, 25, 2)
            .add(TropicalFish.class, 2, 10)
            .add(PufferFish.class, 13, 5)
            .build();

    private final Map<ItemStack, List<Integer>> lootTable2 = new TableBuilder<ItemStack>()
            .add(new ItemStack(Material.NAME_TAG), 8, 5)
            .add(ItemStack.of(Material.SADDLE), 7, 10)
            .add(ItemStack.of(Material.TRIPWIRE_HOOK), 1, 5)
            .add(ItemStack.of(Material.STRING), 12, 5)
            .add(ItemStack.of(Material.BELL), 4, 15)
            .add(ItemStack.of(Material.ANGLER_POTTERY_SHERD), 2, 20)
            .add(ItemStack.of(Material.MUSIC_DISC_13), 5, 10)
            .add(ItemStack.of(Material.LEATHER), 8, 5)
            .add(ItemStack.of(Material.LEAD), 7, 5)
            .add(ItemStack.of(Material.ROTTEN_FLESH), 5, 5)
            .add(ItemStack.of(Material.BONE), 5, 5)
            .add(ExperienceBottle.fullBottle, 4, 15)
            .add(ItemStack.of(Material.KELP), 5, 10)
            .add(ItemStack.of(Material.SEA_PICKLE), 5, 10)
            .add(ItemStack.of(Material.SEAGRASS), 2, 5)
            .add(ItemStack.of(Material.SPONGE), 1, 25)
            .add(ItemStack.of(Material.FISHING_ROD), 1, 25)
            .add(ItemStack.of(Material.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE),1 ,25 )
            .build();

    private final Map<Class<? extends Entity>, List<Integer>> seaCreatureTable = new TableBuilder<Class<? extends Entity>>()
            .add(ElderGuardian.class, 1, 25)
            .add(Guardian.class, 3, 15)
            .add(Axolotl.class, 5, 10)
            .add(Squid.class, 10, 5)
            .add(GlowSquid.class, 7, 7)
            .add(Dolphin.class, 4, 12)
            .add(Tadpole.class, 2, 20)
            .add(Turtle.class, 4, 12)
            .add(Drowned.class, 5, 10)
            .add(Bogged.class, 3, 15)
            .add(Cat.class, 1, 25)
            .add(Slime.class, 3, 15)
            .add(WanderingTrader.class, 1, 25)
            .add(Silverfish.class, 1, 25)
            .build();

    private final Map<Class<? extends Entity>, List<Integer>> netherCreatureTable = new TableBuilder<Class<? extends Entity>>()
            .add(Strider.class, 100, 10)
            .add(Ghast.class, 25, 50)
            .add(Blaze.class, 1, 250)
            .add(MagmaCube.class, 75, 15)
            .add(PigZombie.class, 65, 17)
            .build();

    private static final Map<String, FishingConfiguration> fishingConfigurations = new HashMap<>();

    public FishingTables() {
        fishingConfigurations.put("DEFAULT", new FishingConfiguration(20,40,40, null));
        fishingConfigurations.put("HOT_ROD", new FishingConfiguration(20,40,40, 10));
        fishingConfigurations.put("2", new FishingConfiguration(0,0,0, null));
    }

    public static class FishingConfiguration {
        private final int lootTable1Weight;
        private final int lootTable2Weight;
        private final int seaCreatureWeight;

        public FishingConfiguration(int lootTable1Weight, int lootTable2Weight, int seaCreatureWeight, Integer netherCreatureWeight) {
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

    public static class TableBuilder<T> {

        private HashMap<T, List<Integer>> table;

        public TableBuilder(){
            table = new HashMap<>();
        }

        public TableBuilder<T> add(T fishedThing, int weight, int exp) {
            table.put(fishedThing, List.of(weight, exp));
            return this;
        }

        public Map<T, List<Integer>> build() {
            return Collections.unmodifiableMap(table);
        }

    }

    public void handleFishing(PlayerFishEvent event) {
        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {

            ItemStack rodItem = event.getPlayer().getInventory().getItem(Objects.requireNonNull(event.getHand()));



            // Uses the first fishing configuration (DEFAULT); When making new configurations, configName should be given through RodHandler.
            String configName = RodHandler.catchFish(rodItem);

            FishingConfiguration config = fishingConfigurations.get(configName);
            int totalWeight = config.getTotalWeight();

            // Set the rng weight
            double rng = ThreadLocalRandom.current().nextDouble(0.0, totalWeight);
            Item item = (Item) event.getCaught();

            if (item != null) {
                if (rng < config.getLootTable1Weight()) {

                    spawnEntityOnHook(event, lootTable1);
                    item.setItemStack(new ItemStack(Material.AIR));

                } else if (rng < config.getLootTable1Weight() + config.getLootTable2Weight()) {

                    ItemStack itemStack = generateFishingItem(lootTable2);

                    item.setItemStack(itemStack);

                    Levels.addExp(lootTable2.get(itemStack).get(1), 3, event.getPlayer());

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

        while (hookLocation.getBlock().getType() == Material.WATER) {
            hookLocation.add(0, 1.5, 0);
        }

        Class<? extends Entity> entity = generateSeaCreature(table);

        if (entity != null) {

            Levels.addExp(table.get(entity).get(1), 3, event.getPlayer());

            Entity creature = hookLocation.getWorld().spawn(hookLocation, entity);

            Location playerLocation = event.getPlayer().getLocation().clone();
            org.bukkit.util.Vector direction = playerLocation.subtract(hookLocation).toVector().normalize();

            creature.setVelocity(direction.multiply(0.8));
        }
    }

    public ItemStack generateFishingItem(Map<ItemStack, List<Integer>> lootTable) {


        int totalWeight = 0;

        for (int i = 0; i < lootTable.size(); i++){

            totalWeight += lootTable.get(lootTable.keySet().toArray(new ItemStack[2])[i]).getFirst();

        }
        int randomWeight = ThreadLocalRandom.current().nextInt(totalWeight);

        int currentWeight = 0;
        for (Map.Entry<ItemStack, List<Integer>> entry : lootTable.entrySet()) {
            currentWeight += entry.getValue().getFirst();
            if (randomWeight < currentWeight) {
                return entry.getKey();
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