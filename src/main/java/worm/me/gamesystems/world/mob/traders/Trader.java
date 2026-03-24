package worm.me.gamesystems.world.mob.traders;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import worm.me.Server;
import worm.me.utils.RNG;

import java.util.*;

public class Trader {

    private static final NamespacedKey GENETICS_KEY = new NamespacedKey(Server.getPlugin(), "genetics");
    private static final NamespacedKey FIRST_GEN_KEY = new NamespacedKey(Server.getPlugin(), "firstGen");

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

    public static VillagerProfession getCustomProfession(Villager.Profession bukkitProf) {
        for (VillagerProfession customProf : VillagerProfession.values()) {
            if (customProf.getBukkitProfession().equals(bukkitProf)) {
                return customProf;
            }
        }
        return null;
    }

    public static MerchantRecipe replaceTrade(Villager villager, int level) {
        Villager.Profession profession = villager.getProfession();
        VillagerProfession customProf = getCustomProfession(profession);

        if (customProf == null) return null;

        List<MerchantRecipe> currentRecipes = villager.getRecipes();

        if (level <= 0 || level > customProf.getTrades().size()) {
            return null;
        }

        for (int i = 0; i < 1000; i++) {
            MerchantRecipe newRecipe = RNG.chooseRandom(customProf.getTrades().get(level - 1));
            if (newRecipe == null) continue;

            boolean duplicate = false;
            for (MerchantRecipe existingTrade : currentRecipes) {
                if (existingTrade.getIngredients().equals(newRecipe.getIngredients()) &&
                        existingTrade.getResult().equals(newRecipe.getResult())) {
                    duplicate = true;
                    break;
                }
            }

            if (!duplicate) {
                return newRecipe;
            }
        }
        return null;
    }

    public static void addGenetics(Villager villager, int[] incomingGenetics) {
        int[] currentGenetics = getGenetics(villager);

        for (int i = 0; i < Math.min(currentGenetics.length, incomingGenetics.length); i++) {
            if (incomingGenetics[i] == 1) {
                currentGenetics[i] = 1;
            }
        }

        villager.getPersistentDataContainer().set(GENETICS_KEY, PersistentDataType.INTEGER_ARRAY, currentGenetics);
    }

    public static int[] getGenetics(Villager villager) {
        PersistentDataContainer pdc = villager.getPersistentDataContainer();
        int[] data = pdc.get(GENETICS_KEY, PersistentDataType.INTEGER_ARRAY);
        return (data != null) ? data : new int[VillagerProfession.values().length];
    }

    public static boolean getFirstGen(Villager villager) {
        return villager.getPersistentDataContainer().getOrDefault(FIRST_GEN_KEY, PersistentDataType.BOOLEAN, false);
    }

    public static void setFirstGen(Villager villager, boolean firstGen) {
        villager.getPersistentDataContainer().set(FIRST_GEN_KEY, PersistentDataType.BOOLEAN, firstGen);
    }

    public static class GeneticsBuilder {
        private final int[] genetics;

        public GeneticsBuilder() {
            this.genetics = new int[VillagerProfession.values().length];
        }

        public GeneticsBuilder addGenetic(VillagerProfession profession) {
            genetics[profession.ordinal()] = 1;
            return this;
        }

        public GeneticsBuilder armorer() { return addGenetic(VillagerProfession.ARMORER); }
        public GeneticsBuilder butcher() { return addGenetic(VillagerProfession.BUTCHER); }
        public GeneticsBuilder cartographer() { return addGenetic(VillagerProfession.CARTOGRAPHER); }
        public GeneticsBuilder cleric() { return addGenetic(VillagerProfession.CLERIC); }
        public GeneticsBuilder farmer() { return addGenetic(VillagerProfession.FARMER); }
        public GeneticsBuilder fisherman() { return addGenetic(VillagerProfession.FISHERMAN); }
        public GeneticsBuilder fletcher() { return addGenetic(VillagerProfession.FLETCHER); }
        public GeneticsBuilder leatherworker() { return addGenetic(VillagerProfession.LEATHERWORKER); }
        public GeneticsBuilder librarian() { return addGenetic(VillagerProfession.LIBRARIAN); }
        public GeneticsBuilder mason() { return addGenetic(VillagerProfession.MASON); }
        public GeneticsBuilder shepherd() { return addGenetic(VillagerProfession.SHEPHERD); }
        public GeneticsBuilder toolsmith() { return addGenetic(VillagerProfession.TOOLSMITH); }
        public GeneticsBuilder weaponsmith() { return addGenetic(VillagerProfession.WEAPONSMITH); }

        public int[] build() {
            return genetics.clone();
        }
    }
}