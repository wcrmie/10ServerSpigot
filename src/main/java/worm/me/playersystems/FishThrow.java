package worm.me.playersystems;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.*;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import worm.me.Server;

import java.util.HashMap;
import java.util.Map;

public class FishThrow {
    private final JavaPlugin plugin;

    private static final Map<Material, EntityType> BUCKET_TO_ENTITY = new HashMap<Material, EntityType>() {{
        put(Material.COD_BUCKET, EntityType.COD);
        put(Material.SALMON_BUCKET, EntityType.SALMON);
        put(Material.PUFFERFISH_BUCKET, EntityType.PUFFERFISH);
        put(Material.TROPICAL_FISH_BUCKET, EntityType.TROPICAL_FISH);
        put(Material.AXOLOTL_BUCKET, EntityType.AXOLOTL);
        put(Material.TADPOLE_BUCKET, EntityType.TADPOLE);
    }};

    public FishThrow(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void handleBucketThrow(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (player.isSneaking() && (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) && item != null && BUCKET_TO_ENTITY.containsKey(item.getType())) {

            EntityType entityType = BUCKET_TO_ENTITY.get(item.getType());
            Entity entity = null;
            ItemMeta meta = item.getItemMeta();

            switch (entityType) {
                case COD:
                    entity = player.getWorld().spawn(player.getLocation(), Cod.class);
                    break;
                case SALMON:
                    entity = player.getWorld().spawn(player.getLocation(), Salmon.class);
                    break;

                case PUFFERFISH:
                    entity = player.getWorld().spawn(player.getLocation(), PufferFish.class);
                    break;

                case TROPICAL_FISH:
                    entity = player.getWorld().spawn(player.getLocation(), TropicalFish.class);
                    break;

                case AXOLOTL:
                    entity = player.getWorld().spawn(player.getLocation(), Axolotl.class);
                    break;

                case TADPOLE:
                    entity = player.getWorld().spawn(player.getLocation(), Tadpole.class);
                    break;

            }

            if (entity != null && meta != null && meta.hasDisplayName()) {
                entity.setCustomName(meta.getDisplayName());
            }

            player.getInventory().setItemInMainHand(new ItemStack(Material.WATER_BUCKET));
            event.setCancelled(true);
        }
    }
}