package worm.me.PlayerSystems.Quests;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import worm.me.Server;

import java.util.ArrayList;
import java.util.List;

public class Quests {

    public static List<List<String>> questRewards = new ArrayList<>();

    public int getQuestSlot(Integer ID, Player p){

        PersistentDataContainer container = p.getPersistentDataContainer();

        int[] questSlots = container.getOrDefault(new NamespacedKey(Server.getPlugin(), "quests"), PersistentDataType.INTEGER_ARRAY, new int[3]);

        for (int i = 0; i < questSlots.length; i++){
            if (questSlots[i] == ID){
                return i;
            }
        }

        return -1;

    }

    public void addProgress(Integer QuestID, Player p) {

        PersistentDataContainer container = p.getPersistentDataContainer();

        int slot = getQuestSlot(QuestID, p);

        int[] questProgress = container.getOrDefault(new NamespacedKey(Server.getPlugin(), "questprogress"), PersistentDataType.INTEGER_ARRAY, new int[3]);

        if (slot == -1){
            return;
        } else {
            questProgress[slot]++;
        }
    }

    public void completeQuest(Integer QuestID, Player p) {

            PersistentDataContainer container = p.getPersistentDataContainer();

    }

    public void claimQuestRewards(Integer QuestID, Player P) {

        for (int i = 0; i < questRewards.get(QuestID).size(); i++) {

            String[] reward = questRewards.get(QuestID).get(i).split("_");

            if (reward[0].equals("0")) {

                //ItemStack item = ItemStack.deserialize(reward[1]);
                //P.getInventory().addItem(item);


            }
        }
    }


}
