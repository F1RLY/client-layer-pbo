// File: model/Pasien.java
package model;

import java.time.LocalDateTime;

import java.time.LocalDate;

public class Pasien extends BaseEntity {
    private String nama;
    private String nik;
    private LocalDate tanggalLahir;
    private String alamat;
    private String noTelepon;
    private String jenisKelamin; // "L" atau "P"
    private String golonganDarah; // "A", "B", "AB", "O"
    private String alergi;
    
    // Constructors
    public Pasien() {
        super();
    }
    
    public Pasien(String nama, String nik, LocalDate tanggalLahir, 
                  String alamat, String noTelepon) {
        super();
        this.nama = nama;
        this.nik = nik;
        this.tanggalLahir = tanggalLahir;
        this.alamat = alamat;
        this.noTelepon = noTelepon;
    }
    
    // Getters and Setters
    public String getNama() {
        return nama;
    }
    
    public void setNama(String nama) {
        this.nama = nama;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getNik() {
        return nik;
    }
    
    public void setNik(String nik) {
        this.nik = nik;
        this.updatedAt = LocalDateTime.now();
    }
    
    public LocalDate getTanggalLahir() {
        return tanggalLahir;
    }
    
    public void setTanggalLahir(LocalDate tanggalLahir) {
        this.tanggalLahir = tanggalLahir;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getAlamat() {
        return alamat;
    }
    
    public void setAlamat(String alamat) {
        this.alamat = alamat;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getNoTelepon() {
        return noTelepon;
    }
    
    public void setNoTelepon(String noTelepon) {
        this.noTelepon = noTelepon;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getJenisKelamin() {
        return jenisKelamin;
    }
    
    public void setJenisKelamin(String jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getGolonganDarah() {
        return golonganDarah;
    }
    
    public void setGolonganDarah(String golonganDarah) {
        this.golonganDarah = golonganDarah;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getAlergi() {
        return alergi;
    }
    
    public void setAlergi(String alergi) {
        this.alergi = alergi;
        this.updatedAt = LocalDateTime.now();
    }
    
    @Override
    public String toString() {
        return "Pasien [id=" + id + ", nama=" + nama + ", nik=" + nik + 
               ", noTelepon=" + noTelepon + "]";
    }
    
    // Helper method untuk menghitung usia
    public int getUsia() {
        if (tanggalLahir == null) return 0;
        LocalDate sekarang = LocalDate.now();
        return sekarang.getYear() - tanggalLahir.getYear();
    }
}