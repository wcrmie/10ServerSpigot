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
import worm.me.data.Unlocks;
import worm.me.menu.Menu;
import worm.me.playersystems.abilities.Abilities;
import worm.me.playersystems.abilities.Ability;
import worm.me.playersystems.abilities.AbilityType;
import worm.me.playersystems.abilities.CooldownDisplay;
import worm.me.utils.menu.GuiConstructor;
import worm.me.Server;
import worm.me.utils.chat.Message;
import worm.me.utils.chat.MessageType;

import java.util.*;
import java.util.stream.Collectors;

public class AbilitiesSubMenu extends Menu implements InventoryHolder {
    private final Inventory inventory;
    private final AbilityType abilityType;
    private final int targetSlot;
    private final List<Ability> validAbilities;

    public AbilitiesSubMenu(AbilityType abilityType, Player viewer, int targetSlot, String slotTitle) {
        super(viewer);
        this.abilityType = abilityType;
        this.targetSlot = targetSlot;
        this.validAbilities = new ArrayList<>();
        this.validAbilities.add(Ability.EMPTY);
        this.validAbilities.addAll(Arrays.stream(Ability.values())
                .filter(ability -> ability != Ability.EMPTY && ability.getType() == abilityType)
                .collect(Collectors.toList()));
        this.inventory = initialize();
    }

    @Override
    public Inventory initialize() {
        try {
            Inventory gui = GuiConstructor.chestInventory(
                    ChatColor.DARK_GRAY + getSlotTitle() + " Ability Slot",
                    54,
                    this
            );

            populateAbilitySelection(gui);

            return gui;
        } catch (Exception e) {
            Server.getPlugin().getLogger().warning("Error initializing ability selection for " + getViewer().getName() + ": " + e.getMessage());
            return GuiConstructor.chestInventory(ChatColor.DARK_GRAY + "Ability Selection", 54, this);
        }
    }

    @Override
    public void open() {
        getViewer().openInventory(inventory);
    }

    @Override
    public void handleClick(InventoryClickEvent event) {
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        int slot = event.getSlot();

        if (slot == 49) {
            AbilitiesMainMenu mainMenu = new AbilitiesMainMenu(player);
            mainMenu.open();
            return;
        }

        if (!isValidAbilitySlot(slot)) {
            return;
        }

        int slotID = calculateSlotID(slot);
        if (slotID >= validAbilities.size() || slotID < 0) {
            return;
        }

        Ability selectedAbility = validAbilities.get(slotID);
        handleAbilitySelection(player, selectedAbility);
    }

    private void populateAbilitySelection(Inventory gui) {
        int slotNum = 10;

        for (Ability ability : validAbilities) {
            if ((slotNum + 1) % 9 == 0) slotNum += 2;
            if (slotNum > 44) break;

            if (ability == Ability.EMPTY) {
                gui.setItem(slotNum, createItemStack(
                        Ability.EMPTY.getIcon(),
                        ChatColor.GRAY + Ability.EMPTY.getDisplayName(),
                        Ability.EMPTY.getDescription()
                ));
            } else if (Unlocks.hasAbilityUnlocked(getViewer(), ability.getId()) || getViewer().isOp()) {
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

    private void handleAbilitySelection(Player p, Ability selectedAbility) {
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
                AbilitiesMainMenu mainMenu = new AbilitiesMainMenu(p);
                mainMenu.open();
                return;
            }

            if (active[targetSlot] == selectedAbility.getId()) {
                p.sendMessage(Message.create(MessageType.FAIL, "This ability is already equipped in this slot!"));
                return;
            }

            if (Abilities.getActiveAbilities(p).contains(selectedAbility.getId()) && selectedAbility.getId() != 0) {
                p.sendMessage(Message.create(MessageType.FAIL, "You already have this ability active in another slot!"));
                return;
            }

            if (Abilities.hasAbilityOnCooldown(p)) {
                p.sendMessage(Message.create(MessageType.FAIL, "You have an ability on cooldown!"));
                return;
            }

            if (Abilities.validateAbilityForSlot(p, selectedAbility.getId(), targetSlot) || p.isOp()) {
                active[targetSlot] = selectedAbility.getId();
                container.set(Abilities.abilitiesActive, PersistentDataType.INTEGER_ARRAY, active);
                p.sendMessage(Message.create(MessageType.SUCCESS, "Equipped " + selectedAbility.getDisplayName() + "!"));
                CooldownDisplay.getInstance().refresh(p);
                AbilitiesMainMenu mainMenu = new AbilitiesMainMenu(p);
                mainMenu.open();
            } else {
                p.sendMessage(Message.create(MessageType.FAIL, "You haven't unlocked this ability yet!"));
            }
        } catch (Exception ex) {
            p.sendMessage(Message.create(MessageType.FAIL, "Error equipping ability!"));
            Server.getPlugin().getLogger().warning("Error handling ability selection for " + p.getName() + ": " + ex.getMessage());
        }
    }

    private String colorize(String input) {
        if (input == null) return "";
        return ChatColor.translateAlternateColorCodes('&',
                input.replace("<dark_gray>", "&8")
                        .replace("</dark_gray>", "&r"));
    }

    private String getSlotTitle() {
        return switch (targetSlot) {
            case 0 -> "First";
            case 1 -> "Second";
            case 2 -> "Third";
            default -> "Unknown";
        };
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