package worm.me.gamesystems.fishing.voidfishing;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class VoidFishEvent extends Event {
    private final Player player;
    private final VoidBobber bobber;
    private final VoidBobber.VoidFishState state;
    private final HandlerList handlers = new HandlerList();

    public VoidFishEvent(Player player, VoidBobber bobber, VoidBobber.VoidFishState state) {
        this.player = player;
        this.bobber = bobber;
        this.state = state;
    }

    public Player getPlayer() {
        return this.player;
    }

    public VoidBobber getBobber(){
        return this.bobber;
    }

    public VoidBobber.VoidFishState getState(){
        return this.state;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return null;
    }
}
