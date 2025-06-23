package worm.me.gamesystems.mobs.traders;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.PotionMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * author: worm
 */
public class WanderingTrader {

    private static HashMap<MerchantRecipe, Integer> buying = new HashMap<>();
    private static HashMap<MerchantRecipe, Integer> plants = new HashMap<>();
    private static HashMap<MerchantRecipe, Integer> saps = new HashMap<>();
    private static HashMap<MerchantRecipe, Integer> rarities = new HashMap<>();
    private static HashMap<MerchantRecipe, Integer> blocks = new HashMap<>();

    public void newTrades(org.bukkit.entity.WanderingTrader trader){

        trader.setRecipes(generateTrades());

    }

    public List<MerchantRecipe> generateTrades(){

        List<MerchantRecipe> trades = new ArrayList<>();

        List<HashMap<MerchantRecipe, Integer>> tradeTypes = List.of(buying, saps, plants, rarities, blocks);

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
    public MerchantRecipe getRandomTrade(HashMap<MerchantRecipe, Integer> trades){

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

    public static void initRecipes(){
        // Buying
        MerchantRecipe emerald = new MerchantRecipe(new ItemStack(Material.GOLD_NUGGET, 6), 5);
        emerald.addIngredient(new ItemStack(Material.EMERALD));

        MerchantRecipe iron = new MerchantRecipe(new ItemStack(Material.GOLD_NUGGET, 4), 5);
        iron.addIngredient(new ItemStack(Material.IRON_INGOT, 4));

        MerchantRecipe bread = new MerchantRecipe(new ItemStack(Material.GOLD_NUGGET, 2), 5);
        bread.addIngredient(new ItemStack(Material.BREAD, 6));

        MerchantRecipe water = new MerchantRecipe(new ItemStack(Material.GOLD_NUGGET, 4), 5);
        water.addIngredient(new ItemStack(Material.WATER_BUCKET));

        MerchantRecipe slime = new MerchantRecipe(new ItemStack(Material.GOLD_NUGGET, 3), 5);
        slime.addIngredient(new ItemStack(Material.SLIME_BALL, 4));

        MerchantRecipe string = new MerchantRecipe(new ItemStack(Material.GOLD_NUGGET, 4), 5);
        string.addIngredient(new ItemStack(Material.STRING, 8));

        MerchantRecipe fish = new MerchantRecipe(new ItemStack(Material.GOLD_NUGGET, 4), 5);
        fish.addIngredient(new ItemStack(Material.COOKED_COD, 8));

        MerchantRecipe puffers = new MerchantRecipe(new ItemStack(Material.GOLD_NUGGET, 3), 5);
        puffers.addIngredient(new ItemStack(Material.PUFFERFISH, 2));

        MerchantRecipe milk = new MerchantRecipe(new ItemStack(Material.GOLD_NUGGET, 5), 5);
        milk.addIngredient(new ItemStack(Material.MILK_BUCKET));

        MerchantRecipe invis = new MerchantRecipe(new ItemStack(Material.GOLD_NUGGET, 2), 5);
        invis.addIngredient(new ItemStack(Material.CARROT, 16));

        buying.put(emerald, 5);
        buying.put(iron, 5);
        buying.put(bread, 3);
        buying.put(water, 2);
        buying.put(slime, 7);
        buying.put(string, 7);
        buying.put(fish, 3);
        buying.put(puffers, 2);
        buying.put(milk, 4);
        buying.put(invis, 4);

        // Plants
        MerchantRecipe cocoa = new MerchantRecipe(new ItemStack(Material.COCOA_BEANS, 4), 15);
        cocoa.addIngredient(new ItemStack(Material.GOLD_NUGGET, 8));

        MerchantRecipe sugarcane = new MerchantRecipe(new ItemStack(Material.SUGAR_CANE, 1), 15);
        sugarcane.addIngredient(new ItemStack(Material.GOLD_NUGGET, 10));

        MerchantRecipe cactus = new MerchantRecipe(new ItemStack(Material.CACTUS, 1), 15);
        cactus.addIngredient(new ItemStack(Material.GOLD_NUGGET, 10));

        MerchantRecipe kelp = new MerchantRecipe(new ItemStack(Material.KELP, 1), 15);
        kelp.addIngredient(new ItemStack(Material.GOLD_NUGGET, 8));

        MerchantRecipe moss = new MerchantRecipe(new ItemStack(Material.MOSS_BLOCK, 1), 15);
        moss.addIngredient(new ItemStack(Material.GOLD_NUGGET, 12));

        MerchantRecipe lichen = new MerchantRecipe(new ItemStack(Material.GLOW_LICHEN, 16), 15);
        lichen.addIngredient(new ItemStack(Material.GOLD_NUGGET, 10));

        MerchantRecipe beets = new MerchantRecipe(new ItemStack(Material.BEETROOT_SEEDS, 4), 15);
        beets.addIngredient(new ItemStack(Material.GOLD_NUGGET, 8));

        MerchantRecipe pumpkin = new MerchantRecipe(new ItemStack(Material.PUMPKIN_SEEDS, 4), 15);
        pumpkin.addIngredient(new ItemStack(Material.GOLD_NUGGET, 8));

        MerchantRecipe melon = new MerchantRecipe(new ItemStack(Material.MELON_SEEDS, 4), 15);
        melon.addIngredient(new ItemStack(Material.GOLD_NUGGET, 11));

        MerchantRecipe warts = new MerchantRecipe(new ItemStack(Material.NETHER_WART, 1), 15);
        warts.addIngredient(new ItemStack(Material.GOLD_NUGGET, 15));

        MerchantRecipe sunflower = new MerchantRecipe(new ItemStack(Material.SUNFLOWER, 8), 15);
        sunflower.addIngredient(new ItemStack(Material.GOLD_NUGGET, 8));

        MerchantRecipe rose = new MerchantRecipe(new ItemStack(Material.ROSE_BUSH, 8), 15);
        rose.addIngredient(new ItemStack(Material.GOLD_NUGGET, 8));

        MerchantRecipe lilac = new MerchantRecipe(new ItemStack(Material.LILAC, 8), 15);
        lilac.addIngredient(new ItemStack(Material.GOLD_NUGGET, 8));

        MerchantRecipe peony = new MerchantRecipe(new ItemStack(Material.PEONY, 8), 15);
        peony.addIngredient(new ItemStack(Material.GOLD_NUGGET, 8));

        MerchantRecipe spore = new MerchantRecipe(new ItemStack(Material.SPORE_BLOSSOM, 4), 15);
        spore.addIngredient(new ItemStack(Material.GOLD_NUGGET, 8));

        MerchantRecipe glow = new MerchantRecipe(new ItemStack(Material.GLOW_BERRIES, 4), 15);
        glow.addIngredient(new ItemStack(Material.GOLD_NUGGET, 11));

        MerchantRecipe petals = new MerchantRecipe(new ItemStack(Material.PINK_PETALS, 16), 15);
        petals.addIngredient(new ItemStack(Material.GOLD_NUGGET, 8));

        MerchantRecipe berries = new MerchantRecipe(new ItemStack(Material.SWEET_BERRIES, 4), 15);
        berries.addIngredient(new ItemStack(Material.GOLD_NUGGET, 10));

        MerchantRecipe vines = new MerchantRecipe(new ItemStack(Material.VINE, 8), 15);
        vines.addIngredient(new ItemStack(Material.GOLD_NUGGET, 6));

        plants.put(cocoa, 7);
        plants.put(sugarcane, 7);
        plants.put(cactus, 5);
        plants.put(kelp, 13);
        plants.put(moss, 6);
        plants.put(lichen, 4);
        plants.put(beets, 11);
        plants.put(pumpkin, 13);
        plants.put(melon, 6);
        plants.put(warts, 5);
        plants.put(sunflower, 8);
        plants.put(rose, 10);
        plants.put(lilac, 9);
        plants.put(peony, 10);
        plants.put(spore, 5);
        plants.put(glow, 6);
        plants.put(petals, 10);
        plants.put(berries, 9);
        plants.put(vines, 11);

        MerchantRecipe oak = new MerchantRecipe(new ItemStack(Material.OAK_SAPLING, 1), 15);
        oak.addIngredient(new ItemStack(Material.GOLD_NUGGET, 10));

        MerchantRecipe spruce = new MerchantRecipe(new ItemStack(Material.SPRUCE_SAPLING, 1), 15);
        spruce.addIngredient(new ItemStack(Material.GOLD_NUGGET, 12));

        MerchantRecipe birch = new MerchantRecipe(new ItemStack(Material.BIRCH_SAPLING, 1), 15);
        birch.addIngredient(new ItemStack(Material.GOLD_NUGGET, 9));

        MerchantRecipe jungle = new MerchantRecipe(new ItemStack(Material.JUNGLE_SAPLING, 1), 15);
        jungle.addIngredient(new ItemStack(Material.GOLD_NUGGET, 8));

        MerchantRecipe acacia = new MerchantRecipe(new ItemStack(Material.ACACIA_SAPLING, 1), 15);
        acacia.addIngredient(new ItemStack(Material.GOLD_NUGGET, 10));

        MerchantRecipe darkoak = new MerchantRecipe(new ItemStack(Material.DARK_OAK_SAPLING, 1), 15);
        darkoak.addIngredient(new ItemStack(Material.GOLD_NUGGET, 5));

        MerchantRecipe mangrove = new MerchantRecipe(new ItemStack(Material.MANGROVE_PROPAGULE, 1), 15);
        mangrove.addIngredient(new ItemStack(Material.GOLD_NUGGET, 14));

        MerchantRecipe cherry = new MerchantRecipe(new ItemStack(Material.CHERRY_SAPLING, 1), 15);
        cherry.addIngredient(new ItemStack(Material.GOLD_NUGGET, 11));

        MerchantRecipe azalea = new MerchantRecipe(new ItemStack(Material.FLOWERING_AZALEA, 1), 15);
        azalea.addIngredient(new ItemStack(Material.GOLD_NUGGET, 4));

        MerchantRecipe pale = new MerchantRecipe(new ItemStack(Material.PALE_OAK_SAPLING, 1), 15);
        pale.addIngredient(new ItemStack(Material.GOLD_NUGGET, 7));

        saps.put(oak, 2);
        saps.put(spruce, 5);
        saps.put(birch, 5);
        saps.put(jungle, 5);
        saps.put(acacia, 5);
        saps.put(darkoak, 5);
        saps.put(mangrove, 3);
        saps.put(cherry, 3);
        saps.put(azalea, 3);
        saps.put(pale, 3);

        // Rarities
        MerchantRecipe blazerod = new MerchantRecipe(new ItemStack(Material.BLAZE_ROD, 1), 15);
        blazerod.addIngredient(new ItemStack(Material.GOLD_INGOT, 4));

        MerchantRecipe enderpearl = new MerchantRecipe(new ItemStack(Material.ENDER_PEARL, 2), 15);
        enderpearl.addIngredient(new ItemStack(Material.GOLD_NUGGET, 6));

        MerchantRecipe ghast = new MerchantRecipe(new ItemStack(Material.GHAST_TEAR, 2), 15);
        ghast.addIngredient(new ItemStack(Material.GOLD_INGOT, 2));

        MerchantRecipe creamer = new MerchantRecipe(new ItemStack(Material.MAGMA_CREAM, 4), 15);
        creamer.addIngredient(new ItemStack(Material.GOLD_NUGGET, 6));

        MerchantRecipe pigstep = new MerchantRecipe(new ItemStack(Material.MUSIC_DISC_PIGSTEP, 1), 15);
        pigstep.addIngredient(new ItemStack(Material.GOLD_INGOT, 2));

        MerchantRecipe otherside = new MerchantRecipe(new ItemStack(Material.MUSIC_DISC_OTHERSIDE, 1), 15);
        otherside.addIngredient(new ItemStack(Material.GOLD_INGOT, 2));

        MerchantRecipe breezeRod = new MerchantRecipe(new ItemStack(Material.BREEZE_ROD, 2), 15);
        breezeRod.addIngredient(new ItemStack(Material.GOLD_INGOT, 1));

        MerchantRecipe piglinhead = new MerchantRecipe(new ItemStack(Material.PIGLIN_HEAD, 1), 15);
        piglinhead.addIngredient(new ItemStack(Material.GOLD_INGOT, 4));

        MerchantRecipe trialkey = new MerchantRecipe(new ItemStack(Material.TRIAL_KEY, 1), 5);
        trialkey.addIngredient(new ItemStack(Material.GOLD_INGOT, 3));

        MerchantRecipe wildArmorTrim = new MerchantRecipe(new ItemStack(Material.WILD_ARMOR_TRIM_SMITHING_TEMPLATE, 1), 15);
        wildArmorTrim.addIngredient(new ItemStack(Material.GOLD_INGOT, 1));

        MerchantRecipe coastArmorTrim = new MerchantRecipe(new ItemStack(Material.COAST_ARMOR_TRIM_SMITHING_TEMPLATE, 1), 15);
        coastArmorTrim.addIngredient(new ItemStack(Material.GOLD_INGOT, 1));

        MerchantRecipe duneArmorTrim = new MerchantRecipe(new ItemStack(Material.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE, 1), 15);
        duneArmorTrim.addIngredient(new ItemStack(Material.GOLD_INGOT, 1));

        MerchantRecipe snoutBanner = new MerchantRecipe(new ItemStack(Material.PIGLIN_BANNER_PATTERN, 1), 15);
        snoutBanner.addIngredient(new ItemStack(Material.GOLD_INGOT, 1));

        MerchantRecipe gusterBanner = new MerchantRecipe(new ItemStack(Material.GUSTER_BANNER_PATTERN, 1), 15);
        gusterBanner.addIngredient(new ItemStack(Material.GOLD_INGOT, 1));

        rarities.put(blazerod, 5);
        rarities.put(enderpearl, 5);
        rarities.put(ghast, 5);
        rarities.put(creamer, 10);
        rarities.put(pigstep, 4);
        rarities.put(otherside, 4);
        rarities.put(breezeRod, 6);
        rarities.put(piglinhead, 3);
        rarities.put(trialkey, 3);
        rarities.put(wildArmorTrim, 5);
        rarities.put(coastArmorTrim, 5);
        rarities.put(duneArmorTrim, 5);
        rarities.put(snoutBanner, 7);
        rarities.put(gusterBanner, 7);

        // Blocks
        MerchantRecipe blackstone = new MerchantRecipe(new ItemStack(Material.BLACKSTONE, 16), 15);
        blackstone.addIngredient(new ItemStack(Material.GOLD_NUGGET, 4));

        MerchantRecipe calcite = new MerchantRecipe(new ItemStack(Material.CALCITE, 16), 15);
        calcite.addIngredient(new ItemStack(Material.GOLD_NUGGET, 4));

        MerchantRecipe ice = new MerchantRecipe(new ItemStack(Material.ICE, 16), 15);
        ice.addIngredient(new ItemStack(Material.GOLD_NUGGET, 4));

        MerchantRecipe packedIce = new MerchantRecipe(new ItemStack(Material.SOUL_SAND, 16), 15);
        packedIce.addIngredient(new ItemStack(Material.GOLD_NUGGET, 10));

        MerchantRecipe soulSand = new MerchantRecipe(new ItemStack(Material.SOUL_SOIL, 16), 15);
        soulSand.addIngredient(new ItemStack(Material.GOLD_NUGGET, 7));

        MerchantRecipe quartz = new MerchantRecipe(new ItemStack(Material.MOSSY_COBBLESTONE, 16), 15);
        quartz.addIngredient(new ItemStack(Material.GOLD_NUGGET, 4));

        MerchantRecipe darkpris = new MerchantRecipe(new ItemStack(Material.DARK_PRISMARINE, 16), 15);
        darkpris.addIngredient(new ItemStack(Material.GOLD_NUGGET, 4));

        MerchantRecipe seaLanterns = new MerchantRecipe(new ItemStack(Material.SEA_LANTERN, 16), 15);
        seaLanterns.addIngredient(new ItemStack(Material.GOLD_NUGGET, 4));

        MerchantRecipe purpur = new MerchantRecipe(new ItemStack(Material.PURPUR_BLOCK, 16), 15);
        purpur.addIngredient(new ItemStack(Material.GOLD_NUGGET, 4));

        blocks.put(blackstone, 5);
        blocks.put(calcite, 5);
        blocks.put(ice, 4);
        blocks.put(packedIce, 4);
        blocks.put(soulSand, 4);
        blocks.put(quartz, 5);
        blocks.put(darkpris, 6);
        blocks.put(seaLanterns, 4);
        blocks.put(purpur, 2);



    }




}
