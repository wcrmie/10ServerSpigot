package worm.me.gamesystems.events;

import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public enum Event {
    KOTH("King of the Hill", 300, 60);

    Event(String name, int runTime, int weight) {

    }

    public static class EventRewardsBuilder {

        private HashMap<ItemStack, Integer> rewards = new HashMap<>();

        public EventRewardsBuilder addReward(ItemStack item, int weight) {
            rewards.put(item, weight);
            return this;
        }

    }

}
