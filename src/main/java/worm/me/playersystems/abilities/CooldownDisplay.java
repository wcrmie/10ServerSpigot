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

import java.util.Map;
import java.util.Objects;

import static worm.me.playersystems.abilities.Abilities.abilityCooldown;

public class CooldownDisplay {

    public static Map<Player, Boolean> cooldownDisplay = new java.util.HashMap<>();

    public void UpdateScoreboard(Player p) {

        if (cooldownDisplay.get(p) != null && cooldownDisplay.get(p)) {
            return;
        }

        PersistentDataContainer container = p.getPersistentDataContainer();

        long[] cooldowns = Objects.requireNonNullElse(container.get(abilityCooldown, PersistentDataType.LONG_ARRAY), new long[3]);

        int[] abilityIDs = Objects.requireNonNullElse(container.get(Abilities.abilitiesActive, PersistentDataType.INTEGER_ARRAY), new int[3]);

        Abilities abilities = new Abilities();

        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        Scoreboard board = scoreboardManager.getNewScoreboard();
        board.registerNewObjective("yep", Criteria.DUMMY, Component.text("Abilities"));
        board.getObjective("yep").setDisplaySlot(DisplaySlot.SIDEBAR);

        new BukkitRunnable() {
            @Override
            public void run() {

                board.getObjective("yep").unregister();

                board.registerNewObjective("yep", Criteria.DUMMY, Component.text("Abilities"));
                board.getObjective("yep").setDisplaySlot(DisplaySlot.SIDEBAR);
                board.getObjective("yep").setDisplayName("Abilities");
                board.getObjective("yep").getScore(" ").setScore(5);
                board.getObjective("yep").getScore("  ").setScore(1);
                p.setScoreboard(board);

                cooldownDisplay.put(p, true);

                if (abilities.hasAbilityOnCooldown(p)) {
                    // Update the scoreboard with the cooldowns

                    if (abilityIDs[0] != 0) {
                        if (cooldowns[0] > System.currentTimeMillis() / 1000L) {
                            long timeLeft = cooldowns[0] - (System.currentTimeMillis() / 1000L);
                            board.getObjective("yep").getScore(ChatColor.GRAY + Abilities.abilityGuiComponents.get(abilityIDs[0]).getFirst() + " Cooldown: " + ChatColor.RED + timeLeft).setScore(4);
                        } else {
                            board.getObjective("yep").getScore(ChatColor.GRAY + Abilities.abilityGuiComponents.get(abilityIDs[0]).getFirst() + " Cooldown: " + ChatColor.GREEN + "✔").setScore(4);
                        }
                    } else {
                        board.getObjective("yep").getScore(ChatColor.RED + "Ability slot 1 empty.").setScore(4);
                    }

                    if (abilityIDs[1] != 0) {
                        if (cooldowns[1] > System.currentTimeMillis() / 1000L) {
                            long timeLeft = cooldowns[1] - (System.currentTimeMillis() / 1000L);
                            board.getObjective("yep").getScore(ChatColor.GRAY + Abilities.abilityGuiComponents.get(abilityIDs[1]).getFirst() + " Cooldown: " + ChatColor.RED + timeLeft).setScore(3);
                        } else {
                            board.getObjective("yep").getScore(ChatColor.GRAY + Abilities.abilityGuiComponents.get(abilityIDs[1]).getFirst() + " Cooldown: " + ChatColor.GREEN + "✔").setScore(3);
                        }
                    } else {
                        board.getObjective("yep").getScore(ChatColor.RED + "Ability slot 2 empty.").setScore(3);
                    }

                    if (abilityIDs[2] != 0) {
                        if (cooldowns[2] > System.currentTimeMillis() / 1000L) {
                            long timeLeft = cooldowns[2] - (System.currentTimeMillis() / 1000L);
                            board.getObjective("yep").getScore(ChatColor.GRAY + Abilities.abilityGuiComponents.get(abilityIDs[2]).getFirst() + " Cooldown: " + ChatColor.RED + timeLeft).setScore(2);
                        } else {
                            board.getObjective("yep").getScore(ChatColor.GRAY + Abilities.abilityGuiComponents.get(abilityIDs[2]).getFirst() + " Cooldown: " + ChatColor.GREEN + "✔").setScore(2);
                        }
                    } else {
                        board.getObjective("yep").getScore(ChatColor.RED + "Ability slot 3 empty.").setScore(2);
                    }

                } else {

                    if (abilityIDs[0] != 0) {
                        board.getObjective("yep").getScore(ChatColor.GRAY + Abilities.abilityGuiComponents.get(abilityIDs[0]).getFirst() + " Cooldown: " + ChatColor.GREEN + "✔").setScore(4);
                    } else {
                        board.getObjective("yep").getScore(ChatColor.RED + "Ability slot 1 empty.").setScore(4);
                    }

                    if (abilityIDs[1] != 0) {
                        board.getObjective("yep").getScore(ChatColor.GRAY + Abilities.abilityGuiComponents.get(abilityIDs[1]).getFirst() + " Cooldown: " + ChatColor.GREEN + "✔").setScore(3);
                    } else {
                        board.getObjective("yep").getScore(ChatColor.RED + "Ability slot 2 empty.").setScore(3);
                    }

                    if (abilityIDs[2] != 0) {
                        board.getObjective("yep").getScore(ChatColor.GRAY + Abilities.abilityGuiComponents.get(abilityIDs[2]).getFirst() + " Cooldown: " + ChatColor.GREEN + "✔").setScore(2);

                    } else {
                        board.getObjective("yep").getScore(ChatColor.RED + "Ability slot 3 empty.").setScore(2);
                    }

                    cooldownDisplay.put(p, false);

                    cancel();
                }
            }
        }.runTaskTimer(Server.getPlugin(), 1, 1);
    }


}
