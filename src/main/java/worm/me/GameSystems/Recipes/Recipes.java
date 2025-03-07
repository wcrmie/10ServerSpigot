package worm.me.GameSystems.Recipes;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import worm.me.Server;

import java.lang.constant.Constable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Recipes {

    public static Map<ItemStack, List<? extends Constable>> shapedRecipeMap = new HashMap<>();

    public void addRecipes(){

        shapedRecipeMap.put(new ItemStack(Material.SKELETON_SKULL, 1), List.of("Skull_of_unprepared_player", Material.BONE, Material.BONE, Material.BONE, Material.BONE, Material.BONE, Material.BONE, Material.BONE, Material.BONE, Material.BONE));

        shapedRecipes(shapedRecipeMap);

    }

    public void shapedRecipes(Map<ItemStack, List<? extends Constable>> recipe) {

        Set<ItemStack> keys = recipe.keySet();

        for (ItemStack key : keys) {

            List<? extends Constable> values = recipe.get(key);

            if (Bukkit.getRecipe(new NamespacedKey(Server.getPlugin(), values.getFirst().toString())) == null){
                ShapedRecipe shapedRecipe = new ShapedRecipe(new NamespacedKey(Server.getPlugin(), values.getFirst().toString()), key);

                shapedRecipe.shape("ABC", "DEF", "GHI");

                shapedRecipe.setIngredient('A', (Material) values.get(1));
                shapedRecipe.setIngredient('B', (Material) values.get(2));
                shapedRecipe.setIngredient('C', (Material) values.get(3));
                shapedRecipe.setIngredient('D', (Material) values.get(4));
                shapedRecipe.setIngredient('E', (Material) values.get(5));
                shapedRecipe.setIngredient('F', (Material) values.get(6));
                shapedRecipe.setIngredient('G', (Material) values.get(7));
                shapedRecipe.setIngredient('H', (Material) values.get(8));
                shapedRecipe.setIngredient('I', (Material) values.get(9));

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
