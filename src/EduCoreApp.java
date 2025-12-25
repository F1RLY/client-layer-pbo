// File: EduCoreApp.java (atau MainApp.java)
import view.LoginFrame;
import api.WebSocketServer;
import api.NotificationService;
import javax.swing.*;

public class EduCoreApp {
    public static void main(String[] args) {
        // Start WebSocket Server (in production, this runs in separate thread)
        System.out.println("Starting SMARS Application...");
        
        // Start WebSocket server
        WebSocketServer.startServer(8080);
        System.out.println("WebSocket Server ready on port 8080");
        
        // Initialize Notification Service
        NotificationService notificationService = NotificationService.getInstance();
        boolean wsConnected = notificationService.initialize();
        
        if (wsConnected) {
            System.out.println("WebSocket Client connected successfully");
        } else {
            System.out.println("WebSocket Client in offline mode");
        }
        
        // Start GUI
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
        
        // Add shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            notificationService.shutdown();
            System.out.println("Application shutdown complete");
        }));
    }
}