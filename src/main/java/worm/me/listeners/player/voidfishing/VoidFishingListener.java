package worm.me.listeners.player.voidfishing;

import io.papermc.paper.event.entity.EntityMoveEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import worm.me.gamesystems.fishing.voidfishing.VoidBobber;
import worm.me.gamesystems.fishing.voidfishing.VoidFishEvent;

public class VoidFishingListener implements Listener {

    @EventHandler
    public void onFishing(PlayerFishEvent event) {

        if (VoidBobber.findBobber(event.getHook()) != null) {

            VoidBobber bobber = VoidBobber.findBobber(event.getHook());
            Player player = event.getPlayer();
            VoidBobber.VoidFishState state = bobber.getState();

            if (event.getState().equals(PlayerFishEvent.State.REEL_IN)) {

                if (state == VoidBobber.VoidFishState.FISH_BITE) {
                    // Successful catch
                    bobber.setState(VoidBobber.VoidFishState.FISH_CAUGHT);
                    Bukkit.getPluginManager().callEvent(new VoidFishEvent(player, bobber, VoidBobber.VoidFishState.FISH_CAUGHT));

                    event.getHook().setHookedEntity(null); // set it later
                    bobber.remove();

                } else {
                    // Failed attempt
                    bobber.setState(VoidBobber.VoidFishState.FAILED_CATCH);
                    bobber.remove();
                }

                bobber.getMainLoop().cancel();
            }
        }
    }

    @EventHandler
    public void onBobberMove(EntityMoveEvent event) {

        if (!(event.getEntity() instanceof FishHook bobber)) return;

        if (!VoidBobber.canVoidFish(bobber)) return;

        if (VoidBobber.findBobber(bobber) != null) return;

        if (event.getFrom() == event.getTo()) return;

        if (event.getFrom().getY() >= 210) return;

        //TODO: add logic to check for lure and luck
        VoidBobber voidBobber = new VoidBobber((Player) bobber.getShooter(), bobber, 0, 0);
        voidBobber.startFishing();

    }

    @EventHandler
    public void onVoidFish(VoidFishEvent event) {

        Player player = event.getPlayer();
        VoidBobber.VoidFishState state = event.getState();
        VoidBobber bobber = event.getBobber();

        switch (state) {
            case FISH_BITE -> {
                // i dont really need to do anything here
            }

            case FISH_CAUGHT -> {
                // Handle caught fish
                //Levels.addExp();

            }
            case FAILED_CATCH -> {
                bobber.getMainLoop().cancel();
                bobber.startFishing();

            }
        }

    }
}
