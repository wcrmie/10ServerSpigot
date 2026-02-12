package worm.me.gamesystems.world.mobs.traders;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import worm.me.Server;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Trader {

    public static class TradeBuilder {

        private final Map<MerchantRecipe, Integer> trades = new HashMap<>();

        public TradeBuilder newTrade(ItemStack selling, ItemStack buying, int maxUses, int weight, boolean experienceReward, int villagerExperience) {
            MerchantRecipe recipe = new MerchantRecipe(selling, 0, maxUses, experienceReward, villagerExperience, 0.05F);
            recipe.addIngredient(buying);
            trades.put(recipe, weight);
            return this;
        }

        public Map<MerchantRecipe, Integer> build() {
            return Collections.unmodifiableMap(trades);
        }

    }

    public static MerchantRecipe replaceTrade(Villager villager, int level) {

        Villager.Profession profession = villager.getProfession();

        VillagerProfession villagerProfession = VillagerProfession.valueOf(profession.name().toUpperCase());

        List<MerchantRecipe> trades = new ArrayList<>(villager.getRecipes());

        for (int i = 0; i < 1000; i++) {

            MerchantRecipe newRecipe = getRandomTrade(villagerProfession.getTrades().get(level - 1));

            for (MerchantRecipe trade : trades) {

                if (trade.getIngredients().equals(newRecipe.getIngredients()) && trade.getResult().equals(newRecipe.getResult())) {

                    newRecipe = null;

                }
            }

            if (newRecipe != null) {

                return newRecipe;

            }

        }
        return null;

    }

    public static MerchantRecipe getRandomTrade(Map<MerchantRecipe, Integer> trades){

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

    public static void addGenetics(Villager villager, int[] genetics) {

        int[] newGenetics = getGenetics(villager);

        for (int i = 0; i < genetics.length; i++) {
            if (newGenetics[i] == 0 && genetics[i] == 1) {
                newGenetics[i] = 1; // Add the genetic if it was not already present
            }
        }

        PersistentDataContainer pdc = villager.getPersistentDataContainer();
        pdc.set(new NamespacedKey(Server.getPlugin(), "genetics"), PersistentDataType.INTEGER_ARRAY, newGenetics);

    }

    public static int[] getGenetics(Villager villager) {
        PersistentDataContainer pdc = villager.getPersistentDataContainer();
        return pdc.getOrDefault(new NamespacedKey(Server.getPlugin(), "genetics"), PersistentDataType.INTEGER_ARRAY, new int[VillagerProfession.values().length]);
    }

    public static boolean getFirstGen(Villager villager) {
        PersistentDataContainer pdc = villager.getPersistentDataContainer();
        return pdc.getOrDefault(new NamespacedKey(Server.getPlugin(), "firstGen"), PersistentDataType.BOOLEAN, false);
    }

    public static void setFirstGen(Villager villager, boolean firstGen) {
        PersistentDataContainer pdc = villager.getPersistentDataContainer();
        pdc.set(new NamespacedKey(Server.getPlugin(), "firstGen"), PersistentDataType.BOOLEAN, firstGen);
    }

    public static class GeneticsBuilder {

        private final int[] genetics;


        public GeneticsBuilder() {
            this.genetics = new int[VillagerProfession.values().length];
        }

        public GeneticsBuilder addGenetic(VillagerProfession profession){

            genetics[profession.ordinal()] = 1;

            return this;
        }

        public GeneticsBuilder armorer() {
            return addGenetic(VillagerProfession.ARMORER);
        }
        public GeneticsBuilder butcher() {
            return addGenetic(VillagerProfession.BUTCHER);
        }
        public GeneticsBuilder cartographer() {
            return addGenetic(VillagerProfession.CARTOGRAPHER);
        }
        public GeneticsBuilder cleric() {
            return addGenetic(VillagerProfession.CLERIC);
        }
        public GeneticsBuilder farmer() {
            return addGenetic(VillagerProfession.FARMER);
        }
        public GeneticsBuilder fisherman() {
            return addGenetic(VillagerProfession.FISHERMAN);
        }
        public GeneticsBuilder fletcher() {
            return addGenetic(VillagerProfession.FLETCHER);
        }
        public GeneticsBuilder leatherworker() {
            return addGenetic(VillagerProfession.LEATHERWORKER);
        }
        public GeneticsBuilder librarian() {
            return addGenetic(VillagerProfession.LIBRARIAN);
        }
        public GeneticsBuilder mason() {
            return addGenetic(VillagerProfession.MASON);
        }
        public GeneticsBuilder shepherd() {
            return addGenetic(VillagerProfession.SHEPHERD);
        }
        public GeneticsBuilder toolsmith() {
            return addGenetic(VillagerProfession.TOOLSMITH);
        }
        public GeneticsBuilder weaponsmith() {
            return addGenetic(VillagerProfession.WEAPONSMITH);
        }

        public int[] build() {

            int[] copy = new int[genetics.length];
            System.arraycopy(genetics, 0, copy, 0, genetics.length);
            return copy;
        }
    }
}
