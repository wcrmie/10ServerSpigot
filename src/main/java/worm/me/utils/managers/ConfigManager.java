package worm.me.utils.managers;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import worm.me.Server;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("deprecation")
public class ConfigManager {

    private List<Map<String, Object>> reasons; // List of reason maps
    private String reportWebhook;
    private String moderatorWebhook;
    private long reportCooldown;
    private List<String> blacklistedWords;
    private List<String> filteredWords;

    public void loadConfig() {
        Server.getPlugin().reloadConfig();
        FileConfiguration config = Server.getPlugin().getConfig();

        reportCooldown = config.getLong("report-cooldown");

        reportWebhook = config.getString("report-webhook-url");
        moderatorWebhook = config.getString("moderator-webhook-url");

        // Get raw list of maps with unknown generics
        List<Map<?, ?>> rawList = config.getMapList("reasons");

        // Cast each map and translate color codes
        reasons = rawList.stream()
                .map(rawMap -> {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> reasonMap = (Map<String, Object>) rawMap;

                    String name = (String) reasonMap.get("name");
                    String description = (String) reasonMap.get("description");

                    if (name != null) {
                        reasonMap.put("name", ChatColor.translateAlternateColorCodes('&', name));
                    }
                    if (description != null) {
                        reasonMap.put("description", ChatColor.translateAlternateColorCodes('&', description));
                    }

                    return reasonMap;
                })
                .collect(Collectors.toList());

        blacklistedWords = config.getStringList("automute-words");
        filteredWords = config.getStringList("filtered-words");

    }

    public List<Map<String, Object>> getReasons() {
        return reasons;
    }

    public String getReportWebhook() {
        return reportWebhook;
    }

    public long getReportCooldown() {
        return reportCooldown;
    }

    public List<String> getBlacklistedWords() {
        return blacklistedWords;
    }

    public List<String> getFilteredWords() {
        return filteredWords;
    }

    public String getModeratorWebhook() {
        return moderatorWebhook;
    }
}
