// File: model/Antrean.java
package model;

import java.time.LocalDateTime;

public class Antrean extends BaseEntity {
    private String nomorAntrean;
    private Pasien pasien;
    private Dokter dokter;
    private LocalDateTime waktuMasuk;
    private LocalDateTime waktuPanggil;
    private LocalDateTime waktuSelesai;
    private String status; // "MENUNGGU", "DIPANGGIL", "SELESAI", "BATAL"
    private String ruangan;
    private String catatan;
    
    // Constants untuk status
    public static final String STATUS_MENUNGGU = "MENUNGGU";
    public static final String STATUS_DIPANGGIL = "DIPANGGIL";
    public static final String STATUS_SELESAI = "SELESAI";
    public static final String STATUS_BATAL = "BATAL";
    
    // Constructors
    public Antrean() {
        super();
        this.waktuMasuk = LocalDateTime.now();
        this.status = STATUS_MENUNGGU;
    }
    
    public Antrean(String nomorAntrean, Pasien pasien, Dokter dokter) {
        this();
        this.nomorAntrean = nomorAntrean;
        this.pasien = pasien;
        this.dokter = dokter;
    }
    
    // Getters and Setters
    public String getNomorAntrean() {
        return nomorAntrean;
    }
    
    public void setNomorAntrean(String nomorAntrean) {
        this.nomorAntrean = nomorAntrean;
        this.updatedAt = LocalDateTime.now();
    }
    
    public Pasien getPasien() {
        return pasien;
    }
    
    public void setPasien(Pasien pasien) {
        this.pasien = pasien;
        this.updatedAt = LocalDateTime.now();
    }
    
    public Dokter getDokter() {
        return dokter;
    }
    
    public void setDokter(Dokter dokter) {
        this.dokter = dokter;
        this.updatedAt = LocalDateTime.now();
    }
    
    public LocalDateTime getWaktuMasuk() {
        return waktuMasuk;
    }
    
    public void setWaktuMasuk(LocalDateTime waktuMasuk) {
        this.waktuMasuk = waktuMasuk;
        this.updatedAt = LocalDateTime.now();
    }
    
    public LocalDateTime getWaktuPanggil() {
        return waktuPanggil;
    }
    
    public void setWaktuPanggil(LocalDateTime waktuPanggil) {
        this.waktuPanggil = waktuPanggil;
        this.updatedAt = LocalDateTime.now();
    }
    
    public LocalDateTime getWaktuSelesai() {
        return waktuSelesai;
    }
    
    public void setWaktuSelesai(LocalDateTime waktuSelesai) {
        this.waktuSelesai = waktuSelesai;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
        
        // Update waktu otomatis berdasarkan status
        if (STATUS_DIPANGGIL.equals(status) && this.waktuPanggil == null) {
            this.waktuPanggil = LocalDateTime.now();
        } else if (STATUS_SELESAI.equals(status) && this.waktuSelesai == null) {
            this.waktuSelesai = LocalDateTime.now();
        }
    }
    
    public String getRuangan() {
        return ruangan;
    }
    
    public void setRuangan(String ruangan) {
        this.ruangan = ruangan;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getCatatan() {
        return catatan;
    }
    
    public void setCatatan(String catatan) {
        this.catatan = catatan;
        this.updatedAt = LocalDateTime.now();
    }
    
    // Helper methods
    public boolean isMenunggu() {
        return STATUS_MENUNGGU.equals(status);
    }
    
    public boolean isDipanggil() {
        return STATUS_DIPANGGIL.equals(status);
    }
    
    public boolean isSelesai() {
        return STATUS_SELESAI.equals(status);
    }
    
    public boolean isBatal() {
        return STATUS_BATAL.equals(status);
    }
    
    // Method untuk panggil antrean
    public void panggil(String ruangan) {
        setStatus(STATUS_DIPANGGIL);
        setRuangan(ruangan);
    }
    
    // Method untuk selesaikan antrean
    public void selesaikan() {
        setStatus(STATUS_SELESAI);
    }
    
    // Method untuk batalkan antrean
    public void batalkan(String catatan) {
        setStatus(STATUS_BATAL);
        setCatatan(catatan);
    }
    
    @Override
    public String toString() {
        return "Antrean [nomor=" + nomorAntrean + ", pasien=" + 
               (pasien != null ? pasien.getNama() : "null") + 
               ", dokter=" + (dokter != null ? dokter.getNama() : "null") + 
               ", status=" + status + "]";
    }
}