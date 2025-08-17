package worm.me.discord.bot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import worm.me.Server;
import worm.me.utils.chat.Message;
import worm.me.utils.chat.MessageType;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class DiscordLink {

    public static Map<Integer, Player> linking = new HashMap<>();

    public int generateRandomCode() {

        return ThreadLocalRandom.current().nextInt(100000, 999999);

    }

    public void beginLinking(Player player) {

        int code = generateRandomCode();
        linking.put(code, player);

        player.sendMessage(Message.create(MessageType.SUCCESS, "Your linking code is: " + code + ". This may be used with /link in the Discord."));

        new BukkitRunnable() {
            @Override
            public void run() {
                if (linking.containsKey(code)) {
                    linking.remove(code);
                    player.sendMessage(Message.create(MessageType.FAIL, "Your linking code has expired. Please generate a new one."));
                }
            }
        }.runTaskLater(Server.getPlugin(), 12000L);

    }

    public boolean linkPlayer(int code, String discordId) {

        Player player = linking.get(code);
        if (player != null) {
            player.getPersistentDataContainer().set(new NamespacedKey(Server.getPlugin(), "linked"), PersistentDataType.STRING, discordId);

            linkDiscordAccount(player, discordId);

            BotInitialize.guild.retrieveMemberById(discordId).queue(member -> {

                player.sendMessage(Message.create(MessageType.SUCCESS, "You have successfully linked your Discord account with" + member.getUser().getName()));
                linking.remove(code);

            });

            return true;

        }
        return false;
    }

    public void linkDiscordAccount(Player player, String discordId) {

        Guild guild = BotInitialize.guild;

        guild.retrieveMemberById(discordId).queue(member -> {

            String playerName = player.getName();

            Role verified = guild.getRoleById("1385150010731008071");

            guild.modifyNickname(member, playerName).queue();
            guild.addRoleToMember(UserSnowflake.fromId(discordId), verified).queue();}

        );



    }

}
