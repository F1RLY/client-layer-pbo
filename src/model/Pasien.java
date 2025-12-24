package model;

import java.util.Date;

public class Pasien {
    private int id;
    private String nama;
    private String noKtp;
    private String alamat;
    private String noTelp;
    private Date tanggalLahir;
    private String jenisKelamin;
    
    // Constructor
    public Pasien() {}
    
    public Pasien(String nama, String noKtp, String alamat, String noTelp, 
                  Date tanggalLahir, String jenisKelamin) {
        this.nama = nama;
        this.noKtp = noKtp;
        this.alamat = alamat;
        this.noTelp = noTelp;
        this.tanggalLahir = tanggalLahir;
        this.jenisKelamin = jenisKelamin;
    }
    
    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    
    public String getNoKtp() { return noKtp; }
    public void setNoKtp(String noKtp) { this.noKtp = noKtp; }
    
    public String getAlamat() { return alamat; }
    public void setAlamat(String alamat) { this.alamat = alamat; }
    
    public String getNoTelp() { return noTelp; }
    public void setNoTelp(String noTelp) { this.noTelp = noTelp; }
    
    public Date getTanggalLahir() { return tanggalLahir; }
    public void setTanggalLahir(Date tanggalLahir) { this.tanggalLahir = tanggalLahir; }
    
    public String getJenisKelamin() { return jenisKelamin; }
    public void setJenisKelamin(String jenisKelamin) { this.jenisKelamin = jenisKelamin; }
}