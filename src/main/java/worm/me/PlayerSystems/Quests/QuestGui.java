package worm.me.PlayerSystems.Quests;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import worm.me.GuiConstructor;

public class QuestGui {

    public void questGuiOpen(Player p){
        // Open the quest GUI for the player
        GuiConstructor guiConstructor = new GuiConstructor();
        Inventory GUI = guiConstructor.chestInventory(ChatColor.DARK_GRAY + "Quests", 27);
        p.openInventory(GUI);
    }
}
