package worker;

import model.Pasien;
import service.PasienService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class PasienWorker extends SwingWorker<List<Pasien>, Void> {
    private final PasienService service;
    private final DefaultTableModel tableModel;
    private final JLabel lblStatus; // Opsional: untuk menampilkan teks "Loading..."

    public PasienWorker(PasienService service, DefaultTableModel tableModel, JLabel lblStatus) {
        this.service = service;
        this.tableModel = tableModel;
        this.lblStatus = lblStatus;
    }

    // Proses berat dilakukan di background thread
    @Override
    protected List<Pasien> doInBackground() throws Exception {
        if (lblStatus != null) lblStatus.setText("Sedang mengambil data...");
        
        // Simulasi delay jaringan agar efek "anti-freeze" terlihat saat didemo
        Thread.sleep(1000); 
        
        // Mengambil data dari service (3-tier)
        return service.getAllPasien();
    }

    // Dijalankan di Event Dispatch Thread setelah doInBackground selesai
    @Override
    protected void done() {
        try {
            List<Pasien> pasienList = get(); // Mengambil hasil dari doInBackground
            
            // Bersihkan tabel sebelum diisi data baru
            tableModel.setRowCount(0);
            
            // Isi tabel dengan data terbaru
            for (Pasien p : pasienList) {
                tableModel.addRow(new Object[]{
                    p.getId(),
                    p.getNama(),
                    p.getNoKtp(),
                    p.getAlamat(),
                    p.getNoTelp(),
                    p.getTanggalLahir(),
                    p.getJenisKelamin()
                });
            }
            
            if (lblStatus != null) lblStatus.setText("Data berhasil dimuat.");
            
        } catch (InterruptedException | ExecutionException e) {
            // Exception Handling sesuai Bab D laporan
            JOptionPane.showMessageDialog(null, 
                "Gagal memuat data: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}