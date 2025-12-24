package view.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PasienPanel extends JPanel {
    private Color backgroundColor;
    private JTable table;
    private DefaultTableModel tableModel;
    
    public PasienPanel(Color bgColor) {
        this.backgroundColor = bgColor;
        initUI();
    }
    
    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(backgroundColor);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Title
        JLabel titleLabel = new JLabel("👤 MANAJEMEN DATA PASIEN");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(52, 73, 94));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        // Top: Search Panel
        JPanel searchPanel = createSearchPanel();
        
        // Main content: Split Form and Table
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(400);
        splitPane.setDividerSize(3);
        
        // Left: Form
        JPanel formPanel = new JPanel(new BorderLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        formPanel.add(createFormPanel(), BorderLayout.CENTER);
        
        // Right: Table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        tablePanel.add(createTablePanel(), BorderLayout.CENTER);
        
        splitPane.setLeftComponent(formPanel);
        splitPane.setRightComponent(tablePanel);
        
        add(titleLabel, BorderLayout.NORTH);
        add(searchPanel, BorderLayout.CENTER);
        add(splitPane, BorderLayout.SOUTH);
    }
    
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBackground(new Color(240, 240, 240));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("🔍 PENCARIAN CEPAT"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel searchLabel = new JLabel("Cari berdasarkan:");
        searchLabel.setFont(new Font("Arial", Font.BOLD, 12));
        
        JComboBox<String> searchCombo = new JComboBox<>(new String[]{
            "Nama Pasien", "No. KTP", "No. Telepon", "Alamat"
        });
        
        JTextField searchField = new JTextField(25);
        searchField.setPreferredSize(new Dimension(250, 30));
        
        JButton searchButton = new JButton("🔍 Cari");
        searchButton.setBackground(new Color(41, 128, 185));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFont(new Font("Arial", Font.BOLD, 12));
        searchButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        
        JButton resetButton = new JButton("🔄 Reset");
        resetButton.setBackground(new Color(149, 165, 166));
        resetButton.setForeground(Color.WHITE);
        resetButton.setFont(new Font("Arial", Font.BOLD, 12));
        resetButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        
        panel.add(searchLabel);
        panel.add(searchCombo);
        panel.add(searchField);
        panel.add(searchButton);
        panel.add(resetButton);
        
        return panel;
    }
    
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        // Form Fields
        String[] labels = {
            "Nama Lengkap:", 
            "No. KTP:", 
            "Alamat:", 
            "No. Telepon:", 
            "Tanggal Lahir:", 
            "Jenis Kelamin:"
        };
        
        JComponent[] fields = new JComponent[6];
        
        // Text Fields
        for (int i = 0; i < 4; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            JLabel label = new JLabel(labels[i]);
            label.setFont(new Font("Arial", Font.BOLD, 12));
            panel.add(label, gbc);
            
            gbc.gridx = 1;
            fields[i] = new JTextField(20);
            fields[i].setPreferredSize(new Dimension(200, 30));
            panel.add(fields[i], gbc);
        }
        
        // Tanggal Lahir
        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel lblTgl = new JLabel(labels[4]);
        lblTgl.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(lblTgl, gbc);
        
        gbc.gridx = 1;
        JTextField txtTgl = new JTextField(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        txtTgl.setPreferredSize(new Dimension(200, 30));
        panel.add(txtTgl, gbc);
        fields[4] = txtTgl;
        
        // Jenis Kelamin
        gbc.gridx = 0;
        gbc.gridy = 5;
        JLabel lblJK = new JLabel(labels[5]);
        lblJK.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(lblJK, gbc);
        
        gbc.gridx = 1;
        JComboBox<String> cmbJK = new JComboBox<>(new String[]{"Laki-laki", "Perempuan"});
        cmbJK.setPreferredSize(new Dimension(200, 30));
        panel.add(cmbJK, gbc);
        fields[5] = cmbJK;
        
        // Action Buttons
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        String[] buttonTexts = {"➕ Tambah Baru", "💾 Simpan Perubahan", "🗑️ Hapus Data", "🔄 Form Kosong"};
        Color[] buttonColors = {
            new Color(39, 174, 96),    // Hijau
            new Color(41, 128, 185),   // Biru
            new Color(231, 76, 60),    // Merah
            new Color(149, 165, 166)   // Abu-abu
        };
        
        for (int i = 0; i < buttonTexts.length; i++) {
            JButton btn = new JButton(buttonTexts[i]);
            btn.setBackground(buttonColors[i]);
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Arial", Font.BOLD, 11));
            btn.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
            buttonPanel.add(btn);
        }
        
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(buttonPanel, gbc);
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Table Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        JLabel tableTitle = new JLabel("📋 DAFTAR PASIEN TERDAFTAR");
        tableTitle.setFont(new Font("Arial", Font.BOLD, 14));
        tableTitle.setForeground(new Color(52, 73, 94));
        
        JLabel countLabel = new JLabel("Total: 4 pasien");
        countLabel.setFont(new Font("Arial", Font.BOLD, 12));
        countLabel.setForeground(new Color(41, 128, 185));
        
        headerPanel.add(tableTitle, BorderLayout.WEST);
        headerPanel.add(countLabel, BorderLayout.EAST);
        
        // Table
        String[] columns = {"ID", "NAMA", "NO. KTP", "TELEPON", "TGL LAHIR", "JK"};
        Object[][] data = {
            {1, "Budi Santoso", "3201010101010001", "0811111111", "15/05/1990", "L"},
            {2, "Siti Rahayu", "3201010101010002", "0822222222", "20/08/1985", "P"},
            {3, "Ahmad Wijaya", "3201010101010003", "0833333333", "10/12/1995", "L"},
            {4, "Dewi Anggraini", "3201010101010004", "0844444444", "25/03/1988", "P"}
        };
        
        tableModel = new DefaultTableModel(data, columns) {
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
        table.setSelectionBackground(new Color(220, 240, 255));
        
        // Add selection listener
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedData();
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        
        // Table controls
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        controlPanel.setBackground(Color.WHITE);
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        JButton btnPrint = new JButton("🖨️ Cetak");
        JButton btnExport = new JButton("📤 Ekspor");
        JButton btnRefresh = new JButton("🔄 Refresh");
        
        JButton[] controlButtons = {btnPrint, btnExport, btnRefresh};
        for (JButton btn : controlButtons) {
            btn.setBackground(new Color(240, 240, 240));
            btn.setFont(new Font("Arial", Font.PLAIN, 12));
            btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
            controlPanel.add(btn);
        }
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(controlPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void loadSelectedData() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            // Ini nanti diisi dengan logika load data ke form
            System.out.println("Selected row: " + selectedRow);
        }
    }
    
    // Getter untuk controller nanti
    public JTable getTable() {
        return table;
    }
    
    public DefaultTableModel getTableModel() {
        return tableModel;
    }
}