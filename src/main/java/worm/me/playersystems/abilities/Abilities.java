package worm.me.playersystems.abilities;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import worm.me.playersystems.abilities.active.AgilityDash;
import worm.me.playersystems.abilities.active.AgilityDoubleJump;
import worm.me.playersystems.abilities.active.AgilityLeap;
import worm.me.playersystems.abilities.active.FrogGrab;
import worm.me.playersystems.abilities.active.*;
import worm.me.Server;
import worm.me.playersystems.abilities.passive.SlimeBoots;

import java.util.*;

public class Abilities {

    public static final NamespacedKey abilitiesUnlocked = new NamespacedKey(Server.getPlugin(), "unlockedabilities");

    public static final NamespacedKey abilitiesActive = new NamespacedKey(Server.getPlugin(), "activeabilities");

    public static final NamespacedKey abilityCooldown = new NamespacedKey(Server.getPlugin(), "abilitycooldown");

    public static final Map<Integer, List<String>> abilityGuiComponents = new HashMap<>();

    public static final Map<Integer, int[]> abilityRequirements = new HashMap<>();

    public void abilitiesInit(){
        // Initialize the GUI Components for the abilities.
        // We should initialize stuff in their related classes like this
        // instead of clogging up the Server class.

        // Format: abilityGuiComponents.put("Ability Name", List.of("Material", "Description", "Requirements"));

        abilityGuiComponents.put(0, List.of("Empty", "RED_STAINED_GLASS_PANE", "Empty Slot", "This slot is empty"));

        // ID 1
        abilityGuiComponents.put(1, List.of("Dash", "IRON_SWORD", "Dash forward with a sword using " + ChatColor.GRAY + "[SNEAK] + [RIGHT-CLICK]"));

        // ID 2
        abilityGuiComponents.put(2, List.of("Double Jump", "FEATHER", "Jump a second time in the air!"));

        // ID 3
        abilityGuiComponents.put(3, List.of("Leap", "IRON_AXE", "Leap up in the air with an axe using " + ChatColor.GRAY + "[SNEAK] + [RIGHT-CLICK]"));

        // ID 4
        abilityGuiComponents.put(4, List.of("Wall Jump", "IRON_BARS", "Clutch onto walls using " + ChatColor.GRAY + "[SNEAK]"));

        // ID 5
        abilityGuiComponents.put(5, List.of("Frog Grab", "PINK_CONCRETE_POWDER", "Grab things with your tongue using a tongue item " + ChatColor.GRAY + "[SNEAK] + [Left-Click]"));

        // ID 6
        abilityGuiComponents.put(6, List.of("SCREAM", "GOAT_HORN", "Scream while holding a note block " + ChatColor.GRAY + "[SNEAK] + [Left-Click]"));

        // ID 7
        abilityGuiComponents.put(7, List.of("Shadow Step", "GRAY_STAINED_GLASS", "move forward through walls with a path of shadows following you while holding Gray Stained Glass Block" + ChatColor.GRAY + "[SNEAK] + [Left-Click]"));

        // ID 8
        abilityGuiComponents.put(8, List.of("Slime Boots", "SLIME_BALL", "Bounce while wearing slime boots " + ChatColor.GRAY + "[SNEAK] to Deactivate "));

    }

    public void initializeRequirements(){

        // Format: abilityRequirements.put(abilityID, new int[]{combat, farming, crafting, fishing, smithing, mining, agility, foraging});
        abilityRequirements.put(0, new int[]{1, 0, 0, 0, 0, 0, 0, 0});
        abilityRequirements.put(1, new int[]{5, 0, 0, 0, 0, 0, 10, 0});
        abilityRequirements.put(2, new int[]{10, 0, 0, 0, 0, 0, 5, 0});
        abilityRequirements.put(3, new int[]{0, 0, 0, 0, 0, 0, 15, 0});
        abilityRequirements.put(4, new int[]{0, 0, 0, 0, 0, 0, 20, 0});
        abilityRequirements.put(5, new int[]{10, 40, 0, 50, 0, 0, 20, 0});
        abilityRequirements.put(6, new int[]{20, 10, 5, 15, 0, 0, 10, 20});
        abilityRequirements.put(7, new int[]{20, 0, 5, 0, 5, 5, 25, 0});
        abilityRequirements.put(8, new int[]{10, 0, 5, 10, 10, 0, 10, 0});
    }

    public List<Integer> getActiveAbilities(Player p){
        PersistentDataContainer container = p.getPersistentDataContainer();

        List<Integer> active = new ArrayList<>();

        for (int i : Objects.requireNonNullElse(container.get(abilitiesActive, PersistentDataType.INTEGER_ARRAY), new int[3])){

            active.add(i);

        }

        return active;
    }

    public boolean isActive(Player p, int abilityID){
        return getActiveAbilities(p).contains(abilityID);
    }

    public int getAbilitySlot(Player p, int abilityID){
        List<Integer> active = getActiveAbilities(p);

        for (int i = 0; i < active.size(); i++){
            if (active.get(i) == abilityID){
                return i;
            }
        }

        return -1;
    }

    public boolean hasAbilityOnCooldown(Player p){

        for (long i : Objects.requireNonNullElse(p.getPersistentDataContainer().get(abilityCooldown, PersistentDataType.LONG_ARRAY), new long[3])){
            if (i > System.currentTimeMillis()/1000L){
                return true;
            }
        }
        return false;

    }

    public void handleAbility(Player p, int abilityID){

        PersistentDataContainer container = p.getPersistentDataContainer();

        long[] cooldowns = Objects.requireNonNullElse(container.get(abilityCooldown, PersistentDataType.LONG_ARRAY), new long[3]);

        if (abilityID == 0){
            return;

        } else if (abilityID == 1){

            AgilityDash agilityDash = new AgilityDash();
            agilityDash.dash(p);

            cooldowns[getAbilitySlot(p, 1)] = System.currentTimeMillis()/1000L + 10;

        } else if (abilityID == 2){

            AgilityDoubleJump doubleJump = new AgilityDoubleJump();
            doubleJump.doubleJump(p);

            cooldowns[getAbilitySlot(p, 2)] = System.currentTimeMillis()/1000L + 15;

        } else if (abilityID == 3){

            AgilityLeap leap = new AgilityLeap();
            leap.leap(p);

            cooldowns[getAbilitySlot(p, 3)] = System.currentTimeMillis()/1000L + 10;

        } else if (abilityID == 4){

            // not implemented
            return;

        } else if (abilityID == 5){

            FrogGrab frogGrab = new FrogGrab();
            frogGrab.frogGrab(p);

            cooldowns[getAbilitySlot(p, 5)] = System.currentTimeMillis()/1000L + 10;

        } else if (abilityID == 6){

            AbliityScream AbliityScream = new AbliityScream();
            AbliityScream.scream(p);

            cooldowns[getAbilitySlot(p, 6)] = System.currentTimeMillis()/1000L + 30;

        } else if (abilityID == 7){

            ShadowStep ShadowStep = new ShadowStep();
            ShadowStep.shadowStep(p);

            cooldowns[getAbilitySlot(p, 7)] = System.currentTimeMillis()/1000L + 25;
        } else if (abilityID == 8) {
            SlimeBoots SlimeBoots = new SlimeBoots();
            SlimeBoots.bounce(p);
        }

        container.set(abilityCooldown, PersistentDataType.LONG_ARRAY, cooldowns);

    }

}
