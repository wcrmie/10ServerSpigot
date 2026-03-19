package worm.me.utils.managers;

import org.bukkit.Bukkit;
import worm.me.Server;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

public class WebhookSender {

    public static void sendMessage(String webhookUrl, String content, Logger logger) {

        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            try {
                URI uri = new URI(webhookUrl);
                URL url = uri.toURL();
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("POST");
                connection.addRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                // Escape JSON special characters
                String safeContent = content
                        .replace("\\", "\\\\")
                        .replace("\"", "\\\"")
                        .replace("\n", "\\n")
                        .replace("\r", "\\r");

                String payload = "{ \"content\": \"" + safeContent + "\" }";

                try (OutputStream stream = connection.getOutputStream()) {
                    stream.write(payload.getBytes());
                    stream.flush();
                }

                int responseCode = connection.getResponseCode(); // trigger the request
                if (responseCode >= 400) {
                    logger.warning("Webhook returned HTTP error code: " + responseCode);
                }

            } catch (Exception e) {
                logger.severe("Failed to send webhook report: " + e.getMessage());
            }
        }).orTimeout(3, java.util.concurrent.TimeUnit.SECONDS);
    }

    public static void sendEmbed(String webhookUrl, String embed, Logger logger) {
        try {
            URI uri = new URI(webhookUrl);
            URL url = uri.toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.addRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            String payload = "{" + embed + "}";

            try (OutputStream stream = connection.getOutputStream()) {
                stream.write(payload.getBytes());
                stream.flush();
            }

            int responseCode = connection.getResponseCode(); // trigger the request
            if (responseCode >= 400) {
                logger.warning("Webhook returned HTTP error code: " + responseCode);
            }

        } catch (Exception e) {
            logger.severe("Failed to send webhook embed: " + e.getMessage());
        }
    }

    public static class EmbedBuilder {
        private String title;
        private String description;
        private String color;
        private String footer;

        public EmbedBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        public EmbedBuilder setDescription(String description) {
            this.description = description;
            return this;
        }

        public EmbedBuilder setColor(String color) {
            this.color = color;
            return this;
        }

        public EmbedBuilder setFooter(String footer) {
            this.footer = footer;
            return this;
        }

        public String build() {
            return "\"embeds\": [" +
                    "{" +
                    "\"title\": \"" + title + "\", " +
                    "\"description\": \"" + description + "\", " +
                    "\"color\": \"" + color + "\"" +
                    "\"footer\": {\"text\": \"" + footer + "\"}" +
                    "}" +
                    "]";
        }
    }
}
