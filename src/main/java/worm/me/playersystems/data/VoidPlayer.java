package worm.me.playersystems.data;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import worm.me.playersystems.abilities.Abilities;
import worm.me.playersystems.abilities.CooldownDisplay;
import worm.me.playersystems.social.MessageHead;
import worm.me.gamesystems.items.gear.tools.Tools;
import worm.me.playersystems.quests.QuestAssign;
import worm.me.Server;
import worm.me.utils.chat.Message;
import worm.me.utils.chat.MessageType;

public class VoidPlayer {

    public static void initializeData(Player player){

        PersistentDataContainer container = player.getPersistentDataContainer();
        Server plugin = Server.getPlugin();

        if (!container.has(new NamespacedKey(plugin, "vault"), PersistentDataType.STRING)) {
            container.set(new NamespacedKey(plugin, "vault"), PersistentDataType.STRING, "");
        }

        if (!container.has(new NamespacedKey(plugin, "tools"), PersistentDataType.STRING)) {
            container.set(new NamespacedKey(plugin, "tools"), PersistentDataType.STRING, "");
            Tools.toolsGrantInitial(player);
        }

        if (!container.has(new NamespacedKey(plugin, "unlockedgens"), PersistentDataType.STRING)) {
            container.set(new NamespacedKey(plugin, "unlockedgens"), PersistentDataType.STRING, "");
        }

        if (!container.has(new NamespacedKey(plugin, "levels"), PersistentDataType.INTEGER_ARRAY)) {
            container.set(new NamespacedKey(plugin, "levels"), PersistentDataType.INTEGER_ARRAY, new int[8]);
        }

        if (!container.has(new NamespacedKey(plugin, "exp"), PersistentDataType.LONG_ARRAY)) {
            container.set(new NamespacedKey(plugin, "exp"), PersistentDataType.LONG_ARRAY, new long[8]);
        }

        if (!container.has(new NamespacedKey(plugin, "unlockedabiliies"), PersistentDataType.INTEGER_ARRAY)) {

            int[] unlockedAbilities = new int[100];

            // unlock double jump
            unlockedAbilities[1] = 1;


            container.set(new NamespacedKey(plugin, "unlockedabilities"), PersistentDataType.INTEGER_ARRAY, unlockedAbilities);
        }

        if (!container.has(new NamespacedKey(plugin, "activeabilities"), PersistentDataType.INTEGER_ARRAY)) {
            container.set(new NamespacedKey(plugin, "activeabilities"), PersistentDataType.INTEGER_ARRAY, new int[]{1,0,0});
        }

        if (!container.has(Abilities.abilityCooldown, PersistentDataType.LONG_ARRAY)) {
            container.set(Abilities.abilityCooldown, PersistentDataType.LONG_ARRAY, new long[3]);
        }

        if (!container.has(new NamespacedKey(plugin, "movecooldown"), PersistentDataType.LONG)) {
            container.set(new NamespacedKey(plugin, "movecooldown"), PersistentDataType.LONG, System.currentTimeMillis()/1000L);
        }

        if (!container.has(new NamespacedKey(plugin, "10stats"), PersistentDataType.INTEGER_ARRAY)) {
            container.set(new NamespacedKey(plugin, "10stats"), PersistentDataType.INTEGER_ARRAY, new int[50]);
        }

        if (!container.has(new NamespacedKey(plugin, "repsess"), PersistentDataType.LONG_ARRAY)) {
            container.set(new NamespacedKey(plugin, "repsess"), PersistentDataType.LONG_ARRAY, new long[]{System.currentTimeMillis()/1000L, 0});
        } else if (container.get(new NamespacedKey(plugin, "repsess"), PersistentDataType.LONG_ARRAY)[0] <= ((System.currentTimeMillis()/1000L) - 3600)) {
            container.set(new NamespacedKey(plugin, "repsess"), PersistentDataType.LONG_ARRAY, new long[]{System.currentTimeMillis()/1000L, 0});
            player.sendMessage(Message.create(MessageType.GENERIC, "Your reputation session has reset!"));
        }

        if (!container.has(new NamespacedKey(Server.getPlugin(), "10stats"), PersistentDataType.INTEGER_ARRAY)) {
            container.set(new NamespacedKey(Server.getPlugin(), "10stats"), PersistentDataType.INTEGER_ARRAY, new int[50]);
        }

        if (!container.has(new NamespacedKey(Server.getPlugin(), "dailyprogress"), PersistentDataType.INTEGER_ARRAY)) {
            container.set(new NamespacedKey(Server.getPlugin(), "dailyprogress"), PersistentDataType.INTEGER_ARRAY, new int[3]);
        }

        if (!container.has(new NamespacedKey(Server.getPlugin(), "questclaimed"), PersistentDataType.INTEGER_ARRAY)) {
            container.set(new NamespacedKey(Server.getPlugin(), "questclaimed"), PersistentDataType.INTEGER_ARRAY, new int[3]);
        }

        MessageHead messageHead = new MessageHead(plugin);
        player.getPersistentDataContainer().set(messageHead.armorStandExists, PersistentDataType.BOOLEAN, false);

        // Hide the player's nametag
        Scoreboard score = Bukkit.getScoreboardManager().getMainScoreboard();
        Team nHide = score.getTeam("hiddenName");
        if (nHide == null) {
            nHide = score.registerNewTeam("hiddenName");
            nHide.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        }
        nHide.addEntry(player.getName());

        // Assign quests
        QuestAssign questAssign = new QuestAssign();
        questAssign.dailyCheck(player);

        CooldownDisplay cooldownDisplay = new CooldownDisplay();
        cooldownDisplay.UpdateScoreboard(player);

    }

    public static void registerStats(Player p, int statID) {
        PersistentDataContainer pdc = p.getPersistentDataContainer();
        int[] stats = pdc.get(new NamespacedKey(Server.getPlugin(), "10stats"), PersistentDataType.INTEGER_ARRAY);
        stats[statID]++;
        pdc.set(new NamespacedKey(Server.getPlugin(), "10stats"), PersistentDataType.INTEGER_ARRAY, stats);
    }

    public static int getStat(Player p, int statID) {
        PersistentDataContainer pdc = p.getPersistentDataContainer();
        int[] stats = pdc.get(new NamespacedKey(Server.getPlugin(), "10stats"), PersistentDataType.INTEGER_ARRAY);
        return stats[statID];
    }

}
