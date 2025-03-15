package worm.me.PlayerSystems.Quests;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import worm.me.GuiConstructor;
import worm.me.Server;

import java.util.List;
import java.util.Map;

public class QuestGui {

    private QuestAssign questAssign = new QuestAssign();

    public void questGuiOpen(Player p){
        // Open the quest GUI for the player
        PersistentDataContainer container = p.getPersistentDataContainer();

        Inventory GUI = GuiConstructor.chestInventory(ChatColor.DARK_GRAY + "Quests", 27);

        for (int i = 0; i < 3; i++){
            GUI.setItem(2*i + 9, GuiConstructor.createGuiItem(getQuestMaterial(p, i), ChatColor.GRAY + getQuestName(p, i)," ",
                    ChatColor.DARK_GRAY + getQuestDescription(p, i),
                    ChatColor.LIGHT_PURPLE + "Progress: " + container.getOrDefault(questAssign.dailyprogress, PersistentDataType.INTEGER_ARRAY, new int[3])[i] + "/" + getQuestRequiredProgress(p, i)));
        }

        p.openInventory(GUI);

    }

    public Material getQuestMaterial(Player p, Integer quest){

        // Get the material of the quest
        List<List<String>> m = QuestInitialize.dailyQuestAttributes;
        int questID = p.getPersistentDataContainer().getOrDefault(questAssign.dailies, PersistentDataType.INTEGER_ARRAY, new int[3])[quest];
        return Material.valueOf(m.get(questID).get(3));
    }

    public String getQuestName(Player p, Integer quest){

        // Get the name of the quest
        List<List<String>> m = QuestInitialize.dailyQuestAttributes;
        int questID = p.getPersistentDataContainer().getOrDefault(questAssign.dailies, PersistentDataType.INTEGER_ARRAY, new int[3])[quest];
        return m.get(questID).getFirst();
    }

    public String getQuestDescription(Player p, Integer quest){

        // Get the description of the quest
        List<List<String>> m = QuestInitialize.dailyQuestAttributes;
        int questID = p.getPersistentDataContainer().getOrDefault(questAssign.dailies, PersistentDataType.INTEGER_ARRAY, new int[3])[quest];
        return m.get(questID).get(1);
    }

    public String getQuestRequiredProgress(Player p, Integer quest){

        // Get the progress of the quest
        List<List<String>> m = QuestInitialize.dailyQuestAttributes;
        int questID = p.getPersistentDataContainer().getOrDefault(questAssign.dailies, PersistentDataType.INTEGER_ARRAY, new int[3])[quest];
        return m.get(questID).get(2);
    }

}
