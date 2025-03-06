package worm.me.PlayerSystems.Abilities;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import worm.me.Server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Abilities {

    public static final NamespacedKey moveCooldown = new NamespacedKey(Server.getPlugin(), "movecooldown");

    public static final NamespacedKey abilitiesUnlocked = new NamespacedKey(Server.getPlugin(), "unlockedabilities");

    public static final NamespacedKey abilitiesActive = new NamespacedKey(Server.getPlugin(), "activeabilities");

    public static final Map<String, List<String>> abilityGuiComponents = new HashMap<>();

    public static final Map<Integer, int[]> abilityRequirements = new HashMap<>();

    public void abilitiesInit(){
        // Initialize the GUI Components for the abilities.
        // We should initialize stuff in their related classes like this
        // instead of clogging up the Server class.

        // Format: abilityGuiComponents.put("Ability Name", List.of("Material", "Description", "Requirements"));

        abilityGuiComponents.put("Empty", List.of("RED_STAINED_GLASS_PANE", "Empty Slot", "This slot is empty"));

        // ID 1
        abilityGuiComponents.put("Dash", List.of("IRON_SWORD", "Dash forward with a sword using " + ChatColor.GRAY + "[SNEAK] + [RIGHT-CLICK]"));

        // ID 2
        abilityGuiComponents.put("Double Jump", List.of("FEATHER", "Jump a second time in the air using " + ChatColor.GRAY + "[SNEAK]"));

        // ID 3
        abilityGuiComponents.put("Leap", List.of("IRON_AXE", "Leap up in the air with an axe using " + ChatColor.GRAY + "[SNEAK] + [RIGHT-CLICK]"));

        // ID 4
        abilityGuiComponents.put("Wall Jump", List.of("IRON_BARS", "Clutch onto walls by pressing " + ChatColor.GRAY + "[SNEAK]"));
    }

    public void unlockAbility(Integer ability, Player p){
        // Does player have any other abilities unlocked? (Check for variable)
        PersistentDataContainer container = p.getPersistentDataContainer();
        int[] unlocked = container.get(abilitiesUnlocked, PersistentDataType.INTEGER_ARRAY);

        // Set the ability to unlocked
        unlocked[ability] = 1;
    }

    public void activateAbility(Integer ability, Integer slot, Player p){

        PersistentDataContainer container = p.getPersistentDataContainer();
        int[] active = Objects.requireNonNullElse(container.get(abilitiesActive, PersistentDataType.INTEGER_ARRAY), new int[3]);

        // Add the ability to an active slot
        active[slot] = ability;
    }

    public void initializeRequirements(){

        // Format: abilityRequirements.put(abilityID, new int[]{combat, farming, crafting, fishing, smithing, mining, agility, foraging});
        abilityRequirements.put(0, new int[]{1, 0, 0, 0, 0, 0, 0, 0});
        abilityRequirements.put(1, new int[]{5, 0, 0, 0, 0, 0, 10, 0});
        abilityRequirements.put(2, new int[]{10, 0, 0, 0, 0, 0, 5, 0});
        abilityRequirements.put(3, new int[]{0, 0, 0, 0, 0, 0, 15, 0});
        abilityRequirements.put(4, new int[]{0, 0, 0, 0, 0, 0, 20, 0});
    }
}
