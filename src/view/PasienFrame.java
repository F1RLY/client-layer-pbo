// File: view/PasienFrame.java
package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import controller.PasienController;
import model.Pasien;
import model.User;

public class PasienFrame extends JFrame {
    private PasienController controller;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JButton btnAdd, btnEdit, btnDelete, btnRefresh;
    
    public PasienFrame(User user) {
        controller = new PasienController();
        initComponents();
        setupFrame();
        loadData();
    }
    
    private void initComponents() {
        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Cari:"));
        txtSearch = new JTextField(20);
        searchPanel.add(txtSearch);
        
        JButton btnSearch = new JButton("Cari");
        btnSearch.addActionListener(e -> searchData());
        searchPanel.add(btnSearch);
        
        mainPanel.add(searchPanel, BorderLayout.NORTH);
        
        // Table
        String[] columns = {"ID", "Nama", "NIK", "Telepon", "Alamat"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = new JPanel();
        btnAdd = new JButton("Tambah");
        btnEdit = new JButton("Edit");
        btnDelete = new JButton("Hapus");
        btnRefresh = new JButton("Refresh");
        
        btnAdd.addActionListener(e -> addPasien());
        btnEdit.addActionListener(e -> editPasien());
        btnDelete.addActionListener(e -> deletePasien());
        btnRefresh.addActionListener(e -> loadData());
        
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnRefresh);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void setupFrame() {
        setTitle("Data Pasien");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    
    private void loadData() {
        tableModel.setRowCount(0); // Clear table
        
        for (Pasien pasien : controller.readAll()) {
            Object[] row = {
                pasien.getId(),
                pasien.getNama(),
                pasien.getNik(),
                pasien.getNoTelepon(),
                pasien.getAlamat()
            };
            tableModel.addRow(row);
        }
    }
    
    private void searchData() {
        String keyword = txtSearch.getText().trim();
        if (keyword.isEmpty()) {
            loadData();
            return;
        }
        
        tableModel.setRowCount(0);
        for (Pasien pasien : controller.searchByName(keyword)) {
            Object[] row = {
                pasien.getId(),
                pasien.getNama(),
                pasien.getNik(),
                pasien.getNoTelepon(),
                pasien.getAlamat()
            };
            tableModel.addRow(row);
        }
    }
    
    private void addPasien() {
        // Open dialog form untuk tambah pasien
        JDialog dialog = new PasienFormDialog(this, null);
        dialog.setVisible(true);
        loadData();
    }
    
    private void editPasien() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Pilih pasien yang akan diedit!",
                "Peringatan",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Integer id = (Integer) tableModel.getValueAt(selectedRow, 0);
        Pasien pasien = controller.read(id);
        
        if (pasien != null) {
            JDialog dialog = new PasienFormDialog(this, pasien);
            dialog.setVisible(true);
            loadData();
        }
    }
    
    private void deletePasien() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Pilih pasien yang akan dihapus!",
                "Peringatan",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Integer id = (Integer) tableModel.getValueAt(selectedRow, 0);
        String nama = (String) tableModel.getValueAt(selectedRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Hapus pasien: " + nama + "?",
            "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (controller.delete(id)) {
                JOptionPane.showMessageDialog(this, "Data berhasil dihapus");
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menghapus data");
            }
        }
    }
}

// PasienFormDialog.java (inner class atau separate file)
class PasienFormDialog extends JDialog {
    private Pasien pasien;
    private PasienController controller;
    
    private JTextField txtNama, txtNik, txtTelepon;
    private JTextArea txtAlamat;
    private JButton btnSave, btnCancel;
    
    public PasienFormDialog(JFrame parent, Pasien pasien) {
        super(parent, "Form Pasien", true);
        this.pasien = pasien;
        this.controller = new PasienController();
        
        initComponents();
        setupDialog();
        
        if (pasien != null) {
            loadData();
        }
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Nama
        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(new JLabel("Nama:"), gbc);
        gbc.gridx = 1;
        txtNama = new JTextField(20);
        mainPanel.add(txtNama, gbc);
        
        // NIK
        gbc.gridx = 0; gbc.gridy = 1;
        mainPanel.add(new JLabel("NIK:"), gbc);
        gbc.gridx = 1;
        txtNik = new JTextField(20);
        mainPanel.add(txtNik, gbc);
        
        // Telepon
        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(new JLabel("Telepon:"), gbc);
        gbc.gridx = 1;
        txtTelepon = new JTextField(20);
        mainPanel.add(txtTelepon, gbc);
        
        // Alamat
        gbc.gridx = 0; gbc.gridy = 3;
        mainPanel.add(new JLabel("Alamat:"), gbc);
        gbc.gridx = 1;
        txtAlamat = new JTextArea(3, 20);
        mainPanel.add(new JScrollPane(txtAlamat), gbc);
        
        // Buttons
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel();
        btnSave = new JButton("Simpan");
        btnCancel = new JButton("Batal");
        
        btnSave.addActionListener(e -> saveData());
        btnCancel.addActionListener(e -> dispose());
        
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        mainPanel.add(buttonPanel, gbc);
        
        add(mainPanel);
    }
    
    private void setupDialog() {
        setSize(400, 300);
        setLocationRelativeTo(getParent());
        setResizable(false);
    }
    
    private void loadData() {
        if (pasien != null) {
            txtNama.setText(pasien.getNama());
            txtNik.setText(pasien.getNik());
            txtTelepon.setText(pasien.getNoTelepon());
            txtAlamat.setText(pasien.getAlamat());
        }
    }
    
    private void saveData() {
        // Validasi
        if (txtNama.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama harus diisi!");
            txtNama.requestFocus();
            return;
        }
        
        if (txtNik.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "NIK harus diisi!");
            txtNik.requestFocus();
            return;
        }
        
        // Save data
        if (pasien == null) {
            // Create new
            pasien = new Pasien();
        }
        
        pasien.setNama(txtNama.getText().trim());
        pasien.setNik(txtNik.getText().trim());
        pasien.setNoTelepon(txtTelepon.getText().trim());
        pasien.setAlamat(txtAlamat.getText().trim());
        
        boolean success;
        if (pasien.getId() == null) {
            success = controller.create(pasien);
        } else {
            success = controller.update(pasien);
        }
        
        if (success) {
            JOptionPane.showMessageDialog(this, "Data berhasil disimpan");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan data");
        }
    }
}