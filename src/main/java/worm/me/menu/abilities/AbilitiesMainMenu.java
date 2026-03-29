package worm.me.menu.abilities;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import worm.me.menu.MainSubMenu;
import worm.me.menu.Menu;
import worm.me.playersystems.abilities.Abilities;
import worm.me.playersystems.abilities.Ability;
import worm.me.playersystems.abilities.AbilityType;
import worm.me.utils.Item;
import worm.me.utils.menu.GuiConstructor;
import worm.me.Server;

import java.util.*;

public class AbilitiesMainMenu extends Menu implements InventoryHolder, MainSubMenu {
    private final Inventory inventory;

    private static class SlotInfo {
        final AbilityType type;
        final int slot;
        final String typeName;
        final String trigger;
        final String title;

        SlotInfo(AbilityType type, int slot, String typeName, String trigger, String title) {
            this.type = type;
            this.slot = slot;
            this.typeName = typeName;
            this.trigger = trigger;
            this.title = title;
        }
    }

    private final SlotInfo[] slotInfos = {
            new SlotInfo(AbilityType.DOUBLE_SPACE, 0, "Basic", "Double tap spacebar", "First"),
            new SlotInfo(AbilityType.HORIZONTAL, 1, "Horizontal Movement", "Sneak + Right Click + Sword", "Second"),
            new SlotInfo(AbilityType.VERTICAL, 2, "Vertical Movement", "Sneak + Right Click + Axe", "Third")
    };

    public AbilitiesMainMenu(Player viewer) {
        super(viewer);
        this.inventory = initialize();
    }

    @Override
    public Inventory initialize() {
        try {
            PersistentDataContainer container = getViewer().getPersistentDataContainer();
            Inventory gui = GuiConstructor.chestInventory("Your Active Abilities", 27, this);

            int[] active = Objects.requireNonNullElse(
                    container.get(Abilities.abilitiesActive, PersistentDataType.INTEGER_ARRAY),
                    Ability.DEFAULT_ACTIVE_ABILITIES
            );

            int[] infoSlots = {1, 4, 7};
            for (int i = 0; i < slotInfos.length; i++) {
                gui.setItem(infoSlots[i], createSlotInfoItem(slotInfos[i]));
            }

            for (int i = 0; i < Ability.SLOT_COUNT; i++) {
                gui.setItem(Ability.ABILITY_SLOT_POSITIONS[i], createAbilitySlotItem(active[i]));
            }

            return gui;
        } catch (Exception e) {
            Server.getPlugin().getLogger().warning("Error initializing abilities menu for " + getViewer().getName() + ": " + e.getMessage());
            return GuiConstructor.chestInventory("Your Active Abilities", 27, this);
        }
    }

    @Override
    public void open() {
        getViewer().openInventory(inventory);
    }

    @Override
    public void handleClick(InventoryClickEvent event) {
        event.setCancelled(true);
        int slot = event.getSlot();

        for (int i = 0; i < Ability.ABILITY_SLOT_POSITIONS.length; i++) {
            if (slot == Ability.ABILITY_SLOT_POSITIONS[i]) {
                openAbilitySelection(i);
                return;
            }
        }
    }

    private void openAbilitySelection(int slotIndex) {
        AbilitiesSubMenu subMenu = new AbilitiesSubMenu(slotInfos[slotIndex].type, getViewer(), slotIndex, slotInfos[slotIndex].title);
        subMenu.open();
    }

    private ItemStack createSlotInfoItem(SlotInfo info) {

        return Item.create(Material.BOOK,
                String.format(
                        "Slot %s Info",
                        info.slot + 1
                ),
                " ",
                String.format(
                        "<gray>Type: <white>%s",
                        info.typeName
                ),
                String.format(
                        "<gray>Trigger: <white>%s",
                        info.trigger
                ),
                " ",
                "<yellow>Click slot below to change");
    }

    private ItemStack createAbilitySlotItem(int abilityId) {
        Ability ability = Ability.getById(abilityId);
        if (ability != null && ability.getId() != 0) {
            return Item.create(ability.getIcon(),
                    "<gray>" + ability.getDisplayName(),
                    ability.getDescription());
        } else {
            return Item.create(Material.RED_STAINED_GLASS_PANE,
                    "<gray>Empty Slot",
                    "This slot is empty");
        }
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}