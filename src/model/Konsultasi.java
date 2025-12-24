package model;

import java.util.Date;

public class Konsultasi {
    private int id;
    private Pasien pasien;
    private Dokter dokter;
    private Date tanggal;
    private String keluhan;
    private String diagnosis;
    
    // Constructor, getter, setter
    public Konsultasi() {}
    
    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public Pasien getPasien() { return pasien; }
    public void setPasien(Pasien pasien) { this.pasien = pasien; }
    
    public Dokter getDokter() { return dokter; }
    public void setDokter(Dokter dokter) { this.dokter = dokter; }
    
    public Date getTanggal() { return tanggal; }
    public void setTanggal(Date tanggal) { this.tanggal = tanggal; }
    
    public String getKeluhan() { return keluhan; }
    public void setKeluhan(String keluhan) { this.keluhan = keluhan; }
    
    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
}