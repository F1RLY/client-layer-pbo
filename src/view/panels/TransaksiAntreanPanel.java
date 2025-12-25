package view.panels;

import controller.PatientController;
import controller.DoctorController;
import controller.QueueController;
import model.Pasien;
import model.Dokter;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TransaksiAntreanPanel extends JPanel {
    private PatientController pc;
    private DoctorController dc;
    private QueueController qc;

    // Menggunakan JComboBox objek agar bisa menyimpan ID secara tersembunyi
    private JComboBox<Pasien> cbPasien;
    private JComboBox<Dokter> cbDokter;
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

        // Inisialisasi ComboBox dengan tipe Model
        cbPasien = new JComboBox<>();
        cbDokter = new JComboBox<>();
        btnDaftar = new JButton("Daftarkan Antrean");
        btnDaftar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnDaftar.setBackground(new Color(52, 152, 219));
        btnDaftar.setForeground(Color.WHITE);

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

    public void loadDataToCombo() {
        cbPasien.removeAllItems();
        cbDokter.removeAllItems();

        // Load Pasien
        List<Pasien> pasiens = pc.getAllPatients();
        for (Pasien p : pasiens) {
            cbPasien.addItem(p); // Memasukkan seluruh objek Pasien
        }
        
        // Load Dokter
        List<Dokter> dokters = dc.getAllDokter();
        for (Dokter d : dokters) {
            cbDokter.addItem(d); // Memasukkan seluruh objek Dokter
        }
        
        if(cbPasien.getItemCount() == 0) System.out.println("Warning: Data Pasien Kosong");
    }

    private void prosesPendaftaran() {
        try {
            // Ambil objek terpilih
            Pasien pasienTerpilih = (Pasien) cbPasien.getSelectedItem();
            Dokter dokterTerpilih = (Dokter) cbDokter.getSelectedItem();
            
            if (pasienTerpilih == null || dokterTerpilih == null) {
                JOptionPane.showMessageDialog(this, "Silakan pilih Pasien dan Dokter!");
                return;
            }

            // Panggil method addAntrean di QueueController yang sudah kita buat tadi
            qc.addAntrean(pasienTerpilih, dokterTerpilih);
            
            // Log sukses sederhana (karena pesan sukses sudah ada di Controller)
            System.out.println("Pendaftaran dikirim untuk ID Pasien: " + pasienTerpilih.getId());
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal: " + e.getMessage());
        }
    }
}