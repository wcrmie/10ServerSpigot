package worm.me.discord.bot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import worm.me.Server;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import static worm.me.discord.bot.BotInitialize.staffChannel;

public class BotInteractions {

    public void memberMessage(String name, String message) {

        for (Player p : Bukkit.getOnlinePlayers()) {

            PersistentDataContainer container = p.getPersistentDataContainer();
            if (container.getOrDefault(new NamespacedKey(Server.getPlugin(), "discordmsg"), PersistentDataType.BOOLEAN, true)) {

                if (message.contains("§") || message.contains("\n")) {
                    message = message.replace("§", "").replace("\n", " ");
                }

                p.sendMessage(ChatColor.BLUE + "ᴅɪsᴄᴏʀᴅ" + ChatColor.DARK_GRAY + " | " + ChatColor.GRAY + name + ": " + message);

            }

        }

    }

    public void gameMessage(String message) {

        MessageChannel channel = BotInitialize.channel;

        channel.sendMessage(message).queue();

    }

    public void joinMessage(Player p) {

        EmbedBuilder embed = new EmbedBuilder();
        embed.setThumbnail("https://minotar.net/avatar/" + p.getName() + ".png");
        embed.setDescription(p.getName() + " has joined the server.");
        embed.setColor(Color.GREEN);
        BotInitialize.channel.sendMessageEmbeds(embed.build()).queue();

    }

    public void leaveMessage(Player p) {

        EmbedBuilder embed = new EmbedBuilder();
        embed.setThumbnail("https://minotar.net/avatar/" + p.getName() + ".png");
        embed.setDescription(p.getName() + " has left the server.");
        embed.setColor(Color.RED);
        BotInitialize.channel.sendMessageEmbeds(embed.build()).queue();

    }

    public void listCommand() {
        MessageChannel channel = BotInitialize.channel;

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(Bukkit.getOnlinePlayers().size() + " Online Players:");

        StringBuilder players = new StringBuilder();

        for (Player p : Bukkit.getOnlinePlayers()) {
            players.append(p.getName()).append("\n");
        }

        embed.setDescription(players.toString());
        embed.setColor(Color.CYAN);

        channel.sendMessageEmbeds(embed.build()).queue();
    }

    public void announceMapReset() {

        MessageChannel announcementChannel = BotInitialize.announcementChannel;
        announcementChannel.sendMessageEmbeds(embedMapReset().build()).queue();
        announcementChannel.sendMessage("||<@&1405687316613173430>||").queue();

    }

    public EmbedBuilder embedMapReset() {

        MessageChannel announcementChannel = BotInitialize.announcementChannel;

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Map Reset NOW!");
        embed.setImage("https://cdn.discordapp.com/attachments/862798464046727209/1384728089531256832/guy_falling.gif?ex=68537c07&is=68522a87&hm=4645e0585e202470fabca2314ab8a2974ce1ed252035555bcf89c8bd0b97b734&");
        embed.setFooter("This could be you!");
        embed.setColor(Color.ORANGE);
        return embed;
        return null;
    }

    public static void reportMessage(String reporter, String reported) {

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("New Player Report");
        embed.addField("Reporter: ", reporter, true);
        embed.addField("Reported: ", reported, true);
        embed.setThumbnail("https://minotar.net/avatar/" + reported + ".png");
        embed.setColor(Color.RED);

        staffChannel.sendMessageEmbeds(embed.build()).queue();

    }

}
