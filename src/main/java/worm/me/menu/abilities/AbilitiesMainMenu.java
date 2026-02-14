package worm.me.menu.abilities;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import worm.me.menu.MainSubMenu;
import worm.me.menu.Menu;
import worm.me.playersystems.abilities.Abilities;
import worm.me.playersystems.abilities.Ability;
import worm.me.playersystems.abilities.AbilityType;
import worm.me.playersystems.abilities.CooldownDisplay;
import worm.me.utils.GuiConstructor;
import worm.me.Server;
import worm.me.data.Unlocks;
import worm.me.utils.chat.Message;
import worm.me.utils.chat.MessageType;

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
            Inventory gui = GuiConstructor.chestInventory(ChatColor.DARK_GRAY + "Your Active Abilities", 27, this);

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
            return GuiConstructor.chestInventory(ChatColor.DARK_GRAY + "Your Active Abilities", 27, this);
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
        return createItemStack(Material.BOOK, ChatColor.GRAY + "Slot " + (info.slot + 1) + " Info",
                " ",
                ChatColor.GRAY + "Type: " + ChatColor.WHITE + info.typeName,
                ChatColor.GRAY + "Trigger: " + ChatColor.WHITE + info.trigger,
                " ",
                ChatColor.YELLOW + "Click slot below to change");
    }

    private ItemStack createAbilitySlotItem(int abilityId) {
        Ability ability = Ability.getById(abilityId);
        if (ability != null && ability.getId() != 0) {
            return createItemStack(ability.getIcon(),
                    ChatColor.GRAY + ability.getDisplayName(),
                    colorize(ability.getDescription()));
        } else {
            return createItemStack(Material.RED_STAINED_GLASS_PANE,
                    ChatColor.GRAY + "Empty Slot",
                    "This slot is empty");
        }
    }

    private String colorize(String input) {
        if (input == null) return "";
        return ChatColor.translateAlternateColorCodes('&',
                input.replace("<dark_gray>", "&8")
                        .replace("</dark_gray>", "&r"));
    }

    private ItemStack createItemStack(Material material, String displayName, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(displayName);
            if (lore.length > 0) {
                meta.setLore(Arrays.asList(lore));
            }
            item.setItemMeta(meta);
        }
        return item;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}