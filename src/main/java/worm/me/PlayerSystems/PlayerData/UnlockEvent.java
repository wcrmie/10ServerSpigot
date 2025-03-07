package worm.me.PlayerSystems.PlayerData;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class UnlockEvent extends Event {

    Player player;
    String unlock;
    String unlockType;

    HandlerList handlers = new HandlerList();

    public UnlockEvent(Player player, String unlock, String unlockType){
        this.player = player;
        this.unlock = unlock;
        this.unlockType = unlockType;
    }

    public Player getPlayer(){
        return player;
    }

    public String getUnlock(){
        return unlock;
    }

    public String getUnlockType(){
        return unlockType;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
}
