package worm.me.menu.generators;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.md_5.bungee.api.ChatColor;
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
import worm.me.utils.GuiConstructor;
import worm.me.utils.Item;
import worm.me.utils.chat.Message;
import worm.me.utils.chat.MessageType;

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

        Inventory inventory = GuiConstructor.chestInventory(String.format("Select %s Generator", generatorType.getName()), 27, this);

        List<Material> materials = new java.util.ArrayList<>(generatorType.getMaterials().stream().sorted().toList());

        int[] slots = GuiConstructor.determineCenteredSlots(Math.min(materials.size(), 7));

        for (int i = 1; i < 6; i++) {

            for (int j = 0; j < 7; j++) {

                Material material = materials.removeFirst();

                ItemStack item = Item.create(material, "");

                if (!Unlocks.hasGenUnlocked(getViewer(), material)) inventory.setItem(j, Item.create(Material.RED_STAINED_GLASS_PANE, ChatColor.RED + item.getType().toString() + " (LOCKED)", Unlocks.requirementsTranslation(generatorType.getGenerators().get(material))));

                inventory.setItem(slots[j] + (i * 9), item);

            }
        }

        return inventory;
    }

    @Override
    public void open() {

    }

    @Override
    public void handleClick(InventoryClickEvent event) {

        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();

        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null || clickedItem.getType().isAir() || clickedItem.getType().equals(Material.GRAY_STAINED_GLASS_PANE)) return;

        if (generatorType.getMaterials().contains(clickedItem.getType())) {

            if (Unlocks.hasGenUnlocked(player, clickedItem.getType())){

                PersistentDataContainer container = player.getPersistentDataContainer();
                container.set(generatorType.getKey(), PersistentDataType.STRING, clickedItem.getType().toString());

                player.sendMessage(Component.text(

                        Message.create(MessageType.SUCCESS, String.format("Your %s item has been changed to %s", generatorType.getName(), ((TextComponent) clickedItem.displayName()).content()))

                ));

            }

        }

    }

    @Override
    public @NotNull Inventory getInventory() {
        return null;
    }
}
