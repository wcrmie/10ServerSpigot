package worm.me.menu.generators;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import worm.me.menu.Menu;
import worm.me.playersystems.generator.GeneratorType;

public class GensSubMenu extends Menu implements InventoryHolder {

    Inventory inventory;

    protected GensSubMenu(Player viewer, GeneratorType type) {
        super(viewer);
    }

    @Override
    public Inventory initialize() {
        return null;
    }

    @Override
    public void open() {

    }

    @Override
    public void handleClick(InventoryClickEvent event) {

    }

    @Override
    public @NotNull Inventory getInventory() {
        return null;
    }
}
