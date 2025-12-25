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
import model.Pasien;
import model.ApiResponse;

public class PasienApiClient {
    // Sesuaikan dengan nama folder project PHP Anda
    private static final String BASE_URL = "http://localhost/aplication-tier-pbotubes/public/pasien";
    private final Gson gson = new Gson();

    // 1. FUNGSI UNTUK MENGAMBIL SEMUA DATA (GET)
    public List<Pasien> getAllPasien() {
        try {
            URL url = new URL(BASE_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                // Parsing JSON menggunakan class wrapper ApiResponse
                ApiResponse<List<Pasien>> response = gson.fromJson(reader, 
                    new TypeToken<ApiResponse<List<Pasien>>>(){}.getType());
                return response.getData();
            }
        } catch (Exception e) {
            System.out.println("Error GET Pasien: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    // 2. FUNGSI UNTUK MENAMBAH DATA BARU (POST)
    public boolean addPasien(Pasien pasien) {
        try {
            URL url = new URL(BASE_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Ubah objek Pasien Java menjadi format teks JSON
            String jsonInputString = gson.toJson(pasien);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Cek apakah server membalas dengan kode 200 (OK)
            return conn.getResponseCode() == 200;
        } catch (Exception e) {
            System.out.println("Error POST Pasien: " + e.getMessage());
            return false;
        }
    }

        public boolean updatePasien(Pasien pasien) {
        return sendRequest("PUT", pasien);
    }

    public boolean deletePasien(int id) {
        // Kita kirim objek pasien yang hanya berisi ID untuk delete
        Pasien p = new Pasien(id, "", "");
        return sendRequest("DELETE", p);
    }

    // Helper method agar kode lebih bersih
    private boolean sendRequest(String method, Pasien pasien) {
        try {
            URL url = new URL(BASE_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String json = gson.toJson(pasien);
            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes("utf-8"));
            }
            return conn.getResponseCode() == 200;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}