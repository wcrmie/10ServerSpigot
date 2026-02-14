package worm.me.menu.recipes;

import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import worm.me.gamesystems.recipes.ShapedRecipes;
import worm.me.menu.MainSubMenu;
import worm.me.menu.PagedMenu;
import worm.me.utils.GuiConstructor;
import worm.me.menu.MainGUIMenu;
import worm.me.utils.Item;

public class RecipeBrowserMenu extends PagedMenu implements InventoryHolder, MainSubMenu {

    private final Inventory inventory;
    private int page;

    public RecipeBrowserMenu(Player viewer, int page) {
        super(viewer, page);
        inventory = initialize();
    }

    @Override
    public void turnPage(int page) {

    }

    @Override
    public Inventory initialize() {

        Inventory inventory = GuiConstructor.chestInventory(ChatColor.DARK_GRAY + "Recipes Page " + page, 54, this);

        if (page > 1){

            inventory.setItem(48, Item.create(Material.ARROW, ChatColor.GRAY + "Previous Page"));

        }

        inventory.setItem(50, Item.create(Material.ARROW, ChatColor.GRAY + "Next Page"));

        inventory.setItem(4, Item.create(Material.CRAFTING_TABLE, ChatColor.GRAY + "Recipes"));

        int slot = 9;

        for (int i = 0; i < 28; i++){

            if (i + (28 * (page - 1)) >= ShapedRecipes.shapedRecipeMap.size()){
                break;
            }

            ItemStack recipe = ShapedRecipes.shapedRecipeMap.keySet().toArray(ItemStack[] :: new)[i + (28 * (page - 1))];

            slot++;

            if ((slot + 1) % 9 == 0) {
                slot += 2;
            }

            inventory.setItem(slot, recipe);


        }

        return inventory;
    }

    @Override
    public void open() {

        getViewer().openInventory(inventory);

    }

    @Override
    public void handleClick(InventoryClickEvent e){
        e.setCancelled(true);

        if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) return;

        ItemStack item = e.getCurrentItem();

        Player player = (Player) e.getWhoClicked();

        if (item.displayName().contains(Component.text("Next Page"))){
            turnPage(getPage() + 1);

        } else if (item.displayName().contains(Component.text("Previous Page"))){
            turnPage(getPage() - 1);

        } else if (e.getCurrentItem().equals(Item.create(Material.SPECTRAL_ARROW, ChatColor.GRAY + "Go Back"))){

            MainGUIMenu menu = new MainGUIMenu(player);
            menu.open();

        } else if (ShapedRecipes.shapedRecipeMap.containsKey(item)) {

            RecipeSpecificMenu recipeSpecificMenu = new RecipeSpecificMenu(getViewer(), item);
            recipeSpecificMenu.open();


        }


    }


    @Override
    public @NotNull Inventory getInventory() {
        return null;
    }
}
