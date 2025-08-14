package worm.me.managers;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.logging.Logger;

public class WebhookSender {

    public static void sendReport(String webhookUrl, String content, Logger logger) {
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
    }
}
