package model;

public class Dokter {
    private int id;
    private String nama;
    private String spesialisasi;
    private String jadwalPraktik;
    private String noTelepon;
    private boolean aktif;
    
    // Constructor
    public Dokter() {}
    
    public Dokter(String nama, String spesialisasi, String jadwalPraktik) {
        this.nama = nama;
        this.spesialisasi = spesialisasi;
        this.jadwalPraktik = jadwalPraktik;
        this.aktif = true;
    }
    
    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    
    public String getSpesialisasi() { return spesialisasi; }
    public void setSpesialisasi(String spesialisasi) { this.spesialisasi = spesialisasi; }
    
    public String getJadwalPraktik() { return jadwalPraktik; }
    public void setJadwalPraktik(String jadwalPraktik) { this.jadwalPraktik = jadwalPraktik; }
    
    public String getNoTelepon() { return noTelepon; }
    public void setNoTelepon(String noTelepon) { this.noTelepon = noTelepon; }
    
    public boolean isAktif() { return aktif; }
    public void setAktif(boolean aktif) { this.aktif = aktif; }
    
    @Override
    public String toString() {
        return nama + " - " + spesialisasi;
    }
}