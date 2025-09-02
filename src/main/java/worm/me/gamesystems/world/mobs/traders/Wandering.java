package worm.me.gamesystems.world.mobs.traders;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * author: worm
 */
public class Wandering {

    private final Map<MerchantRecipe, Integer> buying = new Trader.TradeBuilder()
            .newTrade(new ItemStack(Material.GOLD_NUGGET, 6), new ItemStack(Material.EMERALD), 5, 5, false, 0)
            .newTrade(new ItemStack(Material.GOLD_NUGGET, 4), new ItemStack(Material.IRON_INGOT, 4), 5, 5, false, 0)
            .newTrade(new ItemStack(Material.GOLD_NUGGET, 2), new ItemStack(Material.BREAD, 6), 5, 3, false, 0)
            .newTrade(new ItemStack(Material.GOLD_NUGGET, 4), new ItemStack(Material.WATER_BUCKET), 5, 2, false, 0)
            .newTrade(new ItemStack(Material.GOLD_NUGGET, 3), new ItemStack(Material.SLIME_BALL, 4), 5, 7, false, 0)
            .newTrade(new ItemStack(Material.GOLD_NUGGET, 4), new ItemStack(Material.STRING, 8), 5, 7, false, 0)
            .newTrade(new ItemStack(Material.GOLD_NUGGET, 4), new ItemStack(Material.COOKED_COD, 8), 5, 3, false, 0)
            .newTrade(new ItemStack(Material.GOLD_NUGGET, 3), new ItemStack(Material.PUFFERFISH, 2), 5, 2, false, 0)
            .newTrade(new ItemStack(Material.GOLD_NUGGET, 5), new ItemStack(Material.MILK_BUCKET), 5, 4, false, 0)
            .newTrade(new ItemStack(Material.GOLD_NUGGET, 2), new ItemStack(Material.CARROT, 16), 5, 4, false, 0)
            .build();

    private final Map<MerchantRecipe, Integer> plants = new Trader.TradeBuilder()
            .newTrade(new ItemStack(Material.COCOA_BEANS, 4), new ItemStack(Material.GOLD_NUGGET, 8), 15, 7, false, 0)
            .newTrade(new ItemStack(Material.SUGAR_CANE, 1), new ItemStack(Material.GOLD_NUGGET, 10), 15, 5, false, 0)
            .newTrade(new ItemStack(Material.CACTUS, 1), new ItemStack(Material.GOLD_NUGGET, 10), 15, 5, false, 0)
            .newTrade(new ItemStack(Material.KELP, 1), new ItemStack(Material.GOLD_NUGGET, 8), 15, 13, false, 0)
            .newTrade(new ItemStack(Material.MOSS_BLOCK, 1), new ItemStack(Material.GOLD_NUGGET, 12), 15, 6, false, 0)
            .newTrade(new ItemStack(Material.GLOW_LICHEN, 16), new ItemStack(Material.GOLD_NUGGET, 10), 15, 4, false, 0)
            .newTrade(new ItemStack(Material.BEETROOT_SEEDS, 4), new ItemStack(Material.GOLD_NUGGET, 8), 15, 11, false, 0)
            .newTrade(new ItemStack(Material.PUMPKIN_SEEDS, 4), new ItemStack(Material.GOLD_NUGGET, 8), 15, 13, false, 0)
            .newTrade(new ItemStack(Material.MELON_SEEDS, 4), new ItemStack(Material.GOLD_NUGGET, 11), 15, 6, false, 0)
            .newTrade(new ItemStack(Material.NETHER_WART, 1), new ItemStack(Material.GOLD_NUGGET, 15), 15, 5, false, 0)
            .newTrade(new ItemStack(Material.SUNFLOWER, 8), new ItemStack(Material.GOLD_NUGGET, 8), 15, 8, false, 0)
            .newTrade(new ItemStack(Material.ROSE_BUSH, 8), new ItemStack(Material.GOLD_NUGGET, 8), 15, 10, false, 0)
            .newTrade(new ItemStack(Material.LILAC, 8), new ItemStack(Material.GOLD_NUGGET, 8), 15, 9, false, 0)
            .newTrade(new ItemStack(Material.PEONY, 8), new ItemStack(Material.GOLD_NUGGET, 8), 15, 10, false, 0)
            .newTrade(new ItemStack(Material.SPORE_BLOSSOM, 4), new ItemStack(Material.GOLD_NUGGET, 8), 15, 5, false, 0)
            .newTrade(new ItemStack(Material.GLOW_BERRIES, 4), new ItemStack(Material.GOLD_NUGGET, 11), 15, 6, false, 0)
            .newTrade(new ItemStack(Material.PINK_PETALS, 16), new ItemStack(Material.GOLD_NUGGET, 8), 15, 10, false, 0)
            .newTrade(new ItemStack(Material.SWEET_BERRIES, 4), new ItemStack(Material.GOLD_NUGGET, 10), 15, 9, false, 0)
            .newTrade(new ItemStack(Material.VINE, 8), new ItemStack(Material.GOLD_NUGGET, 6), 15, 11, false, 0)
            .build();

    private final Map<MerchantRecipe, Integer> saps = new Trader.TradeBuilder()
            .newTrade(new ItemStack(Material.OAK_SAPLING, 1), new ItemStack(Material.GOLD_NUGGET, 10), 15, 2, false, 0)
            .newTrade(new ItemStack(Material.SPRUCE_SAPLING, 1), new ItemStack(Material.GOLD_NUGGET, 12), 15, 5, false, 0)
            .newTrade(new ItemStack(Material.BIRCH_SAPLING, 1), new ItemStack(Material.GOLD_NUGGET, 9), 15, 5, false, 0)
            .newTrade(new ItemStack(Material.JUNGLE_SAPLING, 1), new ItemStack(Material.GOLD_NUGGET, 8), 15, 5, false, 0)
            .newTrade(new ItemStack(Material.ACACIA_SAPLING, 1), new ItemStack(Material.GOLD_NUGGET, 10), 15, 5, false, 0)
            .newTrade(new ItemStack(Material.DARK_OAK_SAPLING, 1), new ItemStack(Material.GOLD_NUGGET, 5), 15, 5, false, 0)
            .newTrade(new ItemStack(Material.MANGROVE_PROPAGULE, 1), new ItemStack(Material.GOLD_NUGGET, 14), 15, 3, false, 0)
            .newTrade(new ItemStack(Material.CHERRY_SAPLING, 1), new ItemStack(Material.GOLD_NUGGET, 11), 15, 3, false, 0)
            .newTrade(new ItemStack(Material.FLOWERING_AZALEA, 1), new ItemStack(Material.GOLD_NUGGET, 4), 15, 3, false, 0)
            .newTrade(new ItemStack(Material.PALE_OAK_SAPLING, 1), new ItemStack(Material.GOLD_NUGGET, 7), 15, 3, false, 0)
            .build();

    private final Map<MerchantRecipe, Integer> rarities = new Trader.TradeBuilder()
            .newTrade(new ItemStack(Material.BLAZE_ROD, 1), new ItemStack(Material.GOLD_INGOT, 4), 15, 5, false, 0)
            .newTrade(new ItemStack(Material.ENDER_PEARL, 2), new ItemStack(Material.GOLD_NUGGET, 6), 15, 5, false, 0)
            .newTrade(new ItemStack(Material.GHAST_TEAR, 2), new ItemStack(Material.GOLD_INGOT, 2), 15, 5, false, 0)
            .newTrade(new ItemStack(Material.MAGMA_CREAM, 4), new ItemStack(Material.GOLD_NUGGET, 6), 15, 10, false, 0)
            .newTrade(new ItemStack(Material.MUSIC_DISC_PIGSTEP, 1), new ItemStack(Material.GOLD_INGOT, 2), 15, 4, false, 0)
            .newTrade(new ItemStack(Material.MUSIC_DISC_OTHERSIDE, 1), new ItemStack(Material.GOLD_INGOT, 2), 15, 4, false, 0)
            .newTrade(new ItemStack(Material.BREEZE_ROD, 2), new ItemStack(Material.GOLD_INGOT, 1), 15, 6, false, 0)
            .newTrade(new ItemStack(Material.PIGLIN_HEAD, 1), new ItemStack(Material.GOLD_INGOT, 4), 15, 3, false, 0)
            .newTrade(new ItemStack(Material.TRIAL_KEY, 1), new ItemStack(Material.GOLD_INGOT, 3), 5, 3, false, 0)
            .newTrade(new ItemStack(Material.WILD_ARMOR_TRIM_SMITHING_TEMPLATE, 1), new ItemStack(Material.GOLD_INGOT, 1), 15, 5, false, 0)
            .newTrade(new ItemStack(Material.COAST_ARMOR_TRIM_SMITHING_TEMPLATE, 1), new ItemStack(Material.GOLD_INGOT, 1), 15, 5, false, 0)
            .newTrade(new ItemStack(Material.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE, 1), new ItemStack(Material.GOLD_INGOT, 1), 15, 5, false, 0)
            .newTrade(new ItemStack(Material.PIGLIN_BANNER_PATTERN, 1), new ItemStack(Material.GOLD_INGOT, 1), 15, 7, false, 0)
            .newTrade(new ItemStack(Material.GUSTER_BANNER_PATTERN, 1), new ItemStack(Material.GOLD_INGOT, 1), 15, 7, false, 0)
            .build();

    private final Map<MerchantRecipe, Integer> blocks = new Trader.TradeBuilder()
            .newTrade(new ItemStack(Material.BLACKSTONE, 16), new ItemStack(Material.GOLD_NUGGET, 4), 15, 5, false, 0)
            .newTrade(new ItemStack(Material.CALCITE, 16), new ItemStack(Material.GOLD_NUGGET, 4), 15, 5, false, 0)
            .newTrade(new ItemStack(Material.ICE, 16), new ItemStack(Material.GOLD_NUGGET, 4), 15, 4, false, 0)
            .newTrade(new ItemStack(Material.SOUL_SAND, 16), new ItemStack(Material.GOLD_NUGGET, 10), 15, 4, false, 0)
            .newTrade(new ItemStack(Material.SOUL_SOIL, 16), new ItemStack(Material.GOLD_NUGGET, 7), 15, 4, false, 0)
            .newTrade(new ItemStack(Material.MOSSY_COBBLESTONE, 16), new ItemStack(Material.GOLD_NUGGET, 4), 15, 5, false, 0)
            .newTrade(new ItemStack(Material.DARK_PRISMARINE, 16), new ItemStack(Material.GOLD_NUGGET, 4), 15, 6, false, 0)
            .newTrade(new ItemStack(Material.SEA_LANTERN, 16), new ItemStack(Material.GOLD_NUGGET, 4), 15, 4, false, 0)
            .newTrade(new ItemStack(Material.PURPUR_BLOCK, 16), new ItemStack(Material.GOLD_NUGGET, 4), 15, 2, false, 0)
            .build();

    public void newTrades(org.bukkit.entity.WanderingTrader trader){

        trader.setRecipes(generateTrades());

    }

    public List<MerchantRecipe> generateTrades(){

        List<MerchantRecipe> trades = new ArrayList<>();

        List<Map<MerchantRecipe, Integer>> tradeTypes = List.of(buying, saps, plants, rarities, blocks);

        for (int j = 0; j < tradeTypes.size(); j++) {

            for (int i = 0; i < 2; i++) {

                if (j == 1) i++;

                MerchantRecipe newRecipe = getRandomTrade(tradeTypes.get(j));

                if (trades.contains(newRecipe)) {
                    newRecipe = null;
                }

                if (newRecipe != null) {
                    trades.add(newRecipe);
                } else {
                    Bukkit.getLogger().warning("TRADE IS NULL");
                    i--;
                }

            }

        }

        return trades;
    }

    /**
     * Returns a random trade from the given HashMap of trades.
     *
     * @param trades The HashMap of trades to choose from.
     * @return A random MerchantRecipe from the given HashMap.
     */
    public MerchantRecipe getRandomTrade(Map<MerchantRecipe, Integer> trades){

        int totalWeight = 0;

        for (int weight : trades.values()){
            totalWeight += weight;
        }

        int weight = ThreadLocalRandom.current().nextInt(0, totalWeight);

        int tempWeight = 0;

        for (MerchantRecipe recipe : trades.keySet()){

            tempWeight += trades.get(recipe);
            if (weight < tempWeight) {
                return recipe;
            }

        }

        Bukkit.getLogger().warning("TRADE CREATION FAILED");
        return null;
    }
}
