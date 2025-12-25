package controller;

import api.PasienApiClient;
import model.Pasien;
import java.util.ArrayList;
import java.util.List;

public class PatientController {
    private final PasienApiClient apiClient = new PasienApiClient();

    public List<Pasien> getAllPatients() {
        try {
            return apiClient.findAll();
        } catch (Exception e) {
            System.err.println("Error PatientController: " + e.getMessage());
            return new ArrayList<>(); // Kembalikan list kosong jika gagal
        }
    }
    
    public void addPatient(Pasien p) throws Exception {
        apiClient.create(p);
    }
}