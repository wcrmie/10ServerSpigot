package worm.me.gamesystems.recipes;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import worm.me.GuiConstructor;
import worm.me.Server;

import java.util.*;

public class Recipes {

    public static Map<ItemStack, List<List<ItemStack>>> shapedRecipeMap = new HashMap<>();

    public void addRecipes(){

        // ID 0
        shapedRecipeMap.put(new ItemStack(Material.RAIL, 4), List.of(List.of(new ItemStack(Material.STICK), new ItemStack(Material.AIR), new ItemStack(Material.STICK), new ItemStack(Material.STICK), new ItemStack(Material.IRON_NUGGET), new ItemStack(Material.STICK),
                new ItemStack(Material.STICK), new ItemStack(Material.AIR), new ItemStack(Material.STICK)), List.of(GuiConstructor.createGuiItem(Material.STONE, "CHEAP_RAILS"))));
        shapedRecipeMap.put(new ItemStack(Material.POWERED_RAIL, 2), List.of(List.of(new ItemStack(Material.STICK), new ItemStack(Material.REDSTONE), new ItemStack(Material.STICK), new ItemStack(Material.STICK), new ItemStack(Material.GOLD_NUGGET), new ItemStack(Material.STICK),
               new ItemStack(Material.STICK), new ItemStack(Material.AIR), new ItemStack(Material.STICK)), List.of(GuiConstructor.createGuiItem(Material.STONE, "CHEAP_POWERED_RAILS"))));


        shapedRecipes(shapedRecipeMap);

    }

    public void shapedRecipes(Map<ItemStack, List<List<ItemStack>>> recipe) {

        Set<ItemStack> keys = recipe.keySet();

        for (ItemStack key : keys) {

            List<List<ItemStack>> values = recipe.get(key);

            if (Bukkit.getRecipe(new NamespacedKey(Server.getPlugin(), values.get(1).getFirst().getItemMeta().getDisplayName())) == null){
                ShapedRecipe shapedRecipe = new ShapedRecipe(new NamespacedKey(Server.getPlugin(), values.get(1).getFirst().getItemMeta().getDisplayName()), key);

                String shape = "";

                for (int i = 0; i < 9; i++) {
                    if (values.getFirst().get(i).getType() == Material.AIR){
                        shape =  shape + " ";
                    } else {

                        char c = (char) (i + 64);

                        shape = shape + c;
                    }

                    if ((i + 1) % 3 == 0){
                        shape = shape + ",";
                    }
                }

                shapedRecipe.shape(shape.split(","));

                System.out.println(Arrays.toString(shape.split(",")));

                for (int i = 0; i < 9; i++) {
                    if (values.getFirst().get(i).getType() != Material.AIR){

                        char c = (char) (i + 64);

                        shapedRecipe.setIngredient(c, new RecipeChoice.ExactChoice(values.getFirst().get(i)));

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


        Bukkit.removeRecipe(new NamespacedKey("minecraft", "leather_helmet"));
        Bukkit.removeRecipe(new NamespacedKey("minecraft", "iron_helmet"));
        Bukkit.removeRecipe(new NamespacedKey("minecraft", "gold_helmet"));
        Bukkit.removeRecipe(new NamespacedKey("minecraft", "diamond_helmet"));

        Bukkit.removeRecipe(new NamespacedKey("minecraft", "iron_chestplate"));
        Bukkit.removeRecipe(new NamespacedKey("minecraft", "iron_leggings"));
        Bukkit.removeRecipe(new NamespacedKey("minecraft", "iron_boots"));


    }
}
