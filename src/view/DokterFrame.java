// File: view/DokterFrame.java
package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import controller.DokterController;
import model.Dokter;
import model.User;

public class DokterFrame extends JFrame {
    private DokterController controller;
    private JTable table;
    private DefaultTableModel tableModel;
    
    public DokterFrame(User user) {
        controller = new DokterController();
        initComponents();
        setupFrame();
        loadData();
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Toolbar
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAdd = new JButton("Tambah");
        JButton btnEdit = new JButton("Edit");
        JButton btnDelete = new JButton("Hapus");
        JButton btnRefresh = new JButton("Refresh");
        
        btnAdd.addActionListener(e -> addDokter());
        btnEdit.addActionListener(e -> editDokter());
        btnDelete.addActionListener(e -> deleteDokter());
        btnRefresh.addActionListener(e -> loadData());
        
        toolbar.add(btnAdd);
        toolbar.add(btnEdit);
        toolbar.add(btnDelete);
        toolbar.add(btnRefresh);
        
        mainPanel.add(toolbar, BorderLayout.NORTH);
        
        // Table
        String[] columns = {"ID", "Kode", "Nama", "Spesialisasi", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private void setupFrame() {
        setTitle("Data Dokter");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    
    private void loadData() {
        tableModel.setRowCount(0);
        
        for (Dokter dokter : controller.readAll()) {
            Object[] row = {
                dokter.getId(),
                dokter.getKodeDokter(),
                dokter.getNama(),
                dokter.getSpesialisasi(),
                dokter.getStatus()
            };
            tableModel.addRow(row);
        }
    }
    
    private void addDokter() {
        new DokterFormDialog(this, null).setVisible(true);
        loadData();
    }
    
    private void editDokter() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data dokter");
            return;
        }
        
        Integer id = (Integer) tableModel.getValueAt(selectedRow, 0);
        Dokter dokter = controller.read(id);
        
        if (dokter != null) {
            new DokterFormDialog(this, dokter).setVisible(true);
            loadData();
        }
    }
    
    private void deleteDokter() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data dokter");
            return;
        }
        
        Integer id = (Integer) tableModel.getValueAt(selectedRow, 0);
        String nama = (String) tableModel.getValueAt(selectedRow, 2);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Hapus dokter: " + nama + "?",
            "Konfirmasi",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (controller.delete(id)) {
                JOptionPane.showMessageDialog(this, "Berhasil dihapus");
                loadData();
            }
        }
    }
}

class DokterFormDialog extends JDialog {
    private Dokter dokter;
    private DokterController controller;
    
    private JTextField txtKode, txtNama, txtSpesialisasi;
    private JComboBox<String> cmbStatus;
    
    public DokterFormDialog(JFrame parent, Dokter dokter) {
        super(parent, "Form Dokter", true);
        this.dokter = dokter;
        this.controller = new DokterController();
        
        initComponents();
        setupDialog();
        
        if (dokter != null) {
            loadData();
        }
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        
        mainPanel.add(new JLabel("Kode Dokter:"));
        txtKode = new JTextField();
        mainPanel.add(txtKode);
        
        mainPanel.add(new JLabel("Nama:"));
        txtNama = new JTextField();
        mainPanel.add(txtNama);
        
        mainPanel.add(new JLabel("Spesialisasi:"));
        txtSpesialisasi = new JTextField();
        mainPanel.add(txtSpesialisasi);
        
        mainPanel.add(new JLabel("Status:"));
        cmbStatus = new JComboBox<>(new String[]{"AKTIF", "NONAKTIF"});
        mainPanel.add(cmbStatus);
        
        JPanel buttonPanel = new JPanel();
        JButton btnSave = new JButton("Simpan");
        JButton btnCancel = new JButton("Batal");
        
        btnSave.addActionListener(e -> saveData());
        btnCancel.addActionListener(e -> dispose());
        
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void setupDialog() {
        setSize(300, 200);
        setLocationRelativeTo(getParent());
    }
    
    private void loadData() {
        txtKode.setText(dokter.getKodeDokter());
        txtNama.setText(dokter.getNama());
        txtSpesialisasi.setText(dokter.getSpesialisasi());
        cmbStatus.setSelectedItem(dokter.getStatus());
    }
    
    private void saveData() {
        // Validasi
        if (txtKode.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Kode harus diisi");
            return;
        }
        
        if (txtNama.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama harus diisi");
            return;
        }
        
        // Simpan
        if (dokter == null) {
            dokter = new Dokter();
        }
        
        dokter.setKodeDokter(txtKode.getText().trim());
        dokter.setNama(txtNama.getText().trim());
        dokter.setSpesialisasi(txtSpesialisasi.getText().trim());
        dokter.setStatus((String) cmbStatus.getSelectedItem());
        
        boolean success;
        if (dokter.getId() == null) {
            success = controller.create(dokter);
        } else {
            success = controller.update(dokter);
        }
        
        if (success) {
            JOptionPane.showMessageDialog(this, "Data disimpan");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan");
        }
    }
}