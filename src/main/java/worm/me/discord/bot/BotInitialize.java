package worm.me.discord.bot;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class BotInitialize {

    public static MessageChannel channel;

    public static MessageChannel announcementChannel;

    public static MessageChannel staffChannel;

    public static Guild guild;

    public static JDA api;

    public static void initialize() {

        try {
            // Initialize the Discord bot
            api = JDABuilder.createDefault("ODYxMzQyODIxOTkwMTM3ODc2.GrYvJZ.p7s14zK9RHlRsQQSWbDYBV5lVTnxu4hjB5bid4").enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS)
                    .addEventListeners(new BotListener()).build();

            api.awaitReady();

            channel = api.getTextChannelById("1381733550277267608");
            announcementChannel = api.getNewsChannelById("1345095053294571530");
            staffChannel = api.getTextChannelById("1345096439868428340");

            guild = api.getGuildById("1345062374532710541");

            assert guild != null;
            guild.updateCommands().addCommands(
                    Commands.slash("list", "List all online players"),
                    Commands.slash("link", "Link your Minecraft account to Discord").addOption(OptionType.INTEGER, "code", "The code to link your account", true, false)
            ).queue();

            channel.sendMessage("`Server is starting up.`").queue();

            System.out.println("Discord bot initialized successfully.");
        } catch (Exception e) {
            System.err.println("Failed to initialize Discord bot: " + e.getMessage());
            e.printStackTrace();
        }

    }

}
