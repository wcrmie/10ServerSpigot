package worm.me.playersystems.data.levels;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class LevelUpEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final int skill;
    private final int level;
    private final Player player;

    public LevelUpEvent(int skill, int level, Player player) {
        this.skill = skill;
        this.level = level;
        this.player = player;
    }

    public int getLevel() {
        return level;
    }

    public Player getPlayer() {
        return player;
    }

    public int getSkill() {
        return skill;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
