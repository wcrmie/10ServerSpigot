package worm.me.menu.abilities;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import worm.me.menu.Menu;
import worm.me.playersystems.abilities.AbilityType;

public class AbilitiesSubMenu extends Menu implements InventoryHolder {

    private final AbilityType abilityType;

    AbilitiesSubMenu(AbilityType abilityType, Player viewer) {
        super(viewer);
        this.abilityType = abilityType;
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
