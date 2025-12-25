package api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import model.Dokter;
import model.ApiResponse;

public class DoctorApiClient {
    // Sesuaikan URL dengan folder project PHP Anda
    private static final String BASE_URL = "http://localhost/aplication-tier-pbotubes/public/dokter";
    private final Gson gson = new Gson();

    // 1. Ambil Semua Data Dokter (GET)
    public List<Dokter> findAll() {
        try {
            URL url = new URL(BASE_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                ApiResponse<List<Dokter>> response = gson.fromJson(reader, 
                    new TypeToken<ApiResponse<List<Dokter>>>(){}.getType());
                return response.getData();
            }
        } catch (Exception e) {
            System.err.println("Error GET Doctor: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    // 2. Tambah Dokter (POST)
    public boolean addDoctor(Dokter dokter) {
        return sendRequest("POST", dokter);
    }

    // 3. Update Dokter (PUT)
    public boolean updateDoctor(Dokter dokter) {
        return sendRequest("PUT", dokter);
    }

    // 4. Hapus Dokter (DELETE)
    public boolean deleteDoctor(int id) {
        // Kirim objek dokter yang hanya berisi ID untuk dihapus
        Dokter d = new Dokter(id, "", "");
        return sendRequest("DELETE", d);
    }

    // Helper Method untuk mengirim request (POST, PUT, DELETE)
    private boolean sendRequest(String method, Dokter dokter) {
        try {
            URL url = new URL(BASE_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String jsonInputString = gson.toJson(dokter);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            return conn.getResponseCode() == 200;
        } catch (Exception e) {
            System.err.println("Error " + method + " Doctor: " + e.getMessage());
            return false;
        }
    }
}