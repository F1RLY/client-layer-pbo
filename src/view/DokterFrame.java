// File: view/DokterFrame.java
package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import controller.DokterController;
import model.Dokter;
import model.User;
import java.util.List;

public class DokterFrame extends JFrame {
    private DokterController controller;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnAdd, btnEdit, btnDelete, btnRefresh;
    
    public DokterFrame(User user) {
        controller = new DokterController();
        initComponents();
        setupFrame();
        loadData();
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(5, 5));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Toolbar
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        btnAdd = new JButton("Tambah");
        btnEdit = new JButton("Edit");
        btnDelete = new JButton("Hapus");
        btnRefresh = new JButton("Refresh");
        
        // Styling buttons
        btnAdd.setBackground(new Color(0, 123, 255));
        btnAdd.setForeground(Color.WHITE);
        btnDelete.setBackground(new Color(220, 53, 69));
        btnDelete.setForeground(Color.WHITE);
        
        btnAdd.addActionListener(e -> addDokter());
        btnEdit.addActionListener(e -> editDokter());
        btnDelete.addActionListener(e -> deleteDokter());
        btnRefresh.addActionListener(e -> loadData());
        
        toolbar.add(btnAdd);
        toolbar.add(btnEdit);
        toolbar.add(btnDelete);
        toolbar.add(btnRefresh);
        
        mainPanel.add(toolbar, BorderLayout.NORTH);
        
        // Table dengan model yang lebih baik
        String[] columns = {"ID", "Kode", "Nama", "Spesialisasi", "Status", "Telepon"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Non-editable
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                // Untuk render boolean/status lebih baik
                if (columnIndex == 4) return String.class; // Status
                return Object.class;
            }
        };
        
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        
        // Set column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        table.getColumnModel().getColumn(1).setPreferredWidth(80);  // Kode
        table.getColumnModel().getColumn(2).setPreferredWidth(200); // Nama
        table.getColumnModel().getColumn(3).setPreferredWidth(150); // Spesialisasi
        table.getColumnModel().getColumn(4).setPreferredWidth(80);  // Status
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(650, 300));
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Status bar
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel statusLabel = new JLabel("Total dokter: 0");
        statusPanel.add(statusLabel);
        mainPanel.add(statusPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void setupFrame() {
        setTitle("Data Dokter - SMARS");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Add window listener untuk refresh saat dibuka
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
                loadData();
            }
        });
    }
    
    private void loadData() {
        try {
            System.out.println("\n[FRAME] Loading dokter data...");
            
            // Clear table
            tableModel.setRowCount(0);
            
            // Get data from controller
            List<Dokter> dokterList = controller.readAll();
            System.out.println("[FRAME] Controller returned: " + dokterList.size() + " items");
            
            // Add to table
            for (Dokter dokter : dokterList) {
                Object[] row = {
                    dokter.getId(),
                    dokter.getKodeDokter(),
                    dokter.getNama(),
                    dokter.getSpesialisasi(),
                    dokter.getStatus(),
                    dokter.getNoTelepon() != null ? dokter.getNoTelepon() : "-",
                    dokter.getEmail() != null ? dokter.getEmail() : "-"
                };
                tableModel.addRow(row);
            }
            
            // Update status
            updateStatusLabel(dokterList.size());
            
            // Force table refresh
            table.revalidate();
            table.repaint();
            
            System.out.println("[FRAME] Table updated with " + dokterList.size() + " rows");
            
        } catch (Exception e) {
            System.err.println("[FRAME] Error loading data: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Error memuat data: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    private void updateStatusLabel(int count) {
        // Find and update status label
        Component[] components = getContentPane().getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel) {
                Component[] subComps = ((JPanel) comp).getComponents();
                for (Component subComp : subComps) {
                    if (subComp instanceof JLabel && ((JLabel) subComp).getText().startsWith("Total dokter:")) {
                        ((JLabel) subComp).setText("Total dokter: " + count);
                        return;
                    }
                }
            }
        }
    }
    
    private void addDokter() {
        DokterFormDialog dialog = new DokterFormDialog(this, null);
        dialog.setVisible(true);
        
        // Refresh setelah dialog ditutup
        if (dialog.isDataSaved()) {
            loadData();
        }
    }
    
    private void editDokter() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Pilih dokter yang akan diedit!",
                "Peringatan",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Integer id = (Integer) tableModel.getValueAt(selectedRow, 0);
        Dokter dokter = controller.read(id);
        
        if (dokter != null) {
            DokterFormDialog dialog = new DokterFormDialog(this, dokter);
            dialog.setVisible(true);
            
            if (dialog.isDataSaved()) {
                loadData();
            }
        }
    }
    
    private void deleteDokter() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Pilih dokter yang akan dihapus!",
                "Peringatan",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Integer id = (Integer) tableModel.getValueAt(selectedRow, 0);
        String kode = (String) tableModel.getValueAt(selectedRow, 1);
        String nama = (String) tableModel.getValueAt(selectedRow, 2);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Hapus dokter:\n" +
            "Kode: " + kode + "\n" +
            "Nama: " + nama + "\n\n" +
            "Data yang dihapus tidak dapat dikembalikan!",
            "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = controller.delete(id);
            
            if (success) {
                JOptionPane.showMessageDialog(this,
                    "Dokter berhasil dihapus",
                    "Sukses",
                    JOptionPane.INFORMATION_MESSAGE);
                loadData();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Gagal menghapus dokter",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

// DokterFormDialog.java (dengan fix)
class DokterFormDialog extends JDialog {
    private Dokter dokter;
    private DokterController controller;
    private boolean dataSaved = false;
    
    private JTextField txtKode, txtNama, txtSpesialisasi, txtTelepon, txtEmail;
    private JComboBox<String> cmbStatus;
    
    public DokterFormDialog(JFrame parent, Dokter dokter) {
        super(parent, dokter == null ? "Tambah Dokter" : "Edit Dokter", true);
        this.dokter = dokter;
        this.controller = new DokterController();
        
        initComponents();
        setupDialog();
        
        if (dokter != null) {
            loadData();
        } else {
            // Set default untuk dokter baru
            cmbStatus.setSelectedItem("AKTIF");
        }
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        mainPanel.add(new JLabel("Kode Dokter*:"));
        txtKode = new JTextField(15);
        mainPanel.add(txtKode);
        
        mainPanel.add(new JLabel("Nama*:"));
        txtNama = new JTextField(15);
        mainPanel.add(txtNama);
        
        mainPanel.add(new JLabel("Spesialisasi*:"));
        txtSpesialisasi = new JTextField(15);
        mainPanel.add(txtSpesialisasi);
        
        mainPanel.add(new JLabel("No. Telepon:"));
        txtTelepon = new JTextField(15);
        mainPanel.add(txtTelepon);
        
        mainPanel.add(new JLabel("Email:"));
        txtEmail = new JTextField(15);
        mainPanel.add(txtEmail);
        
        mainPanel.add(new JLabel("Status*:"));
        cmbStatus = new JComboBox<>(new String[]{"AKTIF", "NONAKTIF"});
        mainPanel.add(cmbStatus);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        JButton btnSave = new JButton("Simpan");
        JButton btnCancel = new JButton("Batal");
        
        btnSave.addActionListener(e -> saveData());
        btnCancel.addActionListener(e -> dispose());
        
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        
        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void setupDialog() {
        setSize(400, 350);
        setLocationRelativeTo(getParent());
        setResizable(false);
    }
    
    private void loadData() {
        if (dokter != null) {
            txtKode.setText(dokter.getKodeDokter());
            txtNama.setText(dokter.getNama());
            txtSpesialisasi.setText(dokter.getSpesialisasi());
            txtTelepon.setText(dokter.getNoTelepon() != null ? dokter.getNoTelepon() : "");
            txtEmail.setText(dokter.getEmail() != null ? dokter.getEmail() : "");
            cmbStatus.setSelectedItem(dokter.getStatus());
        }
    }
    
    private void saveData() {
        try {
            // Validasi input
            if (txtKode.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Kode dokter harus diisi!", "Validasi", JOptionPane.WARNING_MESSAGE);
                txtKode.requestFocus();
                return;
            }
            
            if (txtNama.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nama dokter harus diisi!", "Validasi", JOptionPane.WARNING_MESSAGE);
                txtNama.requestFocus();
                return;
            }
            
            if (txtSpesialisasi.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Spesialisasi harus diisi!", "Validasi", JOptionPane.WARNING_MESSAGE);
                txtSpesialisasi.requestFocus();
                return;
            }
            
            // Create or update dokter
            if (dokter == null) {
                dokter = new Dokter();
            }
            
            dokter.setKodeDokter(txtKode.getText().trim());
            dokter.setNama(txtNama.getText().trim());
            dokter.setSpesialisasi(txtSpesialisasi.getText().trim());
            dokter.setNoTelepon(txtTelepon.getText().trim());
            dokter.setEmail(txtEmail.getText().trim());
            dokter.setStatus((String) cmbStatus.getSelectedItem());
            
            boolean success;
            if (dokter.getId() == null) {
                success = controller.create(dokter);
            } else {
                success = controller.update(dokter);
            }
            
            if (success) {
                dataSaved = true;
                JOptionPane.showMessageDialog(this, 
                    "Data dokter berhasil disimpan!", 
                    "Sukses", 
                    JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Gagal menyimpan data dokter", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    public boolean isDataSaved() {
        return dataSaved;
    }
}