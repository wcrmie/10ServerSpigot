package worm.me.commands.chat;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import worm.me.Server;
import worm.me.managers.ConfigManager;
import worm.me.managers.WebhookSender;
import worm.me.utils.ReportBook;

import java.util.logging.Logger;

public class ReportCommand implements CommandExecutor {

    private final Server plugin;

    public ReportCommand(Server plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, String @NotNull [] args) {

        if (!(sender instanceof Player reporter)) {
            sender.sendMessage(Component.text("⛔ Only players can use this command.", NamedTextColor.RED));
            return true;
        }

        ConfigManager config = plugin.getConfigManager();

        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (!reporter.hasPermission("report.reload")) {
                reporter.sendMessage(Component.text("❌ You do not have permission to do that.", NamedTextColor.RED));
                return true;
            }
            config.loadConfig();
            reporter.sendMessage(Component.text("✅ Report config reloaded.", NamedTextColor.GREEN));
            return true;
        }

        if (args.length < 1) {
            reporter.sendMessage(Component.text("⚠ Usage: /report <player>", NamedTextColor.RED));
            return true;
        }

        if (args.length == 1) {
            Player reported = Bukkit.getPlayer(args[0]);
            if (reported == null || !reported.isOnline()) {
                reporter.sendMessage(Component.text("❌ Player not found.", NamedTextColor.RED));
                return true;
            }

            ReportBook.openReportBook(reporter, reported.getName(), config);

            return true;
        }

        Player reported = Bukkit.getPlayer(args[0]);
        if (reported == null || !reported.isOnline()) {
            reporter.sendMessage(Component.text("❌ Player not found.", NamedTextColor.RED));
            return true;
        }

        String reason = String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length));

        PersistentDataContainer pdc = reporter.getPersistentDataContainer();
        long[] reportTimes = pdc.getOrDefault(new NamespacedKey(plugin, "reportTimes"), PersistentDataType.LONG_ARRAY, new long[5]);

        for (int i = 0; i < reportTimes.length; i++) {
            if (reportTimes[i] < System.currentTimeMillis()) {

                reportTimes[i] = System.currentTimeMillis() + config.getReportCooldown() * 1000L;

                pdc.set(new NamespacedKey(plugin, "reportTimes"), PersistentDataType.LONG_ARRAY, reportTimes);
                break;

            }

            if (i == reportTimes.length - 1) {
                reporter.sendMessage(Component.text("❌ You have made too many reports in the past 10 minutes. Please wait before sending another.", NamedTextColor.RED));
                return true;
            }
        }

        String message = String.format(
                """
                **✨ New Player Report ✨**
            
                🕵️‍♂️ **Reporter:** %s (`%s`)
                🚩 **Reported:** %s (`%s`)
                📋 **Reason:** %s
                
                <@&1360391623934218261>
                """,
                reporter.getName(), reporter.getUniqueId(),
                reported.getName(), reported.getUniqueId(),
                reason
        );

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.hasPermission("10VS.report.receive")) {
                p.sendMessage(Component.text(message, NamedTextColor.RED));
            }
        }

        String webhookUrl = config.getWebhookUrl();

        if (webhookUrl == null || webhookUrl.isEmpty()) {
            reporter.sendMessage(Component.text("⚠ Webhook URL is not configured!", NamedTextColor.RED));
            return true;
        }

        Logger logger = plugin.getLogger();

//        String embed = new WebhookSender.EmbedBuilder().setTitle("New Player Report")
//                .setDescription(message)
//                .setColor("0xFF0000") // Red color for reports
//                .setFooter("<@&1360391623934218261>").build();

        WebhookSender.sendMessage(webhookUrl, message, logger);

        reporter.sendMessage(Component.text("✅ Your report has been sent. Thank you!", NamedTextColor.GREEN));

        return true;

    }
}
