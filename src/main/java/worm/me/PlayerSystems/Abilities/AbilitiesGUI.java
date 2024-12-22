package worm.me.PlayerSystems.Abilities;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import worm.me.GuiConstructor;

import java.util.Arrays;
import java.util.List;

public class AbilitiesGUI {

    public void abilitiesGuiOpen(Player p){
        GuiConstructor guiConstructor = new GuiConstructor();

        // Create the GUI
        Inventory gui = guiConstructor.chestInventory( ChatColor.DARK_GRAY + "Abilities", 54);

        // Create gui items methodically
        AbilitiesMain abilities = new AbilitiesMain();
        for (int i = 0; i < AbilitiesMain.abilityGuiComponents.size(); i++){
            String abilityName = AbilitiesMain.abilityGuiComponents.keySet().toArray()[i].toString();
            String abilityMaterial = AbilitiesMain.abilityGuiComponents.get(abilityName).getFirst();
            String abilityDescription = AbilitiesMain.abilityGuiComponents.get(abilityName).get(1);

            List<String> abilityReqs = new java.util.ArrayList<>(List.of(""));

            // For each extra requirement, add a lore line. Not all abilities have the same
            // amount of requirements, so we need to loop through the list and add them.
            for (int j = 2; j < AbilitiesMain.abilityGuiComponents.get(abilityName).size(); j++){
                abilityReqs.add(AbilitiesMain.abilityGuiComponents.get(abilityName).get(j));
            }

            // Make sure the slot, or the slot + 1 is not divisible by 9. (we don't want stuff on the edges of the gui)
            int slotNum = i + 10;

            if (slotNum % 9 == 0){
                slotNum++;
            } else if ((slotNum + 1) % 9 == 0){
                slotNum = slotNum + 2;
            }

            // Never have more than 28 items in the GUI
            if (i > 28){
                break;
            }

            // Construct item
            gui.setItem(slotNum, guiConstructor.createGuiItem(Material.getMaterial(abilityMaterial), ChatColor.GRAY + abilityName, abilityDescription, Arrays.toString(abilityReqs.toArray(new String[0]))));

            // Open inventory to player.
            p.openInventory(gui);
        }
    }

    public void guiHandler(ItemStack item, Player p){
        // Handle the GUI events here
        AbilitiesMain abilities = new AbilitiesMain();

        if (item.getType().equals(Material.RED_STAINED_GLASS_PANE)){

        }
    }
}
