package worm.me.PlayerSystems.Quests;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import worm.me.Server;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class QuestAssign {

    public NamespacedKey questAssigned = new NamespacedKey(Server.getPlugin(), "questassigned");

    public NamespacedKey dailies = new NamespacedKey(Server.getPlugin(), "quests");

    public NamespacedKey dailyprogress = new NamespacedKey(Server.getPlugin(), "questprogress");


    public void playerQuestAssign(Player p, Integer questSlot){
        PersistentDataContainer container = p.getPersistentDataContainer();
        int RNG = ThreadLocalRandom.current().nextInt(0, QuestInitialize.dailyQuestAttributes.size() - 1);

        int[] quests = container.getOrDefault(dailies, PersistentDataType.INTEGER_ARRAY, new int[3]);

        quests[questSlot] = RNG;

        container.set(dailies, PersistentDataType.INTEGER_ARRAY, quests);
    }

    public void dailyReset(Player p){
        PersistentDataContainer container = p.getPersistentDataContainer();

        container.set(dailyprogress, PersistentDataType.INTEGER_ARRAY, new int[3]);

        playerQuestAssign(p, 0);

        // If the second quest is the same as the first, reassign it.
        do {
            playerQuestAssign(p, 1);
        } while (Objects.equals(container.getOrDefault(dailies, PersistentDataType.INTEGER_ARRAY, new int[3])[0], container.getOrDefault(dailies, PersistentDataType.INTEGER_ARRAY, new int[3])[1]));

        // And the same here
        do {
            playerQuestAssign(p, 2);
        } while (Objects.equals(container.getOrDefault(dailies, PersistentDataType.INTEGER_ARRAY, new int[3])[2], container.getOrDefault(dailies, PersistentDataType.INTEGER_ARRAY, new int[3])[1]) ||
                Objects.equals(container.getOrDefault(dailies, PersistentDataType.INTEGER_ARRAY, new int[3])[2], container.getOrDefault(dailies, PersistentDataType.INTEGER_ARRAY, new int[3])[0]));

        container.set(questAssigned, PersistentDataType.LONG, System.currentTimeMillis()/1000L);
    }


    public void dailyCheck(Player p){
        PersistentDataContainer container = p.getPersistentDataContainer();
        long lastAssigned = container.getOrDefault(questAssigned, PersistentDataType.LONG, 0L);

        // if the last assigned quest was before today, reset the quests.
        if (Math.round((float) lastAssigned /86400L) < Math.round((float) System.currentTimeMillis() /1000L/86400L)){
            dailyReset(p);
            p.sendMessage(ChatColor.GREEN + "Your daily quests have been reset!");
        }
    }





}
