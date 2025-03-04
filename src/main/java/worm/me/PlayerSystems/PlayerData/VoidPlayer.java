package worm.me.PlayerSystems.PlayerData;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import worm.me.PlayerSystems.PlayerData.Social.Hats.Hats;
import worm.me.PlayerSystems.PlayerData.Social.MessageHead;
import worm.me.PlayerSystems.PlayerData.Social.Skin.Skin;
import worm.me.PlayerSystems.PlayerData.Tools.Tools;
import worm.me.PlayerSystems.Quests.QuestAssign;
import worm.me.Server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class VoidPlayer {

    // We will eventually add all getters and setters for VoidPlayers here, in order to clean stuff up

    public static List<ItemStack> getTools(Player p){

        PersistentDataContainer pdc = p.getPersistentDataContainer();

        String encodedData = pdc.get(new NamespacedKey(Server.getPlugin(), "tools"), PersistentDataType.STRING);

        ArrayList<ItemStack> items = new ArrayList<>();

        if (!encodedData.isEmpty()){
            byte[] rawData = Base64.getDecoder().decode(encodedData);

            try {
                ByteArrayInputStream io = new ByteArrayInputStream(rawData);
                BukkitObjectInputStream is = new BukkitObjectInputStream(io);

                for (int i = 0; i < 4; i++) {

                    items.add((ItemStack) is.readObject());

                }

                is.close();

                return items;

            } catch (IOException | ClassNotFoundException e) {
                Bukkit.getLogger().info(e.getMessage());
            }
        }
        return items;
    }

    public static void setTools(List<ItemStack> tools, Player p){

        PersistentDataContainer pdc = p.getPersistentDataContainer();

        try {
            ByteArrayOutputStream io = new ByteArrayOutputStream();
            BukkitObjectOutputStream os = new BukkitObjectOutputStream(io);

            for (ItemStack item : tools) {
                os.writeObject(item);
            }

            os.flush();

            byte[] rawData = io.toByteArray();

            String encodedData = Base64.getEncoder().encodeToString(rawData);

            pdc.set(new NamespacedKey(Server.getPlugin(), "tools"), PersistentDataType.STRING, encodedData);

            os.close();

        } catch (IOException e) {
            Bukkit.getLogger().info(e.getMessage());
        }

    }

    public static List<ItemStack> getUnlockedGens(Player p){

            PersistentDataContainer pdc = p.getPersistentDataContainer();

            String encodedData = pdc.get(new NamespacedKey(Server.getPlugin(), "unlockedgens"), PersistentDataType.STRING);

            ArrayList<ItemStack> items = new ArrayList<>();

            if (!encodedData.isEmpty()){
                byte[] rawData = Base64.getDecoder().decode(encodedData);

                try {
                    ByteArrayInputStream io = new ByteArrayInputStream(rawData);
                    BukkitObjectInputStream is = new BukkitObjectInputStream(io);

                    int size = is.readInt();

                    for (int i = 0; i < size; i++) {

                        items.add((ItemStack) is.readObject());

                    }

                    is.close();

                    return items;

                } catch (IOException | ClassNotFoundException e) {
                    Bukkit.getLogger().info(e.getMessage());
                }
            }
            return items;
    }

    public static void setUnlockedGens(Player p, List<ItemStack> gens){

            PersistentDataContainer pdc = p.getPersistentDataContainer();

            try {
                ByteArrayOutputStream io = new ByteArrayOutputStream();
                BukkitObjectOutputStream os = new BukkitObjectOutputStream(io);

                os.write(gens.size());

                for (ItemStack item : gens) {
                    os.writeObject(item);
                }

                os.flush();

                byte[] rawData = io.toByteArray();

                String encodedData = Base64.getEncoder().encodeToString(rawData);

                pdc.set(new NamespacedKey(Server.getPlugin(), "unlockedgens"), PersistentDataType.STRING, encodedData);

                os.close();

            } catch (IOException e) {
                Bukkit.getLogger().info(e.getMessage());
            }
    }

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
            container.set(new NamespacedKey(plugin, "unlockedabilities"), PersistentDataType.INTEGER_ARRAY, new int[100]);
        }

        if (!container.has(new NamespacedKey(plugin, "activeabilities"), PersistentDataType.INTEGER_ARRAY)) {
            container.set(new NamespacedKey(plugin, "activeabilities"), PersistentDataType.INTEGER_ARRAY, new int[3]);
        }

        MessageHead messageHead = new MessageHead(plugin);
        player.getPersistentDataContainer().set(messageHead.armorStandExists, PersistentDataType.BOOLEAN, false);

        // Change player's skin
        Skin skin = new Skin(plugin);
        skin.setSkin(player);

        // Hide the player's nametag
        if (!player.isOp()) {
            Scoreboard score = Bukkit.getScoreboardManager().getMainScoreboard();
            Team nHide = score.getTeam("hiddenName");
            if (nHide == null) {
                nHide = score.registerNewTeam("hiddenName");
                nHide.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.FOR_OWN_TEAM);
            }
            nHide.addEntry(player.getName());
        }

        // Assign quests
        QuestAssign questAssign = new QuestAssign(plugin);
        questAssign.dailyCheck(player);

        // Set default hat if nothing is equipped
        Hats hats = new Hats();
        hats.equipDefaultHat(player);

    }

}
