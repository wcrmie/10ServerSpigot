package worm.me.PlayerSystems.PlayerData.Levels;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.persistence.PersistentDataType;
import worm.me.GuiConstructor;
import worm.me.Server;

public class LevelsGUI {

    public void openLevelsGUI(Player p){

        GuiConstructor guiConstructor = new GuiConstructor();
        Levels level = new Levels();

        Inventory gui = guiConstructor.chestInventory("Levels", 54);

    }
}
