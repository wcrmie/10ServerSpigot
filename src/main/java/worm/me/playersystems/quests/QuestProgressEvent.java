package worm.me.playersystems.quests;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class QuestProgressEvent extends Event {

    private static final HandlerList h = new HandlerList();

    private final int questID;

    private final int progress;

    private final int slot;

    private final int maxProgress;

    private final Player player;

    public QuestProgressEvent(int questID, int progress, int slot, int maxProgress, Player player) {
        this.questID = questID;
        this.progress = progress;
        this.slot = slot;
        this.maxProgress = maxProgress;
        this.player = player;
    }

    public int getQuestID() {
        return questID;
    }

    public int getProgress() {
        return progress;
    }

    public int getSlot() {
        return slot;
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    public Player getPlayer() {
        return player;
    }

    public static @NotNull HandlerList getHandlerList() {
        return h;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return h;
    }
}
