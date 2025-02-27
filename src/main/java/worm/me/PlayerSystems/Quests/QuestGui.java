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

    public Material getQuestMaterial(Player p, NamespacedKey quest){

        // Get the material of the quest
        QuestInitialize questInitialize = new QuestInitialize(this.plugin);
        Map<Integer, List<String>> m = QuestInitialize.dailyQuestAttributes;
        int questID = p.getPersistentDataContainer().get(quest, PersistentDataType.INTEGER);
        return Material.valueOf(m.get(questID).get(3));
    }

    public String getQuestName(Player p, NamespacedKey quest){

        // Get the name of the quest
        QuestInitialize questInitialize = new QuestInitialize(this.plugin);
        Map<Integer, List<String>> m = QuestInitialize.dailyQuestAttributes;
        int questID = p.getPersistentDataContainer().get(quest, PersistentDataType.INTEGER);
        return m.get(questID).get(0);
    }

    public String getQuestDescription(Player p, NamespacedKey quest){

        // Get the description of the quest
        QuestInitialize questInitialize = new QuestInitialize(this.plugin);
        Map<Integer, List<String>> m = QuestInitialize.dailyQuestAttributes;
        int questID = p.getPersistentDataContainer().get(quest, PersistentDataType.INTEGER);
        return m.get(questID).get(1);
    }

}
