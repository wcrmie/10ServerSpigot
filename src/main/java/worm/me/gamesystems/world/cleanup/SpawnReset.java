package worm.me.gamesystems.world.cleanup;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import worm.me.gamesystems.world.PlayArea;
import worm.me.gamesystems.world.generation.structure.StructureSupplier;
import worm.me.utils.RNG;

import java.util.Map;

public class SpawnReset {

    private static Map<String, Integer> spawnBuilds = Map.of(

            "spawn_build_village", 15

    );

    public static void replaceSpawn() {

        String structureName = RNG.chooseRandom(spawnBuilds);

        StructureSupplier.placeStructure(structureName, PlayArea.getSpawnLocation().clone().add(0, -1, 0), true);

        for (Player player : Bukkit.getOnlinePlayers()) {

            player.sendMessage(MiniMessage.miniMessage().deserialize(

                    "<yellow>Spawn has been cleaned up!</yellow>"

            ));

        }

    }

}
