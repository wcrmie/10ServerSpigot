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
import java.util.stream.Collectors;

public class AbilitiesMainMenu extends Menu implements InventoryHolder, MainSubMenu {

    private Inventory inventory;

    public AbilitiesMainMenu(Player viewer) {

        super(viewer);
    }

    @Override
    public Inventory initialize() {
        return null;
    }


    @Override
    public void open() {

    }

    @Override
    public void handleClick(InventoryClickEvent event) {

    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

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

    public void abilitiesActive(Player p) {
        try {
            PersistentDataContainer container = p.getPersistentDataContainer();
            Inventory gui = GuiConstructor.chestInventory(ChatColor.DARK_GRAY + "Your Active Abilities", 27, this);

            int[] active = Objects.requireNonNullElse(
                    container.get(Abilities.abilitiesActive, PersistentDataType.INTEGER_ARRAY),
                    Ability.DEFAULT_ACTIVE_ABILITIES
            );

            // Create slot info items using loop
            int[] infoSlots = {1, 4, 7};
            for (int i = 0; i < slotInfos.length; i++) {
                gui.setItem(infoSlots[i], createSlotInfoItem(slotInfos[i]));
            }

            // Set ability slot items using loop
            for (int i = 0; i < Ability.SLOT_COUNT; i++) {
                gui.setItem(Ability.ABILITY_SLOT_POSITIONS[i], createAbilitySlotItem(active[i]));
            }

            p.openInventory(gui);
        } catch (Exception e) {
            p.sendMessage(Message.create(MessageType.FAIL, "Error opening abilities menu!"));
            Server.getPlugin().getLogger().warning("Error opening abilities GUI for " + p.getName() + ": " + e.getMessage());
        }
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

    public void abilitiesSelect(Player p, int slot) {
        try {
            if (slot < 0 || slot >= slotInfos.length) {
                p.sendMessage(Message.create(MessageType.FAIL, "Invalid ability slot!"));
                return;
            }

            SlotInfo slotInfo = slotInfos[slot];
            Inventory gui = GuiConstructor.chestInventory(
                    ChatColor.DARK_GRAY + slotInfo.title + " Ability Slot",
                    54,
                    this
            );

            populateAbilitySelection(gui, slotInfo.type, p);
            p.openInventory(gui);
        } catch (Exception e) {
            p.sendMessage(Message.create(MessageType.FAIL, "Error opening ability selection!"));
            Server.getPlugin().getLogger().warning("Error opening ability selection for " + p.getName() + ": " + e.getMessage());
        }
    }

    private void populateAbilitySelection(Inventory gui, AbilityType requiredType, Player player) {
        int slotNum = 10;

        // Add empty slot option
        gui.setItem(slotNum, createItemStack(
                Ability.EMPTY.getIcon(),
                ChatColor.GRAY + Ability.EMPTY.getDisplayName(),
                Ability.EMPTY.getDescription()
        ));
        slotNum++;

        // Add valid abilities
        List<Ability> validAbilities = Arrays.stream(Ability.values())
                .filter(ability -> ability != Ability.EMPTY && ability.getType() == requiredType)
                .collect(Collectors.toList());

        for (Ability ability : validAbilities) {
            if ((slotNum + 1) % 9 == 0) slotNum += 2;
            if (slotNum > 44) break;

            if (Unlocks.hasAbilityUnlocked(player, ability.getId()) || player.isOp()) {
                gui.setItem(slotNum, createItemStack(
                        ability.getIcon(),
                        ChatColor.GRAY + ability.getDisplayName(),
                        colorize(ability.getDescription())
                ));
            } else {
                List<String> lore = new ArrayList<>();
                lore.add(ChatColor.RED + "Locked Ability");
                String[] requirements = Unlocks.requirementsTranslation(ability.getSkillRequirements());
                for (String req : requirements) {
                    lore.add(ChatColor.DARK_GRAY + " - " + ChatColor.WHITE + req);
                }

                gui.setItem(slotNum, createItemStack(
                        Material.RED_STAINED_GLASS_PANE,
                        ChatColor.GRAY + ability.getDisplayName(),
                        lore.toArray(new String[0])
                ));
            }
            slotNum++;
        }
    }

    public void activateAbility(Player p, int slot, InventoryClickEvent e) {
        try {
            String title = ChatColor.stripColor(e.getView().getTitle());

            if (slot == 49) {
                abilitiesActive(p);
                e.setCancelled(true);
                return;
            }

            boolean isSelectionGUI = false;
            SlotInfo slotInfo = null;

            for (SlotInfo info : slotInfos) {
                if (title.contains(info.title)) {
                    isSelectionGUI = true;
                    slotInfo = info;
                    break;
                }
            }

            if (!isSelectionGUI || slotInfo == null) {
                return;
            }
            if (!isValidAbilitySlot(slot)) {
                e.setCancelled(true);
                return;
            }

            int slotID = calculateSlotID(slot);
            Ability selectedAbility = getAbilityFromSlotID(slotID, slotInfo.type);

            if (selectedAbility == null) {
                e.setCancelled(true);
                return;
            }

            handleAbilitySelection(p, selectedAbility, slotInfo.slot, e);

        } catch (Exception ex) {
            p.sendMessage(Message.create(MessageType.FAIL, "Error selecting ability!"));
            Server.getPlugin().getLogger().warning("Error activating ability for " + p.getName() + ": " + ex.getMessage());
            e.setCancelled(true);
        }
    }

    private boolean isValidAbilitySlot(int slot) {
        return slot >= 9 && slot <= 44 && slot % 9 != 0 && (slot + 1) % 9 != 0;
    }

    private int calculateSlotID(int slot) {
        int slotID = slot - 10;
        for (int i = 0; i <= 9; i++) {
            if (slotID - (i * 9) < 9) {
                slotID = slotID - (i * 2);
                break;
            }
        }
        return slotID;
    }

    private Ability getAbilityFromSlotID(int slotID, AbilityType requiredType) {
        List<Ability> validAbilities = new ArrayList<>();
        validAbilities.add(Ability.EMPTY);
        validAbilities.addAll(Arrays.stream(Ability.values())
                .filter(ability -> ability != Ability.EMPTY && ability.getType() == requiredType)
                .collect(Collectors.toList()));

        if (slotID >= validAbilities.size() || slotID < 0) return null;
        return validAbilities.get(slotID);
    }

    private void handleAbilitySelection(Player p, Ability selectedAbility, int targetSlot, InventoryClickEvent e) {
        try {
            PersistentDataContainer container = p.getPersistentDataContainer();
            int[] active = Objects.requireNonNullElse(
                    container.get(Abilities.abilitiesActive, PersistentDataType.INTEGER_ARRAY),
                    Ability.DEFAULT_ACTIVE_ABILITIES
            );

            if (selectedAbility == Ability.EMPTY) {
                active[targetSlot] = 0;
                container.set(Abilities.abilitiesActive, PersistentDataType.INTEGER_ARRAY, active);
                p.sendMessage(Message.create(MessageType.SUCCESS, "Cleared ability slot!"));

                CooldownDisplay.getInstance().refresh(p);
                abilitiesActive(p);
                return;
            }

            if (active[targetSlot] == selectedAbility.getId()) {
                p.sendMessage(Message.create(MessageType.FAIL, "This ability is already equipped in this slot!"));
                return;
            }

            Abilities abilities = new Abilities();

            if (abilities.getActiveAbilities(p).contains(selectedAbility.getId()) && selectedAbility.getId() != 0) {
                p.sendMessage(Message.create(MessageType.FAIL, "You already have this ability active in another slot!"));
                return;
            }

            if (abilities.hasAbilityOnCooldown(p)) {
                p.sendMessage(Message.create(MessageType.FAIL, "You have an ability on cooldown!"));
                return;
            }

            if (Abilities.validateAbilityForSlot(p, selectedAbility.getId(), targetSlot) || p.isOp()) {
                active[targetSlot] = selectedAbility.getId();
                container.set(Abilities.abilitiesActive, PersistentDataType.INTEGER_ARRAY, active);
                p.sendMessage(Message.create(MessageType.SUCCESS, "Equipped " + selectedAbility.getDisplayName() + "!"));

                CooldownDisplay.getInstance().refresh(p);
                abilitiesActive(p);
            } else {
                p.sendMessage(Message.create(MessageType.FAIL, "You haven't unlocked this ability yet!"));
            }

            e.setCancelled(true);
        } catch (Exception ex) {
            p.sendMessage(Message.create(MessageType.FAIL, "Error equipping ability!"));
            Server.getPlugin().getLogger().warning("Error handling ability selection for " + p.getName() + ": " + ex.getMessage());
        }
    }

    public void initializeAbilitySelection(Player p, InventoryClickEvent e) {
        try {
            int slot = e.getSlot();
            for (int i = 0; i < Ability.ABILITY_SLOT_POSITIONS.length; i++) {
                if (slot == Ability.ABILITY_SLOT_POSITIONS[i]) {
                    abilitiesSelect(p, i);
                    break;
                }
            }
            e.setCancelled(true);
        } catch (Exception ex) {
            Server.getPlugin().getLogger().warning("Error initializing ability selection for " + p.getName() + ": " + ex.getMessage());
        }
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
}