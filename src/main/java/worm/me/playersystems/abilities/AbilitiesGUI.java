package worm.me.playersystems.abilities;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import worm.me.GuiConstructor;
import worm.me.Server;
import worm.me.playersystems.data.Unlocks;
import worm.me.utils.Item;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AbilitiesGUI {

    public void abilitiesActive(Player p){
        PersistentDataContainer container = p.getPersistentDataContainer();
        Inventory gui = GuiConstructor.chestInventory(ChatColor.DARK_GRAY + "Your Active Abilities", 27);

        Abilities abilities = new Abilities();

        int[] active = Objects.requireNonNullElse(container.get(Abilities.abilitiesActive, PersistentDataType.INTEGER_ARRAY), new int[3]);

        Server.pluginMessage("Active: " + Arrays.toString(active), p);

        try {
            String abilityName = Abilities.abilityGuiComponents.get(active[0]).getFirst();
            String abilityMaterial = Abilities.abilityGuiComponents.get(active[0]).get(1);
            String abilityDescription = Abilities.abilityGuiComponents.get(active[0]).get(2);
            gui.setItem(11, Item.create(Material.getMaterial(abilityMaterial), ChatColor.GRAY + abilityName, abilityDescription));
        } catch (Exception e){
            gui.setItem(11, Item.create(Material.RED_STAINED_GLASS_PANE, ChatColor.GRAY + "Empty Slot", "This slot is empty"));
        }

        try {
            String abilityName = Abilities.abilityGuiComponents.get(active[1]).getFirst();
            String abilityMaterial = Abilities.abilityGuiComponents.get(active[1]).get(1);
            String abilityDescription = Abilities.abilityGuiComponents.get(active[1]).get(2);
            gui.setItem(13, Item.create(Material.getMaterial(abilityMaterial), ChatColor.GRAY + abilityName, abilityDescription));
        } catch (Exception e){

            Server.pluginMessage("Error: " + e, p);

            gui.setItem(13, Item.create(Material.RED_STAINED_GLASS_PANE, ChatColor.GRAY + "Empty Slot", "This slot is empty"));
        }

        try {
            String abilityName = Abilities.abilityGuiComponents.get(active[2]).getFirst();
            String abilityMaterial = Abilities.abilityGuiComponents.get(active[2]).get(1);
            String abilityDescription = Abilities.abilityGuiComponents.get(active[2]).get(2);
            gui.setItem(15, Item.create(Material.getMaterial(abilityMaterial), ChatColor.GRAY + abilityName, abilityDescription));
        } catch (Exception e){
            gui.setItem(15, Item.create(Material.RED_STAINED_GLASS_PANE, ChatColor.GRAY + "Empty Slot", "This slot is empty"));
        }

        p.openInventory(gui);
    }

    public void abilitiesSelect(Player p, int slot){

        String title;

        if (slot == 0) {
            title = "First";
        } else if (slot == 1){
            title = "Second";
        } else{
            title = "Third";

        }

        // Create the GUI
        Inventory gui = GuiConstructor.chestInventory( ChatColor.DARK_GRAY + title + " Ability Slot", 54);

        // for each item

        int slotNum = 10;

        for (int i = 0; i < Abilities.abilityGuiComponents.size(); i++){

            // Get the ability name, material, and description
            String abilityName = Abilities.abilityGuiComponents.get(i).get(0);
            String abilityMaterial = Abilities.abilityGuiComponents.get(i).get(1);
            String abilityDescription = Abilities.abilityGuiComponents.get(i).get(2);

            List<String> abilityReqs = new java.util.ArrayList<>(List.of(""));

            // For each extra requirement, add a lore line. Not all abilities have the same
            // amount of requirements, so we need to loop through the list and add them.
            for (int j = 3; j < Abilities.abilityGuiComponents.get(i).size(); j++){
                abilityReqs.add(Abilities.abilityGuiComponents.get(i).get(j));
            }

            // Make sure the slot, or the slot + 1 is not divisible by 9. (we don't want stuff on the edges of the gui)


            if ((slotNum + 1) % 9 == 0) slotNum += 2;


            // Never have more than 28 items in the GUI
            if (i > 28 || i > Abilities.abilityGuiComponents.size()){
                break;
            }

            if (Unlocks.hasAbilityUnlocked(p, i) || p.isOp()){

                // Construct item
                gui.setItem(slotNum, Item.create(Material.valueOf(abilityMaterial), ChatColor.GRAY + abilityName, abilityDescription));

            } else {

                // If the ability is not unlocked, add a red glass pane
                gui.setItem(slotNum, Item.create(Material.RED_STAINED_GLASS_PANE, ChatColor.GRAY + abilityName, Unlocks.requirementsTranslation(Abilities.abilityRequirements.get(i))));

            }

            slotNum++;
            // Open inventory to player.
        }
        p.openInventory(gui);
    }

    public void activateAbility(Player p, int slot, InventoryClickEvent e){
        // Handle the GUI events here
        PersistentDataContainer container = p.getPersistentDataContainer();

        int slotID = slot - 10;

        if (slot < 9 || slot > 44){

            if (slot == 49){
                abilitiesActive(p);
            }

            return;
        }

        if (slot % 9 == 0){
            return;
        } else if ((slot + 1) % 9 == 0){
            return;
        }

        for (int i = 0; i <= 9; i++){
            if (slotID - (i * 9) < 9){
                slotID = slotID - (i * 2);
                break;
            }

        }

        Abilities abilities = new Abilities();

        if (abilities.getActiveAbilities(p).contains(slotID) && slotID != 0){
            Server.pluginMessage(ChatColor.RED + "You already have this ability active!", p);
            return;
        }

        if (abilities.hasAbilityOnCooldown(p)) {
            Server.pluginMessage(ChatColor.RED + "You have an ability on cooldown!", p);
            return;
        }

        if (Unlocks.hasAbilityUnlocked(p, slotID) || p.isOp()){
            int[] active = Objects.requireNonNullElse(container.get(Abilities.abilitiesActive, PersistentDataType.INTEGER_ARRAY), new int[3]);
            if (e.getView().getTitle().contains("First")){
                Server.pluginMessage("First " + slotID, p);
                active[0] = slotID;
                container.set(Abilities.abilitiesActive, PersistentDataType.INTEGER_ARRAY, active);
            }
            if (e.getView().getTitle().contains("Second")){
                active[1] = slotID;
                container.set(Abilities.abilitiesActive, PersistentDataType.INTEGER_ARRAY, active);
            }
            if (e.getView().getTitle().contains("Third")){
                active[2] = slotID;
                container.set(Abilities.abilitiesActive, PersistentDataType.INTEGER_ARRAY, active);
            }
        }

        abilitiesActive(p);

    }

    public void initializeAbilitySelection(Player p, InventoryClickEvent e){

        if (e.getSlot() == 11) abilitiesSelect(p, 0);
        if (e.getSlot() == 13) abilitiesSelect(p, 1);
        if (e.getSlot() == 15) abilitiesSelect(p, 2);
        e.setCancelled(true);

    }

}
