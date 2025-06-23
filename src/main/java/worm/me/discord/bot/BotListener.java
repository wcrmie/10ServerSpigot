package worm.me.discord.bot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.Objects;

public class BotListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(net.dv8tion.jda.api.events.message.MessageReceivedEvent event) {

        if (event.getAuthor().isBot()) return; // Ignore messages from bots

        Message message = event.getMessage();

        if (!message.getChannel().getId().equals("1381733550277267608")) return; // Check if the message is in the specific channel

        String content = message.getContentRaw();
        String author = message.getAuthor().getName();

        BotInteractions botInteractions = new BotInteractions();

        botInteractions.memberMessage(author, content);

    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("list")) {

            event.reply("List sent!").queue();

            BotInteractions botInteractions = new BotInteractions();
            botInteractions.listCommand();
        }

        if (event.getName().equals("link")) {

            event.deferReply().queue();

            DiscordLink discordLink = new DiscordLink();

            if (discordLink.linkPlayer(event.getInteraction().getOption("code").getAsInt(), event.getUser().getId())) {

                System.out.println(event.getUser().getId());

                EmbedBuilder embed = new EmbedBuilder();

                String playerName = DiscordLink.linking.get(event.getInteraction().getOption("code").getAsInt()).getName();

                embed.setImage("https://minotar.net/avatar/" + playerName + ".png");
                embed.setTitle("Link Successful!");
                embed.setFooter("You may now access the discord server!");
                embed.setColor(Color.green);
                embed.setDescription("You have successfully linked your Discord account to " + playerName + ".");

                event.getHook().sendMessageEmbeds(embed.build()).queue();

            } else {

                EmbedBuilder embed = new EmbedBuilder();

                embed.setTitle("Link Failed!");
                embed.setColor(Color.red);
                embed.setDescription("The code you provided is invalid or has already been used.");
                embed.setImage("https://cdn.discordapp.com/attachments/862798464046727209/1384728089531256832/guy_falling.gif?ex=68537c07&is=68522a87&hm=4645e0585e202470fabca2314ab8a2974ce1ed252035555bcf89c8bd0b97b734&");

                event.getHook().sendMessageEmbeds(embed.build()).queue();
            }

        }
    }

}
