package api;

import com.google.gson.Gson;
import model.Antrean;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AntreanApiClient {
    private static final String BASE_URL = "http://localhost/realtime-application-tier-php/public/antrean/current";
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    public Antrean fetchCurrentCalling() throws Exception {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL)).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        // Misal responsenya langsung objek Antrean
        return gson.fromJson(response.body(), Antrean.class);
    }
}