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

    private Server plugin;

    public QuestGui(Server plugin) {
        this.plugin = plugin;
    }

    public void questGuiOpen(Player p){
        // Open the quest GUI for the player
        QuestAssign questAssign = new QuestAssign(this.plugin);
        PersistentDataContainer container = p.getPersistentDataContainer();

        GuiConstructor guiConstructor = new GuiConstructor();
        Inventory GUI = guiConstructor.chestInventory(ChatColor.DARK_GRAY + "Quests", 27);
        GUI.setItem(11, guiConstructor.createGuiItem(getQuestMaterial(p, questAssign.daily1), ChatColor.GRAY + getQuestName(p, questAssign.daily1)," ", ChatColor.DARK_GRAY + getQuestDescription(p, questAssign.daily1), ChatColor.LIGHT_PURPLE + "Progress: " + container.getOrDefault(questAssign.daily1progress, PersistentDataType.INTEGER, 0) + "/" + getQuestRequiredProgress(p, questAssign.daily1)));
        GUI.setItem(13, guiConstructor.createGuiItem(getQuestMaterial(p, questAssign.daily2), ChatColor.GRAY + getQuestName(p, questAssign.daily2)," ", ChatColor.DARK_GRAY + getQuestDescription(p, questAssign.daily2), ChatColor.LIGHT_PURPLE + "Progress: " + container.getOrDefault(questAssign.daily2progress, PersistentDataType.INTEGER, 0) + "/" + getQuestRequiredProgress(p, questAssign.daily2)));
        GUI.setItem(15, guiConstructor.createGuiItem(getQuestMaterial(p, questAssign.daily3), ChatColor.GRAY + getQuestName(p, questAssign.daily3)," ", ChatColor.DARK_GRAY + getQuestDescription(p, questAssign.daily3), ChatColor.LIGHT_PURPLE + "Progress: " + container.getOrDefault(questAssign.daily3progress, PersistentDataType.INTEGER, 0) + "/" + getQuestRequiredProgress(p, questAssign.daily3)));
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

    public String getQuestRequiredProgress(Player p, NamespacedKey quest){

        // Get the progress of the quest
        QuestInitialize questInitialize = new QuestInitialize(this.plugin);
        Map<Integer, List<String>> m = QuestInitialize.dailyQuestAttributes;
        int questID = p.getPersistentDataContainer().get(quest, PersistentDataType.INTEGER);
        return m.get(questID).get(2);
    }

}
