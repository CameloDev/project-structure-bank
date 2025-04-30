package capstoneds2.main_app.Msg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class DiscordAlert {
    private final String webhookUrl ="https://discord.com/api/webhooks/1364756986691981312/qo80Oz-RDL8AU1xuzE1wbfaOmma3fqjDRXmUCIEKbj7ov_hjI4uBKCOacgxDJ5A4GDKn";
    Logger log = LoggerFactory.getLogger(getClass());

    public void AlertDiscord(String mensagem) {
        try {
            String json = String.format("{\"content\": \"%s\"}", mensagem.replace("\"", "\\\""));

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(webhookUrl))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            client.sendAsync(request, HttpResponse.BodyHandlers.discarding());
        } catch (Exception e) {
            System.err.println("❌ Falha ao enviar alerta para Discord: ");
            e.printStackTrace();
        }
    }

}

