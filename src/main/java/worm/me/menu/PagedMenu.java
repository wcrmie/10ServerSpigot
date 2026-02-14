package worm.me.menu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;

public abstract class PagedMenu extends Menu implements InventoryHolder {

    private final int page;

    protected PagedMenu(Player viewer, int page){
        super(viewer);
        this.page = page;

    }

    public int getPage(){
        return page;
    }

    public abstract void turnPage(int page);

}
