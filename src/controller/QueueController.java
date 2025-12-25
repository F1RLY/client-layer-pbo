package controller;

import model.Antrean;
import model.Pasien;
import model.Dokter;
import api.AntreanApiClient;
import java.util.List;
import javax.swing.JOptionPane;

public class QueueController {
    private final AntreanApiClient apiClient = new AntreanApiClient();
    
    // Kita butuh controller lain untuk membantu mengambil data dropdown di UI
    private final PatientController pc = new PatientController();
    private final DoctorController dc = new DoctorController();

    /**
     * 1. Mengambil data antrean yang statusnya 'CALLING' untuk ditampilkan di Monitor
     */
    public Antrean getCurrentCalling() {
        try {
            return apiClient.fetchCurrentCalling();
        } catch (Exception e) {
            System.err.println("Queue Error (fetch): " + e.getMessage());
            return null;
        }
    }

    /**
     * 2. Mendaftarkan antrean baru (digunakan di TransaksiAntreanPanel)
     */
    public void addAntrean(Pasien pasien, Dokter dokter) {
        if (pasien == null || dokter == null) {
            JOptionPane.showMessageDialog(null, "Silakan pilih Pasien dan Dokter terlebih dahulu!");
            return;
        }

        try {
            // Mengirim ID Pasien dan ID Dokter ke API
            boolean sukses = apiClient.saveAntrean(pasien.getId(), dokter.getId());
            
            if (sukses) {
                JOptionPane.showMessageDialog(null, "Antrean Berhasil Dibuat!");
            } else {
                JOptionPane.showMessageDialog(null, "Gagal menyimpan antrean ke server. Cek koneksi API.");
            }
        } catch (Exception e) {
            System.err.println("Queue Error (add): " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan sistem: " + e.getMessage());
        }
    }

    /**
     * 3. Memanggil antrean berikutnya (Mengubah status dari MENUNGGU ke CALLING)
     * Digunakan oleh perawat/admin
     */
    public void panggilBerikutnya() {
        try {
            boolean sukses = apiClient.updateStatusNext(); 
            if (sukses) {
                JOptionPane.showMessageDialog(null, "Berhasil memanggil antrean berikutnya!");
            } else {
                JOptionPane.showMessageDialog(null, "Tidak ada antrean yang menunggu.");
            }
        } catch (Exception e) {
            System.err.println("Queue Error: " + e.getMessage());
        }
    }
    /**
     * 4. Fungsi pembantu untuk memuat data Pasien ke JComboBox
     */
    public List<Pasien> getListPasien() {
        return pc.getAllPatients();
    }

    /**
     * 5. Fungsi pembantu untuk memuat data Dokter ke JComboBox
     */
    public List<Dokter> getListDokter() {
        return dc.getAllDokter();
    }
}