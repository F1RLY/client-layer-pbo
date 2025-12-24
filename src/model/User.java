package model;

public class User {
    private String username;
    private String password;
    private String role; // ADMIN, RESEPSIONIS, DOKTER
    private String namaLengkap;
    private String lastLogin;
    
    // Constructor
    public User() {}
    
    public User(String username, String role, String namaLengkap) {
        this.username = username;
        this.role = role;
        this.namaLengkap = namaLengkap;
    }
    
    // Getters & Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    public String getNamaLengkap() { return namaLengkap; }
    public void setNamaLengkap(String namaLengkap) { this.namaLengkap = namaLengkap; }
    
    public String getLastLogin() { return lastLogin; }
    public void setLastLogin(String lastLogin) { this.lastLogin = lastLogin; }
    
    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(role);
    }
    
    public boolean isResepsionis() {
        return "RESEPSIONIS".equalsIgnoreCase(role);
    }
    
    public boolean isDokter() {
        return "DOKTER".equalsIgnoreCase(role);
    }
}