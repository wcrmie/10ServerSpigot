package worm.me.GameSystems.Recipes;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import worm.me.Server;

import java.lang.constant.Constable;
import java.util.*;

public class Recipes {

    public static Map<ItemStack, List<? extends Constable>> shapedRecipeMap = new HashMap<>();

    public void addRecipes(){

        // ID 0
        shapedRecipeMap.put(new ItemStack(Material.RAIL, 4), List.of("CHEAP_RAILS", Material.STICK, Material.AIR, Material.STICK, Material.STICK, Material.IRON_NUGGET, Material.STICK, Material.STICK, Material.AIR, Material.STICK));
        shapedRecipeMap.put(new ItemStack(Material.POWERED_RAIL, 2), List.of("CHEAP_POWERED_RAILS", Material.STICK, Material.REDSTONE, Material.STICK, Material.STICK, Material.GOLD_NUGGET, Material.STICK, Material.STICK, Material.AIR, Material.STICK));


        shapedRecipes(shapedRecipeMap);

    }

    public void shapedRecipes(Map<ItemStack, List<? extends Constable>> recipe) {

        Set<ItemStack> keys = recipe.keySet();

        for (ItemStack key : keys) {

            List<? extends Constable> values = recipe.get(key);

            if (Bukkit.getRecipe(new NamespacedKey(Server.getPlugin(), values.getFirst().toString())) == null){
                ShapedRecipe shapedRecipe = new ShapedRecipe(new NamespacedKey(Server.getPlugin(), values.getFirst().toString()), key);

                String shape = "";

                for (int i = 1; i < 10; i++) {
                    if (values.get(i) == Material.AIR){
                        shape =  shape + " ";
                    } else {

                        char c = (char) (i + 64);

                        shape = shape + c;
                    }

                    if (i % 3 == 0){
                        shape = shape + ",";
                    }
                }

                shapedRecipe.shape(shape.split(","));

                System.out.println(Arrays.toString(shape.split(",")));

                for (int i = 1; i < 10; i++) {
                    if (values.get(i) == Material.AIR){
                        continue;
                    } else {

                        char c = (char) (i + 64);

                        shapedRecipe.setIngredient(c, (Material) values.get(i));
                    }

                    if ((i - 1) % 3 == 0 && i != 1){
                    }
                }

                Bukkit.addRecipe(shapedRecipe);

            }
        }
    }

    public void removeRecipes(){

        for (ItemStack i : shapedRecipeMap.keySet()) {
            Bukkit.removeRecipe(new NamespacedKey(Server.getPlugin(), shapedRecipeMap.get(i).getFirst().toString()));
        }


        Bukkit.removeRecipe(new NamespacedKey("minecraft", "wooden_pickaxe"));
        Bukkit.removeRecipe(new NamespacedKey("minecraft", "wooden_axe"));
        Bukkit.removeRecipe(new NamespacedKey("minecraft", "wooden_shovel"));
        Bukkit.removeRecipe(new NamespacedKey("minecraft", "wooden_sword"));

        Bukkit.removeRecipe(new NamespacedKey("minecraft", "stone_pickaxe"));
        Bukkit.removeRecipe(new NamespacedKey("minecraft", "stone_axe"));
        Bukkit.removeRecipe(new NamespacedKey("minecraft", "stone_shovel"));
        Bukkit.removeRecipe(new NamespacedKey("minecraft", "stone_sword"));

        Bukkit.removeRecipe(new NamespacedKey("minecraft", "iron_pickaxe"));
        Bukkit.removeRecipe(new NamespacedKey("minecraft", "iron_axe"));
        Bukkit.removeRecipe(new NamespacedKey("minecraft", "iron_shovel"));
        Bukkit.removeRecipe(new NamespacedKey("minecraft", "iron_sword"));

        Bukkit.removeRecipe(new NamespacedKey("minecraft", "diamond_pickaxe"));
        Bukkit.removeRecipe(new NamespacedKey("minecraft", "diamond_axe"));
        Bukkit.removeRecipe(new NamespacedKey("minecraft", "diamond_shovel"));
        Bukkit.removeRecipe(new NamespacedKey("minecraft", "diamond_sword"));

        Bukkit.removeRecipe(new NamespacedKey("minecraft", "golden_pickaxe"));
        Bukkit.removeRecipe(new NamespacedKey("minecraft", "golden_axe"));
        Bukkit.removeRecipe(new NamespacedKey("minecraft", "golden_shovel"));
        Bukkit.removeRecipe(new NamespacedKey("minecraft", "golden_sword"));
    }
}
