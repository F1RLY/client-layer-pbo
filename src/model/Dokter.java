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
    }
    
    public Dokter(String kodeDokter, String nama, String spesialisasi) {
        super();
        this.kodeDokter = kodeDokter;
        this.nama = nama;
        this.spesialisasi = spesialisasi;
        this.status = "AKTIF";
    }
    
    // Getters and Setters
    public String getKodeDokter() {
        return kodeDokter;
    }
    
    public void setKodeDokter(String kodeDokter) {
        this.kodeDokter = kodeDokter;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getNama() {
        return nama;
    }
    
    public void setNama(String nama) {
        this.nama = nama;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getSpesialisasi() {
        return spesialisasi;
    }
    
    public void setSpesialisasi(String spesialisasi) {
        this.spesialisasi = spesialisasi;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getNoTelepon() {
        return noTelepon;
    }
    
    public void setNoTelepon(String noTelepon) {
        this.noTelepon = noTelepon;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }
    
    @Override
    public String toString() {
        return "Dokter [kode=" + kodeDokter + ", nama=" + nama + 
               ", spesialisasi=" + spesialisasi + "]";
    }
}