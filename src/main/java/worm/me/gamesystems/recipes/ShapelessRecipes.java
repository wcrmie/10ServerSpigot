package worm.me.gamesystems.recipes;

import org.bukkit.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import worm.me.GuiConstructor;
import worm.me.Server;

import java.util.*;

public class ShapelessRecipes {

    public static Map<ItemStack, List<List<ItemStack>>> shapelessRecipeMap = new HashMap<>();

    public void addRecipes(){

        // ID 0
        shapelessRecipeMap.put(GuiConstructor.createGuiItem(Material.COOKED_MUTTON, ChatColor.WHITE + "Salted Jerky"), List.of(List.of(new ItemStack(Material.ROTTEN_FLESH), GuiConstructor.createGuiItem(Material.QUARTZ, ChatColor.WHITE + "Salt")), List.of(GuiConstructor.createGuiItem(Material.STONE, "SALTED_JERKY"))));

        // ID 1
        shapelessRecipeMap.put(new ItemStack(Material.STRING, 4), List.of(List.of(new ItemStack(Material.WHITE_WOOL)), List.of(GuiConstructor.createGuiItem(Material.STONE, "STRING"))));

        shapelessRecipes(shapelessRecipeMap);

    }

    public void shapelessRecipes(Map<ItemStack, List<List<ItemStack>>> recipe) {

        Set<ItemStack> keys = recipe.keySet();

        for (ItemStack key : keys) {

            List<List<ItemStack>> values = recipe.get(key);

            String keyName = values.get(1).getFirst().getItemMeta().getDisplayName();

            if (Bukkit.getRecipe(new NamespacedKey(Server.getPlugin(), keyName)) != null){

                Bukkit.removeRecipe(new NamespacedKey(Server.getPlugin(), keyName));

            }
                ShapelessRecipe shapelessRecipe = new ShapelessRecipe(new NamespacedKey(Server.getPlugin(), keyName), key);

                List<ItemStack> materials = values.getFirst();

                for (int i = 0; i < materials.size(); i++) {

                    Material material = materials.get(i).getType();

                    if (material != Material.AIR){

                        if (keyName.equals("SALTED_JERKY") && material == Material.ROTTEN_FLESH) {
                            shapelessRecipe.addIngredient(Material.ROTTEN_FLESH);
                        } else if (material == Material.WHITE_WOOL) {
                            shapelessRecipe.addIngredient(new RecipeChoice.MaterialChoice(Tag.WOOL));

                        } else shapelessRecipe.addIngredient(new RecipeChoice.ExactChoice(values.getFirst().get(i)));

                    }
                }
                Bukkit.addRecipe(shapelessRecipe);
        }
    }


}
