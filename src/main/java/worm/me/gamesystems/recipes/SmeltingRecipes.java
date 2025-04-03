package worm.me.gamesystems.recipes;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class SmeltingRecipes {

    public Map<ItemStack, ItemStack> smeltingRecipes = new HashMap<>();

    public void addSmeltingRecipes(){

        smeltingRecipes.put(new ItemStack(Material.GOLDEN_HELMET), new ItemStack(Material.GOLD_NUGGET, 5));
        smeltingRecipes.put(new ItemStack(Material.GOLDEN_CHESTPLATE), new ItemStack(Material.GOLD_NUGGET, 8));
        smeltingRecipes.put(new ItemStack(Material.GOLDEN_LEGGINGS), new ItemStack(Material.GOLD_NUGGET, 7));
        smeltingRecipes.put(new ItemStack(Material.GOLDEN_BOOTS), new ItemStack(Material.GOLD_NUGGET, 4));
        smeltingRecipes.put(new ItemStack(Material.IRON_HELMET), new ItemStack(Material.GOLD_NUGGET, 5));
        smeltingRecipes.put(new ItemStack(Material.IRON_CHESTPLATE), new ItemStack(Material.GOLD_NUGGET, 8));
        smeltingRecipes.put(new ItemStack(Material.IRON_LEGGINGS), new ItemStack(Material.GOLD_NUGGET, 7));
        smeltingRecipes.put(new ItemStack(Material.IRON_BOOTS), new ItemStack(Material.GOLD_NUGGET, 4));
        smeltingRecipes.put(new ItemStack(Material.CHAINMAIL_HELMET), new ItemStack(Material.GOLD_NUGGET, 4));
        smeltingRecipes.put(new ItemStack(Material.CHAINMAIL_CHESTPLATE), new ItemStack(Material.GOLD_NUGGET, 6));
        smeltingRecipes.put(new ItemStack(Material.CHAINMAIL_LEGGINGS), new ItemStack(Material.IRON_NUGGET, 5));
        smeltingRecipes.put(new ItemStack(Material.CHAINMAIL_BOOTS), new ItemStack(Material.IRON_NUGGET, 3));
        smeltingRecipes.put(new ItemStack(Material.RAW_IRON), new ItemStack(Material.IRON_NUGGET));
        smeltingRecipes.put(new ItemStack(Material.IRON_ORE), new ItemStack(Material.IRON_NUGGET));

    }

    public void initializeSmeltingRecipes(){

        addSmeltingRecipes();

        for (ItemStack itemStack : smeltingRecipes.keySet()) {

            NamespacedKey key = new NamespacedKey("void", itemStack.getType().getTranslationKey().toLowerCase() + "_smelting");

            if (Bukkit.getRecipe(key) != null) {
                continue;
            }

            Bukkit.addRecipe(new FurnaceRecipe(key, smeltingRecipes.get(itemStack), itemStack.getType(), 5F, 50));
        }


    }


}
