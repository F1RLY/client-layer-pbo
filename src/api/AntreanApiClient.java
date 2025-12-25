package api;

import com.google.gson.Gson;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import model.Antrean;

public class AntreanApiClient {
    private static final String BASE_URL = "http://localhost/aplication-tier-pbotubes/public/antrean";
    private final Gson gson = new Gson();

    // Simpan Antrean Baru
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
            e.printStackTrace();
            return false;
        }
    }
    
        public boolean updateStatusNext() {
        try {
            // Memanggil endpoint http://localhost/.../public/antrean/next
            URL url = new URL(BASE_URL + "/next"); 
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            return conn.getResponseCode() == 200;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Antrean fetchCurrentCalling() {
        try {
            URL url = new URL(BASE_URL + "/current");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == 200) {
                java.util.Scanner sc = new java.util.Scanner(conn.getInputStream());
                String response = sc.useDelimiter("\\A").next();
                sc.close();
                // GSON otomatis memetakan JSON ke model Antrean kita tadi
                return gson.fromJson(response, Antrean.class); 
            }
        } catch (Exception e) {
            System.err.println("Gagal ambil data monitor: " + e.getMessage());
        }
        return null;
    }
}