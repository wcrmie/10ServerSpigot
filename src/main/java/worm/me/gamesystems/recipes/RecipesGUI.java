package worm.me.gamesystems.recipes;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import worm.me.utils.GuiConstructor;
import worm.me.utils.info.MainGUIMenu;
import worm.me.utils.Item;

public class RecipesGUI {

    public void openMainGUI(Player p, int page){

        Inventory gui = GuiConstructor.chestInventory(ChatColor.DARK_GRAY + "Recipes Page " + page, 54);

        if (page > 1){

            gui.setItem(48, Item.create(Material.ARROW, ChatColor.GRAY + "Previous Page"));

        }

        gui.setItem(50, Item.create(Material.ARROW, ChatColor.GRAY + "Next Page"));

        gui.setItem(4, Item.create(Material.CRAFTING_TABLE, ChatColor.GRAY + "Recipes"));

        ShapedRecipes shapedRecipes = new ShapedRecipes();

        int slot = 9;

        for (int i = 0; i < 28; i++){

            if (i + (28 * (page - 1)) >= shapedRecipes.shapedRecipeMap.size()){
                break;
            }

            ItemStack recipe = shapedRecipes.shapedRecipeMap.keySet().toArray(ItemStack[] :: new)[i + (28 * (page - 1))];

            slot++;

            if ((slot + 1) % 9 == 0) {
                slot += 2;
            }

            gui.setItem(slot, recipe);


        }

        p.openInventory(gui);

    }

    public void openRecipeGUI(Player p, ItemStack recipe){
        Inventory gui = GuiConstructor.chestInventory(ChatColor.DARK_GRAY + "Recipe of " + recipe.getTranslationKey(), 45);

        gui.setItem(23, Item.create(Material.CRAFTING_TABLE, " "));

        int slot = 9;

        for (int i = 0; i < 9; i++){

            slot ++;

            if (!ShapedRecipes.shapedRecipeMap.get(recipe).getFirst().get(i).getType().isAir()) {
                gui.setItem(slot, ShapedRecipes.shapedRecipeMap.get(recipe).getFirst().get(i));
            } else {
                gui.setItem(slot, new ItemStack(Material.AIR));
            }

            if ((slot - 3) % 9 == 0) {
                slot += 6;
            }


        }

        gui.setItem(25, recipe);

        p.openInventory(gui);
    }

    public void eventHandlerMain(InventoryClickEvent e){
        e.setCancelled(true);

        ShapedRecipes shapedRecipes = new ShapedRecipes();

        String invName = e.getView().getTitle();

        int page = Integer.parseInt(invName.split("Recipes Page ")[1]);

        if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) return;

        if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Next Page")){
            openMainGUI((Player) e.getWhoClicked(), page + 1);

        } else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Previous Page")){
            openMainGUI((Player) e.getWhoClicked(), page - 1);

        } else if (e.getCurrentItem().equals(Item.create(Material.SPECTRAL_ARROW, ChatColor.GRAY + "Go Back"))){

            MainGUIMenu.openMainMenu((Player) e.getWhoClicked());


        }else if (shapedRecipes.shapedRecipeMap.containsKey(e.getCurrentItem())){
            openRecipeGUI((Player) e.getWhoClicked(), e.getCurrentItem());
        }


    }

    public void eventHandlerRecipe(InventoryClickEvent e){
        e.setCancelled(true);

        if (e.getCurrentItem().equals(Item.create(Material.SPECTRAL_ARROW, ChatColor.GRAY + "Go Back"))) {

            openMainGUI((Player) e.getWhoClicked(), 1);
        }

    }


}
