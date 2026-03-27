package worm.me.menu;

import org.bukkit.entity.Player;
import worm.me.menu.main.MainGUIMenu;

public interface MainSubMenu {

    default void goBack(Player viewer) {

        MainGUIMenu menu = new MainGUIMenu(viewer);
        menu.open();

    }

}
