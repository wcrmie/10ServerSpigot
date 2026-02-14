package worm.me.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public abstract class Menu implements InventoryHolder {

    private final Player viewer;

    protected Menu(Player viewer) {
        this.viewer = viewer;
    }

    public abstract Inventory initialize();

    public abstract void open();

    public abstract void handleClick(InventoryClickEvent event);

    protected Player getViewer() {
        return viewer;
    }


}
