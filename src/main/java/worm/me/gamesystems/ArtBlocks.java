package worm.me.gamesystems;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import worm.me.Server;

import java.util.Arrays;
import java.util.List;

public class ArtBlocks {

    private final JavaPlugin plugin;
    private final List<Material> woolCycle = Arrays.asList(
            Material.WHITE_WOOL,
            Material.LIGHT_GRAY_WOOL,
            Material.GRAY_WOOL,
            Material.BLACK_WOOL,
            Material.BROWN_WOOL,
            Material.RED_WOOL,
            Material.ORANGE_WOOL,
            Material.YELLOW_WOOL,
            Material.LIME_WOOL,
            Material.GREEN_WOOL,
            Material.CYAN_WOOL,
            Material.LIGHT_BLUE_WOOL,
            Material.BLUE_WOOL,
            Material.PURPLE_WOOL,
            Material.MAGENTA_WOOL,
            Material.PINK_WOOL
    );

    public ArtBlocks(Server plugin) {
        this.plugin = plugin;
    }

    public void rightClick(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            if (player.getInventory().getItemInMainHand().getType() == Material.AIR) {
                Block block = event.getClickedBlock();
                if (block != null && woolCycle.contains(block.getType())) {
                    Material currentMaterial = block.getType();
                    int currentIndex = woolCycle.indexOf(currentMaterial);
                    int newIndex;
                    if (event.getPlayer().isSneaking()) {
                        newIndex = (currentIndex - 1 + woolCycle.size()) % woolCycle.size();
                    } else {
                        newIndex = (currentIndex + 1) % woolCycle.size();
                    }
                    block.setType(woolCycle.get(newIndex));
                    Location location = block.getLocation();
                    player.getWorld().playSound(location, Sound.BLOCK_WOOL_HIT, 0.3f, 0.9f);
                }
            }
        }
    }
}
