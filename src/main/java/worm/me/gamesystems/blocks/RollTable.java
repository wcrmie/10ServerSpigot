package worm.me.gamesystems.blocks;

import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ThreadLocalRandom;

public class RollTable {

    public static ItemStack rollTable(LootTable table) {

        int totalWeight = table.getWeights().keySet().stream().mapToInt(i -> table.getWeights().get(i)).sum();

        int RNG = ThreadLocalRandom.current().nextInt(0, totalWeight);

        int tempWeight = 0;

        for (ItemStack i : table.getWeights().keySet()) {
            tempWeight += table.getWeights().get(i);
            if (RNG <= tempWeight) {
                return i;
            }
        }
        return null;
    }
}
