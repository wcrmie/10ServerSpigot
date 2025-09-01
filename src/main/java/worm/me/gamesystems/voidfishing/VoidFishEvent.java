package worm.me.gamesystems.voidfishing;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@RequiredArgsConstructor
@Getter
public class VoidFishEvent extends Event {
    private final Player player;
    private final VoidBobber bobber;
    private final VoidBobber.VoidFishState state;
    private final HandlerList handlers = new HandlerList();
}
