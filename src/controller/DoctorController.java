package controller;

import api.DoctorApiClient;
import model.Dokter;
import java.util.ArrayList;
import java.util.List;

public class DoctorController {
    private final DoctorApiClient apiClient = new DoctorApiClient();

    public List<Dokter> getAllDokter() {
        try {
            return apiClient.findAll();
        } catch (Exception e) {
            System.err.println("Error DoctorController: " + e.getMessage());
            return new ArrayList<>(); // Kembalikan list kosong jika gagal
        }
    }
}