package controller;

import api.PasienApiClient;
import model.Pasien;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class PatientController {
    private final PasienApiClient apiClient = new PasienApiClient();

    // Pastikan nama method ini "getAllPatients" agar sesuai dengan MasterDataPanel
    public List<Pasien> getAllPatients() {
        try {
            return apiClient.getAllPasien(); // Memanggil fungsi GET di ApiClient
        } catch (Exception e) {
            System.err.println("Error PatientController: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Method untuk menambah pasien
    public void addPasien(String nama, String nik, DefaultTableModel tableModel) {
        try {
            Pasien baru = new Pasien(0, nama, nik);
            boolean sukses = apiClient.addPasien(baru); // Memanggil fungsi POST di ApiClient
            
            if (sukses) {
                // Opsional: Anda bisa menambahkan log atau pesan sukses di sini
                System.out.println("Berhasil menambah pasien via API");
            }
        } catch (Exception e) {
            System.err.println("Error saat menambah pasien: " + e.getMessage());
        }
    }

        public void updatePasien(int id, String nama, String nik) {
        Pasien p = new Pasien(id, nama, nik);
        if(apiClient.updatePasien(p)) {
            JOptionPane.showMessageDialog(null, "Data berhasil diupdate!");
        }
    }

    public void deletePasien(int id) {
        if(apiClient.deletePasien(id)) {
            JOptionPane.showMessageDialog(null, "Data berhasil dihapus!");
        }
    }
}