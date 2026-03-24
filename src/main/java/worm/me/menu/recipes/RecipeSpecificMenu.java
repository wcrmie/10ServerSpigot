package worm.me.menu.recipes;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import worm.me.gamesystems.recipes.ShapedRecipes;
import worm.me.menu.Menu;
import worm.me.utils.menu.GuiConstructor;
import worm.me.utils.Item;

public class RecipeSpecificMenu extends Menu implements InventoryHolder {

    private final Inventory inventory;
    private final ItemStack itemStack;

    protected RecipeSpecificMenu(Player viewer, ItemStack itemStack) {
        super(viewer);
        this.itemStack = itemStack;
        this.inventory = initialize();

    }

    @Override
    public Inventory initialize() {

        Inventory inventory = GuiConstructor.chestInventory(ChatColor.DARK_GRAY + "Recipe of " + itemStack.displayName(), 45, this);

        inventory.setItem(23, Item.create(Material.CRAFTING_TABLE, " "));

        int slot = 9;

        for (int i = 0; i < 9; i++){

            slot ++;

            if (!ShapedRecipes.shapedRecipeMap.get(itemStack).getFirst().get(i).getType().isAir()) {
                inventory.setItem(slot, ShapedRecipes.shapedRecipeMap.get(itemStack).getFirst().get(i));
            } else {
                inventory.setItem(slot, new ItemStack(Material.AIR));
            }

            if ((slot - 3) % 9 == 0) {
                slot += 6;
            }

        }

        inventory.setItem(25, itemStack);



        return inventory;
    }

    @Override
    public void open(){

        getViewer().openInventory(inventory);

    }

    @Override
    public void handleClick(InventoryClickEvent event) {
        event.setCancelled(true);

        if (event.getCurrentItem().equals(Item.create(Material.SPECTRAL_ARROW, ChatColor.GRAY + "Go Back"))) {

            RecipeBrowserMenu recipeBrowserMenu = new RecipeBrowserMenu(getViewer(), 1);
            recipeBrowserMenu.open();

        }
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}
