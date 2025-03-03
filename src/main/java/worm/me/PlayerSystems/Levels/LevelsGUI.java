package worm.me.PlayerSystems.Levels;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.persistence.PersistentDataType;
import worm.me.GuiConstructor;
import worm.me.server;

public class LevelsGUI {

    private final server plugin;

    public LevelsGUI(server plugin) {
        this.plugin = plugin;
    }

    public void openLevelsGUI(Player p){

        GuiConstructor guiConstructor = new GuiConstructor();
        Level level = new Level(plugin);

        Inventory gui = guiConstructor.chestInventory("Levels", 54);

        gui.setItem(10, guiConstructor.createGuiItem(Material.IRON_SWORD, String.format("Combat Level %b", p.getPersistentDataContainer().get(level.combatLevel, PersistentDataType.INTEGER)),
                String.format("EXP: %b / %b", p.getPersistentDataContainer().get(level.combat, PersistentDataType.INTEGER))));

    }
}
