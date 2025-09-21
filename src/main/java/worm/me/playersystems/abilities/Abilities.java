package worm.me.playersystems.abilities;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import worm.me.playersystems.abilities.active.Dash;
import worm.me.playersystems.abilities.active.DoubleJump;
import worm.me.playersystems.abilities.active.Leap;
import worm.me.playersystems.abilities.active.*;
import worm.me.Server;
import worm.me.playersystems.abilities.passive.SlimeBoots;
import worm.me.playersystems.data.levels.Reqs;

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
        abilityGuiComponents.put(1, List.of("Double Jump", "FEATHER", ChatColor.DARK_GRAY + "Rapidly press space twice in the air to perform a double jump! "));

        // ID 2
        abilityGuiComponents.put(2, List.of("Dash", "IRON_SWORD", ChatColor.DARK_GRAY + "Dash forward " + ChatColor.GRAY + "[SNEAK] + [RIGHT-CLICK] + [Holding Sword]"));

        // ID 3
        abilityGuiComponents.put(3, List.of("Leap", "IRON_AXE", ChatColor.DARK_GRAY + "Leap up in the air " + ChatColor.GRAY + "[SNEAK] + [RIGHT-CLICK] + [Holding Axe]"));

        // ID 4
        abilityGuiComponents.put(4, List.of("Wall Jump", "IRON_BARS", ChatColor.DARK_GRAY + "Clutch onto walls using " + ChatColor.GRAY + "[SNEAK]"));

        // ID 5
        abilityGuiComponents.put(5, List.of("SCREAM", "GOAT_HORN", ChatColor.DARK_GRAY + "Scream as loud as you can using " + ChatColor.GRAY + "[SNEAK] + [LEFT-CLICK] + [Holding Note Block]"));

        // ID 6
        abilityGuiComponents.put(6, List.of("Shadow-Step", "TINTED_GLASS", ChatColor.DARK_GRAY + "Step through space using " + ChatColor.GRAY + "[SNEAK] + [LEFT-CLICK] + [Holding Tinted Glass] + [Standing on Ground]"));

        // ID 7
        abilityGuiComponents.put(7, List.of("Slime Boots", "SLIME_BALL", ChatColor.DARK_GRAY + "Bounce " + ChatColor.GRAY + "[Wearing Slime Boots] ||| [SNEAK] to Deactivate "));

        // ID 8
        abilityGuiComponents.put(8, List.of("Feather-Float", "ELYTRA", ChatColor.DARK_GRAY + "Float through the air swiftly using " + ChatColor.GRAY + "[SNEAK] + [LEFT-CLICK] + [Holding Feather]"));

        // ID 9
        abilityGuiComponents.put(9, List.of("Speed-Step", "RABBIT_FOOT", ChatColor.DARK_GRAY + "Speed off in the direction you are looking using " + ChatColor.GRAY + "[SNEAK] + [LEFT-CLICK] + [Rabbit Foot]"));

        // ID 10
        abilityGuiComponents.put(10, List.of("Tactical Retreat", "BOW", ChatColor.DARK_GRAY + "Gain some distance on your opponent using " + ChatColor.GRAY + "[SNEAK] + [DROP] + [Holding Bow]"));

        // ID 11
        abilityGuiComponents.put(11, List.of("Ability 11", "ARROW", ChatColor.DARK_GRAY + "Shoot a weak arrow and ride it using " + ChatColor.GRAY + "[SNEAK] + [LEFT-CLICK] + [Holding Bow]"));
    }

    public void initializeRequirements(){

        // Format: abilityRequirements.put(abilityID, new int[]{combat, farming, crafting, fishing, smithing, mining, agility, foraging});
        abilityRequirements.put(0, new Reqs.RequirementBuilder().combat(1).build());
        abilityRequirements.put(1, new Reqs.RequirementBuilder().combat(1).build());
        abilityRequirements.put(2, new Reqs.RequirementBuilder().combat(5).agility(3).build());
        abilityRequirements.put(3, new Reqs.RequirementBuilder().combat(5).agility(8).build());
        abilityRequirements.put(4, new Reqs.RequirementBuilder().combat(2000).build());
        abilityRequirements.put(5, new Reqs.RequirementBuilder().combat(20).farming(10).fishing(15).agility(10).foraging(20).build());
        abilityRequirements.put(6, new Reqs.RequirementBuilder().combat(35).smithing(5).mining(5).agility(15).build());
        abilityRequirements.put(7, new int[]{5,  0,  0, 5,  2, 0, 10, 0});
        abilityRequirements.put(8, new int[]{0,  25, 0, 5,  0, 0, 10, 0});
        abilityRequirements.put(9, new int[]{5,  0, 0, 0,  0, 5, 25, 10});
        abilityRequirements.put(10, new int[]{10,  1, 0, 0,  0, 2, 10, 2});
        abilityRequirements.put(11, new int[]{10,  1, 0, 0,  0, 2, 10, 2});
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

    public void handleAbility(Player player, int abilityID){

        PersistentDataContainer container = player.getPersistentDataContainer();

        long[] cooldowns = Objects.requireNonNullElse(container.get(abilityCooldown, PersistentDataType.LONG_ARRAY), new long[3]);

        if (abilityID == 0){
            return;

        } else if (abilityID == 1){

            DoubleJump.doubleJump(player);

            cooldowns[getAbilitySlot(player, 1)] = System.currentTimeMillis()/1000L + 10;

        } else if (abilityID == 2){

            Dash dash = new Dash();
            dash.dash(player);

            cooldowns[getAbilitySlot(player, 2)] = System.currentTimeMillis()/1000L + 10;

        } else if (abilityID == 3){

            Leap leap = new Leap();
            leap.leap(player);

            cooldowns[getAbilitySlot(player, 3)] = System.currentTimeMillis()/1000L + 10;

        } else if (abilityID == 4){

            // not implemented
            return;

        } else if (abilityID == 5){

            Scream Scream = new Scream();
            Scream.scream(player);

            cooldowns[getAbilitySlot(player, 5)] = System.currentTimeMillis()/1000L + 15;

        } else if (abilityID == 6){

            ShadowStep ShadowStep = new ShadowStep();
            ShadowStep.shadowStep(player);

            cooldowns[getAbilitySlot(player, 6)] = System.currentTimeMillis()/1000L + 20;

        } else if (abilityID == 7) {
            SlimeBoots SlimeBoots = new SlimeBoots();
            SlimeBoots.bounce(player);

        } else if (abilityID == 8){
            FeatherDash featherDash = new FeatherDash();
            featherDash.featherDash(player);
            cooldowns[getAbilitySlot(player, 8)] = System.currentTimeMillis()/1000L + 15;
        } else if (abilityID == 9){
            SpeedStep SpeedStep = new SpeedStep();
            SpeedStep.SpeedStep(player);
            cooldowns[getAbilitySlot(player, 9)] = System.currentTimeMillis()/1000L + 20;
        } else if (abilityID == 10){
            TacticalRetreat TacticalRetreat = new TacticalRetreat();
            TacticalRetreat.retreat(player);
            cooldowns[getAbilitySlot(player, 10)] = System.currentTimeMillis()/1000L + 10;
        } else if (abilityID == 11) {
            RideArrow RideArrow = new RideArrow();
            RideArrow.shootRiddenArrow(player);
            cooldowns[getAbilitySlot(player, 11)] = System.currentTimeMillis()/1000L + 20;
        }



        container.set(abilityCooldown, PersistentDataType.LONG_ARRAY, cooldowns);

        CooldownDisplay cd = new CooldownDisplay();
        cd.UpdateScoreboard(player);


    }

    public void setAllCooldowns(Player player) {
        PersistentDataContainer container = player.getPersistentDataContainer();
        int[] active = container.getOrDefault(abilitiesActive, PersistentDataType.INTEGER_ARRAY, new int[3]);
        long[] cooldowns = new long[active.length];
        long now = System.currentTimeMillis() / 1000L;

        for (int i = 0; i < active.length; i++) {
            int abilityId = active[i];

            if (abilityId >= 0 && abilityId < Ability.values().length) {
                Ability ability = Ability.values()[abilityId];
                cooldowns[i] = ability.hasCooldown() ? now + ability.getCooldownSeconds() : 0;
            } else {
                cooldowns[i] = 0;
            }
        }

        container.set(abilityCooldown, PersistentDataType.LONG_ARRAY, cooldowns);
        new CooldownDisplay().UpdateScoreboard(player);
    }


}