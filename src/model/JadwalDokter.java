// File: model/JadwalDokter.java
package model;
import java.time.LocalDateTime;
import java.time.DayOfWeek;
import java.time.LocalTime;

public class JadwalDokter extends BaseEntity {
    private Dokter dokter;
    private DayOfWeek hari;
    private LocalTime jamMulai;
    private LocalTime jamSelesai;
    private Integer kuota;
    private String ruangan;
    
    // Constructors
    public JadwalDokter() {
        super();
    }
    
    public JadwalDokter(Dokter dokter, DayOfWeek hari, 
                       LocalTime jamMulai, LocalTime jamSelesai) {
        super();
        this.dokter = dokter;
        this.hari = hari;
        this.jamMulai = jamMulai;
        this.jamSelesai = jamSelesai;
    }
    
    // Getters and Setters
    public Dokter getDokter() {
        return dokter;
    }
    
    public void setDokter(Dokter dokter) {
        this.dokter = dokter;
        this.updatedAt = LocalDateTime.now();
    }
    
    public DayOfWeek getHari() {
        return hari;
    }
    
    public void setHari(DayOfWeek hari) {
        this.hari = hari;
        this.updatedAt = LocalDateTime.now();
    }
    
    public LocalTime getJamMulai() {
        return jamMulai;
    }
    
    public void setJamMulai(LocalTime jamMulai) {
        this.jamMulai = jamMulai;
        this.updatedAt = LocalDateTime.now();
    }
    
    public LocalTime getJamSelesai() {
        return jamSelesai;
    }
    
    public void setJamSelesai(LocalTime jamSelesai) {
        this.jamSelesai = jamSelesai;
        this.updatedAt = LocalDateTime.now();
    }
    
    public Integer getKuota() {
        return kuota;
    }
    
    public void setKuota(Integer kuota) {
        this.kuota = kuota;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getRuangan() {
        return ruangan;
    }
    
    public void setRuangan(String ruangan) {
        this.ruangan = ruangan;
        this.updatedAt = LocalDateTime.now();
    }
    
    // Helper method untuk cek apakah jadwal masih valid
    public boolean isJadwalValid() {
        return jamMulai != null && jamSelesai != null && 
               jamMulai.isBefore(jamSelesai);
    }
    
    // Helper method untuk durasi praktek
    public long getDurasiJam() {
        if (jamMulai != null && jamSelesai != null) {
            return java.time.Duration.between(jamMulai, jamSelesai).toHours();
        }
        return 0;
    }
    
    @Override
    public String toString() {
        String namaHari = "Senin";
        switch(hari) {
            case MONDAY: namaHari = "Senin"; break;
            case TUESDAY: namaHari = "Selasa"; break;
            case WEDNESDAY: namaHari = "Rabu"; break;
            case THURSDAY: namaHari = "Kamis"; break;
            case FRIDAY: namaHari = "Jumat"; break;
            case SATURDAY: namaHari = "Sabtu"; break;
            case SUNDAY: namaHari = "Minggu"; break;
        }
        
        return "JadwalDokter [dokter=" + (dokter != null ? dokter.getNama() : "null") + 
               ", hari=" + namaHari + ", jam=" + jamMulai + "-" + jamSelesai + "]";
    }
}