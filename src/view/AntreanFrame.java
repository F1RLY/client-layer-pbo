package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import controller.AntreanController;
import controller.DokterController;
import controller.PasienController;
import model.Antrean;
import model.Dokter;
import model.Pasien;
import model.User;

public class AntreanFrame extends JFrame {
    private AntreanController antreanController;
    private DokterController dokterController;
    private PasienController pasienController;
    
    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<String> cmbFilter;
    
    public AntreanFrame(User user) {
        antreanController = new AntreanController();
        dokterController = new DokterController();
        pasienController = new PasienController();
        
        initComponents();
        setupFrame();
        loadData();
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Control Panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JButton btnAdd = new JButton("Buat Antrean");
        JButton btnPanggil = new JButton("Panggil");
        JButton btnSelesai = new JButton("Selesai");
        JButton btnRefresh = new JButton("Refresh");
        
        cmbFilter = new JComboBox<>(new String[]{"SEMUA", "MENUNGGU", "DIPANGGIL", "SELESAI"});
        cmbFilter.addActionListener(e -> filterData());
        
        btnAdd.addActionListener(e -> buatAntrean());
        btnPanggil.addActionListener(e -> panggilAntrean());
        btnSelesai.addActionListener(e -> selesaikanAntrean());
        btnRefresh.addActionListener(e -> loadData());
        
        controlPanel.add(btnAdd);
        controlPanel.add(btnPanggil);
        controlPanel.add(btnSelesai);
        controlPanel.add(new JLabel("Filter:"));
        controlPanel.add(cmbFilter);
        controlPanel.add(btnRefresh);
        
        mainPanel.add(controlPanel, BorderLayout.NORTH);
        
        // Table
        String[] columns = {"No Antrean", "Pasien", "Dokter", "Status", "Waktu Masuk"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Status Panel
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblStats = new JLabel();
        statusPanel.add(lblStats);
        mainPanel.add(statusPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void setupFrame() {
        setTitle("Manajemen Antrean");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    
    private void loadData() {
        tableModel.setRowCount(0);
        
        for (Antrean antrean : antreanController.getAntreanHariIni()) {
            Object[] row = {
                antrean.getNomorAntrean(),
                antrean.getPasien().getNama(),
                antrean.getDokter().getNama(),
                antrean.getStatus(),
                antrean.getWaktuMasuk()
            };
            tableModel.addRow(row);
        }
        
        updateStats();
    }
    
    private void filterData() {
        String filter = (String) cmbFilter.getSelectedItem();
        if ("SEMUA".equals(filter)) {
            loadData();
            return;
        }
        
        tableModel.setRowCount(0);
        for (Antrean antrean : antreanController.getAntreanByStatus(filter)) {
            Object[] row = {
                antrean.getNomorAntrean(),
                antrean.getPasien().getNama(),
                antrean.getDokter().getNama(),
                antrean.getStatus(),
                antrean.getWaktuMasuk()
            };
            tableModel.addRow(row);
        }
    }
    
    private void updateStats() {
        long menunggu = antreanController.getAntreanMenunggu().size();
        long dipanggil = antreanController.getAntreanDipanggil().size();
        long selesai = antreanController.getAntreanByStatus("SELESAI").size();
        
        String stats = String.format("Menunggu: %d | Dipanggil: %d | Selesai: %d", 
            menunggu, dipanggil, selesai);
        
        // Update label
        ((JLabel)((JPanel)getContentPane().getComponent(0)).getComponent(1)).setText(stats);
    }
    
    private void buatAntrean() {
        JDialog dialog = new BuatAntreanDialog(this);
        dialog.setVisible(true);
        loadData();
    }
    
    private void panggilAntrean() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih antrean yang akan dipanggil");
            return;
        }
        
        String nomorAntrean = (String) tableModel.getValueAt(selectedRow, 0);
        String status = (String) tableModel.getValueAt(selectedRow, 3);
        
        if (!"MENUNGGU".equals(status)) {
            JOptionPane.showMessageDialog(this, "Hanya antrean dengan status MENUNGGU yang bisa dipanggil");
            return;
        }
        
        String ruangan = JOptionPane.showInputDialog(this, "Masukkan ruangan:");
        if (ruangan != null && !ruangan.trim().isEmpty()) {
            if (antreanController.panggilAntrean(nomorAntrean, ruangan)) {
                JOptionPane.showMessageDialog(this, "Antrean " + nomorAntrean + " dipanggil ke ruangan " + ruangan);
                loadData();
            }
        }
    }
    
    private void selesaikanAntrean() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih antrean yang akan diselesaikan");
            return;
        }
        
        String nomorAntrean = (String) tableModel.getValueAt(selectedRow, 0);
        String status = (String) tableModel.getValueAt(selectedRow, 3);
        
        if (!"DIPANGGIL".equals(status)) {
            JOptionPane.showMessageDialog(this, "Hanya antrean dengan status DIPANGGIL yang bisa diselesaikan");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Selesaikan antrean " + nomorAntrean + "?",
            "Konfirmasi",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (antreanController.selesaikanAntrean(nomorAntrean)) {
                JOptionPane.showMessageDialog(this, "Antrean selesai");
                loadData();
            }
        }
    }
}

class BuatAntreanDialog extends JDialog {
    private JComboBox<Pasien> cmbPasien;
    private JComboBox<Dokter> cmbDokter;
    private AntreanController antreanController;
    
    public BuatAntreanDialog(JFrame parent) {
        super(parent, "Buat Antrean Baru", true);
        antreanController = new AntreanController();
        
        initComponents();
        setupDialog();
        loadComboBoxData();
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        
        mainPanel.add(new JLabel("Pilih Pasien:"));
        cmbPasien = new JComboBox<>();
        mainPanel.add(cmbPasien);
        
        mainPanel.add(new JLabel("Pilih Dokter:"));
        cmbDokter = new JComboBox<>();
        mainPanel.add(cmbDokter);
        
        JPanel buttonPanel = new JPanel();
        JButton btnSave = new JButton("Buat Antrean");
        JButton btnCancel = new JButton("Batal");
        
        btnSave.addActionListener(e -> buatAntrean());
        btnCancel.addActionListener(e -> dispose());
        
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void setupDialog() {
        setSize(400, 150);
        setLocationRelativeTo(getParent());
    }
    
    private void loadComboBoxData() {
        PasienController pasienCtrl = new PasienController();
        DokterController dokterCtrl = new DokterController();
        
        // Load pasien
        cmbPasien.removeAllItems();
        for (Pasien pasien : pasienCtrl.readAll()) {
            cmbPasien.addItem(pasien);
        }
        
        // Load dokter aktif
        cmbDokter.removeAllItems();
        for (Dokter dokter : dokterCtrl.getAktifDokter()) {
            cmbDokter.addItem(dokter);
        }
    }
    
    private void buatAntrean() {
        Pasien selectedPasien = (Pasien) cmbPasien.getSelectedItem();
        Dokter selectedDokter = (Dokter) cmbDokter.getSelectedItem();
        
        if (selectedPasien == null || selectedDokter == null) {
            JOptionPane.showMessageDialog(this, "Pilih pasien dan dokter");
            return;
        }
        
        Antrean antrean = antreanController.buatAntreanBaru(selectedPasien, selectedDokter);
        if (antrean != null) {
            JOptionPane.showMessageDialog(this, 
                "Antrean berhasil dibuat!\nNomor: " + antrean.getNomorAntrean());
            dispose();
        }
    }
}