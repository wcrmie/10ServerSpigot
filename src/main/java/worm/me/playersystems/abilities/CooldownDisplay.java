package worm.me.playersystems.abilities;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import worm.me.Server;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static worm.me.playersystems.abilities.Abilities.abilityCooldown;

public class CooldownDisplay {

    private static final CooldownDisplay INSTANCE = new CooldownDisplay();
    private static final long COOLDOWN_CHECK_INTERVAL = 20L; // ticks

    public static CooldownDisplay getInstance() {
        return INSTANCE;
    }

    private final Map<Player, BukkitRunnable> activeTasks = new ConcurrentHashMap<>();

    private CooldownDisplay() {}

    public void updateScoreboard(Player player) {
        try {
            PersistentDataContainer container = player.getPersistentDataContainer();

            int[] abilityIDs = Objects.requireNonNullElse(
                    container.get(Abilities.abilitiesActive, PersistentDataType.INTEGER_ARRAY),
                    Ability.DEFAULT_ACTIVE_ABILITIES
            );

            long[] cooldowns = Objects.requireNonNullElse(
                    container.get(abilityCooldown, PersistentDataType.LONG_ARRAY),
                    Ability.DEFAULT_COOLDOWNS
            );

            // Clean up existing task
            cleanupPlayer(player);

            ScoreboardManager manager = Bukkit.getScoreboardManager();
            Scoreboard board = manager.getNewScoreboard();

            var objective = board.registerNewObjective("abilities", Criteria.DUMMY, Component.text("Abilities"));
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);

            BukkitRunnable task = new BukkitRunnable() {
                @Override
                public void run() {
                    try {
                        updateObjective(objective, abilityIDs, cooldowns, player);

                        if (!player.isOnline()) {
                            this.cancel();
                            activeTasks.remove(player);
                            return;
                        }

                        player.setScoreboard(board);

                    } catch (Exception e) {
                        Server.getPlugin().getLogger().warning("Error updating cooldown display for " + player.getName() + ": " + e.getMessage());
                        this.cancel();
                        activeTasks.remove(player);
                    }
                }
            };

            task.runTaskTimer(Server.getPlugin(), 1L, COOLDOWN_CHECK_INTERVAL);
            activeTasks.put(player, task);
        } catch (Exception e) {
            Server.getPlugin().getLogger().warning("Error creating cooldown display for " + player.getName() + ": " + e.getMessage());
        }
    }

    private void updateObjective(org.bukkit.scoreboard.Objective objective, int[] abilityIDs, long[] cooldowns, Player player) {
        objective.getScoreboard().getEntries().forEach(objective.getScoreboard()::resetScores);

        long now = System.currentTimeMillis();
        boolean anyOnCooldown = false;

        objective.getScore(ChatColor.RESET.toString()).setScore(5);

        for (int slot = 0; slot < Ability.SLOT_COUNT; slot++) {
            int abilityId = slot < abilityIDs.length ? abilityIDs[slot] : 0;
            long cooldown = slot < cooldowns.length ? cooldowns[slot] : 0;

            String displayName = getAbilityDisplayName(abilityId, cooldown, now, slot);
            objective.getScore(displayName).setScore(4 - slot);

            if (cooldown > now) {
                anyOnCooldown = true;
            }
        }

        objective.getScore(ChatColor.RESET.toString() + ChatColor.WHITE).setScore(1);

        boolean hasAbilitiesEquipped = false;
        for (int abilityId : abilityIDs) {
            if (abilityId != 0) {
                hasAbilitiesEquipped = true;
                break;
            }
        }
        if (!hasAbilitiesEquipped && !anyOnCooldown) {

        }
    }


    private boolean shouldKeepScoreboardVisible(Player player) {
        PersistentDataContainer container = player.getPersistentDataContainer();
        int[] abilityIDs = Objects.requireNonNullElse(
                container.get(Abilities.abilitiesActive, PersistentDataType.INTEGER_ARRAY),
                Ability.DEFAULT_ACTIVE_ABILITIES
        );

        for (int abilityId : abilityIDs) {
            if (abilityId != 0) {
                return true;
            }
        }
        return false;
    }


    private String getAbilityDisplayName(int abilityId, long cooldown, long now, int slot) {
        if (abilityId == 0) {
            return ChatColor.RED + "Ability slot " + (slot + 1) + " empty.";
        }

        String abilityName = Abilities.abilityGuiComponents
                .getOrDefault(abilityId, List.of("Unknown")).get(0);

        if (cooldown > now) {
            long timeLeft = (cooldown - now + 999) / 1000;
            return ChatColor.GRAY + abilityName + " Cooldown: " + ChatColor.RED + timeLeft + "s";
        } else {
            return ChatColor.GRAY + abilityName + " Cooldown: " + ChatColor.GREEN + "✔";
        }
    }

    public void refresh(Player player) {
        cleanupPlayer(player);
        updateScoreboard(player);
    }

    public void cleanupPlayer(Player player) {
        BukkitRunnable task = activeTasks.remove(player);
        if (task != null) {
            task.cancel();
        }
    }

    public void onPlayerQuit(Player player) {
        cleanupPlayer(player);
    }

    public void cleanupAll() {
        for (BukkitRunnable task : activeTasks.values()) {
            if (task != null) {
                task.cancel();
            }
        }
        activeTasks.clear();
    }
}