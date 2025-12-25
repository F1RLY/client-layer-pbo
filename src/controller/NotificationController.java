// File: controller/NotificationController.java
package controller;

import model.Antrean;
import model.Pasien;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Controller untuk manajemen notifikasi (Observer Pattern)
 */
public class NotificationController extends Observable {
    private List<String> notifications = new ArrayList<>();
    private List<Observer> observers = new ArrayList<>();
    
    // Singleton pattern
    private static NotificationController instance;
    
    public static synchronized NotificationController getInstance() {
        if (instance == null) {
            instance = new NotificationController();
        }
        return instance;
    }
    
    private NotificationController() {
        // Private constructor untuk singleton
    }
    
    /**
     * Tambah observer
     */
    public void addObserver(Observer observer) {
        observers.add(observer);
        System.out.println("Observer ditambahkan: " + observer.getClass().getSimpleName());
    }
    
    /**
     * Hapus observer
     */
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }
    
    /**
     * Notifikasi antrean dipanggil
     */
    public void notifyAntreanDipanggil(Antrean antrean) {
        try {
            if (antrean == null) {
                throw new IllegalArgumentException("Antrean tidak boleh null");
            }
            
            String message = String.format(
                "🚨 ANTREAN DIPANGGIL!\n" +
                "Nomor: %s\n" +
                "Pasien: %s\n" +
                "Dokter: %s\n" +
                "Ruangan: %s\n" +
                "Silakan menuju ruangan yang ditentukan.",
                antrean.getNomorAntrean(),
                antrean.getPasien().getNama(),
                antrean.getDokter().getNama(),
                antrean.getRuangan()
            );
            
            addNotification(message);
            notifyObservers("ANTREAN_DIPANGGIL", message);
            
        } catch (Exception e) {
            System.err.println("Error notifikasi antrean dipanggil: " + e.getMessage());
        }
    }
    
    /**
     * Notifikasi antrean selesai
     */
    public void notifyAntreanSelesai(Antrean antrean) {
        try {
            String message = String.format(
                "✅ ANTREAN SELESAI\n" +
                "Nomor: %s\n" +
                "Pasien: %s\n" +
                "Terima kasih telah menggunakan layanan kami.",
                antrean.getNomorAntrean(),
                antrean.getPasien().getNama()
            );
            
            addNotification(message);
            notifyObservers("ANTREAN_SELESAI", message);
            
        } catch (Exception e) {
            System.err.println("Error notifikasi antrean selesai: " + e.getMessage());
        }
    }
    
    /**
     * Notifikasi pasien baru
     */
    public void notifyPasienBaru(Pasien pasien, String nomorAntrean) {
        try {
            String message = String.format(
                "📝 PASIEN BARU TERDAFTAR\n" +
                "Nama: %s\n" +
                "Nomor Antrean: %s\n" +
                "Silakan menunggu di ruang tunggu.",
                pasien.getNama(),
                nomorAntrean
            );
            
            addNotification(message);
            notifyObservers("PASIEN_BARU", message);
            
        } catch (Exception e) {
            System.err.println("Error notifikasi pasien baru: " + e.getMessage());
        }
    }
    
    /**
     * Notifikasi sistem
     */
    public void notifySystem(String title, String message) {
        try {
            String fullMessage = String.format(
                "⚙️ %s\n%s",
                title,
                message
            );
            
            addNotification(fullMessage);
            notifyObservers("SYSTEM", fullMessage);
            
        } catch (Exception e) {
            System.err.println("Error notifikasi sistem: " + e.getMessage());
        }
    }
    
    /**
     * Notifikasi peringatan
     */
    public void notifyWarning(String warning) {
        try {
            String message = String.format("⚠️ PERINGATAN: %s", warning);
            addNotification(message);
            notifyObservers("WARNING", message);
            
        } catch (Exception e) {
            System.err.println("Error notifikasi peringatan: " + e.getMessage());
        }
    }
    
    /**
     * Tambah notifikasi ke history
     */
    private void addNotification(String message) {
        String timestamp = java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String notification = String.format("[%s] %s", timestamp, message);
        
        notifications.add(notification);
        
        // Simpan maksimal 100 notifikasi terakhir
        if (notifications.size() > 100) {
            notifications.remove(0);
        }
        
        System.out.println("Notifikasi: " + message);
    }
    
    /**
     * Dapatkan semua notifikasi
     */
    public List<String> getAllNotifications() {
        return new ArrayList<>(notifications);
    }
    
    /**
     * Dapatkan notifikasi terbaru
     */
    public List<String> getRecentNotifications(int count) {
        int start = Math.max(0, notifications.size() - count);
        return new ArrayList<>(notifications.subList(start, notifications.size()));
    }
    
    /**
     * Clear semua notifikasi
     */
    public void clearNotifications() {
        notifications.clear();
        notifyObservers("CLEAR", "Notifikasi dibersihkan");
    }
    
    /**
     * Notify semua observers
     */
    private void notifyObservers(String type, String message) {
        for (Observer observer : observers) {
            try {
                observer.update(null, new NotificationData(type, message));
            } catch (Exception e) {
                System.err.println("Error notifying observer: " + e.getMessage());
            }
        }
    }
    
    /**
     * Inner class untuk data notifikasi
     */
    public static class NotificationData {
        private String type;
        private String message;
        private long timestamp;
        
        public NotificationData(String type, String message) {
            this.type = type;
            this.message = message;
            this.timestamp = System.currentTimeMillis();
        }
        
        public String getType() { return type; }
        public String getMessage() { return message; }
        public long getTimestamp() { return timestamp; }
        
        @Override
        public String toString() {
            return String.format("[%s] %s", type, message);
        }
    }
}