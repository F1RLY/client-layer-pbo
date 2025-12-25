// File: controller/AuthController.java
package controller;

import model.User;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller untuk autentikasi dan manajemen user
 */
public class AuthController {
    private Map<String, User> users = new HashMap<>();
    private User currentUser;
    
    // Constructor dengan data dummy untuk testing
    public AuthController() {
        initializeDummyData();
    }
    
    private void initializeDummyData() {
        // Admin
        User admin = new User("admin", "admin123", User.ROLE_ADMIN, "Administrator");
        admin.setEmail("admin@smars.com");
        users.put("admin", admin);
        
        // Resepsionis
        User resepsionis = new User("resepsionis", "resep123", User.ROLE_RESEPSIONIS, "Budi Santoso");
        resepsionis.setEmail("budi@smars.com");
        users.put("resepsionis", resepsionis);
        
        // Dokter
        User dokter = new User("dr_susi", "dokter123", User.ROLE_DOKTER, "Dr. Susi Handayani");
        dokter.setEmail("susi@smars.com");
        users.put("dr_susi", dokter);
    }
    
    /**
     * Login user
     */
    public User login(String username, String password) {
        try {
            if (username == null || username.trim().isEmpty()) {
                throw new IllegalArgumentException("Username tidak boleh kosong");
            }
            
            if (password == null || password.trim().isEmpty()) {
                throw new IllegalArgumentException("Password tidak boleh kosong");
            }
            
            User user = users.get(username);
            
            if (user == null) {
                throw new SecurityException("Username tidak ditemukan");
            }
            
            if (!user.getPassword().equals(password)) {
                throw new SecurityException("Password salah");
            }
            
            if (!user.getAktif()) {
                throw new SecurityException("Akun tidak aktif");
            }
            
            currentUser = user;
            System.out.println("Login berhasil: " + user.getNamaLengkap());
            return user;
            
        } catch (IllegalArgumentException | SecurityException e) {
            System.err.println("Login gagal: " + e.getMessage());
            return null;
        } catch (Exception e) {
            handleException(e, "login");
            return null;
        }
    }
    
    /**
     * Logout user
     */
    public void logout() {
        System.out.println("Logout: " + (currentUser != null ? currentUser.getUsername() : "No user"));
        currentUser = null;
    }
    
    /**
     * Mendapatkan user yang sedang login
     */
    public User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Cek apakah user sudah login
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }
    
    /**
     * Cek apakah user memiliki role tertentu
     */
    public boolean hasRole(String role) {
        if (currentUser == null) return false;
        return currentUser.getRole().equals(role);
    }
    
    /**
     * Register user baru (untuk admin)
     */
    public boolean register(User newUser) {
        try {
            // Validasi
            if (newUser.getUsername() == null || newUser.getUsername().trim().isEmpty()) {
                throw new IllegalArgumentException("Username tidak boleh kosong");
            }
            
            if (users.containsKey(newUser.getUsername())) {
                throw new IllegalArgumentException("Username sudah digunakan");
            }
            
            if (newUser.getPassword() == null || newUser.getPassword().trim().isEmpty()) {
                throw new IllegalArgumentException("Password tidak boleh kosong");
            }
            
            if (newUser.getPassword().length() < 6) {
                throw new IllegalArgumentException("Password minimal 6 karakter");
            }
            
            // Simpan user
            users.put(newUser.getUsername(), newUser);
            System.out.println("User berhasil diregistrasi: " + newUser.getUsername());
            return true;
            
        } catch (IllegalArgumentException e) {
            System.err.println("Registrasi gagal: " + e.getMessage());
            return false;
        } catch (Exception e) {
            handleException(e, "register");
            return false;
        }
    }
    
    /**
     * Update password user
     */
    public boolean updatePassword(String username, String oldPassword, String newPassword) {
        try {
            User user = users.get(username);
            if (user == null) {
                throw new IllegalArgumentException("User tidak ditemukan");
            }
            
            if (!user.getPassword().equals(oldPassword)) {
                throw new SecurityException("Password lama salah");
            }
            
            user.setPassword(newPassword);
            System.out.println("Password berhasil diubah untuk user: " + username);
            return true;
            
        } catch (IllegalArgumentException | SecurityException e) {
            System.err.println("Gagal update password: " + e.getMessage());
            return false;
        } catch (Exception e) {
            handleException(e, "updatePassword");
            return false;
        }
    }
    
    private void handleException(Exception e, String operation) {
        System.err.println("Error pada operasi " + operation + ": " + e.getMessage());
        e.printStackTrace();
    }
}