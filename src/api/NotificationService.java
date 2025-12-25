// File: api/NotificationService.java
package api;

import controller.NotificationController;
import model.Antrean;
import java.util.logging.Logger;

/**
 * Service untuk menghubungkan WebSocket dengan NotificationController
 */
public class NotificationService {
    private static final Logger logger = Logger.getLogger(NotificationService.class.getName());
    
    private WebSocketClient webSocketClient;
    private NotificationController notificationController;
    private String serverUrl;
    
    // Singleton instance
    private static NotificationService instance;
    
    public static synchronized NotificationService getInstance() {
        if (instance == null) {
            instance = new NotificationService();
        }
        return instance;
    }
    
    private NotificationService() {
        this.notificationController = NotificationController.getInstance();
        this.serverUrl = "ws://localhost:8080/antrean";
        this.webSocketClient = new WebSocketClient(serverUrl);
        
        setupWebSocketHandlers();
    }
    
    // Initialize service
    public boolean initialize() {
        logger.info("Initializing NotificationService...");
        
        // Connect to WebSocket server
        boolean connected = webSocketClient.connect();
        
        if (connected) {
            // Join default room
            webSocketClient.joinRoom("monitor");
            webSocketClient.joinRoom("admin");
            
            logger.info("NotificationService initialized successfully");
        } else {
            logger.warning("NotificationService initialized in offline mode");
        }
        
        return connected;
    }
    
    // Setup WebSocket message handlers
    private void setupWebSocketHandlers() {
        webSocketClient.addMessageHandler(message -> {
            try {
                // Parse message
                String[] parts = message.split("\\|", 2);
                if (parts.length < 2) return;
                
                String typeStr = parts[0];
                String data = parts[1];
                
                MessageType type = MessageType.fromString(typeStr);
                if (type == null) return;
                
                // Handle different message types
                switch (type) {
                    case ANTREAN_PANGGIL:
                        handlePanggilMessage(data);
                        break;
                        
                    case ANTREAN_SELESAI:
                        handleSelesaiMessage(data);
                        break;
                        
                    case ANTREAN_BARU:
                        handleAntreanBaruMessage(data);
                        break;
                        
                    case SYSTEM_INFO:
                        logger.info("System info: " + data);
                        break;
                        
                    case SYSTEM_WARNING:
                        logger.warning("System warning: " + data);
                        break;
                        
                    case SYSTEM_ERROR:
                        logger.severe("System error: " + data);
                        break;
                }
                
            } catch (Exception e) {
                logger.severe("Error handling WebSocket message: " + e.getMessage());
            }
        });
    }
    
    // Handle panggil antrean message
    private void handlePanggilMessage(String jsonData) {
        try {
            // Simple JSON parsing (in production, use GSON or Jackson)
            String nomor = extractJsonValue(jsonData, "nomor");
            String pasien = extractJsonValue(jsonData, "pasien");
            String dokter = extractJsonValue(jsonData, "dokter");
            String ruangan = extractJsonValue(jsonData, "ruangan");
            
            // Create notification
            String message = String.format(
                "🚨 ANTREAN DIPANGGIL!\nNomor: %s\nPasien: %s\nDokter: %s\nRuangan: %s",
                nomor, pasien, dokter, ruangan);
            
            notificationController.notifySystem("Pemanggilan Antrean", message);
            
        } catch (Exception e) {
            logger.severe("Error parsing panggil message: " + e.getMessage());
        }
    }
    
    // Handle selesai antrean message
    private void handleSelesaiMessage(String jsonData) {
        try {
            String nomor = extractJsonValue(jsonData, "nomor");
            String message = String.format("✅ ANTREAN SELESAI\nNomor: %s", nomor);
            notificationController.notifySystem("Antrean Selesai", message);
            
        } catch (Exception e) {
            logger.severe("Error parsing selesai message: " + e.getMessage());
        }
    }
    
    // Handle antrean baru message
    private void handleAntreanBaruMessage(String jsonData) {
        try {
            String nomor = extractJsonValue(jsonData, "nomor");
            String pasien = extractJsonValue(jsonData, "pasien");
            String dokter = extractJsonValue(jsonData, "dokter");
            
            String message = String.format(
                "📝 ANTREAN BARU\nNomor: %s\nPasien: %s\nDokter: %s",
                nomor, pasien, dokter);
            
            notificationController.notifySystem("Antrean Baru", message);
            
        } catch (Exception e) {
            logger.severe("Error parsing antrean baru message: " + e.getMessage());
        }
    }
    
    // Utility method to extract value from simple JSON
    private String extractJsonValue(String json, String key) {
        try {
            String searchKey = "\"" + key + "\":\"";
            int startIndex = json.indexOf(searchKey);
            if (startIndex == -1) return "";
            
            startIndex += searchKey.length();
            int endIndex = json.indexOf("\"", startIndex);
            
            if (endIndex == -1) return "";
            return json.substring(startIndex, endIndex);
            
        } catch (Exception e) {
            return "";
        }
    }
    
    // Public methods to send notifications
    public void notifyPanggilAntrean(Antrean antrean) {
        if (webSocketClient.isConnected()) {
            webSocketClient.sendPanggilAntrean(
                antrean.getNomorAntrean(),
                antrean.getPasien().getNama(),
                antrean.getDokter().getNama(),
                antrean.getRuangan()
            );
        }
        
        // Also notify locally
        notificationController.notifyAntreanDipanggil(antrean);
    }
    
    public void notifySelesaiAntrean(Antrean antrean) {
        if (webSocketClient.isConnected()) {
            webSocketClient.sendSelesaiAntrean(antrean.getNomorAntrean());
        }
        
        notificationController.notifyAntreanSelesai(antrean);
    }
    
    public void notifyAntreanBaru(Antrean antrean) {
        if (webSocketClient.isConnected()) {
            webSocketClient.sendAntreanBaru(
                antrean.getNomorAntrean(),
                antrean.getPasien().getNama(),
                antrean.getDokter().getNama()
            );
        }
        
        notificationController.notifyPasienBaru(
            antrean.getPasien(),
            antrean.getNomorAntrean()
        );
    }
    
    // Send custom notification
    public void sendSystemNotification(String title, String message) {
        notificationController.notifySystem(title, message);
    }
    
    // Status methods
    public boolean isConnected() {
        return webSocketClient.isConnected();
    }
    
    public void reconnect() {
        if (!webSocketClient.isConnected()) {
            webSocketClient.disconnect();
            webSocketClient.connect();
        }
    }
    
    public void shutdown() {
        webSocketClient.disconnect();
        logger.info("NotificationService shut down");
    }
}