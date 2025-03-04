package worm.me.PlayerSystems.PlayerData.Levels;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class LevelUpEvent extends Event {

    private final String skill;
    private final int level;
    private final Player player;
    private final static HandlerList HANDLERS = new HandlerList();

    public LevelUpEvent(String skill, int level, Player player) {
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

    public String getSkill() {
        return skill;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }


    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}
