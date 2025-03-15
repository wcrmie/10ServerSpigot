package worm.me.playersystems.quests;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class QuestCompleteEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final int quest;
    private final Player player;
    private final int questSlot;

    public QuestCompleteEvent(int quest, Player player, int questSlot) {
        this.quest = quest;
        this.player = player;
        this.questSlot = questSlot;
    }

    public int getQuest() {
        return quest;
    }

    public int getQuestSlot() {
        return questSlot;
    }

    public Player getPlayer() {
        return player;
    }


    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
}
