package worm.me.menu;

import org.bukkit.entity.Player;

public interface MainSubMenu {

    default void goBack(Player viewer) {

        MainGUIMenu menu = new MainGUIMenu(viewer);
        menu.open();

    }

}
