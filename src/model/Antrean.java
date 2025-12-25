package model;

import model.Enums.StatusAntrean;

public class Antrean extends BaseEntity {
    private String nomor;
    private Pasien pasien;
    private Dokter dokter;
    private StatusAntrean status;

    public Antrean(Integer id, String nomor, Pasien pasien, Dokter dokter) {
        this.id = id;
        this.nomor = nomor;
        this.pasien = pasien;
        this.dokter = dokter;
        this.status = StatusAntrean.MENUNGGU; // Default saat dibuat
    }

    // Getters
    public String getNomor() { return nomor; }
    public Pasien getPasien() { return pasien; }
    public Dokter getDokter() { return dokter; }
    public StatusAntrean getStatus() { return status; }

    // Setters
    public void setStatus(StatusAntrean status) { this.status = status; }
}