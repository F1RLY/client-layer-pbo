package model;

import com.google.gson.annotations.SerializedName;
import model.Enums.StatusAntrean;

public class Antrean extends BaseEntity {
    private String nomor;
    
    @SerializedName("p_nama") // Menangkap alias dari SQL PHP
    private String namaPasien;
    
    @SerializedName("d_nama") // Menangkap alias dari SQL PHP
    private String namaDokter;
    
    private StatusAntrean status;

    public Antrean() {} 

    public String getNomor() { return nomor; }
    public String getNamaPasien() { return namaPasien; }
    public String getNamaDokter() { return namaDokter; }
    public StatusAntrean getStatus() { return status; }
}