package worm.me.gamesystems.world.blocks;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

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

    private final List<Material> concreteCycle = Arrays.asList(
            Material.WHITE_CONCRETE,
            Material.LIGHT_GRAY_CONCRETE,
            Material.GRAY_CONCRETE,
            Material.BLACK_CONCRETE,
            Material.BROWN_CONCRETE,
            Material.RED_CONCRETE,
            Material.ORANGE_CONCRETE,
            Material.YELLOW_CONCRETE,
            Material.LIME_CONCRETE,
            Material.GREEN_CONCRETE,
            Material.CYAN_CONCRETE,
            Material.LIGHT_BLUE_CONCRETE,
            Material.BLUE_CONCRETE,
            Material.PURPLE_CONCRETE,
            Material.MAGENTA_CONCRETE,
            Material.PINK_CONCRETE
    );

    private final List<Material> powderCycle = Arrays.asList(
            Material.WHITE_CONCRETE_POWDER,
            Material.LIGHT_GRAY_CONCRETE_POWDER,
            Material.GRAY_CONCRETE_POWDER,
            Material.BLACK_CONCRETE_POWDER,
            Material.BROWN_CONCRETE_POWDER,
            Material.RED_CONCRETE_POWDER,
            Material.ORANGE_CONCRETE_POWDER,
            Material.YELLOW_CONCRETE_POWDER,
            Material.LIME_CONCRETE_POWDER,
            Material.GREEN_CONCRETE_POWDER,
            Material.CYAN_CONCRETE_POWDER,
            Material.LIGHT_BLUE_CONCRETE_POWDER,
            Material.BLUE_CONCRETE_POWDER,
            Material.PURPLE_CONCRETE_POWDER,
            Material.MAGENTA_CONCRETE_POWDER,
            Material.PINK_CONCRETE_POWDER
    );

    public ArtBlocks(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void rightClick(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            if (player.getInventory().getItemInMainHand().getType() == Material.AIR) {
                Block block = event.getClickedBlock();
                if (block != null) {
                    Material type = block.getType();
                    List<Material> cycle = null;
                    Sound sound = null;

                    if (woolCycle.contains(type)) {
                        cycle = woolCycle;
                        sound = Sound.BLOCK_WOOL_HIT;
                    } else if (concreteCycle.contains(type)) {
                        cycle = concreteCycle;
                        sound = Sound.BLOCK_STONE_HIT;
                    } else if (powderCycle.contains(type)) {
                        cycle = powderCycle;
                        sound = Sound.BLOCK_SAND_HIT;
                    }

                    if (cycle != null) {
                        int currentIndex = cycle.indexOf(type);
                        int newIndex = player.isSneaking()
                                ? (currentIndex - 1 + cycle.size()) % cycle.size()
                                : (currentIndex + 1) % cycle.size();

                        block.setType(cycle.get(newIndex));
                        Location location = block.getLocation();
                        player.getWorld().playSound(location, sound, 0.3f, 0.9f);
                    }
                }
            }
        }
    }
}