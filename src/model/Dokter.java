package model;

public class Dokter extends BaseEntity {
    private String nama;
    private String spesialisasi;

    public Dokter(Integer id, String nama, String spesialisasi) {
        this.id = id;
        this.nama = nama;
        this.spesialisasi = spesialisasi;
    }

    public String getNama() { return nama; }
    public String getSpesialisasi() { return spesialisasi; }
    
    @Override
    public String toString() { return nama + " (" + spesialisasi + ")"; }
}