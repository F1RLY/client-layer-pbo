package controller;

import api.DoctorApiClient;
import model.Dokter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class DoctorController {
    private final DoctorApiClient apiClient = new DoctorApiClient();

    // 1. Mengambil semua data dokter untuk ditampilkan di tabel
    public List<Dokter> getAllDokter() {
        try {
            return apiClient.findAll(); // Memanggil method findAll di DoctorApiClient
        } catch (Exception e) {
            System.err.println("Error DoctorController (GET): " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // 2. Menambah dokter baru
    public void addDoctor(String nama, String spesialisasi, DefaultTableModel tableModel) {
        if (nama.isEmpty() || spesialisasi.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nama dan Spesialisasi harus diisi!");
            return;
        }

        Dokter baru = new Dokter(0, nama, spesialisasi);
        boolean sukses = apiClient.addDoctor(baru);

        if (sukses) {
            JOptionPane.showMessageDialog(null, "Dokter berhasil ditambahkan!");
        } else {
            JOptionPane.showMessageDialog(null, "Gagal menambah dokter ke server.");
        }
    }

    // 3. Mengupdate data dokter
    public void updateDoctor(int id, String nama, String spesialisasi) {
        if (id == 0 || nama.isEmpty() || spesialisasi.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Data tidak lengkap untuk update!");
            return;
        }

        Dokter updateData = new Dokter(id, nama, spesialisasi);
        boolean sukses = apiClient.updateDoctor(updateData);

        if (sukses) {
            JOptionPane.showMessageDialog(null, "Data dokter berhasil diperbarui!");
        } else {
            JOptionPane.showMessageDialog(null, "Gagal memperbarui data dokter.");
        }
    }

    // 4. Menghapus data dokter
    public void deleteDoctor(int id) {
        if (id == 0) {
            JOptionPane.showMessageDialog(null, "Pilih dokter yang ingin dihapus!");
            return;
        }

        boolean sukses = apiClient.deleteDoctor(id);

        if (sukses) {
            JOptionPane.showMessageDialog(null, "Dokter berhasil dihapus!");
        } else {
            JOptionPane.showMessageDialog(null, "Gagal menghapus dokter.");
        }
    }
}