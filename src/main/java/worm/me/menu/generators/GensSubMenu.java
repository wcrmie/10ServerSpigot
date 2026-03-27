package worm.me.menu.generators;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import worm.me.data.Unlocks;
import worm.me.menu.Menu;
import worm.me.playersystems.generator.GeneratorType;
import worm.me.utils.menu.GuiConstructor;
import worm.me.utils.Item;
import worm.me.utils.chat.Message;
import worm.me.utils.chat.MessageType;
import worm.me.utils.menu.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class GensSubMenu extends Menu implements InventoryHolder {

    Inventory inventory;
    GeneratorType generatorType;

    protected GensSubMenu(Player viewer, GeneratorType type) {
        super(viewer);
        this.generatorType = type;
        this.inventory = initialize();
    }

    @Override
    public Inventory initialize() {

        List<Material> materials = new ArrayList<>(generatorType.getMaterials());

        int invSize = 27 + (int) Math.round(Math.ceil(((double) materials.size()) / 7.0) - 1 ) * 9;

        Inventory inventory = GuiConstructor.chestInventory(String.format("Select %s Generator", generatorType.getName()), invSize, this);

        int[] slots = GuiConstructor.determineCenteredSlots(Math.min(materials.size(), 7));

        for (int i = 1; i < 6; i++) {

            if (materials.isEmpty()) break;

            for (int j = 0; j < 7; j++) {

                if (materials.isEmpty()) break;

                Material material = materials.removeFirst();

                ItemStack item = Item.create(material, null);

                if (!Unlocks.hasGenUnlocked(getViewer(), material)) item = Item.create(Material.RED_STAINED_GLASS_PANE, "<red>" + item.getType() + " (LOCKED)", Unlocks.requirementsTranslation(generatorType.getGenerators().get(material)));

                inventory.setItem(slots[j] + (i * 9), item);

            }
        }

        return inventory;
    }

    @Override
    public void open() {
        getViewer().openInventory(inventory);
    }

    @Override
    public void handleClick(InventoryClickEvent event) {

        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();

        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null || clickedItem.getType().isAir() || clickedItem.getType().equals(Material.GRAY_STAINED_GLASS_PANE)) return;

        if (clickedItem.equals(MenuItem.BACK.getItem())) {

            GensMainMenu menu = new GensMainMenu(player);
            menu.open();

        }

        if (generatorType.getMaterials().contains(clickedItem.getType())) {

            if (Unlocks.hasGenUnlocked(player, clickedItem.getType())){

                PersistentDataContainer container = player.getPersistentDataContainer();
                container.set(generatorType.getKey(), PersistentDataType.STRING, clickedItem.getType().toString());

                player.sendMessage(

                    Message.create(

                        MessageType.SUCCESS,

                        String.format(
                                "Your %s item has been changed to ",
                                generatorType.getName()

                        )

                    ).append(clickedItem.effectiveName())
                );

            }

        }

    }

    @Override
    public @NotNull Inventory getInventory() {
        return null;
    }
}
