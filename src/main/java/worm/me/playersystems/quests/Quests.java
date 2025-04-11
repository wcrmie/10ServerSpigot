package worm.me.playersystems.quests;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.enginehub.linbus.stream.token.LinToken;
import worm.me.Server;

public class Quests {

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

        // If quest is completed
        if (slot == -1){
            return;

        } else {
            questProgress[slot]++;

            Bukkit.getServer().getPluginManager().callEvent(new QuestProgressEvent(QuestID, questProgress[slot], slot, Integer.parseInt(QuestInitialize.dailyQuestAttributes.get(QuestID).get(2)), p));

        }
    }

    public int getProgress(int questID, Player p) {

        PersistentDataContainer container = p.getPersistentDataContainer();

        int slot = getQuestSlot(questID, p);

        int[] questProgress = container.getOrDefault(new NamespacedKey(Server.getPlugin(), "questprogress"), PersistentDataType.INTEGER_ARRAY, new int[3]);

        if (slot == -1){
            return -1;
        } else {
            return questProgress[slot];
        }

    }

    public void completeQuest(Integer QuestID, Player p) {

            PersistentDataContainer container = p.getPersistentDataContainer();

            int slot = getQuestSlot(QuestID, p);

            int[] questProgress = container.getOrDefault(new NamespacedKey(Server.getPlugin(), "questprogress"), PersistentDataType.INTEGER_ARRAY, new int[3]);

            questProgress[slot] = -1;

            Bukkit.getServer().getPluginManager().callEvent(new QuestCompleteEvent(QuestID, p, slot));

            container.set(new NamespacedKey(Server.getPlugin(), "questprogress"), PersistentDataType.INTEGER_ARRAY, questProgress);

    }

    public void claimQuestRewards(Integer QuestID, Player player) {

        if (QuestID == 0) {
            handleQuestRewards(QuestRewards.MINE_STONE_REWARD, player);
        } else if (QuestID == 1) {
            handleQuestRewards(QuestRewards.COLLECT_IRON_REWARD, player);
        } else if (QuestID == 2) {
            handleQuestRewards(QuestRewards.GO_FISH_REWARD, player);
        } else if (QuestID == 3) {
            handleQuestRewards(QuestRewards.BUILD_SNOWMEN_REWARD, player);
        } else if (QuestID == 4) {
            handleQuestRewards(QuestRewards.KILL_MOBS_REWARD, player);
        } else if (QuestID == 5) {
            handleQuestRewards(QuestRewards.FISH_SADDLE_REWARD, player);
        } else if (QuestID == 6) {
            handleQuestRewards(QuestRewards.GROW_TREES_REWARD, player);
        } else if (QuestID == 7) {
            handleQuestRewards(QuestRewards.EAT_GAPPLE_REWARD, player);
        }

    }

    public void handleQuestRewards(QuestRewards rewards, Player player) {

        int slots = 0;

        for (ItemStack item : player.getInventory().getContents()) {

            if (item.getType().isAir()) slots++;

        }

        for (ItemStack item : rewards.getItems()) {
            player.getInventory().addItem(item);
        }


    }

    public boolean hasQuest(Player p, int questID) {

        PersistentDataContainer container = p.getPersistentDataContainer();

        int[] questSlots = container.getOrDefault(new NamespacedKey(Server.getPlugin(), "quests"), PersistentDataType.INTEGER_ARRAY, new int[3]);

        for (int i = 0; i < questSlots.length; i++){
            if (questSlots[i] == questID){
                return true;
            }
        }

        return false;

    }

}
