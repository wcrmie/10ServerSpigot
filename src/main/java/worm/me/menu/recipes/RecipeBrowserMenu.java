package worm.me.menu.recipes;

import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import worm.me.gamesystems.recipes.ShapedRecipes;
import worm.me.menu.MainSubMenu;
import worm.me.menu.PagedMenu;
import worm.me.utils.menu.GuiConstructor;
import worm.me.menu.MainGUIMenu;
import worm.me.utils.Item;
import worm.me.utils.menu.MenuItem;

public class RecipeBrowserMenu extends PagedMenu implements InventoryHolder, MainSubMenu {

    private final Inventory inventory;

    public RecipeBrowserMenu(Player viewer, int page) {
        super(viewer, page);
        inventory = initialize();
    }

    @Override
    public void turnPage(int page) {
        RecipeBrowserMenu newMenu = new RecipeBrowserMenu(getViewer(), getPage());
        newMenu.open();
    }

    @Override
    public Inventory initialize() {

        Inventory inventory = GuiConstructor.chestInventory("<dark_gray>Recipes Page " + getPage(), 54, this);

        if (getPage() > 1){

            inventory.setItem(48, MenuItem.PREVIOUS_PAGE.getItem());

        }

        inventory.setItem(50, MenuItem.NEXT_PAGE.getItem());

        inventory.setItem(4, Item.create(Material.CRAFTING_TABLE, "<gray>Recipes"));

        int slot = 9;

        for (int i = 0; i < 28; i++){

            if (i + (28 * (getPage() - 1)) >= ShapedRecipes.shapedRecipeMap.size()){
                break;
            }

            ItemStack recipe = ShapedRecipes.shapedRecipeMap.keySet().toArray(ItemStack[] :: new)[i + (28 * (getPage() - 1))];

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

        if (item.equals(MenuItem.NEXT_PAGE.getItem())) {
            turnPage(getPage() + 1);

        } else if (item.equals(MenuItem.PREVIOUS_PAGE.getItem())) {
            turnPage(getPage() - 1);

        } else if (item.equals(MenuItem.BACK.getItem())) {

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
