package worm.me.gamesystems.mobs.traders;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import worm.me.Server;

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

    public static void addGenetics(Villager villager, int[] genetics) {

        if (villager.getProfession().equals(Villager.Profession.NITWIT)) villager.setProfession(Villager.Profession.NONE);

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
