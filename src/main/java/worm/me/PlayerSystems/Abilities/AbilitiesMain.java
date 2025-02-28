package worm.me.PlayerSystems.Abilities;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class AbilitiesMain {

    public final NamespacedKey moveCooldown = new NamespacedKey("10server", "movecooldown");

    public final NamespacedKey abilitiesUnlocked = new NamespacedKey("10server", "abilitiesunlocked");

    public final NamespacedKey abilitiesActive = new NamespacedKey("10server", "abilitiesactive");

    public static final HashMap<String, List<String>> abilityGuiComponents = new HashMap<>();

    public void abilitiesInit(){
        // Initialize the GUI Components for the abilities.
        // We should initialize stuff in their related classes like this
        // instead of clogging up the server class.

        // Format: abilityGuiComponents.put("Ability Name", List.of("Material", "Description", "Requirements"));

        abilityGuiComponents.put("Empty", List.of("RED_STAINED_GLASS_PANE", "Empty Slot", "This slot is empty"));

        // ID 1
        abilityGuiComponents.put("Dash", List.of("IRON_SWORD", "Dash forward with a sword using " + ChatColor.GRAY + "[SNEAK] + [RIGHT-CLICK]", "CHECK combat 5", "CHECK agility 10"));

        // ID 2
        abilityGuiComponents.put("Double Jump", List.of("FEATHER", "Jump a second time in the air using " + ChatColor.GRAY + "[SNEAK]", "CHECK agility 15"));

        // ID 3
        abilityGuiComponents.put("Leap", List.of("IRON_AXE", "Leap up in the air with an axe using " + ChatColor.GRAY + "[SNEAK] + [RIGHT-CLICK]", "CHECK combat 10", "CHECK agility 5"));
    }

    public void unlockAbility(Integer ability, Player p){
        // Does player have any other abilities unlocked? (Check for variable)
        PersistentDataContainer container = p.getPersistentDataContainer();
        int[] unlocked = container.get(abilitiesUnlocked, PersistentDataType.INTEGER_ARRAY);
        if (unlocked == null){
            unlocked = new int[abilityGuiComponents.size()];
        }

        // Set the ability to unlocked
        unlocked[ability] = 1;
    }

    public void activateAbility(Integer ability, Integer slot, Player p){

        PersistentDataContainer container = p.getPersistentDataContainer();
        int[] active = Objects.requireNonNullElse(container.get(abilitiesActive, PersistentDataType.INTEGER_ARRAY), new int[3]);

        // Add the ability to an active slot
        active[slot] = ability;
    }
}
