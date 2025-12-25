package controller;

import model.Antrean;
import model.Pasien;
import model.Dokter;
import api.AntreanApiClient;
import java.util.List;
import javax.swing.JOptionPane;

public class QueueController {
    private final AntreanApiClient apiClient = new AntreanApiClient();
    // Kita butuh controller lain untuk mengisi dropdown di UI
    private final PatientController pc = new PatientController();
    private final DoctorController dc = new DoctorController();

    // 1. Ambil Antrean yang sedang dipanggil (Method Anda sebelumnya)
    public Antrean getCurrentCalling() {
        try {
            return apiClient.fetchCurrentCalling();
        } catch (Exception e) {
            System.err.println("Queue Error (fetch): " + e.getMessage());
            return null;
        }
    }

    // 2. Tambah Antrean Baru (Menghubungkan UI ke ApiClient)
    public void addAntrean(Pasien pasien, Dokter dokter) {
        if (pasien == null || dokter == null) {
            JOptionPane.showMessageDialog(null, "Pilih Pasien dan Dokter dulu!");
            return;
        }

        try {
            // Kita kirim ID pasien dan ID dokter ke ApiClient
            boolean sukses = apiClient.saveAntrean(pasien.getId(), dokter.getId());
            
            if (sukses) {
                JOptionPane.showMessageDialog(null, "Antrean Berhasil Dibuat!");
            } else {
                JOptionPane.showMessageDialog(null, "Gagal menyimpan antrean ke server.");
            }
        } catch (Exception e) {
            System.err.println("Queue Error (add): " + e.getMessage());
        }
    }

    // 3. Helper untuk mengisi ComboBox di UI
    public List<Pasien> getListPasien() {
        return pc.getAllPatients();
    }

    public List<Dokter> getListDokter() {
        return dc.getAllDokter();
    }
}