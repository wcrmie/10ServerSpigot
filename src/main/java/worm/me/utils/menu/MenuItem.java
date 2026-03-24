package worm.me.utils.menu;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import worm.me.utils.Item;

public enum MenuItem {
    NEXT_PAGE(new Item.ItemBuilder(Item.create(Material.ARROW, "<gray>Previous Page")).addPersistent(PersistentDataType.STRING, "next", "menu").build()),
    PREVIOUS_PAGE(new Item.ItemBuilder(Item.create(Material.ARROW, "<gray>Previous Page")).addPersistent(PersistentDataType.STRING, "previous", "menu").build()),
    BACK(new Item.ItemBuilder(Item.create(Material.ARROW, "<gray>Previous Page")).addPersistent(PersistentDataType.STRING, "back", "menu").build()),
    CLOSE(new Item.ItemBuilder(Item.create(Material.ARROW, "<gray>Previous Page")).addPersistent(PersistentDataType.STRING, "close", "menu").build()),;

    private final ItemStack itemStack;


    MenuItem(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ItemStack getItem() {
        return itemStack;
    }
}
