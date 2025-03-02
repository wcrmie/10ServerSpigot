package worm.me.PlayerSystems.Social.Skin;

import net.skinsrestorer.api.exception.DataRequestException;
import net.skinsrestorer.api.exception.MineSkinException;
import net.skinsrestorer.api.property.InputDataResult;
import net.skinsrestorer.api.storage.PlayerStorage;
import net.skinsrestorer.api.storage.SkinStorage;
import org.bukkit.entity.Player;
import worm.me.server;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class Skin {

    private final server plugin;

    public Skin(server plugin) {
        this.plugin = plugin;
    }

    public void setSkin(Player player){
        SkinStorage storage = plugin.skinsRestorer.getSkinStorage();
        try {

            int RNG = ThreadLocalRandom.current().nextInt(1, 4);

            Optional<InputDataResult> result = storage.findOrCreateSkinData("worrm");

            if (RNG == 1){
                result = storage.findOrCreateSkinData("goobier");
            } else if (RNG == 2){
                result = storage.findOrCreateSkinData("snowmaam");
            } else if (RNG == 3) {
                result = storage.findOrCreateSkinData("worrm");
            }


            if (result.isEmpty()){
                player.sendMessage("Skin could not be loaded.");
                return;
            }

            PlayerStorage playerStorage = plugin.skinsRestorer.getPlayerStorage();

            playerStorage.setSkinIdOfPlayer(player.getUniqueId(), result.get().getIdentifier());

            plugin.skinsRestorer.getSkinApplier(Player.class).applySkin(player);
        } catch (DataRequestException | MineSkinException e) {
            throw new RuntimeException(e);

        }
    }

}
