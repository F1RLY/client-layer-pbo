package view.panels;

import controller.PasienController;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PasienPanel extends JPanel {
    private Color backgroundColor;
    
    // Components
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtNama, txtNoKtp, txtAlamat, txtNoTelp, txtTanggalLahir;
    private JComboBox<String> cmbJenisKelamin;
    private JButton btnTambah, btnUpdate, btnHapus, btnClear;
    private JButton btnCari, btnResetSearch, btnPrint, btnExport, btnRefresh;
    private JTextField txtCari;
    private JComboBox<String> cmbCari;
    private JLabel lblJumlahPasien;
    
    public PasienPanel(Color bgColor) {
        this.backgroundColor = bgColor;
        initUI();
        applySmartInput(); // Memanggil fitur proteksi input
    }
    
    // Fitur baru untuk mencegah bug KTP
    private void applySmartInput() {
        // Limit KTP: Maksimal 16 angka & tidak boleh huruf
        addDigitLimit(txtNoKtp, 16);
        txtNoKtp.setToolTipText("Masukkan tepat 16 digit angka KTP");

        // Limit Telepon: Maksimal 13 angka & tidak boleh huruf
        addDigitLimit(txtNoTelp, 13);
        txtNoTelp.setToolTipText("Masukkan nomor telepon (maks 13 digit)");
    }

    // Helper untuk membatasi input hanya angka dan panjang tertentu
    private void addDigitLimit(JTextField field, int limit) {
        field.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                // Jika bukan angka atau sudah mencapai batas, batalkan input
                if (!Character.isDigit(c) || field.getText().length() >= limit) {
                    e.consume(); 
                }
            }
        });
    }
    
    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(backgroundColor);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Title
        JLabel titleLabel = new JLabel("MANAJEMEN DATA PASIEN");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(52, 73, 94));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        // Search Panel
        JPanel searchPanel = createSearchPanel();
        
        // Main content: Split Form and Table
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(400);
        splitPane.setDividerSize(3);
        
        // Left: Form
        JPanel formPanel = createFormPanel();
        
        // Right: Table
        JPanel tablePanel = createTablePanel();
        
        splitPane.setLeftComponent(formPanel);
        splitPane.setRightComponent(tablePanel);
        
        JPanel mainPanel = new JPanel(new BorderLayout(0, 10));
        mainPanel.setBackground(backgroundColor);
        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(splitPane, BorderLayout.CENTER);
        
        add(titleLabel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBackground(new Color(240, 240, 240));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("PENCARIAN"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        panel.add(new JLabel("Cari berdasarkan:"));
        cmbCari = new JComboBox<>(new String[]{"Nama", "No. KTP", "No. Telepon", "Alamat"});
        panel.add(cmbCari);
        
        txtCari = new JTextField(20);
        panel.add(txtCari);
        
        btnCari = new JButton("Cari");
        panel.add(btnCari);
        
        btnResetSearch = new JButton("Reset");
        panel.add(btnResetSearch);
        
        lblJumlahPasien = new JLabel("Total: 0 pasien");
        lblJumlahPasien.setFont(new Font("Arial", Font.BOLD, 12));
        lblJumlahPasien.setForeground(new Color(41, 128, 185));
        panel.add(lblJumlahPasien);
        
        return panel;
    }
    
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("FORM PASIEN"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Fields
        txtNama = new JTextField(15);
        txtNoKtp = new JTextField(15);
        txtAlamat = new JTextField(15);
        txtNoTelp = new JTextField(15);
        txtTanggalLahir = new JTextField(15);
        txtTanggalLahir.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        cmbJenisKelamin = new JComboBox<>(new String[]{"Laki-laki", "Perempuan"});

        // Layouting labels and fields
        addFormField(panel, "Nama Lengkap:", txtNama, gbc, 0);
        addFormField(panel, "No. KTP:", txtNoKtp, gbc, 1);
        addFormField(panel, "Alamat:", txtAlamat, gbc, 2);
        addFormField(panel, "No. Telepon:", txtNoTelp, gbc, 3);
        addFormField(panel, "Tanggal Lahir:", txtTanggalLahir, gbc, 4);
        addFormField(panel, "Jenis Kelamin:", cmbJenisKelamin, gbc, 5);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        buttonPanel.setBackground(Color.WHITE);
        btnTambah = new JButton("Tambah");
        btnUpdate = new JButton("Update");
        btnHapus = new JButton("Hapus");
        btnClear = new JButton("Clear");
        
        buttonPanel.add(btnTambah);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnHapus);
        buttonPanel.add(btnClear);
        
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 5, 5, 5);
        panel.add(buttonPanel, gbc);
        
        return panel;
    }

    private void addFormField(JPanel panel, String label, JComponent comp, GridBagConstraints gbc, int y) {
        gbc.gridwidth = 1; gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(comp, gbc);
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("DAFTAR PASIEN"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        controlPanel.setBackground(Color.WHITE);
        btnPrint = new JButton("Print");
        btnExport = new JButton("Export");
        btnRefresh = new JButton("Refresh");
        controlPanel.add(btnPrint); controlPanel.add(btnExport); controlPanel.add(btnRefresh);
        
        String[] columns = {"ID", "NAMA", "NO. KTP", "ALAMAT", "TELEPON", "TGL LAHIR", "JK"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        
        table = new JTable(tableModel);
        table.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(table);
        
        panel.add(controlPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    // Getters (Sama seperti sebelumnya)
    public JTextField getTxtNama() { return txtNama; }
    public JTextField getTxtNoKtp() { return txtNoKtp; }
    public JTextField getTxtAlamat() { return txtAlamat; }
    public JTextField getTxtNoTelp() { return txtNoTelp; }
    public JTextField getTxtTanggalLahir() { return txtTanggalLahir; }
    public JComboBox<String> getCmbJenisKelamin() { return cmbJenisKelamin; }
    public JButton getBtnTambah() { return btnTambah; }
    public JButton getBtnUpdate() { return btnUpdate; }
    public JButton getBtnHapus() { return btnHapus; }
    public JButton getBtnClear() { return btnClear; }
    public JButton getBtnCari() { return btnCari; }
    public JButton getBtnResetSearch() { return btnResetSearch; }
    public JTextField getTxtCari() { return txtCari; }
    public JComboBox<String> getCmbCari() { return cmbCari; }
    public JButton getBtnPrint() { return btnPrint; }
    public JButton getBtnExport() { return btnExport; }
    public JButton getBtnRefresh() { return btnRefresh; }
    public JTable getTable() { return table; }
    public DefaultTableModel getTableModel() { return tableModel; }

    public void updateJumlahPasien(int jumlah) {
        lblJumlahPasien.setText("Total: " + jumlah + " pasien");
    }
    
    public void setController(PasienController controller) {
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    controller.loadSelectedPasienToForm(selectedRow);
                }
            }
        });
    }
}