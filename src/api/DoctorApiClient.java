package api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Dokter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class DoctorApiClient {
    // Sesuaikan URL dengan route di PHP Laragon Anda
    private static final String BASE_URL = "http://localhost/realtime-application-tier-php/public/dokter";
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    public List<Dokter> findAll() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        // Menggunakan struktur ApiResponse yang sama dengan MahasiswaApiClient
        ApiResponse<List<Dokter>> apiResp = gson.fromJson(response.body(),
                new TypeToken<ApiResponse<List<Dokter>>>() {}.getType());

        if (!apiResp.success) throw new Exception(apiResp.message);
        return apiResp.data;
    }

    // Helper class internal untuk mapping JSON
    private static class ApiResponse<T> {
        boolean success;
        T data;
        String message;
    }
}