// File: model/User.java
package model;

import java.time.LocalDateTime;

public class User extends BaseEntity {
    private String username;
    private String password;
    private String role; // "ADMIN", "RESEPSIONIS", "DOKTER"
    private String namaLengkap;
    private String email;
    private String noTelepon;
    private Boolean aktif;
    
    // Role constants
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_RESEPSIONIS = "RESEPSIONIS";
    public static final String ROLE_DOKTER = "DOKTER";
    
    // Constructors
    public User() {
        super();
        this.aktif = true;
    }
    
    public User(String username, String password, String role, String namaLengkap) {
        this();
        this.username = username;
        this.password = password;
        this.role = role;
        this.namaLengkap = namaLengkap;
    }
    
    // Getters and Setters
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getNamaLengkap() {
        return namaLengkap;
    }
    
    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getNoTelepon() {
        return noTelepon;
    }
    
    public void setNoTelepon(String noTelepon) {
        this.noTelepon = noTelepon;
        this.updatedAt = LocalDateTime.now();
    }
    
    public Boolean getAktif() {
        return aktif;
    }
    
    public void setAktif(Boolean aktif) {
        this.aktif = aktif;
        this.updatedAt = LocalDateTime.now();
    }
    
    // Helper methods untuk role checking
    public boolean isAdmin() {
        return ROLE_ADMIN.equals(role);
    }
    
    public boolean isResepsionis() {
        return ROLE_RESEPSIONIS.equals(role);
    }
    
    public boolean isDokter() {
        return ROLE_DOKTER.equals(role);
    }
    
    @Override
    public String toString() {
        return "User [username=" + username + ", role=" + role + 
               ", nama=" + namaLengkap + "]";
    }
}