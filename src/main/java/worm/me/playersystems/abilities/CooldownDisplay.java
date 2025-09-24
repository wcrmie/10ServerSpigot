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
    public static CooldownDisplay getInstance() {
        return INSTANCE;
    }

    private final Map<Player, BukkitRunnable> activeTasks = new ConcurrentHashMap<>();

    private CooldownDisplay() {}

    public void updateScoreboard(Player player) {
        PersistentDataContainer container = player.getPersistentDataContainer();

        int[] abilityIDs = Objects.requireNonNullElse(
                container.get(Abilities.abilitiesActive, PersistentDataType.INTEGER_ARRAY),
                new int[]{0, 0, 0}
        );

        long[] cooldowns = Objects.requireNonNullElse(
                container.get(abilityCooldown, PersistentDataType.LONG_ARRAY),
                new long[]{0, 0, 0}
        );

        if (activeTasks.containsKey(player)) {
            activeTasks.get(player).cancel();
        }

        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();

        var objective = board.registerNewObjective("abilities", Criteria.DUMMY, Component.text("Abilities"));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                board.getEntries().forEach(board::resetScores);

                long now = System.currentTimeMillis();
                boolean anyOnCooldown = false;

                objective.getScore(ChatColor.RESET.toString()).setScore(5);

                for (int slot = 0; slot < 3; slot++) {
                    int abilityId = slot < abilityIDs.length ? abilityIDs[slot] : 0;
                    long cooldown = slot < cooldowns.length ? cooldowns[slot] : 0;

                    String displayName;
                    if (abilityId == 0) {
                        displayName = ChatColor.RED + "Ability slot " + (slot + 1) + " empty.";
                    } else {
                        String abilityName = Abilities.abilityGuiComponents
                                .getOrDefault(abilityId, List.of("Unknown")).get(0);

                        if (cooldown > now) {
                            long timeLeft = (cooldown - now + 999) / 1000;
                            displayName = ChatColor.GRAY + abilityName + " Cooldown: " + ChatColor.RED + timeLeft + "s";
                            anyOnCooldown = true;
                        } else {
                            displayName = ChatColor.GRAY + abilityName + " Cooldown: " + ChatColor.GREEN + "✔";
                        }
                    }

                    objective.getScore(displayName).setScore(4 - slot);
                }

                objective.getScore(ChatColor.RESET.toString() + ChatColor.WHITE).setScore(1);

                player.setScoreboard(board);

                if (!anyOnCooldown) {
                    this.cancel();
                    activeTasks.remove(player);
                }
            }
        };

        task.runTaskTimer(Server.getPlugin(), 1L, 20L);
        activeTasks.put(player, task);
    }


    public void refresh(Player player) {
        updateScoreboard(player);
    }
}
