package worm.me.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import worm.me.playersystems.quests.QuestInitialize;
import worm.me.playersystems.quests.QuestProgressEvent;
import worm.me.playersystems.quests.Quests;

public class QuestListener implements Listener {

    @EventHandler
    public void onKillMobs(EntityDeathEvent event) {
        // Handle mob kill events here

        if (event.getEntity().getKiller() != null && event.getEntity().getKiller() instanceof Player p) {
            Quests quests = new Quests();
            if (quests.hasQuest(p, 4)) {

                quests.addProgress(4, p);

            }
        }
    }

    @EventHandler
    public void onQuestProgress(QuestProgressEvent event) {
        // Handle quest progress events here
        Quests quests = new Quests();
        Player player = event.getPlayer();
        int questId = event.getQuestID();
        int progress = event.getProgress();

        if (progress >= event.getMaxProgress()) {
            // Quest is completed
            player.sendMessage("Congratulations! You have completed the quest: " + questId);
            // Add any additional logic for quest completion here
        }
    }


}
