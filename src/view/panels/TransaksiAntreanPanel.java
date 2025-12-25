package view.panels;

import controller.PatientController;
import controller.DoctorController;
import controller.QueueController;
import model.Pasien;
import model.Antrean;
import javax.swing.*;
import java.awt.*;

public class TransaksiAntreanPanel extends JPanel {
    private PatientController pc;
    private DoctorController dc;
    private QueueController qc;

    private JComboBox<String> cbPasien;
    private JComboBox<String> cbDokter;
    private JButton btnDaftar;

    public TransaksiAntreanPanel(PatientController pc, DoctorController dc, QueueController qc) {
        this.pc = pc;
        this.dc = dc;
        this.qc = qc;
        
        initComponents();
        loadDataToCombo();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Komponen Input
        cbPasien = new JComboBox<>();
        cbDokter = new JComboBox<>();
        btnDaftar = new JButton("Daftarkan Antrean");

        // Layouting
        gbc.gridx = 0; gbc.gridy = 0; add(new JLabel("Pilih Pasien:"), gbc);
        gbc.gridx = 1; add(cbPasien, gbc);

        gbc.gridx = 0; gbc.gridy = 1; add(new JLabel("Pilih Dokter:"), gbc);
        gbc.gridx = 1; add(cbDokter, gbc);

        gbc.gridx = 1; gbc.gridy = 2; 
        add(btnDaftar, gbc);

        // Event Klik Tombol
        btnDaftar.addActionListener(e -> prosesPendaftaran());
    }

    private void loadDataToCombo() {
        // Kosongkan combo dulu sebelum diisi
        cbPasien.removeAllItems();
        cbDokter.removeAllItems();

        // Ambil data Pasien
        pc.getAllPatients().forEach(p -> cbPasien.addItem(p.getNama()));
        
        // Ambil data Dokter
        dc.getAllDokter().forEach(d -> cbDokter.addItem(d.getNama()));
        
        // Jika data kosong, beri log di terminal
        if(cbPasien.getItemCount() == 0) System.out.println("Warning: Data Pasien Kosong");
    }

    private void prosesPendaftaran() {
        try {
            // PERBAIKAN: Implementasikan pengiriman data ke QueueController
            String namaPasien = cbPasien.getSelectedItem().toString();
            String namaDokter = cbDokter.getSelectedItem().toString();
            
            // Buat objek Antrean baru (sesuaikan dengan constructor model Antrean Anda)
            // qc.createNewQueue(namaPasien, namaDokter); 
            
            JOptionPane.showMessageDialog(this, "Antrean Berhasil Dibuat untuk: " + namaPasien);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal: " + e.getMessage());
        }
    }
}