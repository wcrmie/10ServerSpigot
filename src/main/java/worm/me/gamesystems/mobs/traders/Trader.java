package worm.me.gamesystems.mobs.traders;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Trader {

    public static class TradeBuilder {

        private final Map<MerchantRecipe, Integer> trades = new HashMap<>();

        public TradeBuilder newTrade(ItemStack selling, ItemStack buying, int maxUses, int weight) {
            MerchantRecipe recipe = new MerchantRecipe(selling, maxUses);
            recipe.addIngredient(buying);
            trades.put(recipe, weight);
            return this;
        }

        public Map<MerchantRecipe, Integer> build() {
            return Collections.unmodifiableMap(trades);
        }

    }

}
