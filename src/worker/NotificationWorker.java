// File: worker/NotificationWorker.java
package worker;

import controller.NotificationController;
import java.util.List;
import java.util.logging.Level;

/**
 * Worker untuk background notification tasks
 */
public class NotificationWorker extends BaseWorker {
    private NotificationController notificationController;
    
    public NotificationWorker() {
        super("NotificationWorker", 10000); // Run setiap 10 detik
        this.notificationController = NotificationController.getInstance();
    }
    
    @Override
    protected void performTask() throws Exception {
        logger.fine("NotificationWorker performing tasks...");
        
        // 1. Cleanup notifikasi lama (hanya simpan 50 terbaru)
        cleanupOldNotifications();
        
        // 2. Check jika ada notifikasi urgent
        checkUrgentNotifications();
        
        // 3. Log aktivitas
        logActivity();
        
        logger.fine("NotificationWorker tasks completed");
    }
    
    private void cleanupOldNotifications() {
        try {
            List<String> notifications = notificationController.getAllNotifications();
            if (notifications.size() > 50) {
                // Dalam implementasi nyata, ini akan menghapus dari storage
                logger.info("Notification cleanup: " + notifications.size() + " notifications in queue");
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error cleanup notifications: " + e.getMessage(), e);
        }
    }
    
    private void checkUrgentNotifications() {
        try {
            List<String> recent = notificationController.getRecentNotifications(5);
            for (String notification : recent) {
                if (notification.contains("🚨") || notification.contains("ANTREAN DIPANGGIL")) {
                    logger.info("Urgent notification found: " + 
                        notification.substring(0, Math.min(50, notification.length())) + "...");
                }
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error check urgent notifications: " + e.getMessage(), e);
        }
    }
    
    private void logActivity() {
        try {
            List<String> recent = notificationController.getRecentNotifications(3);
            if (!recent.isEmpty()) {
                logger.fine("Recent notifications: " + recent.size() + " items");
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error logging activity: " + e.getMessage(), e);
        }
    }
    
    // Method untuk broadcast system message
    public void broadcastSystemMessage(String message) {
        notificationController.notifySystem("System Broadcast", message);
        logger.info("System broadcast sent: " + message);
    }
    
    // Method untuk periodic health check
    public void sendHealthCheck() {
        notificationController.notifySystem("Health Check", 
            "NotificationWorker running - " + java.time.LocalTime.now());
        logger.fine("Health check sent");
    }
}