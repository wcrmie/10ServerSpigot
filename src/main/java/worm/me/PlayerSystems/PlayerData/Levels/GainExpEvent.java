package worm.me.PlayerSystems.PlayerData.Levels;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public final class GainExpEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Player player;
    private long expValue;
    private int skillType;

    public GainExpEvent(Player p, long exp, int skill) {
        this.player = p;
        this.expValue = exp;
        this.skillType = skill;
    }

    public Player getPlayer() {
        return player;
    }

    public long getExpValue() {
        return expValue;
    }

    public int getSkillType() {
        return skillType;
    }

    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
