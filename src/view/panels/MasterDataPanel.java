package view.panels;

import controller.PatientController;
import controller.DoctorController;
import model.Pasien;
import model.Dokter;
import worker.BaseWorker; // Pastikan import ini ada

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MasterDataPanel extends JPanel {
    private String tipe;
    private PatientController pc;
    private DoctorController dc;
    private JTable table;
    private DefaultTableModel tableModel;

    public MasterDataPanel(String tipe, Object controller) {
        this.tipe = tipe;
        
        // Memisahkan controller berdasarkan tipe panel
        if (tipe.equals("PASIEN")) {
            this.pc = (PatientController) controller;
        } else {
            this.dc = (DoctorController) controller;
        }
        
        initComponents();
        loadData(); // Memanggil data saat pertama kali panel dibuka
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // 1. Bagian Atas (Header)
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.add(new JLabel("Kelola Data " + tipe));
        
        JButton btnRefresh = new JButton("Refresh Data");
        btnRefresh.addActionListener(e -> loadData());
        header.add(btnRefresh);

        // 2. Setting Kolom Tabel
        String[] columns = (tipe.equals("PASIEN")) ? 
            new String[]{"ID", "Nama Pasien", "NIK"} : 
            new String[]{"ID", "Nama Dokter", "Spesialis"};
            
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        
        add(header, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public void loadData() {
        // Bersihkan tabel sebelum mengisi data baru
        tableModel.setRowCount(0);
        
        // Gunakan BaseWorker agar UI tidak freeze (macet)
        BaseWorker<Object> dataWorker = new BaseWorker<>(
            () -> {
                // TUGAS BACKGROUND: Ambil data dari list (Controller)
                if (tipe.equals("PASIEN")) {
                    return (List<Object>) (List<?>) pc.getAllPatients();
                } else {
                    return (List<Object>) (List<?>) dc.getAllDokter();
                }
            },
            (dataList) -> {
                // TUGAS UI: Masukkan hasil ke tabel
                for (Object obj : dataList) {
                    if (tipe.equals("PASIEN")) {
                        Pasien p = (Pasien) obj;
                        tableModel.addRow(new Object[]{p.getId(), p.getNama(), p.getNik()});
                    } else {
                        Dokter d = (Dokter) obj;
                        tableModel.addRow(new Object[]{d.getId(), d.getNama(), d.getSpesialisasi()});
                    }
                }
            },
            (error) -> {
                // TUGAS UI: Jika error (seperti connection refused)
                JOptionPane.showMessageDialog(this, "Gagal memuat data: " + error.getMessage());
            }
        );

        dataWorker.execute(); // Jalankan proses di background
    }
}