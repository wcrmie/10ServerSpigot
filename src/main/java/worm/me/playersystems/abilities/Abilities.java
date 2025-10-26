package worm.me.playersystems.abilities;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import worm.me.Server;
import worm.me.playersystems.data.Unlocks;

import java.util.*;

public class Abilities {

    public static final NamespacedKey abilitiesActive = new NamespacedKey(Server.getPlugin(), "activeabilities");
    public static final NamespacedKey abilityCooldown = new NamespacedKey(Server.getPlugin(), "abilitycooldown");

    public static final Map<Integer, List<String>> abilityGuiComponents = new HashMap<>();
    public static final Map<Integer, int[]> abilityRequirements = new HashMap<>();

    public static void abilitiesInit() {
        initializeGuiComponents();
        initializeRequirements(); // Initialize requirements here
    }

    public static void initializeRequirements() {
        // Use Ability enum as single source of truth
        abilityRequirements.clear();
        for (Ability ability : Ability.values()) {
            abilityRequirements.put(ability.getId(), ability.getSkillRequirements());
        }
    }

    private static void initializeGuiComponents() {
        abilityGuiComponents.put(0, List.of("Empty", "RED_STAINED_GLASS_PANE", "Empty Slot"));

        abilityGuiComponents.put(1, List.of("Double Jump", "FEATHER",
                ChatColor.DARK_GRAY + "Rapidly press space twice in the air to perform a double jump!"));

        abilityGuiComponents.put(2, List.of("Dash", "IRON_SWORD",
                ChatColor.DARK_GRAY + "Dash forward " + ChatColor.GRAY + "[SNEAK] + [RIGHT-CLICK] + [Holding Sword]"));

        abilityGuiComponents.put(3, List.of("Leap", "IRON_AXE",
                ChatColor.DARK_GRAY + "Leap up in the air " + ChatColor.GRAY + "[SNEAK] + [RIGHT-CLICK] + [Holding Axe]"));

        abilityGuiComponents.put(4, List.of("Wall Jump", "IRON_BARS",
                ChatColor.DARK_GRAY + "Clutch onto walls using " + ChatColor.GRAY + "[SNEAK]"));

        abilityGuiComponents.put(5, List.of("SCREAM", "GOAT_HORN",
                ChatColor.DARK_GRAY + "Scream as loud as you can using " + ChatColor.GRAY + "[SNEAK] + [LEFT-CLICK] + [Holding Note Block]"));

        abilityGuiComponents.put(6, List.of("Shadow-Step", "TINTED_GLASS",
                ChatColor.DARK_GRAY + "Step through space using " + ChatColor.GRAY + "[SNEAK] + [LEFT-CLICK] + [Holding Tinted Glass] + [Standing on Ground]"));

        abilityGuiComponents.put(8, List.of("Feather-Float", "ELYTRA",
                ChatColor.DARK_GRAY + "Float through the air swiftly using " + ChatColor.GRAY + "[SNEAK] + [LEFT-CLICK] + [Holding Feather]"));

        abilityGuiComponents.put(9, List.of("Speed-Step", "RABBIT_FOOT",
                ChatColor.DARK_GRAY + "Speed off in the direction you are looking using " + ChatColor.GRAY + "[SNEAK] + [LEFT-CLICK] + [Rabbit Foot]"));

        abilityGuiComponents.put(10, List.of("Tactical Retreat", "BOW",
                ChatColor.DARK_GRAY + "Gain some distance on your opponent using " + ChatColor.GRAY + "[SNEAK] + [DROP] + [Holding Bow]"));

        abilityGuiComponents.put(11, List.of("Ability 11", "ARROW",
                ChatColor.DARK_GRAY + "Shoot a weak arrow and ride it using " + ChatColor.GRAY + "[SNEAK] + [LEFT-CLICK] + [Holding Bow]"));
    }

    public static List<Integer> getActiveAbilities(Player p) {
        PersistentDataContainer container = p.getPersistentDataContainer();
        int[] activeArray = Objects.requireNonNullElse(
                container.get(abilitiesActive, PersistentDataType.INTEGER_ARRAY),
                Ability.DEFAULT_ACTIVE_ABILITIES
        );

        List<Integer> active = new ArrayList<>();
        for (int abilityId : activeArray) {
            active.add(abilityId);
        }
        return active;
    }

    public void triggerSlot1(Player player) {
        triggerSlot(player, 0);
    }

    public void triggerSlot2(Player player) {
        triggerSlot(player, 1);
    }

    public void triggerSlot3(Player player) {
        triggerSlot(player, 2);
    }

    private void triggerSlot(Player player, int slot) {
        if (slot < 0 || slot >= Ability.SLOT_COUNT) {
            return;
        }

        try {
            PersistentDataContainer container = player.getPersistentDataContainer();
            int[] active = container.getOrDefault(abilitiesActive, PersistentDataType.INTEGER_ARRAY,
                    Ability.DEFAULT_ACTIVE_ABILITIES);
            long[] cooldowns = container.getOrDefault(abilityCooldown, PersistentDataType.LONG_ARRAY,
                    Ability.DEFAULT_COOLDOWNS);

            int abilityId = active[slot];
            if (abilityId == 0) {
                player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "X" + ChatColor.DARK_GRAY + "] " +
                        ChatColor.RED + "No ability in this slot.");
                return;
            }

            long now = System.currentTimeMillis();
            if (slot < cooldowns.length && cooldowns[slot] > now) {
                long secondsLeft = (cooldowns[slot] - now) / 1000;
                Ability ability = Ability.getById(abilityId);
                String abilityName = ability != null ? ability.getDisplayName() : "Unknown";

                player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "X" + ChatColor.DARK_GRAY + "] " +
                        ChatColor.RED + "Your " + abilityName + " is on cooldown for " + secondsLeft + "s.");
                return;
            }

            handleAbility(player, abilityId);
        } catch (Exception e) {
            player.sendMessage(ChatColor.RED + "Error triggering ability slot!");
            Server.getPlugin().getLogger().warning("Error triggering ability slot " + slot + " for " + player.getName() + ": " + e.getMessage());
        }
    }

    public static long getAbilityCooldownRemaining(Player p, int abilitySlot) {
        long now = System.currentTimeMillis();
        long[] cooldowns = Objects.requireNonNullElse(
                p.getPersistentDataContainer().get(abilityCooldown, PersistentDataType.LONG_ARRAY),
                Ability.DEFAULT_COOLDOWNS
        );

        return Math.max(0, cooldowns[abilitySlot] - now);
    }

    public static boolean isOnCooldown(Player p, int abilitySlot) {
        return getAbilityCooldownRemaining(p, abilitySlot) > 0;
    }

    public boolean hasAbilityOnCooldown(Player p) {
        long now = System.currentTimeMillis();
        long[] cooldowns = Objects.requireNonNullElse(
                p.getPersistentDataContainer().get(abilityCooldown, PersistentDataType.LONG_ARRAY),
                Ability.DEFAULT_COOLDOWNS
        );

        for (long cd : cooldowns) {
            if (cd > now) {
                return true;
            }
        }
        return false;
    }

    public void handleAbility(Player player, int abilityID) {
        try {
            Ability ability = Ability.getById(abilityID);
            if (ability == null || abilityID == 0) {
                player.sendMessage(ChatColor.RED + "Invalid ability!");
                return;
            }

            ability.execute(player);
            setCooldownForAbility(player, abilityID);

        } catch (Exception e) {
            player.sendMessage(ChatColor.RED + "Error using ability!");
            Server.getPlugin().getLogger().severe("Error executing ability " + abilityID + " for " + player.getName() + ": " + e.getMessage());
        }
    }

    public void setCooldownForAbility(Player player, int abilityID) {
        try {
            PersistentDataContainer container = player.getPersistentDataContainer();
            int[] active = container.getOrDefault(abilitiesActive, PersistentDataType.INTEGER_ARRAY,
                    Ability.DEFAULT_ACTIVE_ABILITIES);
            long[] cooldowns = container.getOrDefault(abilityCooldown, PersistentDataType.LONG_ARRAY,
                    Ability.DEFAULT_COOLDOWNS);
            long now = System.currentTimeMillis();

            for (int i = 0; i < active.length; i++) {
                if (active[i] == abilityID) {
                    Ability ability = Ability.getById(active[i]);
                    if (ability != null && ability.hasCooldown()) {
                        cooldowns[i] = now + (ability.getCooldownSeconds() * 1000L);
                    } else {
                        cooldowns[i] = 0;
                    }
                    break;
                }
            }

            container.set(abilityCooldown, PersistentDataType.LONG_ARRAY, cooldowns);
            CooldownDisplay.getInstance().refresh(player);
        } catch (Exception e) {
            Server.getPlugin().getLogger().warning("Error setting cooldown for ability " + abilityID + ": " + e.getMessage());
        }
    }

    public void setAllCooldowns(Player player) {
        try {
            PersistentDataContainer container = player.getPersistentDataContainer();
            int[] active = container.getOrDefault(abilitiesActive, PersistentDataType.INTEGER_ARRAY,
                    Ability.DEFAULT_ACTIVE_ABILITIES);
            long[] cooldowns = new long[active.length];
            long now = System.currentTimeMillis();

            for (int i = 0; i < active.length; i++) {
                Ability ability = Ability.getById(active[i]);
                if (ability != null && ability.hasCooldown()) {
                    cooldowns[i] = now + (ability.getCooldownSeconds() * 1000L);
                } else {
                    cooldowns[i] = 0;
                }
            }

            container.set(abilityCooldown, PersistentDataType.LONG_ARRAY, cooldowns);
            CooldownDisplay.getInstance().refresh(player);
        } catch (Exception e) {
            Server.getPlugin().getLogger().warning("Error setting all cooldowns for " + player.getName() + ": " + e.getMessage());
        }
    }

    public static void validateAllAbilitySlots(Player player) {
        for (int i = 0; i < Ability.SLOT_COUNT; i++) {
            validateAbilitySlot(player, i, Ability.getRequiredTypeForSlot(i));
        }
    }

    public static void validateAbilitySlot(Player player, int slot, AbilityType requiredType) {
        if (slot < 0 || slot >= Ability.SLOT_COUNT) return;

        try {
            PersistentDataContainer container = player.getPersistentDataContainer();
            int[] active = container.getOrDefault(Abilities.abilitiesActive, PersistentDataType.INTEGER_ARRAY,
                    Ability.DEFAULT_ACTIVE_ABILITIES);

            int abilityId = active[slot];
            Ability ability = Ability.getById(abilityId);

            if (ability == null || ability == Ability.EMPTY || ability.getType() != requiredType) {
                active[slot] = Ability.EMPTY.getId();
                container.set(Abilities.abilitiesActive, PersistentDataType.INTEGER_ARRAY, active);
            }
        } catch (Exception e) {
            Server.getPlugin().getLogger().warning("Error validating ability slot " + slot + " for " + player.getName() + ": " + e.getMessage());
        }
    }

    public static boolean validateAbilityForSlot(Player player, int abilityId, int slot) {
        Ability ability = Ability.getById(abilityId);
        if (ability == null) return false;

        AbilityType requiredType = Ability.getRequiredTypeForSlot(slot);
        if (ability.getType() != requiredType) return false;

        Abilities abilities = new Abilities();
        if (abilities.getActiveAbilities(player).contains(abilityId) && abilityId != 0) {
            return false; // Already equipped elsewhere
        }

        return Unlocks.hasAbilityUnlocked(player, abilityId) || player.isOp();
    }
}