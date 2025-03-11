package worm.me.PlayerSystems.Abilities;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class AbilityAttemptEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;

    private final Integer abilityID;

    public AbilityAttemptEvent(Integer abilitySlot, Player player){
        this.abilityID = abilitySlot;
        this.player = player;

    }


    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void setCancelled(boolean cancel) {

    }

    public Integer getAbilityID(){
        return abilityID;
    }

    public Player getPlayer(){
        return player;
    }

    public static @NotNull HandlerList getHandlerList() {
        return handlers;
    }

}
