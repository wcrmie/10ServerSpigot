package worm.me.utils;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class RNG {

    /**
     *
     * @param weightedItems A map from something to an integer where the integer is the weight of the key in terms of
     *                      chance
     * @return some random thing from map based on weight
     * @param <T> something in map
     *
     */
    public static <T> T chooseRandom(Map<T, Integer> weightedItems) {

        int totalWeight = weightedItems.values().stream().mapToInt(Integer::intValue).sum();

        int randomWeight = ThreadLocalRandom.current().nextInt(totalWeight);

        int currentWeight = 0;
        for (T item : weightedItems.keySet()) {
            currentWeight += weightedItems.get(item);
            if (randomWeight < currentWeight) {
                return item;
            }
        }

        return null;

    }

}
