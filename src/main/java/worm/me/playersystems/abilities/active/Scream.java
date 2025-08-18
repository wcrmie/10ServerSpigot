package worm.me.playersystems.abilities.active;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import worm.me.playersystems.abilities.Abilities;

public class Scream {

    private final Abilities ability = new Abilities();

    public void scream(Player player, double radius, Sound sound, float baseVolume, float pitch) {
        Location playerLocation = player.getLocation();

        for (Player nearbyPlayer : player.getWorld().getPlayers()) {
            double distance = nearbyPlayer.getLocation().distance(playerLocation);

            if (distance <= radius) {

                float volumeAtDistance;

                if (distance <= 1.0) {
                    volumeAtDistance = baseVolume;
                } else {
                    float minVolume = 0.1f;
                    volumeAtDistance = baseVolume - ((float)distance / (float)radius) * (baseVolume - minVolume);
                }
                nearbyPlayer.playSound(playerLocation, sound, volumeAtDistance, pitch);
            }
        }
    }

    public void scream(Player player) {
        scream(player, 50.0, Sound.ENTITY_GHAST_SCREAM, 5.0f, 0.3f);
    }

}
