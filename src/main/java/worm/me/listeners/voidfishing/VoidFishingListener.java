package worm.me.listeners.voidfishing;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import worm.me.gamesystems.voidfishing.VoidBobber;
import worm.me.gamesystems.voidfishing.VoidFishEvent;
import worm.me.playersystems.data.levels.Levels;

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
                    bobber.remove();

                } else {
                    // Failed attempt
                    bobber.setState(VoidBobber.VoidFishState.FAILED_CATCH);
                    bobber.remove();
                }


            }

        }



    }

    @EventHandler
    public void onVoidFish(VoidFishEvent event) {

        Player player = event.getPlayer();
        VoidBobber.VoidFishState state = event.getState();
        VoidBobber bobber = event.getBobber();

        switch (state) {
            case FISH_BITE -> {

            }

            case FISH_CAUGHT -> {
                // Handle caught fish
                //Levels.addExp();

            }
            case FAILED_CATCH -> {
                // Handle failed attempt

            }
        }

    }
}
