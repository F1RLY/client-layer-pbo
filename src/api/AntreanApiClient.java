package api;

import com.google.gson.Gson;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import model.Antrean;
import model.ApiResponse;

public class AntreanApiClient {
    private static final String BASE_URL = "http://localhost/aplication-tier-pbotubes/public/antrean";
    private final Gson gson = new Gson();

    // Untuk POST (Simpan)
    public boolean saveAntrean(int pasienId, int dokterId) {
        try {
            URL url = new URL(BASE_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String jsonInput = String.format("{\"pasien_id\": %d, \"dokter_id\": %d}", pasienId, dokterId);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonInput.getBytes("utf-8"));
            }
            return conn.getResponseCode() == 200;
        } catch (Exception e) {
            return false;
        }
    }

    // Untuk GET (Ambil yang sedang dipanggil)
    public Antrean fetchCurrentCalling() {
        try {
            URL url = new URL(BASE_URL); // Tambahkan endpoint spesifik jika ada, misal /current
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == 200) {
                Scanner sc = new Scanner(conn.getInputStream());
                String response = sc.useDelimiter("\\A").next();
                sc.close();
                // Parsing logic tergantung struktur JSON ApiResponse Anda
                return gson.fromJson(response, Antrean.class); 
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}