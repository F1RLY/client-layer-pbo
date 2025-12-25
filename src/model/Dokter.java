// File: model/Dokter.java
package model;

import java.time.LocalDateTime;

public class Dokter extends BaseEntity {
    private String kodeDokter;
    private String nama;
    private String spesialisasi;
    private String noTelepon;
    private String email;
    private String status; // "AKTIF" atau "NONAKTIF"
    
    // Constructors
    public Dokter() {
        super();
        this.status = "AKTIF"; // Default status
    }
    
    public Dokter(String kodeDokter, String nama, String spesialisasi) {
        this(); // Panggil default constructor
        this.kodeDokter = kodeDokter;
        this.nama = nama;
        this.spesialisasi = spesialisasi;
    }
    
    // Getters and Setters (tanpa update timestamp di setiap setter)
    public String getKodeDokter() {
        return kodeDokter;
    }
    
    public void setKodeDokter(String kodeDokter) {
        this.kodeDokter = kodeDokter;
    }
    
    public String getNama() {
        return nama;
    }
    
    public void setNama(String nama) {
        this.nama = nama;
    }
    
    public String getSpesialisasi() {
        return spesialisasi;
    }
    
    public void setSpesialisasi(String spesialisasi) {
        this.spesialisasi = spesialisasi;
    }
    
    public String getNoTelepon() {
        return noTelepon;
    }
    
    public void setNoTelepon(String noTelepon) {
        this.noTelepon = noTelepon;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return String.format("Dokter[%s] %s - %s", 
            kodeDokter, nama, spesialisasi);
    }
    
    // Helper method untuk update timestamp manual jika perlu
    public void markUpdated() {
        this.updatedAt = LocalDateTime.now();
    }
}