package api;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Pasien;

public class PasienApiClient {
    // Sesuaikan BASE_URL dengan nama folder project Anda di Laragon www
    private static final String BASE_URL = "http://localhost/realtime-application-tier-php/public/pasien";
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    public List<Pasien> findAll() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .GET()
                .build();
        
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        // Membaca format ApiResponse yang Anda gunakan di tugas sebelumnya
        ApiResponse<List<Pasien>> apiResp = gson.fromJson(response.body(),
                new TypeToken<ApiResponse<List<Pasien>>>() {}.getType());
        
        if (!apiResp.success) throw new Exception(apiResp.message);
        return apiResp.data;
    }

    public void create(Pasien p) throws Exception {
        String json = gson.toJson(p);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        handleResponse(response);
    }

    // Helper untuk handle response sesuai standar tugas Anda
    private void handleResponse(HttpResponse<String> response) throws Exception {
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new RuntimeException("HTTP Error " + response.statusCode());
        }
        ApiResponse<?> apiResp = gson.fromJson(response.body(), ApiResponse.class);
        if (!apiResp.success) throw new Exception(apiResp.message);
    }

    // Wrapper JSON agar sinkron dengan Tier-PHP
    private static class ApiResponse<T> {
        boolean success;
        T data;
        String message;
    }
}