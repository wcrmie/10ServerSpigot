package worm.me.gamesystems.recipes;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import worm.me.GuiConstructor;
import worm.me.playersystems.MainGUIMenu;
import worm.me.Server;

public class RecipesGUI {

    public void openMainGUI(Player p, int page){

        Inventory gui = GuiConstructor.chestInventory(ChatColor.DARK_GRAY + "Recipes Page " + page, 54);

        if (page > 1){

            gui.setItem(48, GuiConstructor.createGuiItem(Material.ARROW, ChatColor.GRAY + "Previous Page"));

        }

        gui.setItem(50, GuiConstructor.createGuiItem(Material.ARROW, ChatColor.GRAY + "Next Page"));

        gui.setItem(4, GuiConstructor.createGuiItem(Material.CRAFTING_TABLE, ChatColor.GRAY + "Recipes"));

        Recipes recipes = new Recipes();

        int slot = 10;

        for (int i = 0; i < 28; i++){

            Server.pluginMessage(ChatColor.RED + "" + slot + ", " + i, p);

            if (i + (28 * (page - 1)) >= recipes.shapedRecipeMap.size()){
                break;
            }

            ItemStack recipe = recipes.shapedRecipeMap.keySet().toArray(ItemStack[] :: new)[i + (28 * (page - 1))];

            slot = slot + i;

            if ((slot + 1) % 9 == 0) {
                slot += 2;
            }

            gui.setItem(slot, recipe);


        }

        p.openInventory(gui);

    }

    public void openRecipeGUI(Player p, ItemStack recipe){
        Inventory gui = GuiConstructor.chestInventory(ChatColor.DARK_GRAY + "Recipe of " + recipe.getTranslationKey(), 45);

        gui.setItem(23, GuiConstructor.createGuiItem(Material.CRAFTING_TABLE, " "));

        int slot = 9;

        for (int i = 0; i < 9; i++){

            slot ++;

            if (!Recipes.shapedRecipeMap.get(recipe).getFirst().get(i).getType().isAir()) {
                gui.setItem(slot, Recipes.shapedRecipeMap.get(recipe).getFirst().get(i));
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

        Recipes recipes = new Recipes();

        String invName = e.getView().getTitle();

        int page = Integer.parseInt(invName.split("Recipes Page ")[1]);

        if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Next Page")){
            openMainGUI((Player) e.getWhoClicked(), page + 1);

        } else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Previous Page")){
            openMainGUI((Player) e.getWhoClicked(), page - 1);

        } else if (e.getCurrentItem().equals(GuiConstructor.createGuiItem(Material.SPECTRAL_ARROW, ChatColor.GRAY + "Go Back"))){

            MainGUIMenu.openMainMenu((Player) e.getWhoClicked());


        }else if (recipes.shapedRecipeMap.containsKey(e.getCurrentItem())){
            openRecipeGUI((Player) e.getWhoClicked(), e.getCurrentItem());
        }


    }

    public void eventHandlerRecipe(InventoryClickEvent e){
        e.setCancelled(true);

        if (e.getCurrentItem().equals(GuiConstructor.createGuiItem(Material.SPECTRAL_ARROW, ChatColor.GRAY + "Go Back"))) {

            openMainGUI((Player) e.getWhoClicked(), 1);
        }

    }


}
