package model;

public class Pasien {
    private int id;
    private String nama;
    private String nik;
    private String noTelepon;
    private String alamat;
    private int nomorAntrean;
    private String status;
    private String tanggalDaftar;
    private String namaDokter;
    
    // Constructor
    public Pasien() {}
    
    public Pasien(String nama, String nik, String noTelepon, String alamat) {
        this.nama = nama;
        this.nik = nik;
        this.noTelepon = noTelepon;
        this.alamat = alamat;
        this.status = "MENUNGGU";
    }
    
    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    
    public String getNik() { return nik; }
    public void setNik(String nik) { this.nik = nik; }
    
    public String getNoTelepon() { return noTelepon; }
    public void setNoTelepon(String noTelepon) { this.noTelepon = noTelepon; }
    
    public String getAlamat() { return alamat; }
    public void setAlamat(String alamat) { this.alamat = alamat; }
    
    public int getNomorAntrean() { return nomorAntrean; }
    public void setNomorAntrean(int nomorAntrean) { this.nomorAntrean = nomorAntrean; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getTanggalDaftar() { return tanggalDaftar; }
    public void setTanggalDaftar(String tanggalDaftar) { this.tanggalDaftar = tanggalDaftar; }
    
    public String getNamaDokter() { return namaDokter; }
    public void setNamaDokter(String namaDokter) { this.namaDokter = namaDokter; }
    
    @Override
    public String toString() {
        return nama + " (No: " + nomorAntrean + ")";
    }
}