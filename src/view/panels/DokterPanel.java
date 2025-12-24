package view.panels;

import controller.DokterController;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class DokterPanel extends JPanel {
    private Color backgroundColor;
    
    // Components
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtNama, txtSpesialis, txtNoTelp, txtAlamat;
    private JButton btnTambah, btnUpdate, btnHapus, btnClear;
    
    public DokterPanel(Color bgColor) {
        this.backgroundColor = bgColor;
        initUI();
    }
    
    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(backgroundColor);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Title
        JLabel titleLabel = new JLabel("MANAJEMEN DATA DOKTER");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(52, 73, 94));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        // Main content split
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(350);
        splitPane.setDividerSize(3);
        
        // Left: Form
        JPanel formPanel = createFormPanel();
        
        // Right: Table
        JPanel tablePanel = createTablePanel();
        
        splitPane.setLeftComponent(formPanel);
        splitPane.setRightComponent(tablePanel);
        
        add(titleLabel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
    }
    
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        // Labels
        String[] labels = {"Nama Dokter:", "Spesialis:", "No. Telepon:", "Alamat:"};
        
        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            JLabel label = new JLabel(labels[i]);
            label.setFont(new Font("Arial", Font.BOLD, 12));
            panel.add(label, gbc);
            
            gbc.gridx = 1;
            JTextField field = new JTextField(15);
            field.setPreferredSize(new Dimension(200, 30));
            panel.add(field, gbc);
            
            // Assign to variables
            switch (i) {
                case 0: txtNama = field; break;
                case 1: txtSpesialis = field; break;
                case 2: txtNoTelp = field; break;
                case 3: txtAlamat = field; break;
            }
        }
        
        // Buttons - BIARKAN DEFAULT STYLE
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        
        btnTambah = new JButton("Tambah");
        btnUpdate = new JButton("Update");
        btnHapus = new JButton("Hapus");
        btnClear = new JButton("Clear");
        
        // HAPUS SEMUA SET BACKGROUND/FOREGROUND/BORDER CUSTOM
        // HAPUS: btnTambah.setBackground(new Color(39, 174, 96));
        // HAPUS: btnTambah.setForeground(Color.WHITE);
        btnTambah.setFont(new Font("Arial", Font.PLAIN, 12));
        
        // HAPUS: btnUpdate.setBackground(new Color(41, 128, 185));
        // HAPUS: btnUpdate.setForeground(Color.WHITE);
        btnUpdate.setFont(new Font("Arial", Font.PLAIN, 12));
        
        // HAPUS: btnHapus.setBackground(new Color(231, 76, 60));
        // HAPUS: btnHapus.setForeground(Color.WHITE);
        btnHapus.setFont(new Font("Arial", Font.PLAIN, 12));
        
        // HAPUS: btnClear.setBackground(new Color(149, 165, 166));
        // HAPUS: btnClear.setForeground(Color.WHITE);
        btnClear.setFont(new Font("Arial", Font.PLAIN, 12));
        
        // HAPUS: btnTambah.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        // HAPUS: btnUpdate.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        // HAPUS: btnHapus.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        // HAPUS: btnClear.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        
        buttonPanel.add(btnTambah);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnHapus);
        buttonPanel.add(btnClear);
        
        gbc.gridx = 0;
        gbc.gridy = labels.length;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 0, 0);
        panel.add(buttonPanel, gbc);
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        // Table
        String[] columns = {"ID", "NAMA", "SPESIALIS", "TELEPON", "ALAMAT"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(245, 245, 245));
        
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    // ========== GETTER METHODS untuk Controller ==========
    
    public JTextField getTxtNama() { return txtNama; }
    public JTextField getTxtSpesialis() { return txtSpesialis; }
    public JTextField getTxtNoTelp() { return txtNoTelp; }
    public JTextField getTxtAlamat() { return txtAlamat; }
    
    public JButton getBtnTambah() { return btnTambah; }
    public JButton getBtnUpdate() { return btnUpdate; }
    public JButton getBtnHapus() { return btnHapus; }
    public JButton getBtnClear() { return btnClear; }
    
    public JTable getTable() { return table; }
    public DefaultTableModel getTableModel() { return tableModel; }
    
    // Method untuk set controller
    public void setController(DokterController controller) {
        // Attach table selection listener
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    controller.loadSelectedDokterToForm(selectedRow);
                }
            }
        });
    }
}