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

    private final Server plugin;

    public QuestAssign(Server plugin) {
        this.plugin = plugin;
    }

    public NamespacedKey questAssigned = new NamespacedKey("10server", "questassigned");

    public NamespacedKey daily1 = new NamespacedKey("10server", "quest1");
    public NamespacedKey daily2 = new NamespacedKey("10server", "quest2");
    public NamespacedKey daily3 = new NamespacedKey("10server", "quest3");

    public NamespacedKey daily1progress = new NamespacedKey(Server.getPlugin(), "daily1progress");
    public NamespacedKey daily2progress = new NamespacedKey(Server.getPlugin(), "daily2progress");
    public NamespacedKey daily3progress = new NamespacedKey(Server.getPlugin(), "daily3progress");

    public NamespacedKey weekly = new NamespacedKey("10server", "quest4");


    public void playerQuestAssign(Player p, NamespacedKey questSlot){
        PersistentDataContainer container = p.getPersistentDataContainer();
        int RNG = ThreadLocalRandom.current().nextInt(0, QuestInitialize.dailyQuestAttributes.size() - 1);
        container.set(questSlot, PersistentDataType.INTEGER, RNG);
    }

    public void dailyReset(Player p){
        PersistentDataContainer container = p.getPersistentDataContainer();
        playerQuestAssign(p, daily1);
        playerQuestAssign(p, daily2);

        // If the second quest is the same as the first, reassign it.
        while (Objects.equals(container.get(daily2, PersistentDataType.INTEGER), container.get(daily1, PersistentDataType.INTEGER))){
            playerQuestAssign(p, daily2);
        }

        playerQuestAssign(p, daily3);

        // And the same here
        while (Objects.equals(container.get(daily3, PersistentDataType.INTEGER), container.get(daily1, PersistentDataType.INTEGER)) || Objects.equals(container.get(daily3, PersistentDataType.INTEGER), container.get(daily2, PersistentDataType.INTEGER))){
            playerQuestAssign(p, daily3);
        }
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
