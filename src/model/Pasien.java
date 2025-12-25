package model;

public class Pasien extends BaseEntity {
    private String nama;
    private String nik;

    public Pasien(Integer id, String nama, String nik) {
        this.id = id;
        this.nama = nama;
        this.nik = nik;
    }

    public String getNama() { return nama; }
    public String getNik() { return nik; }
    
    @Override
    public String toString() { return nama; } // Agar bagus saat tampil di ComboBox

}
