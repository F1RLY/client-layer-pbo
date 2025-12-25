package view.panels;

import controller.PatientController;
import controller.DoctorController;
import model.Pasien;
import model.Dokter;
import worker.BaseWorker;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class MasterDataPanel extends JPanel {
    private String tipe;
    private PatientController pc;
    private DoctorController dc;
    private JTable table;
    private DefaultTableModel tableModel;
    
    private JTextField txtField1;
    private JTextField txtField2;
    private JLabel lblIdValue; // Untuk menyimpan ID yang sedang dipilih secara tersembunyi

    public MasterDataPanel(String tipe, Object controller) {
        this.tipe = tipe;
        
        // Inisialisasi controller berdasarkan tipe yang dikirim
        if (tipe.equals("PASIEN")) {
            this.pc = (PatientController) controller;
        } else {
            this.dc = (DoctorController) controller;
        }
        
        initComponents();
        loadData(); // Ambil data saat panel pertama kali dimunculkan
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // --- 1. BAGIAN ATAS (HEADER + FORM INPUT) ---
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        // Panel Judul & Tombol Refresh
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.add(new JLabel("<html><h2>Kelola Data " + tipe + "</h2></html>"));
        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> loadData());
        headerPanel.add(btnRefresh);

        // Panel Form Input (Nama dan NIK/Spesialis)
        JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblIdValue = new JLabel("0"); // Simpan ID di sini (0 berarti data baru)
        txtField1 = new JTextField(15);
        txtField2 = new JTextField(15);
        
        JButton btnAdd = new JButton("Tambah");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Hapus");
        btnDelete.setBackground(new Color(255, 100, 100)); // Warna merah

        // Penamaan Label berdasarkan Tipe
        formPanel.add(new JLabel(tipe.equals("PASIEN") ? "Nama:" : "Nama Dokter:"));
        formPanel.add(txtField1);
        formPanel.add(new JLabel(tipe.equals("PASIEN") ? "NIK:" : "Spesialis:"));
        formPanel.add(txtField2);
        
        formPanel.add(btnAdd);
        formPanel.add(btnUpdate);
        formPanel.add(btnDelete);

        topPanel.add(headerPanel);
        topPanel.add(formPanel);

        // --- 2. BAGIAN TENGAH (TABEL DATA) ---
        String[] columns = (tipe.equals("PASIEN")) ? 
            new String[]{"ID", "Nama Pasien", "NIK"} : 
            new String[]{"ID", "Nama Dokter", "Spesialis"};
            
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; } 
        };
        table = new JTable(tableModel);
        
        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // --- 3. LOGIKA INTERAKSI & EVENT HANDLING ---

        // A. KLIK TABEL (Auto-fill data ke form)
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    lblIdValue.setText(table.getValueAt(row, 0).toString());
                    txtField1.setText(table.getValueAt(row, 1).toString());
                    txtField2.setText(table.getValueAt(row, 2).toString());
                }
            }
        });

        // B. TOMBOL TAMBAH
        btnAdd.addActionListener(e -> {
            String val1 = txtField1.getText().trim();
            String val2 = txtField2.getText().trim();
            
            if (val1.isEmpty() || val2.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Isi semua kolom dulu!");
                return;
            }

            if (tipe.equals("PASIEN")) {
                pc.addPasien(val1, val2, tableModel);
            } else {
                dc.addDoctor(val1, val2, tableModel);
            }
            loadData(); // Refresh tabel
            clearFields(); // Kosongkan input
        });

        // C. TOMBOL UPDATE
        btnUpdate.addActionListener(e -> {
            int id = Integer.parseInt(lblIdValue.getText());
            if (id == 0) {
                JOptionPane.showMessageDialog(this, "Pilih data di tabel yang mau di-update!");
                return;
            }

            String val1 = txtField1.getText().trim();
            String val2 = txtField2.getText().trim();

            if (tipe.equals("PASIEN")) {
                pc.updatePasien(id, val1, val2);
            } else {
                dc.updateDoctor(id, val1, val2);
            }
            loadData();
            clearFields();
        });

        // D. TOMBOL HAPUS (DELETE)
        btnDelete.addActionListener(e -> {
            int id = Integer.parseInt(lblIdValue.getText());
            if (id == 0) {
                JOptionPane.showMessageDialog(this, "Pilih data di tabel yang mau dihapus!");
                return;
            }

            int konfirmasi = JOptionPane.showConfirmDialog(this, "Yakin hapus data ID " + id + "?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (konfirmasi == JOptionPane.YES_OPTION) {
                if (tipe.equals("PASIEN")) {
                    pc.deletePasien(id);
                } else {
                    dc.deleteDoctor(id);
                }
                loadData();
                clearFields();
            }
        });
    }

    // Fungsi untuk mengosongkan inputan form
    private void clearFields() {
        lblIdValue.setText("0");
        txtField1.setText("");
        txtField2.setText("");
        table.clearSelection();
    }

    // Fungsi Load Data menggunakan Worker (Background Thread)
    public void loadData() {
        tableModel.setRowCount(0); // Kosongkan tabel
        
        BaseWorker<Object> dataWorker = new BaseWorker<>(
            () -> {
                // Background Task
                if (tipe.equals("PASIEN")) {
                    return (List<Object>) (List<?>) pc.getAllPatients();
                } else {
                    return (List<Object>) (List<?>) dc.getAllDokter();
                }
            },
            (dataList) -> {
                // UI Task (Selesai ambil data)
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
                JOptionPane.showMessageDialog(this, "Gagal memuat data: " + error.getMessage());
            }
        );

        dataWorker.execute();
    }
}