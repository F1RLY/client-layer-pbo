package model;

public class Dokter {
    private int id;
    private String nama;
    private String spesialis;
    private String noTelp;
    private String alamat;
    
    // Constructor
    public Dokter() {}
    
    public Dokter(String nama, String spesialis, String noTelp, String alamat) {
        this.nama = nama;
        this.spesialis = spesialis;
        this.noTelp = noTelp;
        this.alamat = alamat;
    }
    
    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    
    public String getSpesialis() { return spesialis; }
    public void setSpesialis(String spesialis) { this.spesialis = spesialis; }
    
    public String getNoTelp() { return noTelp; }
    public void setNoTelp(String noTelp) { this.noTelp = noTelp; }
    
    public String getAlamat() { return alamat; }
    public void setAlamat(String alamat) { this.alamat = alamat; }
}