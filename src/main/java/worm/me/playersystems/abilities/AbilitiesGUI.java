package worm.me.playersystems.abilities;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import worm.me.utils.GuiConstructor;
import worm.me.Server;
import worm.me.playersystems.data.Unlocks;
import worm.me.utils.chat.Message;
import worm.me.utils.chat.MessageType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AbilitiesGUI {

    public void abilitiesActive(Player p) {
        PersistentDataContainer container = p.getPersistentDataContainer();
        Inventory gui = GuiConstructor.chestInventory(ChatColor.DARK_GRAY + "Your Active Abilities", 27);

        int[] active = Objects.requireNonNullElse(container.get(Abilities.abilitiesActive, PersistentDataType.INTEGER_ARRAY), new int[3]);

        gui.setItem(1, createItemStack(Material.BOOK, ChatColor.GRAY + "Slot 1 Info",
                " ",
                ChatColor.GRAY + "Type: " + ChatColor.WHITE + "Basic",
                ChatColor.GRAY + "Trigger: " + ChatColor.WHITE + "Double tap spacebar",
                " ",
                ChatColor.YELLOW + "Click slot below to change"));

        gui.setItem(4, createItemStack(Material.BOOK, ChatColor.GRAY + "Slot 2 Info",
                " ",
                ChatColor.GRAY + "Type: " + ChatColor.WHITE + "Horizontal Movement",
                ChatColor.GRAY + "Trigger: " + ChatColor.WHITE + "Sneak + Right Click + Sword",
                " ",
                ChatColor.YELLOW + "Click slot below to change"));

        gui.setItem(7, createItemStack(Material.BOOK, ChatColor.GRAY + "Slot 3 Info",
                " ",
                ChatColor.GRAY + "Type: " + ChatColor.WHITE + "Vertical Movement",
                ChatColor.GRAY + "Trigger: " + ChatColor.WHITE + "Sneak + Right Click + Axe",
                " ",
                ChatColor.YELLOW + "Click slot below to change"));

        gui.setItem(10, createAbilitySlotItem(active[0]));
        gui.setItem(13, createAbilitySlotItem(active[1]));
        gui.setItem(16, createAbilitySlotItem(active[2]));

        p.openInventory(gui);
    }

    private ItemStack createAbilitySlotItem(int abilityId) {
        Ability ability = Ability.getById(abilityId);
        if (ability != null && ability.getId() != 0) {
            return createItemStack(ability.getIcon(), ChatColor.GRAY + ability.getDisplayName(), ability.getDescription());
        } else {
            return createItemStack(Material.RED_STAINED_GLASS_PANE, ChatColor.GRAY + "Empty Slot", "This slot is empty");
        }
    }

    private String colorize(String input) {
        if (input == null) return "";
        return ChatColor.translateAlternateColorCodes('&',
                input.replace("<dark_gray>", "&8")
                        .replace("</dark_gray>", "&r"));
    }

    public void abilitiesSelect(Player p, int slot) {
        String title;
        AbilityType requiredType;

        if (slot == 0) {
            title = "First";
            requiredType = AbilityType.DOUBLE_SPACE;
        } else if (slot == 1) {
            title = "Second";
            requiredType = AbilityType.HORIZONTAL;
        } else {
            title = "Third";
            requiredType = AbilityType.VERTICAL;
        }

        Inventory gui = GuiConstructor.chestInventory(ChatColor.DARK_GRAY + title + " Ability Slot", 54);
        int slotNum = 10;

        gui.setItem(slotNum, createItemStack(
                Ability.EMPTY.getIcon(),
                ChatColor.GRAY + Ability.EMPTY.getDisplayName(),
                Ability.EMPTY.getDescription()
        ));
        slotNum++;

        for (Ability ability : Ability.values()) {
            if (ability == Ability.EMPTY) continue;

            if (ability.getType() != requiredType) continue;

            if ((slotNum + 1) % 9 == 0) slotNum += 2;

            if (slotNum > 44) break;

            if (Unlocks.hasAbilityUnlocked(p, ability.getId()) || p.isOp()) {
                gui.setItem(slotNum, createItemStack(
                        ability.getIcon(),
                        ChatColor.GRAY + ability.getDisplayName(),
                        colorize(ability.getDescription())
                ));
            } else {
                List<String> requirementsLore = List.of(Unlocks.requirementsTranslation(ability.getSkillRequirements()));
                List<String> lore = new ArrayList<>();
                lore.add(ChatColor.RED + "Locked Ability");
                for (String req : requirementsLore) {
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

        p.openInventory(gui);
    }

    public void activateAbility(Player p, int slot, InventoryClickEvent e) {
        PersistentDataContainer container = p.getPersistentDataContainer();

        int slotID = slot - 10;

        if (slot < 9 || slot > 44) {
            if (slot == 49) abilitiesActive(p);
            return;
        }
        if (slot % 9 == 0 || (slot + 1) % 9 == 0) return;

        for (int i = 0; i <= 9; i++) {
            if (slotID - (i * 9) < 9) {
                slotID = slotID - (i * 2);
                break;
            }
        }

        AbilityType requiredType = null;
        int targetSlot = -1;
        if (e.getView().getTitle().contains("First")) {
            requiredType = AbilityType.DOUBLE_SPACE;
            targetSlot = 0;
        } else if (e.getView().getTitle().contains("Second")) {
            requiredType = AbilityType.HORIZONTAL;
            targetSlot = 1;
        } else if (e.getView().getTitle().contains("Third")) {
            requiredType = AbilityType.VERTICAL;
            targetSlot = 2;
        }
        if (requiredType == null) return;

        List<Ability> validAbilities = new ArrayList<>();
        validAbilities.add(Ability.EMPTY);
        for (Ability ability : Ability.values()) {
            if (ability.getType() == requiredType && ability.getId() != 0) {
                validAbilities.add(ability);
            }
        }

        if (slotID >= validAbilities.size() || slotID < 0) return;
        Ability selectedAbility = validAbilities.get(slotID);

        int[] active = Objects.requireNonNullElse(
                container.get(Abilities.abilitiesActive, PersistentDataType.INTEGER_ARRAY),
                new int[3]
        );

        if (selectedAbility == Ability.EMPTY) {
            active[targetSlot] = 0;
            container.set(Abilities.abilitiesActive, PersistentDataType.INTEGER_ARRAY, active);
            p.sendMessage(Message.create(MessageType.SUCCESS, "Cleared ability slot!"));
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

        if (Unlocks.hasAbilityUnlocked(p, selectedAbility.getId()) || p.isOp()) {
            active[targetSlot] = selectedAbility.getId();
            container.set(Abilities.abilitiesActive, PersistentDataType.INTEGER_ARRAY, active);
            p.sendMessage(Message.create(MessageType.SUCCESS, "Equipped " + selectedAbility.getDisplayName() + "!"));
            abilitiesActive(p);
        } else {
            p.sendMessage(Message.create(MessageType.FAIL, "You haven't unlocked this ability yet!"));
        }
    }

    public void initializeAbilitySelection(Player p, InventoryClickEvent e) {
        if (e.getSlot() == 10) abilitiesSelect(p, 0);
        if (e.getSlot() == 13) abilitiesSelect(p, 1);
        if (e.getSlot() == 16) abilitiesSelect(p, 2);
        e.setCancelled(true);
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
