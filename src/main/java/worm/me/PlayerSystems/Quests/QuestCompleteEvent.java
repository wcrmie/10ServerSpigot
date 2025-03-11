package worm.me.PlayerSystems.Quests;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class QuestCompleteEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final int quest;
    private final Player player;

    public QuestCompleteEvent(int quest, Player player) {
        this.quest = quest;
        this.player = player;
    }

    public int getQuest() {
        return quest;
    }

    public Player getPlayer() {
        return player;
    }


    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
}
